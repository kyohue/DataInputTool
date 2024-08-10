import kall.Thread.MyBlockingQueue;
import kall.Thread.MyThreadFactory;
import kall.Thread.MyThreadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class ThreadTest {
    public static void main(String[] args) {
        ThreadFactory factory = new MyThreadFactory("test");
        LinkedBlockingQueue<Runnable> queue = new MyBlockingQueue<>(9);
        MyThreadPool executor = new MyThreadPool(2, 30, 1, TimeUnit.MINUTES, queue, factory);

        for (int i = 10; i > -3; i--) {
            int a = i;
            executor.execute(() -> {
                System.out.println(Thread.currentThread().getName() + " enter..a=" + a);
//                System.out.println(Thread.currentThread().getName()+": number:" + a + " try 10/a:" + 10 / a);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executor.shutdown();

        //            executor.awaitTermination(10, TimeUnit.SECONDS);
        while (executor.getActiveCount()!=0){

        }
        System.out.println("total time:" + executor.getPoolRunningTime().get() + "ms");
        System.out.println("main: activeThread:" + executor.getActiveCount());
        System.out.println("main: how many task:" + executor.getTaskCount());
        System.out.println("main: how many task completed:" + executor.getCompletedTaskCount());

    }
}
