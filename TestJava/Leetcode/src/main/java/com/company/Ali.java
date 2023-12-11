package com.company;

import java.util.*;
import java.io.*;

//4 6
//1 2 3 a b c
//3 4
//n k 6 9

public class Ali {
    public static List<String> numberRes = new LinkedList<>();

    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(new File("/Users/sunyindong/Downloads/quit/ShuaTi/src/main/resources/input.txt"));
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            numberRes.clear();
            int m = sc.nextInt();
            int n = sc.nextInt();
            sc.nextLine();      // 去换行
            String[] str = sc.nextLine().split(" ");
            Arrays.sort(str);
            StringBuilder realBuffer = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                realBuffer.append(str[i]);
            }
            String realStr = realBuffer.toString();

            // [0, numberCount)
            int allNumber = 0;
            for (int i = 0; i < n; i++) {
                char tmp = str[i].charAt(0);
                if (Character.isDigit(tmp)) {
                    allNumber++;
                }
            }

            StringBuilder one = new StringBuilder();
            for (int needNumber = 1; needNumber <= allNumber && needNumber <= m - 2; needNumber++) {
                backtrack(realStr, m, allNumber, needNumber, 0, 0, one);
            }

            String[] res = numberRes.toArray(new String[numberRes.size()]);
            for (String oneStr : res) {
                System.out.println(oneStr);
            }
//            System.out.println("-----");
        }
    }

    public static void backtrack(String realStr, int m, int allNumber, int needNumber, int curNum,
                                 int curChar, StringBuilder one) {
        if (curNum == needNumber && curChar == (m - needNumber)) {
            //            System.out.println("tmp: " + one.toString());
            numberRes.add(one.toString());
            return;
        } else if (curNum == needNumber && curChar < (m - needNumber)) {
            String realOne = one.toString();
            int bigNumber = 0;
            if (realOne.length() > 0) {
                bigNumber = realStr.indexOf(realOne.charAt(realOne.length() - 1));
            }

            // >= , 
            int startChar = bigNumber >= allNumber ? (bigNumber + 1) : allNumber;
            for (int i = startChar; i < realStr.length(); i++) {
                one.append(realStr.charAt(i));
                backtrack(realStr, m, allNumber, needNumber, curNum, curChar + 1, one);
                one.replace(one.length() - 1, one.length(), "");
            }
        } else {
            String realOne = one.toString();
            // 只有第一个数字，需要区别对待
            int bigNumber = -1;
            if (realOne.length() > 0) {
                bigNumber = realStr.indexOf(realOne.charAt(realOne.length() - 1));
            }

            int startNum = bigNumber == -1 ?  0: bigNumber + 1;
            for (int i = startNum; i < allNumber; i++) {
                one.append(realStr.charAt(i));
                backtrack(realStr, m, allNumber, needNumber, curNum + 1, curChar, one);
                one.replace(one.length() - 1, one.length(), "");
            }
        }
    }
}
    
