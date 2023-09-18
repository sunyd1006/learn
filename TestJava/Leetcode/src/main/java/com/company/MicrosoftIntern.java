package com.company;

import org.junit.Test;

import java.util.PriorityQueue;

public class MicrosoftIntern {
    @Test
    public void test(){
        System.out.println(minStep(6, 6));
        System.out.println(minStep(6, 9));
        System.out.println(minStep(6, 10));
        System.out.println(minStep(6, 12));
    }
    
    
    public int minStep(int m, int n) {
        int FalseInput = -1;
        if (m < 1 || n < 1) return FalseInput;
        if(m==n) return 0;
        
        PriorityQueue<Integer> heap = new PriorityQueue<Integer>((a, b) -> {
            return b - a;
        });
        
        int start = 2;
        while (start <= m/2){
            if (m % start == 0){
                if( !heap.contains(start) ) heap.add(start);
            }
            start++;
        }
        
        int count = 0;
        int tmp_m = m;
        if (m <= n) {
            while (tmp_m < n) {
                if (tmp_m + heap.peek() <= n) {
                    tmp_m = tmp_m + heap.peek();
                } else {
                    while (!heap.isEmpty() && tmp_m + heap.peek() > n) {
                        heap.poll();
                    }
                    if (heap.isEmpty()) return FalseInput;
                    tmp_m = tmp_m + heap.peek();
                }
                
                // --
                heap.clear();
                start = 2;
                while (start <= tmp_m/2){
                    if (tmp_m % start == 0){
                        if( !heap.contains(start) ) heap.add(start);
                    }
                    start++;
                }
                
                count++;
            }
        } else {
            while (tmp_m > n) {
                if (tmp_m + heap.peek() > n) {
                    tmp_m = tmp_m - heap.peek();
                } else {
                    while (!heap.isEmpty() && tmp_m - heap.peek() < n) {
                        heap.poll();
                    }
                    if (heap.isEmpty()) return FalseInput;
                    tmp_m = tmp_m - heap.peek();
                }
                count++;
            }
        }
        
        return count;
    }
}
