package com.learn_basic.threadLeecode;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

/**
 * - https://leetcode-cn.com/problems/print-zero-even-odd/solution/java-san-chong-xing-neng-you-yue-de-jie-h4pxx/
 *
 * 要求：打印 010203
 * ZeroEvenOddReentrantLock: ReentrantLock代码块独占 + volatile 阻塞判断 + condition通知放行
 * ZeroEvenOddSemaphore: Semaphore(阻塞+通知）
 * ZeroEvenOddVolatile: volatile 阻塞判断 + yield 阻塞
 */
public class ZeroEvenOdd1116 {
  public static void main(String[] args) {
    ZeroEvenOddReentrantLock demo = new ZeroEvenOddReentrantLock(3);
    new Thread(() -> {
      IntConsumer intConsumer = (i) -> {
        System.out.println(String.format("doubleConsumer receive-->%s", i));
      };
      try {
        demo.zero(intConsumer);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(() -> {
      IntConsumer intConsumer = (i) -> {
        System.out.println(String.format("doubleConsumer receive-->%s", i));
      };
      try {
        demo.even(intConsumer);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();

    new Thread(() -> {
      IntConsumer intConsumer = (i) -> {
        System.out.println(String.format("doubleConsumer receive-->%s", i));
      };
      try {
        demo.odd(intConsumer);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).start();
  }
}

class ZeroEvenOddReentrantLock {
  private int n;
  private volatile int curValue = 0;
  private Lock l = new ReentrantLock();
  private Condition z = l.newCondition();
  private Condition o = l.newCondition();
  private Condition e = l.newCondition();

  public ZeroEvenOddReentrantLock(int n) {
    this.n = n;
  }

  // printNumber.accept(x) outputs "x", where x is an integer.
  public void zero(IntConsumer printNumber) throws InterruptedException {
    l.lock();
    try {
      for (int i = 1; i <= n; i++) {
        if (curValue != 0) {
          z.await();
        }
        printNumber.accept(0);
        if (i % 2 == 1) {
          curValue = 1;
          o.signal();
        } else {
          curValue = 2;
          e.signal();
        }
      }
    } finally {
      l.unlock();
    }
  }

  public void even(IntConsumer printNumber) throws InterruptedException {
    l.lock();
    try {
      for (int i = 2; i <= n; i += 2) {
        if (curValue != 2) {
          e.await();
        }
        printNumber.accept(i);
        curValue = 0;
        z.signal();
      }
    } finally {
      l.unlock();
    }
  }

  public void odd(IntConsumer printNumber) throws InterruptedException {
    l.lock();
    try {
      for (int i = 1; i <= n; i += 2) {
        if (curValue != 1) {
          o.await();
        }
        printNumber.accept(i);
        curValue = 0;
        z.signal();
      }
    } finally {
      l.unlock();
    }
  }
}

/**
 * 可见，结果是2*n个数字，其中有n个零，剩下的就是奇数和偶数对半。
 * 于本题来说打印0是优先分配资源进行执行的，打印奇数和偶数都是受限制的。
 * 优先执行的结束后释放受限执行的线程的资源，受限线程才可继续执行，所以设置odd和even的资源初始值（即信号量）为0，
 * 每当零打印完成后，按照打印顺序为受限线程释放资源。odd和even执行完成后，需要释放打印零的资源，使得循环继续。
 */
class ZeroEvenOddSemaphore {
  private int n;
  private Semaphore z, o, e;
  public ZeroEvenOddSemaphore(int n) {
    this.n = n;
    z = new Semaphore(1);
    o = new Semaphore(0);
    e = new Semaphore(0);
  }

  public void zero(IntConsumer printNumber) throws InterruptedException {
    for (int i = 1; i <= n; i++) {
      z.acquire();
      printNumber.accept(0);
      if ((i & 1) == 0)
        e.release();
      else
        o.release();
    }
  }

  public void odd(IntConsumer printNumber) throws InterruptedException {
    for (int i = 1; i <= n; i += 2) {
      o.acquire();
      printNumber.accept(i);
      z.release();
    }
  }

  public void even(IntConsumer printNumber) throws InterruptedException {
    for (int i = 2; i <= n; i += 2) {
      e.acquire();
      printNumber.accept(i);
      z.release();
    }
  }
}

class ZeroEvenOddVolatile {
  private int n;
  private volatile int curValue = 0; // 方法二： 不用锁，直接用 volatile

  public ZeroEvenOddVolatile(int n) {
    this.n = n;
  }

  public void zero(IntConsumer printNumber) throws InterruptedException {
    for (int i = 1; i <= n; i++) {
      while (curValue != 0) {
        Thread.yield();
      }
      printNumber.accept(0);
      if (i % 2 == 1) {
        curValue = 1;
      } else {
        curValue = 2;
      }

    }
  }

  public void even(IntConsumer printNumber) throws InterruptedException {
    for (int i = 2; i <= n; i += 2) {
      while (curValue != 2) {
        Thread.yield();
      }
      printNumber.accept(i);
      curValue = 0;
    }
  }

  public void odd(IntConsumer printNumber) throws InterruptedException {
    for (int i = 1; i <= n; i += 2) {
      while (curValue != 1) {
        Thread.yield();
      }
      printNumber.accept(i);
      curValue = 0;
    }
  }
}
