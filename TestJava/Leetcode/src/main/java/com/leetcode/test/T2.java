package com.leetcode.test;

import com.datastruct.leetcode.ListNode;

/**
 * <p>xx</p>
 * <li>xx</li>
 *
 * @Author: sunyindong.syd
 * @Date: 2025/1/17 00:19
 */
public class T2 {

  // 翻转链表的一部分
  public static ListNode reverse(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;
    while (curr != null) {
      ListNode next = curr.next;
      curr.next = prev;
      prev = curr;
      curr = next;
    }
    return prev;
  }

  // 递归地翻转每对连续0之间的链表
  public static ListNode reverseBetweenZeros(ListNode head) {
    // 递归终止条件
    if (head == null || head.next == null) {
      return head;
    }

    // 递归处理后续部分
    head.next = reverseBetweenZeros(head.next);

    // 如果当前节点是0，查找下一个0
    if (head.val == 0 && head.next != null && head.next.val == 0) {
      ListNode start = head.next;
      ListNode end = start.next;

      // 翻转0和0之间的部分
      start.next = reverseBetweenZeros(end);

      // 连接反转后的链表
      head.next = start;
    }

    return head;
  }

  // 输出链表
  public static void printList(ListNode head) {
    ListNode temp = head;
    while (temp != null) {
      System.out.print(temp.val + " ");
      temp = temp.next;
    }
    System.out.println();
  }

  public static void main(String[] args) {
    // 构造链表：0 -> 3 -> 2 -> 1 -> 0 -> 3 -> 4
    ListNode head = new ListNode(0);
    head.next = new ListNode(3);
    head.next.next = new ListNode(2);
    head.next.next.next = new ListNode(1);
    head.next.next.next.next = new ListNode(0);
    head.next.next.next.next.next = new ListNode(3);
    head.next.next.next.next.next.next = new ListNode(4);

    System.out.println("Original list:");
    printList(head);

    // 翻转每对0之间的部分
    ListNode resultList = reverseBetweenZeros(head);

    System.out.println("Reversed list:");
    printList(resultList);
  }
}
