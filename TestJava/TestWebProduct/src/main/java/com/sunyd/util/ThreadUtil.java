package com.sunyd.util;


import lombok.extern.log4j.Log4j2;

/**
 * @author syd
 */
@Log4j2
public class ThreadUtil {

  public static void safeSleepMillis(long sleepMs, String sleepReason) {
    try {
      Thread.sleep(sleepMs);
      log.info("SafeSleep success. sleepMs: {}, sleepReason: {}", sleepMs, sleepReason);
    } catch (InterruptedException e) {
      log.error("SafeSleep error. sleepMs: {}, sleepReason: {}", sleepMs, sleepReason, e);
    }
  }
}
