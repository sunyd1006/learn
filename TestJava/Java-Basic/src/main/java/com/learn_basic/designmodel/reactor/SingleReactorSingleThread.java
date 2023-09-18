package com.learn_basic.designmodel.reactor;

public class SingleReactorSingleThread {
  public static void main(String[] args) {
    int workCount =Runtime.getRuntime().availableProcessors();
    System.out.println(workCount);
  }
}
