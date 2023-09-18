package com.sunyd.util;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

/**
 * 
 *
 * @Author: syd
 * @Date: 2022/12/13 13:33
 */
public class MetricsUtil {

  public static Counter counter(String metricName, String description, Iterable<Tag> tags) {
    return Counter
            .builder(metricName)
            .description(description)
            .tags(tags)
            .register(Metrics.globalRegistry);
  }

}
