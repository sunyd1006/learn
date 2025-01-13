package com.datastruct.leetcode;

import lombok.Data;

@Data
public class Pair<F, S> {
    F first;
    S second;
    public static <F,S> Pair of(F first, S second) {
        Pair<F, S> p = new Pair();
        p.first = first;
        p.second = second;
        return p;
    }
}
