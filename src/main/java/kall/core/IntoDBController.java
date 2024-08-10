package kall.core;

import kall.IO.DBConnectionPool;
import kall.IO.In;
import kall.IO.Out;
import kall.Thread.MyRunnableFactory;
import kall.Thread.MyThreadFactory;
import kall.Thread.MyThreadPool;
import kall.entity.Data;
import kall.reflection.InjectData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class IntoDBController {
    public static void main(String[] args) {
        IntoDBController controller = new IntoDBController();
        controller.intoDBControl_v_0();
    }

    public void intoDBControl_v_0() {
        DBConnectionPool connectionPool = DBConnectionPool.getInstance();
        int ThreadNum = 4;
        int n = 2;
        ThreadFactory factoryRead = new MyThreadFactory("Read");
        MyThreadPool poolRead = new MyThreadPool(ThreadNum, factoryRead);
        ThreadFactory factoryInDB = new MyThreadFactory("InDB");
        MyThreadPool poolInDB = new MyThreadPool(ThreadNum * n, factoryInDB);
        MyRunnableFactory showCostTimeRun = new MyRunnableFactory();

        In in = new In();
        Out out = new Out();
        InjectData injectData = new InjectData();

        ArrayList<LinkedBlockingQueue<List<String>>> queueArrayList = new ArrayList<>(ThreadNum);
        ArrayList<LinkedBlockingQueue<List<Data>>> queueArrayListToDB = new ArrayList<>(ThreadNum);
        //容易出错
        CountDownLatch latch = new CountDownLatch(ThreadNum * n + ThreadNum);


        for (int i = 0; i < ThreadNum; i++) {
            queueArrayList.add(new LinkedBlockingQueue<>(100));
            queueArrayListToDB.add(new LinkedBlockingQueue<>(100));
        }

        Runnable readTask = showCostTimeRun.recordCostTime(() -> {
            in.readData_v1_0(queueArrayList);
        });
        long realCostTime = System.currentTimeMillis();
        poolRead.execute(readTask);
        for (int i = 0; i < ThreadNum; i++) {
            int cList = i;
            ReentrantLock lock = new ReentrantLock();
            poolRead.execute(showCostTimeRun.recordCostTime(() -> {
                LinkedBlockingQueue<List<String>> queue = queueArrayList.get(cList);
                LinkedBlockingQueue<List<Data>> intoDBQueue = queueArrayListToDB.get(cList);
                do {
                    try {
                        List<String> onlyRead = queue.take();
                        List<Data> dataList = injectData.injectTest(onlyRead);
                        int dis = dataList.size() / n;
                        int next = dis;
                        int last = 0;
                        while (next <= dataList.size()) {
                            intoDBQueue.add(dataList.subList(last, next));
                            last = next;
                            next += dis;
                        }
                        if (last != dataList.size())
                            intoDBQueue.add(dataList.subList(last, dataList.size()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (!queue.isEmpty());
                latch.countDown();
            }));
            for (int j = 0; j < n; j++)
                poolInDB.execute(showCostTimeRun.recordCostTime(() -> {
                    LinkedBlockingQueue<List<Data>> intoDBQueue = queueArrayListToDB.get(cList);
                    Connection connection = null;
                    do {
                        connection = connectionPool.getConnection();
                    } while (connection == null);
                    List<Data> dataList = null;
                    try {
                        dataList = intoDBQueue.take();
                        out.intoDB(dataList, connection);
                        connectionPool.releaseConnection(connection);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    while (!intoDBQueue.isEmpty()) {
                        try {
                            lock.lockInterruptibly();
                            if (!intoDBQueue.isEmpty()) {
                                dataList = intoDBQueue.take();
                                lock.unlock();
                            } else {
                                lock.unlock();
                                break;
                            }
                            out.intoDB(dataList, connection);
                            connectionPool.releaseConnection(connection);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    latch.countDown();
                }));
        }
        try {
            latch.await();
            realCostTime = System.currentTimeMillis() - realCostTime;
            System.out.println("Total cost time:" + realCostTime + "ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            poolRead.shutdown();
            poolInDB.shutdown();
        }
        try {
            poolRead.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println("poolRead close..");
            poolInDB.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println("poolInDB close..");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
