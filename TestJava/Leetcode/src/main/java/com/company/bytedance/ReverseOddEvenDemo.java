package com.company.bytedance;


import com.ListNode;
import org.junit.jupiter.api.Test;

/**
 * 给定一个奇数位升序，偶数位降序的链表，将其重新排序。
 * <p>
 *  输入: 1->8->3->6->5->4->7->2->NULL
 *  输出: 1->2->3->4->5->6->7->8->NULL
 * </p>
 */
public class ReverseOddEvenDemo {
	
	@Test
	public void test() {
		ListNode head = ListNode.buildListNode(new int[]{1, 8, 3, 6, 5, 4, 7, 2});
		ListNode.printListNode(head);
		ListNode even = reverseOddEven(head);
		ListNode.printListNode(even);
	}
	
	public ListNode reverseOddEven(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		
		ListNode oddHead = head, evenHead = head.next;
		ListNode odd = oddHead, even = evenHead;
		while (odd != null && even != null) {
			odd.next = even.next;
			even.next = odd.next != null ? odd.next.next : null;
			
			odd = odd.next;
			even = even.next;
		}
		
		ListNode newEvenHead = reverseList(evenHead);
		odd = oddHead;
		even = newEvenHead;
		ListNode resFake = new ListNode(-1);
		ListNode p = resFake;
		while (odd != null && even != null) {
			if (odd.val < even.val) {
				p.next = odd;
				odd = odd.next;
			} else {
				p.next = even;
				even = even.next;
			}
			p = p.next;
		}
		p.next = odd != null ? odd : even;
		return resFake.next;
	}
	
	public ListNode reverseList(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode last = reverseList(head.next);
		head.next.next = head;
		head.next = null;
		return last;
	}
}
