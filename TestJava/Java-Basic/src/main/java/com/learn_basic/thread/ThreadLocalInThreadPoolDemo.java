package com.learn_basic.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

interface Task {
  void process();
}

class Task1 implements Task {
  public void process() {
    // 在每个用户线程执行任务时可直接通过UserContext.getCurrentUser()拿到当前用户数据
    System.out.print(String.format("===> TaskThreadLocal [%s] %s \n", Thread.currentThread().getName(), UserContext.getCurrentUser()));
  }
}

class UserContext implements AutoCloseable {
  private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

  // 为每个线程准备一个Context，将当前用户数据放进去
  public UserContext(String user) {
    threadLocal.set(user);
  }

  public static String getCurrentUser() {
    System.out.println("  == InUserContext: " + Thread.currentThread().getName() + " " + threadLocal.get());
    return threadLocal.get();
  }

  @Override
  public void close() {
  }
}

class TaskThread implements Runnable {
  private final String user;

  public TaskThread(String user) {
    this.user = user;
  }

  @Override
  public void run() {
    /**
     * 背景：用户线程池中的有很多个线程, 每个线程又包括不同任务，如何在每个任务都能获取到当前用户数据
     * 解决方法： 为每个线程准备一个Context，将当前用户数据放进去
     * 注意点：
     * 1. 线程池是复用的，也就是thread1在这一次run()中threadLocalMap应该和上一次run()threadLocalMap不一样，所以用这种自动释放模型
     * 2. UserContext:: private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
     */
    try (UserContext t = new UserContext(this.user)) {  // 相当于 map.put(currentThead, user);
      // UserContext.getCurrentUser();
      new Task1().process();   // Task1中也可以获取 ctx
    } // 相当于 map.remove(currentThead, user);
    System.out.println("\n");
  }
}

public class ThreadLocalInThreadPoolDemo {
  public static void main(String[] args) {
    // 先准备一个线程池，为六个用户执行六个线程，
    ExecutorService es = Executors.newFixedThreadPool(3);
    String[] users = new String[]{"tom", "jerry", "jack", "rose", "bob", "alice"};
    for (String user : users) {
      es.submit(new TaskThread(user));
    }
  }

}