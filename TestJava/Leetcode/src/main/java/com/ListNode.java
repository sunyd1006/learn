package com;

import org.junit.jupiter.api.Test;

import javax.swing.plaf.IconUIResource;
import java.util.HashMap;
import java.util.Map;

public class ListNode {
    public int val;
    public ListNode next;
    
    public ListNode() {
    }
    
    public ListNode(int val) {
        this.val = val;
    }
    
    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
    
    @Override
    public String toString() {
        String res = val + " ";
        return res;
    }
    
    public static void printListNode(ListNode head) {
        if (head == null) return;
        ListNode p = head;
        while (p != null) {
            System.out.print(p.val + " ");
            p = p.next;
        }
        System.out.println();
    }
    
    /**
     * 重复元素，会复用
     * @param nums
     * @return
     */
    public static ListNode buildListNode(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        ListNode fakeHead = new ListNode(-1, null);
        ListNode p = fakeHead;
        Map<Integer, ListNode> map = new HashMap<>();
        for (int num : nums) {
            p.next = map.getOrDefault(num, new ListNode(num));  // 重复即用重复元素， for 链表环
            p = p.next;
            map.put(num, p);    // 加入缓存
        }
        return fakeHead.next;
    }
}