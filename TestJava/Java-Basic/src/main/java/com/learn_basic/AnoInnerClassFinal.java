package com.learn_basic;

// https://blog.csdn.net/u010393325/article/details/80643636?utm_medium=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-2%7Edefault%7EBlogCommendFromMachineLearnPai2%7Edefault-1.essearch_pc_relevant
// 匿名内部类，必须 final
public class AnoInnerClassFinal {
  public void function(String a) {
    // Variable 'a' is accessed from within inner class, needs to be final or effectively final
    // public void function(final String a) {
    new Thread() {
      @Override
      public void run() {
        System.out.println(a);
      }
    }.start();
  }


  public static void main(String[] args) {
    new AnoInnerClassFinal().function("a");
  }
}