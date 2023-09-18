package com.tmp;

import com.TreeNode;
import org.aspectj.weaver.StandardAnnotation;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
	static String path = "/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/";
	
	public static void main(String[] args) throws FileNotFoundException {
		// tx1();
		// tx2();
		tx3();
		// tx4();
	}
	
	static Stack<Long> numStk = new Stack<>();
	public static void tx3() throws FileNotFoundException {
		// Scanner sc = new Scanner(new File(path + "input.txt"));
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		System.out.println(helper(line));
	}
	
	private static long helper(String str) {
		if (str.length() == 0) {
			return 0L;
		}
		char sign = '+';
		char pre = '+';
		long sum = 0L;
		for (int i = 0; i < str.length(); i++) {
			char cur = str.charAt(i);
			if (cur >= '0' && cur <= '9') {
				sum = sum * 10 - '0' + cur;
			}
			
			if (i == str.length() - 1 || cur < '0' || cur > '9') {
				if (sign == '+') numStk.push(sum);
				else if (sign == 'x') {
					if (cur != '@') {
						numStk.push(numStk.pop() * sum);
					} else {
						numStk.push(sum);
					}
				} else if (sign == '@') {
					long tmp = numStk.pop();
					numStk.push(tmp | (tmp + sum));
					if (pre == 'x') {
						numStk.push(numStk.pop() * numStk.pop());
					}
				}
				pre = sign;
				sign = cur;
				sum = 0L;
			}
		}
		sum = 0L;
		while (!numStk.isEmpty()) sum += numStk.pop();
		return sum;
	}
	
	// public static void tx3() throws FileNotFoundException {
	// 	// Scanner sc = new Scanner(new File(path + "input.txt"));
	// 	Scanner sc = new Scanner(System.in);
	//
	// 	String line = sc.nextLine();
	// 	StringBuilder builder = new StringBuilder(line);
	// 	int idxA = builder.toString().indexOf('@');
	// 	while (idxA >= 0) {
	// 		int i1 = computeA(builder.toString(), idxA - 1, idxA + 1);
	// 		builder.replace(idxA - 1, idxA + 2, i1 + "");
	// 		idxA = builder.toString().indexOf('@');
	// 	}
	//
	// 	idxA = builder.toString().indexOf('x');
	// 	while (idxA >= 0) {
	// 		int i1 = computeX(builder.toString(), idxA - 1, idxA + 1);
	// 		builder.replace(idxA - 1, idxA + 2, i1 + "");
	// 		idxA = builder.toString().indexOf('x');
	// 	}
	//
	// 	idxA = builder.toString().indexOf('+');
	// 	while (idxA >= 0) {
	// 		int i1 = computePlus(builder.toString(), idxA - 1, idxA + 1);
	// 		builder.replace(idxA - 1, idxA + 2, i1 + "");
	// 		idxA = builder.toString().indexOf('+');
	// 	}
	//
	// 	System.out.println(builder);
	// }
	//
	// private static int computePlus(String line, int left, int right) {
	// 	int a = line.charAt(left) - '0';
	// 	int b = line.charAt(right) - '0';
	// 	return a + b;
	// }
	//
	// private static int computeX(String line, int left, int right) {
	// 	int a = line.charAt(left) - '0';
	// 	int b = line.charAt(right) - '0';
	// 	return a * b;
	// }
	//
	// private static int computeA(String line, int left, int right ) {
	// 	int a = line.charAt(left) - '0';
	// 	int b = line.charAt(right) - '0';
	// 	int sum = a + b;
	// 	return a | sum;
	// }
	
	static Map<Integer, TreeNode> mapKey2Node;
	static Map<Integer, Pair> father2Node;
	static class Pair {
		TreeNode father;
		boolean isLeftFather;
		
		public Pair(TreeNode father, boolean isLeftFather) {
			this.father = father;
			this.isLeftFather = isLeftFather;
		}
	}
	
	public TreeNode solve(TreeNode root, int[][] b) {
		mapKey2Node = new HashMap<>();
		father2Node = new HashMap<>();
		dfsForMap(root);
		for (int[] outer : b) {
			TreeNode left = mapKey2Node.get(outer[0]);
			TreeNode right = mapKey2Node.get(outer[1]);
			boolean isFather = isFather(left, right);
			if (!isFather) { // ee: 非法就跳过
				Pair lePair = father2Node.get(left.val);
				Pair rePair = father2Node.get(right.val);
				if (lePair.isLeftFather) {
					lePair.father.left = right;
					father2Node.put(right.val, new Pair(lePair.father, true));
				} else {
					lePair.father.right = right;
					father2Node.put(right.val, new Pair(lePair.father, false));
				}
				if (rePair.isLeftFather) {
					rePair.father.left = left;
					father2Node.put(left.val, new Pair(rePair.father, true));
				} else {
					rePair.father.right = left;
					father2Node.put(left.val, new Pair(rePair.father, false));
				}
			}
		}
		return root;
	}
	
	public void dfsForMap(TreeNode root) {
		if (root == null) return;
		if (root.left != null) {
			dfsForMap(root.left);
			father2Node.put(root.left.val, new Pair(root, true));
		}
		mapKey2Node.put(root.val, root);
		if (root.right != null) {
			dfsForMap(root.right);
			father2Node.put(root.right.val, new Pair(root, false));
		}
	}
	
	public boolean isFather(TreeNode a, TreeNode b) {
		if (a == null) return false;
		return a.val == b.val || isFather(a.left, b) || isFather(a.right, b);
	}
	
	@Test
	public void test4() {
		int[][] ints = {
						{1, 2}, {2, 3}, {1, 7}};
		TreeNode treeNode = TreeNode.buildTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
		TreeNode resNode = solve(treeNode, ints);
		
	}
	
	// --------------------
	public static void tx1() throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path + "input.txt"));
		// Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		while (n > 0) {
			n -= 1;
			int num = sc.nextInt();
			int a = 1 + num;
			while (findMin(a) < 1 + num) {
				a += 1;
			}
			int b = a + num;
			while (findMin(b) < a + num) {
				b += 1;
			}
			System.out.println(a * b);
		}
	}
	
	private static int findMin(int n) {
		for (int i = 2; i <= (int) Math.sqrt(n); i++) {
			if (n % i == 0) {
				return i;
			}
		}
		return n;
	}
	
	// ------------------------
	// 1
	// 7
	// 5 4 4 2 2 5 9
	public static void tx2() throws FileNotFoundException {
		Scanner sc = new Scanner(new File(path + "input.txt"));
		// Scanner sc = new Scanner(System.in);
		int T = sc.nextInt();
		while (T-- > 0) {
			int n = sc.nextInt();
			int[] nums = new int[n];
			int tmpN = n, i = 0;
			while (tmpN-- > 0) {
				nums[i++] = sc.nextInt();
			}
			int max = 0;
			// for (int j = n - 1; j >= 0; j--) {
			// 	if (nums[j] + j < n) {
			// 		nums[j] += nums[j + nums[j]];
			// 	}
			// 	max = Math.max(max, nums[j]);
			// }
			System.out.println(max);
		}
	}
}

//


/**
 *
 */