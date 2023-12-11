package com.tree;

import java.util.*;
import java.io.*;

class Node {
	public int val;
	public Node right;
	public Node left;
	
	public Node(int val) {
		this.val = val;
	}
}

// https://www.nowcoder.com/questionTerminal/c37ec6a9e4084b9c943be2d3a369e177?f=discussion
// 在二叉树中，找一个节点的后继节点。主要要建树，然后查找，建树比较麻烦
public class Main_find_next_node {
	public static void main(String[] args) throws FileNotFoundException {
		// Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		Node head = constructTree(sc, n);
		int k = sc.nextInt();
		// Node res = findNext(head, k);
		findNextSun(head, k);
		if (realRes == null)
			System.out.println(0);
		else
			System.out.println(realRes.val);
	}
	
	public static Node constructTree(Scanner sc, int n) {
		HashMap<Integer, Node> map = new HashMap<>();
		int rootVal = sc.nextInt();
		Node root = new Node(rootVal);
		map.put(rootVal, root);
		for (int i = 0; i < n; i++) {
			int nodeVal = sc.nextInt();
			int leftVal = sc.nextInt();
			int rightVal = sc.nextInt();
			if (map.containsKey(nodeVal)) {
				Node tmpNode = map.get(nodeVal);
				Node leftNode = leftVal == 0 ? null : new Node(leftVal);
				Node rightNode = rightVal == 0 ? null : new Node(rightVal);
				tmpNode.left = leftNode;
				tmpNode.right = rightNode;
				if (leftVal != 0)
					map.put(leftVal, leftNode);
				if (rightVal != 0)
					map.put(rightVal, rightNode);
			}
		}
		return root;
	}
	
	static Node pre = null;
	static Node realRes = null;
	public static void findNextSun(Node head, int k) {
		if (head == null) return;
		findNextSun(head.left, k);
		
		if (pre != null && pre.val == k) {
			realRes = head;
		}
		pre = head;
		
		findNextSun(head.right, k);
	}
}
//
// 45 1
// 1 13 17
// 13 38 23
// 38 34 21
// 34 26 32
// 26 0 5
// 5 0 6
// 6 0 0
// 32 25 0
// 25 35 37
// 35 0 0
// 37 43 42
// 43 0 0
// 42 0 0
// 21 19 0
// 19 28 7
// 28 39 9
// 39 30 45
// 30 41 12
// 41 0 0
// 12 0 0
// 45 0 8
// 8 0 0
// 9 2 10
// 2 31 0
// 31 0 36
// 36 0 0
// 10 0 0
// 7 0 0
// 23 16 27
// 16 0 0
// 27 40 44
// 40 0 0
// 44 0 0
// 17 18 15
// 18 29 11
// 29 20 4
// 20 0 0
// 4 0 0
// 11 0 22
// 22 0 0
// 15 14 24
// 14 33 0
// 33 0 3
// 3 0 0
// 24 0 0
// 12