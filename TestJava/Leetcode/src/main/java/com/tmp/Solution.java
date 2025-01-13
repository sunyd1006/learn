package com.tmp;


//	String path = "/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/";


import com.datastruct.JsonUtil;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;




class Solution {
    public String minWindow(String s, String t) {

        Map<Character, Integer> need = new HashMap<>();
        Map<Character, Integer> has = new HashMap<>();
        for (char c : t.toCharArray()) {
            need.put(c, need.getOrDefault(c, 0) + 1);
        }

        int left = 0, right = 0, meet = 0;
        String res = null;
        while (right < s.length()) {
            Character c = s.charAt(right);
            Integer orDefault = has.getOrDefault(c, 0);
            has.put(c, orDefault + 1);
            right++;

            // only == 是第一次命中
            if (need.containsKey(c) && has.get(c).equals(need.get(c))) {
                meet = meet + 1;
            }

            System.out.println(String.format("left: %s, right: %s", left, right));
            System.out.println(String.format("      meet: %s, need: %s, \n       has: %s", meet, JsonUtil.toJson(need), s.substring(left, right)));

            while (meet == need.size()) {
                if (res == null || res.length() > right - left) {
                    res = s.substring(left, right);
                }
                Character c1 = s.charAt(left);
                has.put(c1, has.get(c1) - 1);
                left++;

                if (need.containsKey(c1) && has.get(c1) < need.get(c1)) {
                    meet = meet - 1;
                }
            }
        }
        return res;
    }


    public static void main(String[] args) {

        Solution solution = new Solution();

        System.out.println(solution.minWindow("ADOBECODEBANC", "ABC"));
    }
}





