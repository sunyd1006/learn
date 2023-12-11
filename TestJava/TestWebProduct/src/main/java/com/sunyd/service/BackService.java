package com.sunyd.service;


import com.sunyd.common.exception.DefaultException;
import com.sunyd.common.exception.ExceptionStatus;
import com.sunyd.dao.MySqlDB;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@ToString
@Data
public class BackService {
  List<Exception> list;
  Random random;

  @Autowired
  MySqlDB mySqlDB;

  public BackService() {
    list = new LinkedList<>();
    random = new Random();
  }

  public void throwExceptionRandom() throws Exception {
    list.clear();

    list.add(new RuntimeException());
    list.add(new DefaultException(ExceptionStatus.RESOURCE_NOT_FOUND, "MySQLServiceNotFoundError"));
    list.add(new DefaultException(ExceptionStatus.INTERNAL_ERROR, "MySQLServiceGetFailed"));
    // list.add(new ClassNotFoundException());
    // list.add(new ClassNotFoundException());

    int index = random.nextInt(list.size() - 1);
    if (list.get(index) != null) {
      Exception e = list.get(index);
      log.error(e.getMessage(), e);
      throw e;
    } else {
      System.out.println("service.throwExceptionRandom() run success!");
    }
  }

  public String fakeRead() {
    return "hello world";
  }

  public String fakeWrite(String outputContent) {
    System.out.println(outputContent);
    return "success";
  }

}
