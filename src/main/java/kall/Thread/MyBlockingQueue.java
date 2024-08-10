package kall.Thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyBlockingQueue<E> extends LinkedBlockingQueue<E> {
    public MyBlockingQueue(int capacity) {
        super((capacity));
    }

    @Override
    public boolean offer(E e) {
        System.out.println(Thread.currentThread().getName() + ": Task added to queue,remain task: " + this.size());
        return super.offer(e);
    }

    @Override
    public E take() throws InterruptedException {
        E e = super.take();
        System.out.println(Thread.currentThread().getName() + ": Task taken from queue,remain task: " + this.size());
        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + ": Task polled-timedOut from queue,remain task: " + this.size());
        return super.poll(timeout, unit);
    }

    @Override
    public E poll() {
        System.out.println(Thread.currentThread().getName() + ": Task polled from queue,remain task: " + this.size());
        return super.poll();
    }
}
