package com.learn_basic.threadLeecode;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class printPhilosopher_1226 {
  public static void main(String[] args) {
    DiningPhilosophers_ReentrantLock demo = new DiningPhilosophers_ReentrantLock();
    ThreadPoolExecutor pool = new ThreadPoolExecutor(
            5,
            5,
            10,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    for (int i = 0; i < 5; i++) {
      int finalI = i;
      pool.execute(() -> {
        try {
          demo.wantsToEat(finalI,
                  () -> System.out.println(Thread.currentThread().getName() + " 拿左"),
                  () -> System.out.println(Thread.currentThread().getName() + " 拿右"),
                  () -> System.out.println(Thread.currentThread().getName() + " 吃"),
                  () -> System.out.println(Thread.currentThread().getName() + " 放左"),
                  () -> System.out.println(Thread.currentThread().getName() + " 放右"));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
    }


  }
}

class DiningPhilosophers_ReentrantLock {
  private ReentrantLock[] forks = new ReentrantLock[5]; // 叉子是否可用
  private Semaphore limit = new Semaphore(4); // 保证同时只有4人拿叉子，防止死锁

  public DiningPhilosophers_ReentrantLock() {
    for (int i = 0; i < forks.length; i++) {
      forks[i] = new ReentrantLock();
    }
  }

  public void wantsToEat(int philosopher,
                         Runnable pickLeftFork,
                         Runnable pickRightFork,
                         Runnable eat,
                         Runnable putLeftFork,
                         Runnable putRightFork) throws InterruptedException {
    limit.acquire(); // 确保只有四个人
    forks[philosopher].lock(); // 加锁
    forks[(philosopher + 1) % 5].lock();

    pickLeftFork.run(); // 拿起左边筷子 右边筷子 吃
    pickRightFork.run();
    eat.run();
    putLeftFork.run(); // 放下左边筷子 右边筷子
    putRightFork.run();

    forks[(philosopher + 1) % 5].unlock(); // 解锁
    forks[philosopher].unlock();
    limit.release();
  }
}
