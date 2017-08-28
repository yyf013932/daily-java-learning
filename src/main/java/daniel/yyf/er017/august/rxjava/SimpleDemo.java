package daniel.yyf.er017.august.rxjava;

import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RxJava 使用了一种拓展的观察者模式
 * Observable 被观察者，是事件的产生者（信息的发送方）
 * Observer 观察者，事件的消费者（信息的接收方），3个回调函数 onNext、onComplete、onError
 * Subscribe 将观察者与被观察者建立关系
 * 一个常用的使用过程就是先创建观察者和被观察者，然后建立关系 {@link SimpleDemo#simpleMethod()}
 * <p>
 * 但是在默认情况下，事件的发生和消费在同一个线程，可以使用Scheduler进行不同线程的设置.
 * 通过{@link Observable#subscribeOn(Scheduler)}和{@link Observable#observeOn(Scheduler)}进行线程的设置
 * Created by daniel.yyf on 2017/8/23.
 */
public class SimpleDemo {

    static void simpleMethod() {
        //创建被观察者
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //发送信息
                subscriber.onNext("message 1");
                subscriber.onNext("message 2");
                subscriber.onNext("message 3");
                //信息发送完成事件
                subscriber.onCompleted();
            }
        });

        //一样的实现
//        Observable<String> observable=Observable.just("message 1","message 2","message 3");

        //创建观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError");
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext");
                System.out.println("get message " + s);
            }
        };

        //建立关系
        //建立完成后就开始相应处理
        observable.subscribe(observer);


    }

    static void schedulerMethod() {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        //create observable
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + " create number:" + i);
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        });
        observable
                //设置subscribe发生的线程
                .subscribeOn(Schedulers.from(executorService))
                //设置subscriber的回调线程
                .observeOn(Schedulers.newThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        executorService.shutdown();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println(Thread.currentThread().getName() + " got number:" + integer);
                    }
                });
    }

    static void subjectMethod(){

    }

    public static void main(String[] args) {
//        simpleMethod();
        schedulerMethod();
    }
}
