package com.datastruct.labuladong;

import java.util.Arrays;
import org.junit.Assert;

// https://labuladong.online/algo/data-structure-basic/cycle-array/#%E4%BB%A3%E7%A0%81%E5%AE%9E%E7%8E%B0

/**
 * 数组的Index区间是 [start, end), start和end一定在arr的有效索引范围内
 *
 * 在头部查询:   return arr[ start ];
 * 在尾部查询:   return arr[ (end - 1 + size) % size ] ;
 *
 * 在数组头部添加：
 *      start = (start - 1 + size);
 *      arr[ start ] = value;
 * 在数组头部删除:
 *      arr [ start ] = null;
 *      start = (start + 1 ) % size;
 *
 * 在数组尾部添加:
 *      arr [ end % size ] = val;
 *      end = ( end + 1 ) % size ;
 * 在数组尾部删除:
 *      end = ( end - 1 + size ) % size ;       // 因为是开区间，所以先左移；同时凡是 -1 都要+size
 *      arr [ end ] = null;
 */
public class CycleArray {
    int[] arr;
    int start, end, size;

    CycleArray(int initArrSize) {
        arr = new int[5];
        start = end = 0;
        size = initArrSize;
    }

    public void AddFirst(int i) {
        start = (start - 1 + size) % size;
        arr[start] = i;
    }
    public void removeFirst() {
        arr[start] = -1;
        start = (start + 1) % size;
    }

    public void AddLast(int i) {
        arr[end % size] = i;
        end = ( end + 1 ) % size ;
    }

    public void removeLast() {
        end = (end - 1 + size) % size;
        arr[end] = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CycleArray that = (CycleArray) o;
        return size == that.size &&
                Arrays.equals(arr, that.arr);
    }

    public String toString() {
        return Arrays.toString(arr);
    }

    public static void main(String[] args) {
        CycleArray e = new CycleArray(5);
        CycleArray i = CycleArray.Init(new int[] {6, 2, 3, 4, 5});

        e.AddLast(1);
        e.AddLast(2);
        e.AddLast(3);
        e.AddLast(4);
        e.AddLast(5);
        e.AddLast(6);
        System.out.println(e);
        System.out.println(i);

        Assert.assertTrue(e.equals(i));
    }

    public static CycleArray Init(int[] that) {
        if (that == null || that.length  == 0) return null;
        CycleArray res = new CycleArray(that.length);

        res.start = res.end = 0;
        res.size = that.length;
        for (int i = 0; i < that.length; i++) {
            res.arr[i] = that[i];
        }
        return res;
    }
}
