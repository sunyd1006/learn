package com.company;

class Node {
    Node next;
    int val;
    Node(){}
    Node(int v){val = v;}
}

public class Meituan {
    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        n1.next = n2;
        n2.next = n3;
        n3.next = n2;
        System.out.println(isCycle(n1));
    }

    public static boolean isCycle(Node node){
        if(node==null || node.next == null)
            return false;

        Node fast = node.next;
        Node low = node;
        while(fast !=null && fast.next !=null){
            if(fast.equals(low)) return true;
            fast = fast.next.next;
            low = low.next;
        }
        return false;
    }
}