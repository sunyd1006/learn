package com.company.tencent;


import java.io.*;
import java.util.*;

public class Main3 {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc = new Scanner(System.in);
//        Scanner sc = new Scanner(new File("/Users/sunyindong/Downloads/all_file/ShuaTi/src/main/resources/input.txt"));
		while (sc.hasNext()) {
			int T = sc.nextInt();
			for (int i = 0; i < T; i++) {
				int n = sc.nextInt();
				int[] time = new int[n];
				for (int j = 0; j < n; j++) {
					time[j] = sc.nextInt();
				}
				System.out.println(minTime(time, n));
			}
		}
	}
	
	public static int minTime(int[] time, int n) {
		if (n == 1) return time[0];
		if (n == 2) return Math.max(time[0], time[1]);
		Arrays.sort(time);
		int a;
		int sum = 0;
		for (a = n - 1; a > 2; a -= 2) {
			if ((time[0] + time[1] + time[1] + time[a]) < (time[0] + time[0] + time[a - 1] + time[a])) {
				sum = sum + time[0] + time[1] + time[1] + time[a];
			} else {
				sum = sum + time[0] + time[0] + time[a - 1] + time[a];
			}
		}
		if (a == 2) {
			sum = sum + time[0] + time[1] + time[2];
		} else if (a == 1) {
			sum = sum + time[a];
		} else {
			sum = sum + time[0];
		}
		return sum;
	}
}
