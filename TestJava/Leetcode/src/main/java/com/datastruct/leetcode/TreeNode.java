package com.datastruct.leetcode;


import com.datastruct.Util;
import org.junit.Assert;

import javax.sound.midi.Soundbank;
import java.util.Deque;
import java.util.LinkedList;

public class TreeNode {
  public int val;
  public TreeNode left;
  public TreeNode right;

  public TreeNode() {
  }

  public TreeNode(int val) {
    this.val = val;
  }

  public TreeNode(int val, TreeNode left, TreeNode right) {
    this.val = val;
    this.left = left;
    this.right = right;
  }

  public static String printPreOrder(TreeNode node) {
    System.out.print("PreOrder: ");
    String res = preorder(node);
    System.out.println(res);
    return res;
  }

  public static String printInOrder(TreeNode node) {
    System.out.print("InOrder: ");
    String res = inorder(node);
    System.out.println(res);
    return res;
  }

  public static String preorder(TreeNode node) {
    if (node == null) return "";
    String res = "";
    res += (node.val + " ");
    res += preorder(node.left);
    res += preorder(node.right);
    return res;
  }

  public static String inorder(TreeNode node) {
    if (node == null) return "";
    String res = "";
    res += inorder(node.left);
    res += (node.val + " ");
    res += inorder(node.right);
    return res;
  }

  static String NULL = "NULL";
  static String SEP = ",";

  public static String printBFS(TreeNode root) {
		StringBuilder sb = new StringBuilder();
		if (root == null) {
			sb.append(NULL);
		} else {
			Deque<TreeNode> queue = new LinkedList<>();
			queue.add(root);
      int currentLevel = 1;
			while (!queue.isEmpty()) {

				TreeNode poll = queue.poll();
        if (poll != null) {
          sb.append(poll.val).append(SEP);
          queue.add(poll.left);
          queue.add(poll.right);
        } else {
          sb.append(NULL).append(SEP);
        }

        if (--currentLevel == 0) {
          sb.append("\n");
          currentLevel = queue.size();
        }
			}
			System.out.println(sb);
		}
		return sb.toString();
	}


  public static void main(String[] args) {
    TreeNode root = Util.buildBinaryTree(new Integer[]{1, 2, 3, null, 5, 6, 7});
    // Assert.assertTrue(TreeNode.inorder(root).contains("2 5 1 6 3 7"));
		//
    // TreeNode.printPreOrder(root);
    // Assert.assertTrue(TreeNode.preorder(root).contains("1 2 5 3 6 7"));

		TreeNode.printBFS(root);
		// Assert.assertTrue(TreeNode.preorder(root).contains("1 2 5 3 6 7"));
  }
}

