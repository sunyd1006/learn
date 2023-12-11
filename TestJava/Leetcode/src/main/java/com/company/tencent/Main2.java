package com.company.tencent;

import java.io.*;
import  java.util.*;

public class Main2 {
    public static void main(String[] args) throws FileNotFoundException {
//        Scanner sc = new Scanner(System.in);
        Scanner sc = new Scanner(new File("/Users/sunyindong/Downloads/all_file/ShuaTi/src/main/resources/input.txt"));
        while (sc.hasNext()){
            int n = sc.nextInt();
            sc.nextLine();
            String s = sc.nextLine();
            System.out.println(minLen(s,n));
        }
    }

    public static int minLen(String s, int n){
        Stack<Character> deque = new Stack<>();
        char[] sChar = s.toCharArray();
        for (int i = 0; i < n; i++) {
            deque.add(sChar[i]);
        }
        int index = 0;
        StringBuilder sb = new StringBuilder(deque.pop().toString());
        while(!deque.isEmpty()){
            int x1 = index>=0?sb.charAt(index) - '0': 0;
            int x2 = deque.pop() - '0';
            if(x1+x2 ==10){
                index--;
                sb.deleteCharAt(index+1);
            }else {
                sb.append(x2);
                index++;
            }
        }
        return  sb.length();
    }
}
