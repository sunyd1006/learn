package com.company.tencent;

import java.io.*;
import java.util.*;

class TreeNode {
    int val = 0;
    TreeNode left = null;
    TreeNode right = null;
    TreeNode(int v){ val = v; }
    
    public static String printNode(TreeNode root){
        Deque<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        StringBuilder res = new StringBuilder("");
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                TreeNode tmp = queue.pollFirst();
                res.append(tmp.val+" ");
                if (tmp.left != null) {
                    queue.add(tmp.left);
                }
                if (tmp.right != null) {
                    queue.add(tmp.right);
                }
            }
        }
        return res.toString();
    }
}

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //        Scanner sc = new Scanner(new File("/Users/sunyindong/Downloads/quit/ShuaTi/src/main/resources/input
        //        .txt"));
        //        Scanner sc = new Scanner(System.in);

        Main mymain = new Main();
        TreeNode root = new TreeNode(1);
        TreeNode left = new TreeNode(2);
        TreeNode right = new TreeNode(3);
        
        
//        root.left = left;
//        System.out.println("------------");
//        System.out.println(TreeNode.printNode(root));
//        System.out.println(TreeNode.printNode(mymain.solve(root)));
//        
//        root.left = left;
//        root.right = right;
//        System.out.println("------------");
//        System.out.println(TreeNode.printNode(root));
//        System.out.println(TreeNode.printNode(mymain.solve(root)));

        TreeNode four = new TreeNode(4);
        TreeNode five= new TreeNode(5);
        root.left = left;
        root.right = right;
        root.left.left = four;
        root.left.right = five;
        
        System.out.println("------------");
        System.out.println(TreeNode.printNode(root));
        System.out.println(TreeNode.printNode(mymain.solve(root)));

        TreeNode six= new TreeNode(6);
        TreeNode seven = new TreeNode(7);
        root.left = left;
        root.right = right;
        root.left.left = four;
        root.left.right = five;
        root.right.left = six;

        System.out.println("------------");
        System.out.println(TreeNode.printNode(root));
        System.out.println(TreeNode.printNode(mymain.solve(root)));

        root.left = left;
        root.right = right;
        root.left.left = four;
        root.left.right = five;
        root.right.left = six;
        root.right.right = seven;
        
        System.out.println("------------");
        System.out.println(TreeNode.printNode(root));
        System.out.println(TreeNode.printNode(mymain.solve(root)));
        
        TreeNode eight = new TreeNode(8);

        root.left = left;
        root.right = right;
        root.left.left = four;
        root.left.right = five;
        root.right.left = six;
        root.right.right = seven;
        root.left.left.left = eight;

        System.out.println("------------");
        System.out.println(TreeNode.printNode(root));
        System.out.println(TreeNode.printNode(mymain.solve(root)));
    }

    public TreeNode solve(TreeNode root) {
        // write code here
        Deque<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        boolean isFrist = true;
        int lavel = 1;
        int whichLavelShouldBeRetain = 1;
        int currentNeed = 1;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            if(lavel>1) currentNeed *= 2;
            if (!isFrist && sz!= currentNeed) {
                whichLavelShouldBeRetain = lavel - 1;
                break;
            }
            for (int i = 0; i < sz; i++) {
                TreeNode tmp = queue.pollFirst();
                if (tmp.left != null) {
                    queue.add(tmp.left);
                }
                if (tmp.right != null) {
                    queue.add(tmp.right);
                }
            }
            
            whichLavelShouldBeRetain = lavel;
            lavel++;
            
            // 接下来要遍历 lavel
            isFrist = false;
        }

        queue.clear();
        queue.add(root);
        lavel = 1;
        while (!queue.isEmpty()) {
            int sz = queue.size();
            for (int i = 0; i < sz; i++) {
                if (lavel == whichLavelShouldBeRetain) {
                    // 到达指定层次
                    TreeNode tmp = queue.pollFirst();
                    tmp.left = null;
                    tmp.right = null;
                } else {
                    TreeNode tmp = queue.pollFirst();
                    if (tmp.left != null) {
                        queue.add(tmp.left);
                    }
                    if (tmp.right != null)  {
                        queue.add(tmp.right);
                    }
                }
            }
            lavel++;
        }
        return root;
    }
}