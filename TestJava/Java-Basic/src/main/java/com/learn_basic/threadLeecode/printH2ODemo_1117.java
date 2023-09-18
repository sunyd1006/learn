package com.learn_basic.threadLeecode;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * // https://blog.csdn.net/KD1996/article/details/106740273
 * H2OSemaphore H线程是一个线程跑了2次，也可执行。但是题目暗示是3个线程
 *
 * OHH
 * HOH
 * HHO
 */
public class printH2ODemo_1117 {
  public static void main(String[] args) {
    H2OCyclicBarrier h2o = new H2OCyclicBarrier();
    new Thread(() -> {
      while (true) {
        try {
          h2o.hydrogen(() -> System.out.print("H"));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    new Thread(() -> {
      while (true) {
        try {
          h2o.hydrogen(() -> System.out.print("H"));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

    new Thread(() -> {
      while (true) {
        try {
          h2o.oxygen(() -> System.out.print("O"));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();

  }
}


class H2OSemaphore {
  public H2OSemaphore() {
  }

  private Semaphore h = new Semaphore(2);
  private Semaphore o = new Semaphore(0);

  public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
    h.acquire();
    releaseHydrogen.run();
    o.release();
  }

  public void oxygen(Runnable releaseOxygen) throws InterruptedException {
    o.acquire(2);
    releaseOxygen.run();
    h.release(2);
  }
}

/**
 * 让两个线程打印H，一个线程打印O，打印完分别调用await()，这三个线程都到达CyclicBarrier，再让CyclicBarrier通知继续开始新的打印
 * 让两个打印H的线程和一个打印O的线程直接await()，三个线程到达CyclicBarrier后，三个线程从await()之后再开始打印
 * ————————————————
 * 版权声明：本文为CSDN博主「KD1996」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/KD1996/article/details/106740273
 */
class H2OCyclicBarrier{
  public H2OCyclicBarrier() {
    barrier = new CyclicBarrier(3, ()->{
      System.out.println(""); // 最后一个到达的线程打印换行符
    });
  }
  private CyclicBarrier barrier;

  public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
    releaseHydrogen.run();
    try {
      barrier.await();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }

  public void oxygen(Runnable releaseOxygen) throws InterruptedException {
    releaseOxygen.run();
    try {
      barrier.await();
    } catch (BrokenBarrierException e) {
      e.printStackTrace();
    }
  }
}

class H20Synchronized{

}
