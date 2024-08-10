package kall.Thread;

public class MyRunnableFactory {
    public Runnable recordCostTime(Runnable r) {
        return new Runnable() {
            @Override
            public void run() {
//                long time = System.currentTimeMillis();
                r.run();
//                System.out.println(Thread.currentThread().getName() + "-costTime:" + (System.currentTimeMillis() - time) + "ms");
            }
        };
    }
}
