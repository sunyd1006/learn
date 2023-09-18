package com.company;


import org.junit.Test;

import java.util.Stack;

// WXG 技术研究1面 5道笔试题
public class Main_WXG {
	static String path = "/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/";
	
	@Test
	public void test1() {
		// System.out.println(isLucky("1"));
		System.out.println(isLucky("14"));
		System.out.println(isLucky("144"));
		System.out.println(isLucky("1411144"));
		System.out.println(isLucky("14441"));
	}
	
	public static boolean isLucky(String num) {
		if (num == null || num.length() == 0 || num.charAt(0) != '1') {
			return false;
		}
		int sz = num.length();
		int[] dp = new int[sz];
		dp[0] = 1;
		for (int i = 1; i < sz; i++) {
			for (int j = 0; j <= 2; j++) {
				int end = i + 1;
				int start = i - j;
				if (dp[i] == 1) break;
				
				if (start >= 0) {
					String item = num.substring(start, end);
					switch (item) {
						case "1":
						case "14":
						case "144": {
							if (start == 0) {
								dp[i] = 1;
							} else {
								dp[i] = dp[start - 1] == 1 ? 1 : dp[i];
							}
							break;
						}
					}
				}
			}
		}
		return dp[sz - 1] == 1;
	}
	
	@Test
	public void test2() {
		ListNode li4 = new ListNode(4, null);
		ListNode li3 = new ListNode(3, li4);
		ListNode li2 = new ListNode(2, li3);
		ListNode li1 = new ListNode(1, li2);
		System.out.println(isCycle_2(li1));
		li4.next = li2;
		System.out.println(isCycle_2(li1));
	}
	
	public static boolean isCycle_2(ListNode head) {
		if (head == null || head.next == null) return false;
		ListNode low = head, fast = head.next;
		while (fast != null && fast.next != null) {
			if (fast == low) return true;
			fast = fast.next.next;
			low = low.next;
		}
		return false;
	}
	
	@Test
	public void test3() {
		BigInteger bigInteger = new BigInteger("1233311110111321321213111");
		bigInteger.add("22003000033432423423432432432432");
		System.out.println(bigInteger);
	}
	
	public static class BigInteger {
		private String val;
		
		public void add(String need) {
			add(new BigInteger(need));
		}
		
		public void add(BigInteger need) {
			if (need == null) return;
			String needAdd = need.getVal();
			if (needAdd.equals("")) return;
			int overFlow = 0;
			if (needAdd.length() > val.length()) {
				String tmp = val;
				val = needAdd;
				needAdd = tmp;
			}
			
			needAdd = new StringBuilder(needAdd).reverse().toString();
			val = new StringBuilder(val).reverse().toString();
			
			StringBuffer buffer = new StringBuffer();
			int needAddSz = needAdd.length();
			for (int i = 0; i <= val.length(); i++) {
				char valChar = '0';
				if (i < val.length()) {
					valChar = val.charAt(i);
				}
				char needChar = '0';
				if (i < needAddSz) {
					needChar = needAdd.charAt(i);
				}
				if (!Character.isDigit(valChar) || !Character.isDigit(needChar)) {
					throw new RuntimeException("ERROR INPUT");
				}
				
				int curNum = (needChar - '0') + (valChar - '0') + overFlow;
				overFlow = curNum / 10;
				curNum = curNum % 10;
				buffer.append(curNum);
			}
			val = buffer.reverse().toString();
			preZeroTrim();
		}
		
		private void preZeroTrim() {
			for (int i = 0; i < val.length(); i++) {
				if (val.charAt(i) == '0') {
					continue;
				} else {
					val = val.substring(i);
					break;
				}
			}
		}
		
		public BigInteger() {
			this.val = "";
		}
		
		public BigInteger(String val) {
			this.val = val;
		}
		
		public String getVal() {
			return val;
		}
		
		public void setVal(String val) {
			this.val = val;
		}
		
		@Override
		public String toString() {
			return val;
		}
	}
	
	@Test
	public void test4() {
		int[] in = {1, 2, 3, 4, 5};
		int[] out = {3, 2, 4, 1, 5};
		System.out.println(isPostOrder_4(in, out));
		
		int[] out2 = {2, 4, 3, 1, 5};
		System.out.println(isPostOrder_4(in, out2));
		
		int[] out3 = {5, 3, 4, 2, 1};
		System.out.println(isPostOrder_4(in, out3));
	}
	
	public static boolean isPostOrder_4(int[] in, int[] out) {
		if (in == null || out == null) return false;
		if (in.length != out.length) return false;
		Stack<Integer> stack = new Stack<>();
		for (int i = 0, j = 0; i < out.length; i++) {
			stack.add(in[i]);
			while (!stack.isEmpty() && stack.peek().equals(out[j])) {
				stack.pop();
				j++;
			}
		}
		return stack.isEmpty();
	}
	
	@Test
	public void test5() {
		TreeNode tree22 = new TreeNode(2);
		TreeNode tree21 = new TreeNode(2);
		TreeNode tree1 = new TreeNode(1);
		TreeNode tree3 = new TreeNode(3);
		tree1.left = tree21;
		tree1.right = tree22;
		
		System.out.println(isSame_5(tree1, tree1));
		tree22.left = tree3;
		System.out.println(isSame_5(tree1, tree1));
	}
	
	public static boolean isSame_5(TreeNode left, TreeNode right) {
		if (left == null && right == null) {
			return true;
		}
		if (left == null || right == null) {
			return false;
		}
		return left.val == right.val && isSame_5(left.left, right.right) && isSame_5(left.right, right.left);
	}
	
	static class TreeNode {
		int val;
		TreeNode left, right;
		
		public TreeNode(int val) {
			this.val = val;
		}
	}
	
	static class ListNode {
		int val;
		ListNode next;
		
		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}
}

