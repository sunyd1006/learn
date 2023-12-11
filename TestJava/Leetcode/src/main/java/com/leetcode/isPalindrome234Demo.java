package com.leetcode;

import com.ListNode;

public class isPalindrome234Demo {
    public static void main(String[] args) {
        isPalindrome234Demo isPalindrome234Demo = new isPalindrome234Demo();
        
    }
    
    /**
     * 234，回文链表
     *
     * @param head
     * @return
     */
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        
        // 双指针找中点
        ListNode dummy = new ListNode(-1, head);
        ListNode low = dummy, fast = dummy;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            low = low.next;
        }
        
        // 更改链表结构, low.next此时代表下一个指针
        ListNode newHead = reverseLinkedList(low.next);
        low.next = null;
        
        // 遍历结构
        ListNode left = head, right = newHead;
        boolean res = true;
        while (left != null && right != null) {
            if (left.val != right.val) {
                res = false;
            }
            left = left.next;
            right = right.next;
        }
        
        // 恢复链表
        ListNode oldHead = reverseLinkedList(newHead);
        // 连接左右
        low.next = oldHead;
        return res;
    }
    
    public ListNode reverseLinkedList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode newHead = reverseLinkedList(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
}



