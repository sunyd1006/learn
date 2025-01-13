package util;

public class Tool {
    public static void main(String[] args) {
        ListNode listNode = constructListNode(new int[]{1, 2, 3, 4});
        printListNode(listNode);
    }

    public static ListNode constructListNode(int[] arr) {
        if (arr == null) return null;
        ListNode res = new ListNode(-1, null);
        ListNode p = res;
        for (int i = 0; i < arr.length; i++) {
            p.next = new ListNode(arr[i], null);
            p = p.next;
        }
        return res.next;
    }

    public static void printListNode(ListNode head) {
        ListNode p = head;
        while (p != null) {
            System.out.printf(p.val + ", ");
            p = p.next;
        }
        System.out.println();
    }

}
