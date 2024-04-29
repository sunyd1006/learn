package com.learn.util;

public class PrintUtil {
  public static void format(String format, Object... args) {
    System.out.println(String.format(format, args));
  }
}
