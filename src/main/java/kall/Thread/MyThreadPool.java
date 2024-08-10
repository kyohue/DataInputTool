package kall.Thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class MyThreadPool extends ThreadPoolExecutor {
    private AtomicLong totalTime = new AtomicLong(0);
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    @Override
    public void execute(Runnable command) {
        super.execute(wrap(command, clientTrace()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(wrap(task, clientTrace()));
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTime.set(System.currentTimeMillis());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        if (t != null) {
            System.err.println(Thread.currentThread().getName() + " Exception.");
        } else {
            long duration = System.currentTimeMillis() - startTime.get();
            totalTime.addAndGet(duration);
        }
        startTime.remove();
    }

    /**
     *
     * @return 线程池所有任务串行累加耗时ms
     */
    public AtomicLong getPoolRunningTime() {
        return totalTime;
    }

    /**
     *
     * @return 异常，调用该函数的栈信息会被记录到这个异常中
     */
    private Exception clientTrace() {
        return new Exception("Client stack trace");
    }

    /**
     *
     * @param task 待执行的任务
     * @param clientStack 包含调用clientTrace函数的栈信息
     * @return 一个可执行任务
     */
    private Runnable wrap(final Runnable task, final Exception clientStack) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } catch (Exception e) {
                    clientStack.printStackTrace();
                    throw e;
                }
            }
        };
    }
}
