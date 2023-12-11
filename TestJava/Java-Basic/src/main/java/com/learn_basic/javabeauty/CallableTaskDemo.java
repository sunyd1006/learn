package com.learn_basic.javabeauty;


import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static java.lang.Thread.sleep;

//创建任务类，类似Runable
public class CallableTaskDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        try {
            sleep(3000);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "CallableReturnResult";
    }

    // 测试 Callable 返回结果
    @Test
    public static void testCallableTask() throws InterruptedException {
        // 创建异步任务
        FutureTask<String> futureTask = new FutureTask<>(new CallableTaskDemo());
        // 启动线程
        new Thread(futureTask).start();
        try {
            // 等待任务执行完毕，并返回结果
            String result = futureTask.get();
            System.out.println(result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    // 创建资源
    private static volatile Object resourceA = new Object();
    private static volatile Object resourceB = new Object();
    public static void demo2() throws InterruptedException {
        // 创建线程
        Thread threadA = new Thread(new Runnable() {
            public void run() {
                try {
                    // 获取resourceA共享资源的监视器锁
                    synchronized (resourceA) {
                        System.out.println("threadA get resourceA lock");
                        // 获取resourceB共享资源的监视器锁
                        synchronized (resourceB) {
                            System.out.println("threadA get resourceB lock");
                            // 线程A阻塞，并释放获取到的resourceA的锁
                            System.out.println("threadA release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 创建线程
        Thread threadB = new Thread(new Runnable() {
            public void run() {
                try {
                    // 休眠1s
                    Thread.sleep(1000);
                    // 获取resourceA共享资源的监视器锁
                    synchronized (resourceA) {
                        System.out.println("threadB get resourceA lock");
                        System.out.println("threadB try get resourceB lock...");
                        // 获取resourceB共享资源的监视器锁
                        synchronized (resourceB) {
                            System.out.println("threadB get resourceB lock");
                            // 线程B阻塞，并释放获取到的resourceA的锁
                            System.out.println("threadB release resourceA lock");
                            resourceA.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        // 启动线程
        threadA.start();
        threadB.start();

        // 等待两个线程结束
        threadA.join();
        threadB.join();
        System.out.println("main over");
    }
    /**
     * 测试 join ： join， 等待其他线程运行完毕，比如在Main中写one.join(), Main等one运行完毕
     */
    @Test
    public static void testJoin() {
        //线程one
        Thread threadOne = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("threadOne begin run!");
                for (; ; ) {
                }
            }
        });
        // 获取主线程
        final Thread mainThread = Thread.currentThread();
        // 线程two
        Thread threadTwo = new Thread(new Runnable() {
            @Override
            public void run() {
                // 休眠1s
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //中断主线程
                mainThread.interrupt();
            }
        });
        // 启动子线程
        threadOne.start();
        // 延迟1s启动线程
        threadTwo.start();
        try {
            //等待线程one执行结束
            threadOne.join();
        } catch (InterruptedException e) {
            System.out.println("main thread:" + e);
        }
    }
}