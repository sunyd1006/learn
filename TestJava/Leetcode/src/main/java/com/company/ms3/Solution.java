package com.company.ms3;


import org.junit.jupiter.api.Test;


import java.util.*;

// 微软秋招 笔试题3 2021年09月21日11:22:56
public class Solution {
	/**
	 * {2, 4, 1, 6, 5, 9, 7},   241， 65 97
	 * {4, 3, 2, 6, 1},         43261
	 * {2, 1, 6, 4, 3, 7}       21 643 7
	 * 最少可以分成几份，使得每一分都没有更小的值。
	 */
	public int solution(int[] A) {
		if (A == null || A.length <= 1) {
			return 1;
		}
		Map<Integer, Integer> AItem2Idx = new HashMap<>();
		for (int i = 0; i < A.length; i++) {
			AItem2Idx.put(A[i], i);
		}
		int[] ACopy = A.clone();
		Arrays.sort(ACopy);
		int res = 0, lastIdx = -1;
		for (int curItem : ACopy) {
			int idx = AItem2Idx.get(curItem);
			if (idx == ACopy.length - 1) {
				res++;
				break;
			} else if (idx > lastIdx) {
				res++;
				lastIdx = Math.max(idx, lastIdx);
			}
			// if idx < lastIdx do nothing
		}
		return res;
	}
	
	@Test
	public void test2() {
		int[][] ca = {
						{2, 4, 1, 6, 5, 9, 7},
						{4, 3, 2, 6, 1},
						{2, 1, 6, 4, 3, 7}
		};
		for (int[] ints : ca) {
			System.out.println(solution(ints));
		}
	}
	
	
	public String solution1(String S) {
		String res = "NO";
		if(S==null || S.length()==0) {
			return  res;
		}
		int[] flag = new int[26];
		int[] target = new int[26];
		for (int i = 0; i < S.length(); i++) {
			char cur = S.charAt(i);
			if (Character.isLowerCase(cur)) {
				flag[cur - 'a'] = 1;
			}
			if (Character.isUpperCase(cur)) {
				target[cur - 'A'] = 1;
			}
		}
		for (int i = 25; i >=0; i--) {
			if (flag[i]==1 && target[i] == 1) {
				return String.valueOf((char) ('A' + i));
			}
		}
		return res;
	}
}