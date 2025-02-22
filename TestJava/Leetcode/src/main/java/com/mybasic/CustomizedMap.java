package com.mybasic;


//	String path = "/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/";


import lombok.Data;

@Data
class Entry {
  String key;
  String value;
  Entry next;

  public Entry(String key, String value) {
    this.key = key;
    this.value = value;
    this.next = null;
  }
}

// 1. template
// 2. thread safe
class Map {
  Entry[] array;
  Object mutex;
  int size, capacity;
  public Map(int capacity) {
    this.mutex = new Object();
    this.array = new Entry[capacity];
    this.size = 0;
    this.capacity = capacity;
  }

  public void put(String key, String value) {
    // 数组扩容和拷贝数组
    // insert into array
    synchronized (mutex) {
      int idx = hashFunc(key);
      // find and set
      Entry cur = array[idx];
      if (cur == null) {
        array[idx] = new Entry(key, value);
        return;
      }

      Entry last = cur;
      while (cur != null) {
        if (cur.key.equals(key)) {
          cur.value = value; // same key
          return;
        }
        last = cur; // store last cur;
        cur = cur.next;
      }
      // not empty bucket, but we still need insert one item.
      last.next = new Entry(key, value);
    }
  }

  void checkSize() {
  }

  public String get(String key) {
    int idx = hashFunc(key);
    Entry cur = array[idx];
    while (cur != null) {
      if (cur.key.equals(key)) {
        return cur.value;
      }
      cur = cur.next;
    }
    return null;
  }

  int hashFunc(String key) {
    return key.hashCode() % array.length;
  }
}


class CustomizedMap {

  public static void main(String[] args) {

    Map map = new Map(10);
    map.put("k1", "v1");
    map.put("k1", "v2");
    map.put("k3", "v3");

    System.out.println(map.get("k1"));
    System.out.println(map.get("k3"));
  }
}





