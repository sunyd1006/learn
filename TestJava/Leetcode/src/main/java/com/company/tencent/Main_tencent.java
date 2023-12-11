package com.lee.company.tencent;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// 秋招腾讯笔试
public class Main_tencent {
 
  @Test  // 用函数来模拟求一个区域的面积，要用积分
  public void test2() throws FileNotFoundException {
    Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input2.txt"));
    // Scanner sc = new Scanner(System.in);
    while (sc.hasNext()){
      int k = sc.nextInt();
      int b = sc.nextInt();
      double y = findY(k, b);
      // System.out.println(k + "   " + b);
      // System.out.println(" y: " + y);
      double x = b - y;
      double res = (Math.pow(x, k+1) -1 ) /(k+1)  + 0.5 * (b*b + x*x) - b * x;
      System.out.println(res);
    }
  }

  public static double findY(int k, int b) {
    double left = 0, right = b;
    while (left < right) {
      double preY = (right - left) / 2 + left;
      double res = Math.pow(b - preY, k);
      double target = Math.abs(res - preY);
      if (target < 0.000000001) {   // del 0
        return preY;
      } else if (res > 0) {
        right = preY;
        // left = preY;
      } else {
        left = preY;
        // right = preY;
      }
    }
    return b;
  }
  
  @Test // blue ,red 之间的跳转。其实是个dfs
  public  void test3() throws FileNotFoundException {
    Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input3.txt"));
    // Scanner sc = new Scanner(System.in);
    // while (sc.hasNext()) {
    int n = sc.nextInt();
    int m = sc.nextInt();
    board = new boolean[n][m];
    visited = new boolean[n][m];
    sc.nextLine();
    for (int i = 0; i < n; i++) {
      String line = sc.nextLine();
      for (int j = 0; j < m; j++) {
        if (line.charAt(j) == 'r') {
          board[i][j] = true; // red = 1
        } else {
          board[i][j] = false; //
        }
      }
    }
    
    int x = sc.nextInt()-1;
    int y = sc.nextInt()-1;
    boolean isRed = board[x][y] ? true : false;
    res = 1;
    visited[x][y] = true;
    howManyStep(x, y, isRed);
    System.out.println(res);
    // }
  }
  
  static boolean[][] board;
  static boolean[][] visited;
  static int[][] direction = new int[][]{
          {1, -2}, {2, -1}, {2, 1}, {1, 2},
          {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}
  };
  static int res = 1;
  
  public static void howManyStep(int x, int y, boolean isRed) {
    
    for (int i = 0; i < direction.length; i++) {
      int nextX = x + direction[i][0];
      int nextY = y + direction[i][1];
      if (inBoardAndTarget(nextX, nextY)) {
        boolean nextIsRed = board[nextX][nextX] ? true : false;
        boolean isOk = (isRed && !nextIsRed || !isRed && nextIsRed)?true:false;
        if (visited[nextX][nextY]) continue;  //
        
        if (!isOk) continue;
        visited[nextX][nextY] = true;
        res++;
        // System.out.println("x " + (x + 1) + " , " + (y + 1));
        howManyStep(nextX, nextY, nextIsRed);
        // visited[x][y] = false;
      }
    }
  }
  
  public static boolean inBoardAndTarget(int x, int y) {
    if (x < 0 || y < 0 || x >= board.length || y >= board[0].length) {
      return false;
    }
    if (visited[x][y]) return false;
    return true;
  }
}

