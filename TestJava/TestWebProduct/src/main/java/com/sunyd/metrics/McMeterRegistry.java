package com.sunyd.metrics;

import com.sunyd.config.CommonConfig;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * <li>https://www.tony-bro.com/posts/1386774700/index.html </li>
 * <li>https://blog.csdn.net/wangyueshu/article/details/113387671 </li>
 *
 * @Author: syd
 * @Date: 2022/12/12 17:48
 */
@Component
@Log4j2
@Data
public class McMeterRegistry {

  @Autowired
  CommonConfig commonConfig;

  @Bean
  MeterRegistryCustomizer<MeterRegistry> meterCommonTags() {
    return (registry) -> registry.config().commonTags(
            Arrays.asList(
                    Tag.of("application", commonConfig.getApplicationName()),
                    Tag.of("env", commonConfig.getProfile()),
                    Tag.of("ip", commonConfig.getHostAddress())
            ));
  }

  ScheduledExecutorService executor;

  @PostConstruct
  public void tag() {
    Metrics.addRegistry(new SimpleMeterRegistry());

    Counter counter = Metrics.counter("test.random.count", "tag1", "val1", "tag2", "val2");
    Random random = new Random();

    executor = Executors.newSingleThreadScheduledExecutor();
    executor.scheduleAtFixedRate(() -> {
              counter.increment(random.nextInt(100));
            },
            2L, 2L, TimeUnit.SECONDS);
  }

  @PreDestroy
  public void destroy() {
    executor.shutdown();
  }

}
