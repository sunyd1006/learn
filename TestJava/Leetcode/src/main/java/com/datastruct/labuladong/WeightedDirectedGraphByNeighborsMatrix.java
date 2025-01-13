package com.datastruct.labuladong;

import org.junit.Assert;

import java.util.LinkedList;
import java.util.List;

/**
 * -1 表示无边
 */
public class WeightedDirectedGraphByNeighborsMatrix implements Graph {
    int[][] matrix;

    public WeightedDirectedGraphByNeighborsMatrix(int n) {
        matrix = new int[n + 1][n + 1];
    }

    @Override
    public void addEdge(int from, int to, int weight) {
        matrix[from][to] = weight;
    }

    @Override
    public void removeEdge(int from, int to) {
        matrix[from][to] = Integer.MIN_VALUE;
    }

    @Override
    public boolean hasEdge(int from, int to) {
        return matrix[from][to] != Integer.MIN_VALUE;
    }

    @Override
    public int weight(int from, int to) {
        return matrix[from][to];
    }

    @Override
    public List<Edge> neighbors(int v) {
        List<Edge> res = new LinkedList<>();
        for (int j = 0; j < matrix.length; j++) {
            res.add(new Edge(j, matrix[v][j]));
        }
        return res;
    }

    @Override
    public int size() {
        return matrix.length;
    }

    public static void main(String[] args) {
        TestGraphFunc(new WeightedDirectedGraphByNeighborsMatrix(5));
    }

    public static void TestGraphFunc(Graph graph) {
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 0, 3);
        graph.addEdge(2, 1, 4);

        // 1. 打印所有的链接表
        String expectedStr = "0: (0, 0) (1, 1) (2, 0) (3, 0) (4, 0) (5, 0) \n" + "1: (0, 0) (1, 0) (2, 2) (3, 0) (4, 0) (5, 0) \n" + "2: (0, 3) (1, 4) (2, 0) (3, 0) (4, 0) (5, 0) \n" + "3: (0, 0) (1, 0) (2, 0) (3, 0) (4, 0) (5, 0) \n" + "4: (0, 0) (1, 0) (2, 0) (3, 0) (4, 0) (5, 0) \n" + "5: (0, 0) (1, 0) (2, 0) (3, 0) (4, 0) (5, 0) ";
        StringBuilder realStr = new StringBuilder("");
        for (int i = 0; i < graph.size(); i++) {
            realStr.append(i + ": ");
            for (Edge e : graph.neighbors(i)) {
                realStr.append("(" + e.to + ", " + e.weight + ") ");
            }
            realStr.append("\n");
        }
//        Assert.assertEquals(expectedStr, realStr.toString());
        System.out.println("graph: \n" + realStr);

        // 2. 检查两个领接表
        expectedStr = "2 -> 0, wight: 3 \n" + "2 -> 1, wight: 4 \n" + "2 -> 2, wight: 0 \n" + "2 -> 3, wight: 0 \n" + "2 -> 4, wight: 0 \n" + "2 -> 5, wight: 0 \n";
        realStr = new StringBuilder("");
        StringBuilder finalRealStr = realStr;
        graph.neighbors(2).forEach(edge -> {
            finalRealStr.append(2 + " -> " + edge.to + ", wight: " + edge.weight + " \n");
        });
        System.out.println("result: \n" + finalRealStr);
//        Assert.assertEquals(expectedStr, finalRealStr.toString());
    }
}
