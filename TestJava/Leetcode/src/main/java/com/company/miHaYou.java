package com.lee.company;


import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

// 米哈游 第1次 mihayou
public class miHaYou {
  public static void main(String[] args) throws IOException {
    // Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
    Scanner sc = new Scanner(System.in);
    int t = sc.nextInt();
    sc.nextLine();
    while ((t--) > 0) {
      String line = sc.nextLine();
      boolean ab = isAB(line);
      if(ab){
        System.out.println("YES");
      }else{
        System.out.println("NO");
      }
    }
  }

  public static boolean isAB(String name) {
    Stack<Character> stack = new Stack<>();
    for (int i = 0; i < name.length(); i++) {
      char curChar = name.charAt(i);
      boolean isContinue = false;
      while (!stack.isEmpty() && stack.peek().equals('a') && curChar == 'b') {
        stack.pop();
        isContinue = true;
        break;
      }
      if (isContinue) continue;
      stack.push(curChar);
    }
    return stack.isEmpty();
  }
}
