package com.learn_basic.lock;

import java.util.concurrent.CyclicBarrier;

/**
 *
 * @author Snailclimb
 * @date 2018年10月1日
 * @Description: 测试 CyclicBarrier 类中带参数的 await() 方法
 *
 * 1. 让多个线程都达到一个位置（await()后，最后到达的会执行 new CyclicBarrier(threadNum, new Runnable() {}) 中的方法
 */
// https://snailclimb.gitee.io/javaguide/#/docs/java/multi-thread/AQS%E5%8E%9F%E7%90%86%E4%BB%A5%E5%8F%8AAQS%E5%90%8C%E6%AD%A5%E7%BB%84%E4%BB%B6%E6%80%BB%E7%BB%93?id=_52-cyclicbarrier-%e7%9a%84%e4%bd%bf%e7%94%a8%e7%a4%ba%e4%be%8b
public class CyclicBarrierDemo {
    /**
     * Thread-2 到达栅栏 A
     * Thread-3 到达栅栏 A
     * Thread-0 到达栅栏 A
     * Thread-1 到达栅栏 A
     * Thread-4 到达栅栏 A
     * Thread-4 完成最后任务  最后一个完成
     * Thread-4 冲破栅栏 A
     * Thread-2 冲破栅栏 A
     * ...
     */
    public static void main(String[] args) {
        int threadNum = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadNum, new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 完成最后任务");
                // 所有线程会等待全部线程到达栅栏之后才会继续执行，并且最后到达的线程会完成 Runnable 的任务。
            }
        });
        
        for(int i = 0; i < threadNum; i++) {
            new TaskThread(barrier).start();
        }
    }
    
    static class TaskThread extends Thread {
        CyclicBarrier barrier;
        public TaskThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(getName() + " 到达栅栏 A");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 A");
                
                Thread.sleep(2000);
                System.out.println(getName() + " 到达栅栏 B");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // ==================================
    
    // // 请求的数量, 相当于threadCount个任务
    // private static final int threadCount = 550;
    // // 需要同步的线程数量
    // private static final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    //
    //  public static void main(String[] args) InterruptedException {
    //     // 创建线程池
    //     ExecutorService threadPool = Executors.newFixedThreadPool(10);
    //
    //     for (int i = 0; i < threadCount; i++) {
    //         final int threadNum = i;
    //         Thread.sleep(1000);
    //         threadPool.execute(() -> {
    //             try {
    //                 test(threadNum);
    //             } catch (InterruptedException e) {
    //                 e.printStackTrace();
    //             } catch (BrokenBarrierException e) {
    //                 e.printStackTrace();
    //             }
    //         });
    //     }
    //     threadPool.shutdown();
    // }
    //
    // public static void test(int threadnum) throws InterruptedException, BrokenBarrierException {
    //     System.out.println("threadnum:" + threadnum + "is ready");
    //     try {
    //         /**等待60秒，保证子线程完全执行结束*/
    //         cyclicBarrier.await(60, TimeUnit.SECONDS);
    //     } catch (Exception e) {
    //         System.out.println("-----CyclicBarrierException------");
    //     }
    //     System.out.println("threadnum:" + threadnum + "is finish");
    // }

}

