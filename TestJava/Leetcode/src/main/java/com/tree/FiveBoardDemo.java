package com.lee.tree;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

// 五子棋原题：https://www.cnblogs.com/yalphait/p/7779222.html
// 解法参考，判断8个方向：https://blog.csdn.net/qq_28051453/article/details/51843130
public class FiveBoardDemo {
    
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(
                new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
        // // 不带缓冲的
        // Scanner sc = new Scanner(System.in);
        // // 带缓冲的
        // BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        
        while (sc.hasNext()) {
            int N = sc.nextInt();
            int[][] board = new int[N][N];
            int n = sc.nextInt();
            int hei = 0;
            while (hei++ < n) {
                int x = sc.nextInt();
                int y = sc.nextInt();
                int value = (hei - 1) % 2 == 0 ? 2 : 1;
                board[x][y] = value;
                if (checkBoard(board, x, y, value)) {
                    int whoWin = (value) % 2 == 0 ? 0 : 1;
                    System.out.println( hei + " " + whoWin);
                    return;
                }
            }
            
            // sc.nextLine() 读取掉换行符
        }
    }
    
    private static boolean checkBoard(int[][] board, int x, int y, int value) {
        int[][] up = new int[][]{
                {0, 1},
                {-1, -1},
                {-1, 0},
                {-1, -1},
                {0, -1},
                {-1, 1},
                {1, 0},
                {1, 1}
        };
        
        int sz = board.length;
        boolean flag = false;
        for (int de = 0; de < up.length; de++) {
            int count = 0;
            for (int i = x, j = y; i < sz && j < sz && i>=0 && j>=0; ) {
                if (board[i][j] == value ) {
                    count = flag == false ? 0 : count;
                    count++;
                    flag = true;
                }else{
                    count = 0;
                    flag = false;
                }
                if (count >= 5) return true;
                i += up[de][0];
                j += up[de][1];
            }
        }
        return false;
    }
    
    class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
}


   






