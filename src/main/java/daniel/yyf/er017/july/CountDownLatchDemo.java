package daniel.yyf.er017.july;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by daniel.yyf on 2017/7/31.
 * @see CountDownLatch 是一个同步工具类，工作流类似于执行完N个任务后才能执行后续任务
 * @see CountDownLatch#CountDownLatch(int) CountDownLatch的初始化方法 必须指定一个数字，表示前置任务数量，且无法更改
 * @see CountDownLatch#countDown() 方法用来减少计数量，可以当做是一个任务完成后通知其他任务
 * @see CountDownLatch#await()
 * @see CountDownLatch#await(long, TimeUnit) 用在主任务中阻塞，等待其他任务的完成（调用了指定次数的countDown方法后）
 */
public class CountDownLatchDemo {
    private CountDownLatch countDownLatch;
    ExecutorService executorService;

    CountDownLatchDemo() {
        //初始化countDownLatch时需要指定前置任务数量，并在相应的进程中减少对应的量
        countDownLatch = new CountDownLatch(2);
        executorService = Executors.newFixedThreadPool(2);
    }

    void run() {
        Service s1 = new Service(countDownLatch, "##1");
        Service s2 = new Service(countDownLatch, "##2");

        System.out.println("main service start to work");

        executorService.execute(s1);
        executorService.execute(s2);

        try {
            //设置超时，3000毫秒
            countDownLatch.await(3000,TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("main service finished");

        executorService.shutdown();
    }


    public static void main(String[] args) {
        (new CountDownLatchDemo()).run();
    }
}

/**
 * 需要完成的前置任务
 */
class Service implements Runnable {
    /**
     * 需要引用此锁在任务执行完成后调用countDown方法
     */
    CountDownLatch cdl;
    String name;

    public Service(CountDownLatch cdl, String name) {
        this.cdl = cdl;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("service " + name + " starts!!");
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
            System.out.println("service " + name + " ends!!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (cdl != null)
                //调用countDown方法减少计数
                cdl.countDown();
        }
    }
}
