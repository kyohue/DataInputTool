package kall.Thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadFactory implements ThreadFactory {
    private String name;
    private AtomicInteger id = new AtomicInteger(1);
    private ThreadGroup threadGroup = null;

    public MyThreadFactory(String name) {
        this.name = name;
    }

    public MyThreadFactory(String name, ThreadGroup threadGroup) {
        this.name = name;
        this.threadGroup = threadGroup;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t;
        if (threadGroup == null) {
            t = new Thread(r, name + "-Thread-" + id.getAndIncrement());
        } else {
            t = new Thread(threadGroup, r, name + "-Thread-" + id.getAndIncrement());
        }
        t.setDaemon(true);
        System.out.println(t.getName() + " is created at:" + System.currentTimeMillis());
        return t;
    }
}
