package com.learn_basic.thread;

import java.util.*;

abstract class Executer {
  abstract class WorkerThreadDemo extends Thread{ // 要任务实现 Runnable 接口
    protected Boolean isRunning = true; // 当前线程是否可用
    abstract void setIsRunning();
  };

  protected int workerNumber = 5; // 设置默认开启线程个数 5 个
  protected WorkerThreadDemo[] workThreads; // 线程工作组
  protected List<Runnable> taskQueue = new LinkedList<Runnable>();  //任务队列 用个 list 后面添加方法用同步锁定就行

  abstract void addTask(Runnable Runnable);
  abstract void start();
  abstract void waitForIdle() throws InterruptedException;
  abstract void stop();
}

/**
 * 自定义线程池
 * https://www.cnblogs.com/jtfr/p/10507900.html
 */
public class ThreadPoolDemo {
  public static void main(String[] args) {
    ThreadPool poll = new ThreadPool(-5);
    poll.start();

    for (int i = 0; i < 100; i++) {
      poll.addTask(new Thread(new Runnable() {
        @Override
        public void run() {
          System.out.println(Thread.currentThread().getName());
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }));
    }

    poll.waitForIdle();
    poll.stop(false);  // 调用销毁方法，不执行剩下的任务
  }
}

class ThreadPool extends Executer {
  public ThreadPool(int workerNumber) {
    if (workerNumber > 0) { // 小于零 说明输入错误，就不赋值，使用默认值。
      super.workerNumber = workerNumber;
    }
    workThreads = new WorkerThread[super.workerNumber];  // 开启工作空间
  }

  @Override
  public void start() {
    for (int i = 0; i < workerNumber; i++) { // 循环创建所有线程
      workThreads[i] = new WorkerThread();  // 创建的线程添加到 工作组 中
      new Thread(workThreads[i], "ThreadPool-worker " + (i + 1)).start();
      System.out.println("初始化线程数" + (i + 1));
    }
  }

  @Override // 添加任务方法，即把对象添加到 队列中, 同时唤醒所有 wait中的线程
  public void addTask(Runnable task) {
    synchronized (taskQueue) {
      taskQueue.add(task);
      taskQueue.notifyAll();
    }
  }

  @Override
  public void waitForIdle() {
    for (int i = 0; i < workerNumber; i++) { // 循环创建所有线程
      try {
        workThreads[i].join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(workThreads[i].getName() + "join!");
    }
  }

  // 销毁线程池，默认是等待线程执行完了再销毁
  public void stop() {
    stop(true);
  }

  /**
   * 销毁线程池，可以指定不执行队列中剩下的任务，直接销毁线程池了
   * true 表示继续执行剩下的任务。
   */
  public void stop(boolean waitForAllRunningDone) {
    if (waitForAllRunningDone) { // 默认是等待线程执行完了再销毁
      while (!taskQueue.isEmpty()) { // 循环是否还存在任务，如果存在等待20毫秒处理时间
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

    //如果任务队列已处理完成，销毁线程，清空任务
    for (int i = 0; i < workerNumber; i++) {
      workThreads[i].setIsRunning();
      workThreads[i] = null;
    }
    taskQueue.clear();
    System.out.println("线程池销毁了");

    // 不能设置 taskQueue==null 其他正在调用的地方可能报 null指针异常。
    //taskQueue = null;
  }

  /**
   * 内部类：做线程工作类
   */
  class WorkerThread extends WorkerThreadDemo {
    @Override
    public void run() {
      Runnable runnable = null;
      // 死循环，除非外界调用销毁方法，设定 isRunning 为false
      while (isRunning) {
        synchronized (taskQueue) { // 上面用的 list 非线程安全，所以这里要同步去任务
          while (isRunning && taskQueue.isEmpty()) { // 如果线程活的，但是 taskQueue 是空，线程等待 20 毫秒
            try {
              taskQueue.wait(20);  // wait会释放锁，其他工作线程可以继续执行同步代码块里面内容
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }

          /**
           * done 因为可能被唤醒时，是isRuning false, 而不是 taskQueue 有东西
           * easy error: 为什么还要判断 taskQueue 非空?：
           * 获取任务，注意：要在同步代码块里面获取任务。
           */
          if (!taskQueue.isEmpty()) {
            // list模拟队列，所以获取第一个元素。
            runnable = taskQueue.remove(0);
          }
        }

        /**
         *  done runnable是从 taskQueue 拿到的任务，故而需要判断任务的内容
         *  注意：执行任务要在同步代码块外面，把锁释放出来给其他线程.
         */
        if (runnable != null) {
          runnable.run(); // 执行 run 方法，要任务实现Runnable接口，实际上是为了保证有 run 方法，和线程没关系。
          runnable = null; // 结束后置 null 方便回收
        }
      }
    }

    /**
     * 销毁线程，实际上就是不再死循环，正常结束了工作线程
     */
    public void setIsRunning() {
      this.isRunning = false;
    }
  }
}