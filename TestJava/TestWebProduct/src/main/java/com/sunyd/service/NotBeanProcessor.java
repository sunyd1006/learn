package com.sunyd.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sunyd.util.ThreadUtil;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * 
 *
 * @Author: syd
 * @Date: 2022/12/12 16:23
 */
@Log4j2
@Data
public class NotBeanProcessor {
  String processorName;

  @JsonIgnore
  BackService service;
  @JsonIgnore
  Thread dispatchThread;

  public NotBeanProcessor(BackService service, String name) {
    this.service = service;
    this.processorName = name;

    dispatchThread = new Thread(this::run);
    dispatchThread.start();
  }

  public void run() {
    Counter counter = Metrics.counter("processor_counter", "processor", processorName);

    while (true) {
      String s = service.fakeRead();
      counter.increment(100);
      Metrics.counter(processorName).increment(100);

      service.fakeWrite(s);
      ThreadUtil.safeSleepMillis(1000, "read");
    }
  }


}
