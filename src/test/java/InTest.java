import kall.IO.DBConnectionPool;
import kall.IO.In;
import kall.IO.Out;
import kall.Thread.MyBlockingQueue;
import kall.Thread.MyThreadFactory;
import kall.Thread.MyThreadPool;
import kall.entity.Data;
import kall.reflection.InjectData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class InTest {
    public static void main(String[] args) {
        InTest inTest = new InTest();
//        inTest.readDTest();
//        inTest.readDataTest();
        inTest.readDataTest_v1();

    }

    private void readDataTest_v1() {
        int num = 4;
        ThreadFactory factory = new MyThreadFactory("readData");
        MyThreadPool executor = new MyThreadPool(num, num, 1, TimeUnit.MINUTES, new MyBlockingQueue<>(10), factory);
        OutTest outTest = new OutTest();
        DBConnectionPool connectionPool = DBConnectionPool.getInstance();
        Out out = new Out();


        InjectData injectData = new InjectData();
        ArrayList<LinkedBlockingQueue<List<String>>> queueArrayList = new ArrayList<>(num);
        CountDownLatch latch = new CountDownLatch(num);

        for (int i = 0; i < num; i++) {
            queueArrayList.add(new LinkedBlockingQueue<>(100));
        }

        long time = System.currentTimeMillis();
        executor.submit(() -> {
            new In().readData_v1_0(queueArrayList);
            latch.countDown();
        });
        for (int i = 0; i < num; i++) {
            int a = i;
            executor.submit(() -> {
                long TTime = System.currentTimeMillis();
                Connection connection = null;
                do {
                    connection = connectionPool.getConnection();
                } while (connection == null);
                LinkedBlockingQueue<List<String>> queue = queueArrayList.get(a);
                do {
                    try {
                        List<String> onlyRead = queue.take();
                        List<Data> dataList = injectData.injectTest(onlyRead);
//                        outTest.insertTest(dataList.get(0), connection);
                        out.intoDB(dataList.subList(0,2), connection);
                        connectionPool.releaseConnection(connection);
//                        System.out.println(dataList.get(0));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } while (!queue.isEmpty());
                latch.countDown();
                System.out.println(Thread.currentThread().getName() + "-TTime=" + (System.currentTimeMillis() - TTime) + "ms");
            });
        }

        try {
            latch.await();
            time = System.currentTimeMillis() - time;

            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);

            System.out.println(executor.getPoolRunningTime() + "ms");
            System.out.println(time + "ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void readDataTest() {
        ThreadFactory factory = new MyThreadFactory("readData");
        MyThreadPool executor = new MyThreadPool(4, 4, 1, TimeUnit.MINUTES, new MyBlockingQueue<>(10), factory);
        LinkedBlockingQueue<List<Data>> queue = new LinkedBlockingQueue<>(1000);
        LinkedBlockingQueue<List<String>> conQueue = new LinkedBlockingQueue<>(1000);
        InjectData injectData = new InjectData();

        AtomicBoolean isFinish = new AtomicBoolean(false);
        Long time = System.currentTimeMillis();
        executor.submit(() -> {
            isFinish.set(new In().readData_v0_1(conQueue));
//            isFinish.set(new In().readData_v0(queue));
        });
        for (int i = 0; i < 4; i++)
            executor.submit(() -> {
                while (!isFinish.get() || !conQueue.isEmpty()) {
                    List<String> OnlyRead = null;
                    try {
                        OnlyRead = conQueue.take();
                        List<Data> dataList = injectData.injectTest(OnlyRead);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                }
            });
//        for (int i = 0; i < 1; i++)
//            executor.submit(() -> {
//                while (!queue.isEmpty() || !isFinish.get()) {
//                    List<Data> dataList = null;
//                    try {
//                        dataList = queue.take();
//                        System.out.println(dataList.get(0));
//
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            });

        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.MINUTES);
            time = System.currentTimeMillis() - time;
            System.out.println(executor.getPoolRunningTime() + "ms");
            System.out.println(time + "ms");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void readDTest() {
        new In().readDataTest();
    }
}
