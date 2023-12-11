package com.leetcode;

import java.io.*;
import java.util.*;

// 小米
public class Main_hebingshuzi {
  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(
            new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
    // Scanner sc = new Scanner(System.in);
    while (sc.hasNext()) {
      String line = sc.nextLine();
      String[] split = line.split(",");
      String[] split1 = split[0].split("=");
      String[] split2 = split[1].split("=");
      int m = Integer.parseInt(split1[1]);
      int n = Integer.parseInt(split2[1]);

      String[] mArrayOld = new String[0];
      if (m >= 1) {
        mArrayOld = sc.nextLine().split(",");
      }

      String[] mArray = new String[m + n];
      for (int i = 0; i < mArrayOld.length; i++) {
        mArray[i] = mArrayOld[i];
      }

      String[] nArray = new String[0];
      if (n >= 1) {
        nArray = sc.nextLine().split(",");
      }
      int i = 0, j = 0;
      while (i < m && j < n) {
        int tmpM = Integer.parseInt(mArray[i]);
        int tmpN = Integer.parseInt(nArray[j]);
        if (tmpM < tmpN) {
          i++;
        } else {
          String mOld = mArray[i];
          mArray[i] = nArray[j];
          nArray[j] = mOld;
          i++;
          j++;
        }
      }
      // Arrays.sort(mArray);
      // Arrays.sort(nArray);
      j = 0;
      for (int k = m; j < n; j++) {
        mArray[k] = nArray[j];
        k++;
      }

      for (int k = 0; k < m + n; k++) {
        System.out.print(mArray[k] + " ");
      }
      System.out.println("");
    }
  }
  

}

//
// m=2,n=4
// 4,6
// 1,2,3,5
// m=0, n=0
// m=1, n=0
// 1
// m=0, n=2
// 1,2
// m=1, n=2
// 1
// 2,3
// m=2, n=1
// 1,2
// 3
// m=2,n=2
// 1,2
// 3,4
// m=4,n=2
// 1,2,3,5
// 4,6

