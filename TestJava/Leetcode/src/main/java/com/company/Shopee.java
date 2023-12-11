package com.lee.company;

import org.junit.Test;
import java.util.*;

public class Shopee {
    @Test
    public void test2() {
        System.out.println(divide(7, 2)); // 3
        System.out.println(divide(7, 3)); // 4
        System.out.println(divide(7, 4)); // 4
    }

    // 第二题
    int res, n, k;
    public int divide(int n, int k) {
        // write code here
        this.res = 0;
        this.n = n;
        this.k = k;
        backtrack(0, 0, 0);
        return res;
    }

    public void backtrack(int nextI, int curK, int curSum) {
        if (k == curK) {
            if (curSum == n) res++;
        } else {
            int start = nextI > 1 ? nextI : 1;
            for (int i = start; i < n; i++) {
                // stack.addLast(i);
                backtrack(i, curK + 1, curSum + i);
                // stack.pollLast();
            }
        }
    }

    @Test
    public void test1() {
        System.out.println(GetXMLValue("<people><name>shopee</name></people>", "people.name"));
    }

    public String GetXMLValue(String inxml, String path) {
        // write code here
        String[] strs = path.split("\\.");
        StringBuilder builder = new StringBuilder();
        for (String str : strs) {
            builder.append('<');
            builder.append(str);
            builder.append('>');
        }
        if (builder.length() * 2 >= inxml.length() || !isBefore(builder.toString(), inxml)) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        for (int i = builder.length(); inxml.charAt(i) != '<'; i++) {
            res.append(inxml.charAt(i));
        }
        return res.toString();
    }

    public boolean isBefore(String s1, String s2) {
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    @Test
    public void test3() {
        System.out.println(findBalancedIndex(new int[]{1, 2, 3, 4, 6}));
    }

    public int findBalancedIndex(int[] inputArray) {
        // write code here
        int sz = inputArray.length;
        if (sz == 0 || sz == 1 || sz == 2) return -1;
        long sum = 0;
        for (int i = 1; i < sz; i++) {
            sum += inputArray[i];
        }
        long subSum = 0;
        for (int i = 1; i < sz; i++) {
            subSum += inputArray[i - 1];
            sum -= inputArray[i];
            if (subSum == sum) return i;
        }
        return -1;
    }
}

