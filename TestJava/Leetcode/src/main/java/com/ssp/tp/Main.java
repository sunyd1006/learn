package com.ssp.tp;


import java.io.FileNotFoundException;
import java.util.*;

public class Main {
  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(System.in);
    int n = sc.nextInt();
    int m = sc.nextInt();
    int x = sc.nextInt();
    int y = sc.nextInt();
    int z = sc.nextInt();
    char[][] target = new char[n][m];
    sc.nextLine();

    for (int i = 0; i < n; i++) {
      String str = sc.nextLine();
      for (int j = 0; j < m; j++) {
        target[i][j] = str.charAt(j);
      }
    }

    String a = sc.nextLine();
    System.out.println(path(target, a, x, y, z));
  }

  public static long path(char[][] target, String a, int x, int y, int z){
    long count = 0;
    int row = target.length;
    int col = target[0].length;
    Map<Character, List<Integer>> map = new HashMap<>();
    for (int i = 0; i < row; i++) {
      for (int j = 0; j < col; j++) {
        List<Integer> temp = new ArrayList<>();
        temp.add(i);
        temp.add(j);
        map.put(target[i][j], temp);
      }
    }

    int p = 0;
    int q = 0;
    for (int i = 0; i < a.length(); i++) {
     count += z;
     char cur = a.charAt(i);
     List<Integer> list = map.get(cur);
     int curP = list.get(0);
     int curQ = list.get(1);
     if(curP == p || curQ == q){
       count += (long) (Math.abs(p - curP) + Math.abs(q - curQ)) * x;
       p = curP;
       q = curQ;
     }else{
       count += (long) (Math.abs(p - curP) + Math.abs(q - curQ)) * x;
       p = curP;
       q = curQ;
       count += y;
     }
    }
    return count;
  }
}
