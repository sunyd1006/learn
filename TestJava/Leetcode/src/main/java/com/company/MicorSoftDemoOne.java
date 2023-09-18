package com.company;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class MicorSoftDemoOne {

    public static void main(String[] args) throws IOException {
        MicorSoftDemoOne one = new MicorSoftDemoOne();
        int N = 5;
        int[] A = new int[]{2, 2, 1, 2};
        int[] B = new int[]{1, 3, 4, 4};
        System.out.println(one.solution(N, A, B));    
    }

    public int solution(int[] A) {
        // write your code in Java SE 8
        int noNegativeRes = -1;
        if(A==null || A.length==0)
            return noNegativeRes;
        int res = noNegativeRes;
        int n = A.length;
        for(int i=0; i<n; ){
            if(A[i]>=0 ){
                // A[i] > 0
                int tempRes = 0;
                int start = i;
                while(i<n && A[i]>=0){
                    tempRes+=A[i];
                    i++;
                }
                res = tempRes>res? tempRes: res;
            }else{
                // A[i]< 0
                i++;
            }
        }
        return res;
    }

    // two
    public int solution(int N, int[] A, int[] B) {
        // write your code in Java SE 8

        // N: 1..N
        // A: 1..M
        int M = A.length;
        int[][] graph = new int[N+1][N+1];
        for(int i=0; i<M; i++){
            graph[A[i]][B[i]] = 1;
            graph[A[i]][0]++;
            graph[0][B[i]]++;
        }

        // howMuchConnectEveryNode[i][0] : 有多少个领接点
        // howMuchConnectEveryNode[i][1] : 是那一个节点
        // howMuchConnectEveryNode[i][2] : 分配的值

        int[][] howMuchConnectEveryNode = new int[N+1][3];
        for(int i=1; i<graph.length; i++){
            howMuchConnectEveryNode[i][0] = graph[i][0] + graph[0][i];
            // idx = 1 store idx
            howMuchConnectEveryNode[i][1] = i;
        }

        // howMuchConnectEveryNode[i][0] 从打到小， howMuchConnectEveryNode[i][1] 从大到小
        Arrays.sort(howMuchConnectEveryNode, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if(o1[0] == o2[0])
                    return  o2[1] - o1[1];
                return o2[0] - o1[0];
            }
        });

        // 分配节点
        int tempM = N;
        for(int i=0; i<graph.length && tempM>0; i++){
            howMuchConnectEveryNode[i][2] = tempM;
            tempM--;
        }

        int res = 0;
        for(int i=0; i<graph.length; i++){
            res += howMuchConnectEveryNode[i][0] * howMuchConnectEveryNode[i][2];
        }

        return  res;
    }


    // three
    public  String solution(String S) {
        // write your code in Java SE 8
        String[] str = new String[]{"AB", "BA", "CD", "DC"};
        StringBuilder builder = new StringBuilder(S);
        boolean stillHasTransform = true;
        while(stillHasTransform){
            stillHasTransform = false;
            for(int i=0; i<str.length; i++){
                int idx = -1;
                if((idx = builder.indexOf(str[i])) != -1){
                    builder.replace(idx, idx+2, "");
                    // if there still exists removing action.
                    stillHasTransform = true;
                }
            }
        }
        return  builder.toString();
    }
}
