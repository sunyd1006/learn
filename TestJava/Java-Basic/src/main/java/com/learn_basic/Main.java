package com.learn_basic;

class ListNode {
   int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }


public class Main {

    public static void main(String[] args) {
        ListNode l4 = new ListNode(4, null);
        ListNode l3 = new ListNode(4, l4);
        ListNode l2 = new ListNode(4, l3);
        ListNode l1 = new ListNode(4, l2);


        Main main = new Main();
        ListNode reverse = main.reverse(l1);
        main.print(reverse);
    }


    public ListNode reverseKGroup(ListNode head, int k) {
        // write your code here.
        ListNode fake = new ListNode(-1, head), p = head, last = fake;
        while (p != null) {
            // 1. find next head, store it to p
            int tmpk = k;
            ListNode oldp = p, prep = null;
            while (tmpk-- > 0 && p != null) {
                prep = p;
                p = p.next;
            }
            if (p == null) {
                break;
            }

            // 2 cut last item
            prep.next = null;

            // 3 reverse it
            ListNode xx = reverse(oldp);

            // 4. connect it
            last.next = prep;
            last = oldp;
        }
        return fake.next;
    }

    public  ListNode reverse(ListNode head) {
        ListNode fake = new ListNode(-1, head), p = head;
        while (p != null) {
            ListNode next = p.next;
            p.next = fake.next;
            fake.next = p;
            p = next;
        }
        return fake.next;
    }

    public void print(ListNode p) {
        while (p != null) {
            System.out.print(p.val + ", ");
        }
        System.out.println("");
    }
}