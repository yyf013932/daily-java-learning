package daniel.yyf.er017.august.threadlocal;

import java.util.concurrent.*;

/**
 * Created by daniel.yyf on 2017/8/15.
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(4);

        Runnable r = new Runnable() {
            ThreadLocalNum threadLocalNum = new ThreadLocalNum();

            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ":" + threadLocalNum.get());
                    threadLocalNum.increase();
                    try {
                        TimeUnit.MILLISECONDS.sleep((int) (Math.random() * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                countDownLatch.countDown();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            executor.execute(r);
        }
        try {
            countDownLatch.await(5,TimeUnit.SECONDS);
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    static class ThreadLocalNum {
        ThreadLocal<Integer> val = ThreadLocal.withInitial(() -> 0);

        int get() {
            return val.get();
        }

        void increase() {
            val.set(val.get() + 1);
        }
    }
}

