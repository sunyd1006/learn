package com.learn.util;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadUtil {
  public static void safeSleep(long millis, String sleepReason) {
    try {
      Thread.sleep(millis);
      log.debug("SafeSleep success. sleepMs: {}, sleepReason: {}", millis, sleepReason);
    } catch (InterruptedException e) {
      log.error("SafeSleep error. sleepMs: {}, sleepReason: {}", millis, sleepReason, e);
    }
  }

  public static void safeStopThread(@NonNull Thread thread, String stopReason) {
    if (thread.isAlive()) {
      try {
        thread.interrupt();
        thread.join(5000);
      } catch (InterruptedException ignored) {
      }
      log.info("SafeStopThread. stopReason: {}, threadName: {}", stopReason, thread.getName());
    }
  }

  public static void safeShutdownPool(ExecutorService threadPool, String stopReason) {
    boolean terminated = true;
    Exception ex = null;
    try {
      threadPool.shutdown();
      terminated = threadPool.awaitTermination(5, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      ex = ignored;
    }
    if (!terminated || ex != null) {
      log.error("ThreadPool exit abnormally, reason: {}", stopReason, ex);
    }
  }
}
