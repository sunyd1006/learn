package com.learn_basic.threadLeecode;

// https://leetcode-cn.com/problems/print-foobar-alternately/solution/duo-xian-cheng-liu-mai-shen-jian-ni-xue-d220n/
// BlockingQueue
public class print30Number {
  public static void main(String[] args) throws InterruptedException {
    Resource30 fooBar = new Resource30(30);
    Thread first = new Thread(() -> {
      fooBar.printA();
    });

    Thread second = new Thread(() -> {
      fooBar.printB();
    });

    Thread three = new Thread(() -> {
      fooBar.printC();
    });

    Thread four = new Thread(() -> {
      fooBar.printD();
    });

    first.start();
    second.start();
    three.start();
    four.start();

    first.join();
    second.join();
    three.join();
    four.join();

    System.out.println(Thread.currentThread().getName());
  }
}

class Resource30 {
  private volatile int start;
  private Object mutex1;
  private Object mutex2;
  private int n;

  public Resource30(int n) {
    this.start = 0;
    this.n = n;
    mutex1 = new Object();
    mutex2 = new Object();
  }

  public void printA() {
    ABCCommonPrint();
  }

  public void printB() {
    ABCCommonPrint();
  }

  public void printC() {
    ABCCommonPrint();
  }

  private void ABCCommonPrint() {
    // while ((start & 1) == 0);
    // System.out.println(" here3 " + start);
    synchronized (mutex1) {
      while (start < n) {
        if ((start & 1) == 0) {
          try {
            mutex1.wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }

        if (start < n) {  // done 为什么要判断这里呢？可能别的线程已经 start++过，这个线程就不必在继续工作了。
          System.out.println(Thread.currentThread().getName() + ": " + start);
          start++;
        }
        mutex1.notify();
      }
    }

  }

  public void printD() {
    while (start < n) {
      if ((start & 1) == 0) {
        // System.out.println(" here " + start);
        synchronized (mutex2) {
          if ((start & 1) == 1) {
            try {
              mutex2.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          if (start < n) {
            System.out.println(Thread.currentThread().getName() + ": " + start);
            start++;
          }
        }
      } else {
        // System.out.println(" here2 " + start);
        ABCCommonPrint();
      }
    }
  }

}
