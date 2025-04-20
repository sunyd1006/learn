package com.tmp;

import com.datastruct.LeUtil;
import com.datastruct.leetcode.ListNode;
import com.datastruct.leetcode.Pair;

import javax.rmi.CORBA.Util;
import javax.swing.*;
import java.nio.channels.Pipe;
import java.util.Comparator;
import java.util.List;


/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {

  int processPipes(List<Integer> pipes, int h) {
    if (pipes == null || pipes.size() == 0) return 0;
    pipes.sort(Comparator.comparingInt(a -> a));

    int left = 1, right = h;
    int minSpeed = 0;
    while (left <= right) {
      int mid = left + (right - left) / 2;
      Pair<Boolean, Integer> of = processAll(pipes, pipes[mid], h);
      if (of.getFirst())
    }
    return 0;
  }

  Pair<Boolean, Integer> processAll(List<Integer> pipes, int speed, int h) {
    int totalH = 0;
    for (int i = 0; i < pipes.size(); i++) {
      int cur = pipes.get(i);
      int sum = cur * (1 + speed) / 2;
      if (cur > sum) {
        return Pair.of(false, Integer.MAX_VALUE);
      }

      int speedBak = speed;
      while (cur > 0) {
        cur -= speedBak;
        speedBak--;
        totalH++;
      }
    }
    if (totalH <= h) return Pair.of(true, totalH);
    return Pair.of(false, totalH);
  }


  public static void main(String[] args) {

  }
}
