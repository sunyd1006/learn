package com.company;

public class MainAli {
    int[] stack;
    int capacity;
    int size;
    int ptr;
    MainAli(){
        capacity = 2;
        stack = new int[capacity];
        size = 0;
        ptr = 0;
    }
    MainAli(int capacity) throws StackException {
        if(capacity<=0 ) throw new StackException("Capactiy must be positve! ");
        this.capacity = capacity;
        stack = new int[capacity];
        this.size = 0;
        this.ptr = 0;
    }

    public int pop() throws StackException {
        if(size==0)  throw new StackException("Stack is empty!");
        size--;
        ptr = (ptr - 1 + capacity)%capacity;
        return stack[ptr];
    }

    public int push(int val){
        if(capacity==0 ) return  -1;
        if(size==capacity){
            stack[ptr] = val;
        }else{
            stack[ptr] = val;
            size++;
        }
        ptr = (ptr+1 + capacity)%capacity;
        return  ptr;
    }

    public static void main(String[] args) throws StackException {
        MainAli demo = new MainAli(2);

//        case 1, capacity = 0
//        StackDemo demo = new StackDemo(0);

//        case 2 , 空stack 直接pop
//        System.out.println(demo.pop());

//        case 3
        demo.push(1);
        demo.push(2);
        System.out.println(demo.pop());     // 2
        System.out.println(demo.pop());     // 1
        demo.push(3);
        demo.push(4);
        demo.push(5);
        System.out.println(demo.pop());     // 5
        System.out.println(demo.pop());     // 4
    }

    class StackException extends Exception {
        public StackException(String message){
            super(message);
        }
    }
}









