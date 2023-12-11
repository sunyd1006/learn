package com.learn_basic.lock;

import static java.lang.Thread.sleep;

import org.junit.Test;

/**
 * 用一个方法定时更新特定值，定时操作：https://www.jianshu.com/p/db1611a16cb8
 * <ul>
 *     <li>重点测试：多个线程，执行多个对象obj1, obj2, obj3 ， obj123的方法是否可以并行</li>
 *     <li>不测试：多个线程，执行同一个对象obj1的方法method，当然不可以并行 </li>
 *     <li>1. 非 static 方法, synchronized function 锁住的是实例对象，故而多个对象可以同时执行对象自己的方法, 1 等价于2</li>
 *     <li>2. 非 static 方法, synchronized(this) 锁 实例对象this, 1 等价于2  </li>
 *     <li>3. 非 static 方法, synchronized(SyncDemo.class) 锁类对象, 所有实例对象(包括类对象）都不可在调用此方法    </li>
 *     <li>4. static 方法，synchronized function 锁的类对象, 所有实例对象(包括类对象）都不可在调用此方法 </li>
 * </ul>
 */
public class SynchronizedDemo {
    static int[] array = {1,2,3,4,5};
    public static void main(String[] args) {
        SynchronizedDemo demo = new SynchronizedDemo();

        // 测试：非 static 方法, 锁住的是对象，故而多个对象可以同时执行对象自己的方法
        demo.synchronizedFunction();
        /**
         *  test开始..
         * test开始..
         * test开始..
         * test结束..
         * test结束..
         * test结束..
         */

        // demo.synchronizedThisObj();        // 结果同上

        // demo.synchronizedClass();
        /**
         * test开始..
         * test结束..
         * test开始..
         * test结束..
         * test开始..
         * test结束..
         */

        // demo.synchronizedStaticFunction();   // 结果同上

    }

    @Test
    // 有问题
    public void testTimeTask(){
        // new Timer("timer - ").schedule(new TimerTask() {
        //     @SneakyThrows
        //     @Override
        //     public void run() {
        //         System.out.println("开始改变 array int[] 的值");
        //
        //         // 并没有保证原子操作， array[i] 的顺序还是[5，2，3，4，5]
        //         synchronized(SynchronizedDemo.class) {
        //             for (int i = 0; i < 5; i++) {
        //                 array[i] = array[i]*5;
        //                 sleep(5000);
        //             }
        //         }
        //         System.out.println("结束改变 array int[] 的值");
        //     }
        // }, 1000);
        //
        // while (true){
        //     new Thread(()->{
        //         for (int i = 0; i < 5; i++) {
        //             System.out.print(array[i] + " ");
        //         }
        //         System.out.println(" ");
        //     }).start();
        // }
    }

    @Test
    // note 单测执行不会等子线程都结束
    public void synchronizedFunction() {
        // 多个线程执行 1个对象的方法
        SyncDemo syncDemo = new SyncDemo();
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(()->{
                syncDemo.synchronizedFunction();
                syncDemo.synchronizedThisObj();
            });
            thread.start();
        }

        // 多个线程执行 多个对象的方法
        // for (int i = 0; i < 3; i++) {
        //     Thread thread = new Thread(new SyncDemo()::synchronizedFunction);
        //     thread.start();
        // }
    }

    @Test
    // note 单测执行不会等子线程都结束
    public void synchronizedThisObj() {
        // 多个线程执行 1个对象的方法
        // SyncDemo syncDemo = new SyncDemo();
        // for (int i = 0; i < 3; i++) {
        //     Thread thread = new Thread(syncDemo::synchronizedThisObj);
        //     thread.start();
        // }

        // 多个线程执行 多个对象的方法
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new SyncDemo()::synchronizedThisObj);
            thread.start();
        }
    }

    @Test
    // note 单测执行不会等子线程都结束
    public void synchronizedClass() {
        // 多个线程执行 1个对象的方法
        // SyncDemo syncDemo = new SyncDemo();
        // for (int i = 0; i < 3; i++) {
        //     Thread thread = new Thread(syncDemo::synchronizedClass);
        //     thread.start();
        // }

        // 多个线程执行 多个对象的方法
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(new SyncDemo()::synchronizedClass);
            thread.start();
        }
    }

    @Test
    // note 单测执行不会等子线程都结束
    public void synchronizedStaticFunction() {
        // 多个线程执行 1个 类对象的方法
        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(()->{
                SyncDemo.synchronizedStaticFunction();
            });
            thread.start();
        }
    }
}

class SyncDemo {
    // 1. 锁 实例对象的方法, 1 等价于2
    public synchronized void synchronizedFunction() {
        System.out.println("synchronizedFunction test开始..");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test结束..");
    }

    // 2. 锁 实例对象this, 1 等价于2
    public void synchronizedThisObj(){
        synchronized (this){
            System.out.println("synchronizedThisObj test开始..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test结束..");
        }
    }

    // 3. 锁 类对象 Sync.class
    public void synchronizedClass(){
        synchronized (SyncDemo.class){
            System.out.println("synchronizedThisObj test开始..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("test结束..");
        }
    }

    // 4. 锁 类对象 的方法
    public static void synchronizedStaticFunction(){
        System.out.println("synchronizedThisObj test开始..");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test结束..");
    }
}


