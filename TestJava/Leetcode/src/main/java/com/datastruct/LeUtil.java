package com.datastruct;

import com.datastruct.leetcode.ListNode;
import com.datastruct.leetcode.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

public class LeUtil {
    // 构建一个链表
    public static ListNode buildListNode(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }
        ListNode fakeHead = new ListNode(-1, null);
        ListNode p = fakeHead;
        for (int num : nums) {
            p.next = new ListNode(num);
            p = p.next;
        }
        return fakeHead.next;
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
     * 按照左右子树的数组来构建
     *   TreeNode root = LeUtil.buildTree(new Integer[]{1, 2, 3, null, 5, 6, 7});
     *   Assert.assertTrue(TreeNode.inorder(root).contains("2 5 1 6 3 7"));
     *
     * @param nums
     * @return
     */
    public static TreeNode buildBinaryTree(Integer[] nums) {
        if (nums == null || nums.length == 0) {
            throw new RuntimeException("Error Input");
        }
        TreeNode root = new TreeNode(nums[0]);
        Queue<TreeNode> queue = new LinkedList<TreeNode>() {{
            add(root);
        }};
        int i = 1, len = nums.length;
        while (!queue.isEmpty() && i < len) {
            TreeNode poll = queue.poll();
            if (i < len && nums[i] != null) {
                poll.left = new TreeNode(nums[i]);
                queue.add(poll.left);
            }
            i++;
            if (i < len && nums[i] != null) {
                poll.right = new TreeNode(nums[i]);
                queue.add(poll.right);
            }
            i++;
        }
        return root;
    }
}
