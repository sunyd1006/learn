package com.lee.tree;

import java.io.*;
import java.util.*;

/**
 * 并查集
 *
 * 用 root 表示每个人都隶属于那个帮派
 * 用 findRoot 去查找自己属于那个帮派
 * 注意要对新来的元素 ， 建立自己属于那个帮派  root[cur] = findRoot(yourPair)
 */
public class Main_bing_cha_ji {
	static int[] root;
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
		// Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		root = new int[n+1];
		int[] weights = new int[n+1];
		for (int i = 1; i <= n; i++) {
			root[i] = i;
		}
		while ((m--) > 0) {
			int left = sc.nextInt();
			int right = sc.nextInt();
			root[right] = findRoot(left);  // 关联帮派
		}
		for (int i = 1; i <= n; i++) {
			weights[i] = sc.nextInt();
		}
		int max = 0;
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 1; i <= n; i++) {
			Integer orDefault = map.getOrDefault(findRoot(i), 0);
			int newValue = orDefault + weights[i];
			map.put(findRoot(i), newValue);
			max = newValue > max ? newValue : max;
		}
		System.out.println(max);
	}
	
	public static int findRoot(int x) {
		if (root[x] == x)
			return x;
		else
			return findRoot(root[x]);
	}
}

// 6 5
// 1 2
// 2 3
// 3 4
// 4 5
// 5 6
// 10 4 3 20 5 12

// 54


// 6 3
// 1 2
// 2 3
// 4 5
// 11 2 3 15 8 22

// 23