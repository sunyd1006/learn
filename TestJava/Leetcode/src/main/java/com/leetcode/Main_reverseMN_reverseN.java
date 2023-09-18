package com.leetcode;


import com.ListNode;

import java.io.FileNotFoundException;

// 翻转链表节点
public class Main_reverseMN_reverseN {
	public static void main(String[] args) throws FileNotFoundException {
		ListNode four = new ListNode(4, null);
		ListNode three = new ListNode(3, four);
		ListNode two = new ListNode(2, three);
		ListNode one = new ListNode(1, two);
		ListNode zero = new ListNode(0, one);
		ListNode dummy = new ListNode(1, zero);
		Main_reverseMN_reverseN main = new Main_reverseMN_reverseN();
		printListNode(one);
		// printListNode(main.reverseMN(one, 2, 4));
		// printListNode(main.reverseN(one, 2));
		
		// 每 k 个节点翻转一次, 但最后2个无法保证
		printListNode(zero);
		printListNode(main.reverseN_forLast(zero, 3, 3));
	}
	
	public static void printListNode(ListNode head) {
		if (head == null) return;
		ListNode p = head;
		while (p != null) {
			System.out.print(p.val + "  ");
			p = p.next;
		}
		System.out.println("");
	}
	
	/**
	 * 1 2 3 4
	 * m = 2, n=4
	 * <p>
	 * 1 4 3 2
	 */
	public ListNode reverseMN(ListNode head, int m, int n) {
		if (m == 1) {
			return reverseN(head, n);
		}
		// NOTE 翻转【m, n] 等价于翻转 [0, m-n] 以0节点开始的，m-n个节点
		head.next = reverseMN(head.next, m - 1, n - 1);
		return head;
	}
	
	ListNode successor = null;
	
	public ListNode reverseN(ListNode head, int n) {
		// if (head == null || head.next == null) return head;
		if (n == 1) {
			successor = head.next;
			return head;
		}
		ListNode last = reverseN(head.next, n - 1);
		head.next.next = head;
		head.next = successor;
		return last;
	}
	
	public ListNode reverseN_forLast(ListNode head, int curK, int realK) {
		if (head == null || head.next == null) return head;
		if (curK == 1) {
			successor = reverseN_forLast(head.next, realK, realK);
			return head;
		}
		ListNode last = reverseN_forLast(head.next, curK - 1, realK);
		head.next.next = head;
		head.next = successor;
		return last;
	}
	
}


