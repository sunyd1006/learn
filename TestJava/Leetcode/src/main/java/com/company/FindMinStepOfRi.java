package com.company;

import java.io.File;
import java.io.IOException;
import java.util.*;

// find min of 日
public class FindMinStepOfRi {
    public static void main(String[] args) throws IOException {
        // Scanner sc = new Scanner(System.in);
        Scanner sc = new Scanner(
                new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
        while (sc.hasNext()) {
            count = -1;
            queue = new LinkedList<>();
            int w = sc.nextInt();
            int h = sc.nextInt();
            sc.nextLine();
            char[][] board = new char[w][h];
            int tmpW = 0;
            while ( tmpW < w){
                board[tmpW] = sc.nextLine().toCharArray();
                tmpW++;
            }
    
            int Hx=0, Hy=0;
            int Tx=-1, Ty=-1;
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    if(board[i][j]=='H'){
                        Hx=i;
                        Hy=j;
                        continue;
                    }
                    if(board[i][j]=='T'){
                        Tx=i;
                        Ty=j;
                        continue;
                    }
                    if(Hx!=-1 && Tx!=-1) break;
                }
            }
            
            queue.add(new int[]{Hx, Hy});
            while (!queue.isEmpty()){
                int sz = queue.size();
                count++; // ask
                while ( (sz--) > 0){
                    int[] point = queue.poll();
                    int i = point[0], j = point[1];
                    
                    if(findTarget(board, i, j)){
                        break;
                    }
    
                    if(i>=1 && i<w && j>=0 && j<h && board[i-1][j]!='#') {
                        queue.add( new int[]{ i-2, j-1} );
                        queue.add(new int[] {i-2, j+1});
                    }
    
                    if(i<w-1 && i>=0  && j<h && j>=0 && board[i+1][j]!='#') {
                        queue.add( new int[]{ i+2, j-1 } );
                        queue.add(new int[] { i+2, j+1 });
                    }
    
                    if( i>=0 && i<w && j<h && j>=1 &&  board[i][j-1]!='#' ) {
                        queue.add( new int[]{ i-1, j-2 } );
                        queue.add(new int[] { i+1, j-2});
                    }
    
                    if( i>=0 && i<w && j>=0 && j<h-1 && board[i][j+1]!='#') {
                        queue.add( new int[]{ i+1, j+2 } );
                        queue.add(new int[] { i-1, j+2});
                    }
                }
            }
            System.out.println(count);
        }
    }
    
    public static Queue<int[]> queue;
    public static int count;
    public static boolean findTarget(char[][] board, int i, int j){
        if(board==null ||  board.length==0 || board[0].length==0 || i< 0 || i>=board.length || j<0 || j>=board[0].length
                 || board[i][j] == '#') {    //
            return false;
        }
        if(board[i][j]=='T'){
            return true;
        }
        board[i][j] = '#';
        return false;
    }
    
    class CustomException extends Exception {
        public CustomException(String message) {
            super(message);
        }
    }
}












