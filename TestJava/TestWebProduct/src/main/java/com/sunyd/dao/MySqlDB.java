package com.sunyd.dao;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.*;


@Slf4j
@Component
public class MySqlDB {
  List<Exception> list;
  Random random;

  public MySqlDB() {
    list = new LinkedList<>();
    random = new Random();
  }

  public String getUserNameByUserId(int id) {
    boolean isException = random.nextBoolean();
    System.out.println("isBoolean:    " + isException);
    if (isException) {
      int a = 1 / 0;
    }
    return String.valueOf(id);
  }

}
