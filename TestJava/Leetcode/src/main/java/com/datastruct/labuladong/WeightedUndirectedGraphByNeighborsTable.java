package com.datastruct.labuladong;

import org.junit.Assert;

import java.util.List;

public class WeightedUndirectedGraphByNeighborsTable implements Graph {
    WeightedDirectedGraphByNeighborsTable graph;
    public WeightedUndirectedGraphByNeighborsTable(int maxIdxNumber) {
        graph = new WeightedDirectedGraphByNeighborsTable(maxIdxNumber);
    }

    @Override
    public void addEdge(int from, int to, int weight) {
        graph.addEdge(from, to, weight);
        graph.addEdge(to, from, weight);
    }

    @Override
    public void removeEdge(int from, int to) {
        graph.removeEdge(from, to);
        graph.removeEdge(to, from);
    }

    @Override
    public boolean hasEdge(int from, int to) {
        return graph.hasEdge(from, to);
    }

    @Override
    public int weight(int from, int to) {
        return graph.weight(from, to);
    }

    @Override
    public List<Edge> neighbors(int v) {
        return graph.neighbors(v);
    }

    @Override
    public int size() {
        return graph.size();
    }

    public static void TestGraphFunc(Graph graph) {
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 2);
        graph.addEdge(2, 0, 3);
        graph.addEdge(2, 1, 4);

        // 1. 打印所有的链接表
        String expectedStr = "0: (1, 1) \n" +
                "1: (2, 2) \n" +
                "2: (0, 3) (1, 4) \n";
        StringBuilder realStr = new StringBuilder("");
        for (int i = 0; i < graph.size(); i++) {
            realStr.append(i + ": ");
            for (Edge e : graph.neighbors(i)) {
                realStr.append("(" + e.to + ", " + e.weight + ") ");
            }
            realStr.append("\n");
        }
        Assert.assertEquals(expectedStr, realStr.toString());
        System.out.println("graph: \n" + realStr);

        // 2. 检查两个领接表
        expectedStr = "2 -> 0, wight: 3 \n" +
                "2 -> 1, wight: 4 \n" ;
        realStr = new StringBuilder("");
        StringBuilder finalRealStr = realStr;
        graph.neighbors(2).forEach(edge -> {
            finalRealStr.append(2 + " -> " + edge.to + ", wight: " + edge.weight + " \n");
        });
        System.out.println("result: \n" + finalRealStr);
        Assert.assertEquals(expectedStr, finalRealStr.toString());
    }

    public static void main(String[] args) {
        TestGraphFunc(new WeightedUndirectedGraphByNeighborsTable(3));
    }

}
