package com.company;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 *  网易笔试，时空穿梭，是一个 dfs 题目
 */
public class Main_wangyi {
	static String path = "/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/";
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path + "input.txt"));
		// Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T-- > 0) {
			N = sc.nextInt();
			min = N + 5;
			int M1 = sc.nextInt();
			int M2 = sc.nextInt();
			
			paths = new HashMap<>();
			doors = new HashMap<>();
			mem = new HashSet<>();
			while (M1-- > 0) {
				int left = sc.nextInt();
				int right = sc.nextInt();
				List<Integer> leftLi = paths.getOrDefault(left, new LinkedList<>());
				List<Integer> rightLi = paths.getOrDefault(right, new LinkedList<>());
				leftLi.add(right);
				rightLi.add(left);
				paths.put(left, leftLi);
				paths.put(right, rightLi);
			}
			
			while (M2-- > 0) {
				int left = sc.nextInt();
				int right = sc.nextInt();
				List<Integer> leftLi = doors.getOrDefault(left, new LinkedList<>());
				List<Integer> rightLi = doors.getOrDefault(right, new LinkedList<>());
				leftLi.add(right);
				rightLi.add(left);
				doors.put(left, leftLi);
				doors.put(right, rightLi);
			}
			backtrack(1, -1, 0, new HashSet<>(), false);
			System.out.println(min);
		}
	}
	
	static int min, N;
	static Map<Integer, List<Integer>> paths;
	static Map<Integer, List<Integer>> doors;
	static Set<point> mem;
	
	public static void backtrack(int cur, int pre, int time, Set<Integer> havePath, boolean isDoor) {
		if (cur == N) {
			min = Math.min(min, time);
			return;
		}
		if (time > min) {
			return;
		}
		if(mem.contains(new Point(cur, pre))){
			return;
		}
		
		// biao
		for (Integer integer : paths.getOrDefault(cur, new LinkedList<>())) {
			if (integer.equals(pre) || havePath.contains(integer)) {
				continue;
			}
			havePath.add(integer);
			backtrack(integer, cur, time + 1, havePath, false);
			havePath.remove(integer);
		}
		
		// li
		List<Integer> doorLi = doors.getOrDefault(cur, new LinkedList<>());
		List<Integer> doorLiExt = new LinkedList<>(doorLi);
		doorLiExt.add(cur);
		for (Integer integer : doorLi) {
			boolean isPre = false;
			if (integer.equals(cur) && !integer.equals(pre)) {
				isPre = true;
			} else if (havePath.contains(integer)) {
				continue;
			}
			if (isPre) {
				havePath.add(integer);
				backtrack(integer, cur, time + 1, havePath, true);
				havePath.remove(integer);
			} else {
				havePath.add(integer);
				time = isDoor ? time + 1 : time;
				backtrack(integer, cur, time, havePath, true);
				havePath.remove(integer);
			}
		}
		mem.add(new point(cur, pre));
	}
}

class point{
	int left, right;
	public point(int left, int right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		point point = (point) o;
		return left == point.left && right == point.right;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(left, right);
	}
}

/**
 * 2 10 99
 */

//
//
// public static void test1() {
// 	Scanner sc = new Scanner(System.in);
// 	int T = sc.nextInt();
// 	while (T-- > 0) {
// 		int N = sc.nextInt();
// 		String NStr = String.valueOf(N);
// 		StringBuilder builder = new StringBuilder();
// 		for (int i = 0; i < NStr.length(); i++) {
// 			int charI = NStr.charAt(i) - '0';
// 			String item = Integer.toBinaryString(charI);
// 			builder.append(item);
// 		}
//
// 		String s = builder.toString();
// 		s = removePreZero(s);
// 		String reverS = new StringBuilder(s).reverse().toString();
// 		String substring = removePreZero(reverS);
//
// 		char last = substring.length() > 0 ? substring.charAt(0) : ' ';
// 		StringBuilder res = new StringBuilder();
// 		if (last != ' ') {
// 			res.append(last);
// 		}
// 		for (int i = 1; i < substring.length(); i++) {
// 			char cur = substring.charAt(i);
// 			if (last != cur) {
// 				res.append(cur);
// 				last = cur;
// 			}
// 		}
// 		System.out.println(res);
// 	}
// }
//
// 	public static String removePreZero(String string) {
// 		int oneIdx = findOneIdx(string);
// 		return string.substring(oneIdx);
// 	}
//
// 	public static int findOneIdx(String s) {
// 		for (int i = 0; i < s.length(); i++) {
// 			if (s.charAt(i) == '1') {
// 				return i;
// 			}
// 		}
// 		return s.length();
// 	}