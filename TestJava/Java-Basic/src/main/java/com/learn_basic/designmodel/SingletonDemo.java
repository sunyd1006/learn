package com.learn_basic.designmodel;

public class SingletonDemo {
  private SingletonDemo() {
  }
  //私有化构造方法
  private static volatile SingletonDemo singleTon = null;

  public static SingletonDemo getInstance() {
    //第一次校验
    if (singleTon == null) {
      synchronized (SingletonDemo.class) {
        //第二次校验
        if (singleTon == null) {
          singleTon = new SingletonDemo();
        }
      }
    }
    return singleTon;
  }

  public static void main(String[] args) {
    for (int i = 0; i < 200; i++) {
      new Thread(() -> System.out.println(Thread.currentThread().getName() + ":" + SingletonDemo.getInstance().hashCode())).start();
    }
  }

  // NOTE: 饿汉模式
  // private static final Singleton singleton = new Singleton();
  // private Singleton(){ }
  //
  // public static Singleton getInstance(){
  //     return singleton;
  // }

  // NOTE: 懒汉模式
  // private static Singleton singleton;
  //
  // private Singleton() {
  // }
  //
  // synchronized public static Singleton getInstance() {
  //     if (singleton == null)
  //         singleton = new Singleton();
  //     return singleton;
  // }
}