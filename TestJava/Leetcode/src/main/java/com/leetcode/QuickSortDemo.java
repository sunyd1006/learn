package com.lee.leetcode;


import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

public class QuickSortDemo {
	public static void main(String[] args) {
		QuickSortDemo quickSortDemo = new QuickSortDemo();
		int[] nums = {1, 3, 5, 2, 4, 6};
		
		quickSortDemo.quickSort(nums, 0, nums.length - 1);
		for (int i : nums) {
			System.out.print(i + " ");
		}
	}
	
	public void quickSort(int[] nums, int left, int right) {
		if (left < right) {
			int mid = partition(nums, left, right);
			partition(nums, left, mid - 1);
			partition(nums, mid + 1, right);
		}
	}
	
	public int partition(int[] nums, int left, int right) {
		int x = nums[left];
		while (left < right) {
			while (left < right && x <= nums[right]) right--;
			nums[left] = nums[right];
			
			while (left < right && nums[left] <= x) left++;
			nums[right] = nums[left];
		}
		nums[left] = x;
		return left;
	}
	
	// 拼多多 找括号的
	public static class Main_pdd {
	    public static void main(String[] args) throws IOException {
	        // Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
	        Scanner sc = new Scanner(System.in);
	        int N = sc.nextInt();
	        String s = sc.next();
	        char[] ops = s.toCharArray();
	        LinkedList<Character> stack = new LinkedList<>();
	        int op = 0, pre = 0;
	        for (int i = 0; i < N; i++) {
	            if (ops[i] == '(') {
	                stack.add(op++, '(');
	                pre = count(stack);
	                System.out.print(pre + " ");
	            } else if (ops[i] == ')') {
	                stack.add(op++, ')');
	                pre = count(stack);
	                System.out.print(pre + " ");
	            } else if (ops[i] == 'L') {
	                op--;
	                op = Math.max(0, op);
	                System.out.print(pre + " ");
	            } else if (ops[i] == 'R') {
	                op++;
	                op = Math.min(op, stack.size());
	                System.out.print(pre + " ");
	            } else {
	                stack.remove(--op);
	                pre = count(stack);
	                System.out.print(pre + " ");
	            }
	        }
	    }
	
	    private static int count(LinkedList<Character> s) {
	        int depth = 0, max = 0, cha = 0;
	        Deque<Character> stack = new LinkedList<>();
	        for (int i = 0; i < s.size(); i++) {
	            if (s.get(i) == '(') {
	                stack.push('(');
	                depth++;
	                max = Math.max(max, depth);
	            } else {
	                if (stack.isEmpty()) {
	                    cha--;
	                } else {
	                    stack.poll();
	                    depth--;
	                }
	            }
	        }
	        if (!stack.isEmpty() || cha < 0) return cha - stack.size();
	        return max;
	    }
	}
}