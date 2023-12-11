package com.learn_basic.lock;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * 因为当线程获取读锁的时候，可能有其他线程同时也在持有读锁，因此不能把获取读锁的线程“升级”为写锁；
 * 而对于获得写锁的线程，它一定独占了读写锁，因此可以继续让它获取读锁，当它同时获取了写锁和读锁后，
 * 还可以先释放写锁继续持有读锁，这样一个写锁就“降级”为了读
 *
 * https://segmentfault.com/a/1190000019035590?utm_source=sf-similar-article
 * <p> 总结：</p>
 * <li> 一个线程要想同时持有写锁和读锁，必须先获取写锁再获取读锁；</li>
 * <li> 写锁可以“降级”为读锁；读锁不能“升级”为写锁。</li>
 *
 */
// https://blog.csdn.net/qqchenjunwei/article/details/79039252?utm_source=app&app_version=4.14.1
public class ReadWriteLockDemo {
  private Map<String, Object> map = new HashMap<>();
  private ReadWriteLock rwl = new ReentrantReadWriteLock(); // 创建读写锁
  private Lock r = rwl.readLock();
  private Lock w = rwl.writeLock();
  public Object get(String key) {
    r.lock(); // 读锁
    System.out.println(Thread.currentThread().getName() + "开始执行读操作");
    try {
      return map.get(key);
    } finally {
      System.out.println(Thread.currentThread().getName() + "读操作完成");
      r.unlock();
    }
  }

  public void put(String key, Object value) {
    w.lock(); // 写锁
    System.out.println(Thread.currentThread().getName() + "开始执行写操作");
    try {
      try {
        Thread.sleep(3000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      map.put(key, value);
    } finally {
      System.out.println(Thread.currentThread().getName() + "写操作完成");
      w.unlock();
    }
  }

  public static void main(String[] args) {
    final ReadWriteLockDemo demo = new ReadWriteLockDemo();
    new Thread(new Runnable() {
      @Override
      public void run() {
        demo.get("key1");
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        demo.put("key1", "value1");
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        demo.put("key3", "value3");
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        demo.put("key4", "value4");
      }
    }).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        demo.get("key3");
      }
    }).start();
  }
}