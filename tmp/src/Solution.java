import java.util.*;

public class Solution {
    static Solution main = new Solution();
    public static void main(String[] args) {
        int[][] case1 = new int[][] {
                {0, 1},
                {1, 2}
        };
        int n = 2;
        int target = 0;

    }

    public void canLearn(int[][] dependencies, int target, int classNum) {
        // build nodes into map
        for (int[] ints : case1) {
            ClassEntity.addClassItem(ints[0], ints[1]);
        }

        // case1: target node没有前驱节点，是可以直接学习的
        // case2: target node 去前驱节点，递归下去，也是没有前驱节点的，是可以直接学习的
        // case3: target node 的前驱节点里面包含有环，这是不可以直接学习的，return false

        HashSet<ClassEntity> visited = new HashSet<>();
        LinkedList<ClassEntity> traces = new LinkedList<>();
//        ClassEntity.getClassEntity();
    }

    boolean canLearn;
    void trace(Set<ClassEntity> visited, List<ClassEntity> classTraces, int targetClassId, ClassEntity nextNode) {


    }
}

class ClassEntity {
    public int classId;
    public List<ClassEntity> preClassNodes = new LinkedList<>();
    private ClassEntity(int val) {
        this.classId = val;
    }
    public void addPreClass(int classId) {
        ClassEntity classIdNode = nodes.getOrDefault(classId, new ClassEntity(classId));
        preClassNodes.add(classIdNode);
    }

    // classId, classNode
    public static Map<Integer, ClassEntity> nodes = new HashMap<>();
    public static ClassEntity getClassEntity(int classId) {
        return nodes.getOrDefault(classId, null);
    }
    public static void addClassItem(int preClassId, int classId) {
        ClassEntity preClassIdNode = nodes.getOrDefault(preClassId, new ClassEntity(preClassId));
        preClassIdNode.addPreClass(classId);
        nodes.put(preClassId, preClassIdNode);
    }
}
