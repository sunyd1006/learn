package com.ssp.tp;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * systemd 启动机器, 3个机器，2个查询。 1号机器依赖与2， 2号机器依赖于3， 3号机器不依赖什么。
 * 请问启动1号机器后，总共多少机器。关闭2号机器后，总供多少机器
 *
 * 3 2
 * 1 2
 * 1 3
 * 0
 * 1 1
 * 0 2
 */
public class Main_JD_QZ {
  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(
            new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
    // Scanner sc = new Scanner(System.in);
    Set<Integer> startSet = new HashSet<>();
    int n = sc.nextInt();
    int q = sc.nextInt();

    sc.nextLine();
    Map<Integer, Set<Integer>> mapNext = new HashMap<>();
    Map<Integer, Set<Integer>> mapPre = new HashMap<>();

    int curLin = 1;
    while (n-- > 0) {
      String line = sc.nextLine();
      String[] s = line.split(" ");
      if (s.length > 1) {
        for (int i = 1; i < s.length; i++) {
          int curJ = Integer.parseInt(s[i]);
          Set<Integer> orDefault = mapNext.getOrDefault(curLin, new HashSet<>());
          orDefault.add(curJ);
          mapNext.put(curLin, orDefault);

          Set<Integer> orPre = mapPre.getOrDefault(curJ, new HashSet<>());
          orPre.add(curLin);
          mapPre.put(curJ, orPre);
        }
      }
      curLin++;
    }

    while (q-- > 0) {
      String query = sc.nextLine();
      String[] qArray = query.split(" ");

      boolean isStart = qArray[0].equals("0") ? false : true;
      Integer needStart = Integer.parseInt(qArray[1]);

      if (isStart) {
        if (!startSet.contains(needStart)) {
          startSet.add(needStart);

          Set<Integer> integers = mapNext.getOrDefault(needStart, new HashSet<>());
          Deque<Integer> deque = new LinkedList<>(integers);

          while (!deque.isEmpty()) {
            Integer need = deque.pollFirst();
            if (startSet.contains(need)) {
              continue;
            } else {
              startSet.add(need);
              Set<Integer> orDefault = mapNext.getOrDefault(need, null);
              if (orDefault != null) {
                deque.addAll(orDefault);
              }
            }
          }
        }
      } else {
        if (startSet.contains(needStart)) {
          startSet.remove(needStart);

          Set<Integer> integers = mapPre.getOrDefault(needStart, new HashSet<>());
          Deque<Integer> deque = new LinkedList<>(integers);
          while (!deque.isEmpty()) {
            Integer need = deque.pollFirst();
            if (!startSet.contains(need)) {
              continue;
            } else {
              startSet.remove(need);
              Set<Integer> orDefault = mapPre.getOrDefault(need, null);
              if (orDefault != null) {
                deque.addAll(orDefault);
              }
            }
          }
        }
      }
      System.out.println(startSet.size());
    }
  }
}
