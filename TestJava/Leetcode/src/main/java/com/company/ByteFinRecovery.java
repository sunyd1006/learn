package com.company;


import com.ListNode;
import org.junit.Test;

public class ByteFinRecovery {
    public static void main(String[] args) {
        System.out.println("haha");
    }
    
    @Test
    public void findMinValueFromVArray(){
        System.out.println("haha");
        int[] test = {9, 7, 5, 1, 3, 6, 8};
        System.out.println(findMin(test));
    
        int[] test2 = new int[]{9, 7, 5, 1, 3};
        System.out.println(findMin(test2));
    
        int[] test3 = new int[]{5, 1, 3, 6, 8};
        System.out.println(findMin(test3));
    
        int[] test4 = new int[]{9, 7, 5, 1};
        System.out.println(findMin(test4));
    }
    
    public int findMin(int[] nums){
        if(nums==null || nums.length==0) return -1;
        int sz = nums.length;
        int left=0, right=nums.length-1;
        while(left<=right){
            int mid = left + (right-left)/2;
            if(mid+1<sz && nums[mid]>nums[mid+1]){
                left = mid+1;
            }else if( mid-1>=0 && nums[mid]>nums[mid-1] ){
                right = mid-1;
            }else{
                return nums[mid];
            }
        }
        return nums[left];
    }
    
    
    @Test
    public void reverseListAndWapDemo(){
        ListNode one4 = new ListNode(4, null);
        ListNode one3 = new ListNode(3, one4);
        ListNode one2 = new ListNode(2, one3);
        ListNode one1 = new ListNode(1, one2);
        
        System.out.println("---------");
        
        ListNode ptr =  reverseListAndWap(one1) ;
        while(ptr!=null){
            System.out.print(ptr.val+" ");
            ptr= ptr.next;
        }
    }
    
    public ListNode reverseListAndWap(ListNode head){
        ListNode tmp = new ListNode(0, head);
        ListNode low=tmp, fast=tmp;
        
        // 快慢指针
        while(fast.next!=null){
            fast = fast.next.next;
            low = low.next;
        }
        
        // System.out.println("haha " + low.val);
        
        // 断开链表
        ListNode rightHead = low.next;
        low.next=null;
        
        // System.out.println(low.val);
        
        // 翻转第二个链表
        ListNode newRightHead = reverseList(rightHead);
        
        // 合并链表
        ListNode dummp = new ListNode(-1, null);
        ListNode left = head, right = newRightHead;
        ListNode p = dummp;
        
        int flag=0;
        while(left!=null && right!=null){
            if(flag%2==0){
                p.next = left;
                p = p.next;
                left=left.next;
            }else{
                p.next = right;
                p = p.next;
                right=right.next;
            }
            flag++;
        }
        
        while(left!=null){
            p.next = left;
            p = p.next;
            left=left.next;
        }
        
        while(right!=null){
            p.next = right;
            p = p.next;
            right=right.next;
        }
        return dummp.next;
    }
    
    public static ListNode reverseList(ListNode head){
        if(head==null || head.next==null) return head;
        ListNode res = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return res;
    }
}

