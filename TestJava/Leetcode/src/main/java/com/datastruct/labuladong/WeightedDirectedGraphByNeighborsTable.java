package com.datastruct.labuladong;

import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class WeightedDirectedGraphByNeighborsTable implements Graph {

    // 邻接表，graph[v] 存储节点 v 的所有邻居节点及对应权重
    private List<Edge>[] graph;

    public WeightedDirectedGraphByNeighborsTable(int maxIdxNumber) {
        // 我们这里简单起见，建图时要传入节点总数，这其实可以优化
        // 比如把 graph 设置为 Map<Integer, List<Edge>>，就可以动态添加新节点了
        graph = new List[maxIdxNumber + 1];
        for (int i = 0; i < maxIdxNumber; i++) {
            graph[i] = new ArrayList<>();
        }
    }

    // 增，添加一条带权重的有向边，复杂度 O(1)
    public void addEdge(int from, int to, int weight) {
        graph[from].add(new Edge(to, weight));
    }

    // 删，删除一条有向边，复杂度 O(V)
    public void removeEdge(int from, int to) {
        for (int i = 0; i < graph[from].size(); i++) {
            if (graph[from].get(i).to == to) {
                graph[from].remove(i);
                break;
            }
        }
    }

    // 查，判断两个节点是否相邻，复杂度 O(V)
    public boolean hasEdge(int from, int to) {
        for (Edge e : graph[from]) {
            if (e.to == to) {
                return true;
            }
        }
        return false;
    }

    // 查，返回一条边的权重，复杂度 O(V)
    public int weight(int from, int to) {
        for (Edge e : graph[from]) {
            if (e.to == to) {
                return e.weight;
            }
        }
        throw new IllegalArgumentException("No such edge");
    }

    // 上面的 hasEdge、removeEdge、weight 方法遍历 List 的行为是可以优化的
    // 比如用 Map<Integer, Map<Integer, Integer>> 存储邻接表
    // 这样就可以避免遍历 List，复杂度就能降到 O(1)

    // 查，返回某个节点的所有邻居节点，复杂度 O(1)
    public List<Edge> neighbors(int v) {
        return graph[v];
    }

    @Override
    public int size() {
        return graph.length;
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
        TestGraphFunc(new WeightedDirectedGraphByNeighborsTable(3));
    }

    public static Graph getGraph(Class<Graph> clazz, int listNumber) {
        if (clazz.equals(WeightedDirectedGraphByNeighborsTable.class)) {
            return new WeightedDirectedGraphByNeighborsTable(listNumber);
        }
        throw new RuntimeException("NotSupported");
    }
}
