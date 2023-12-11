package com.sunyd.config;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

/**
 * 
 *
 * @Author: syd
 * @Date: 2022/12/13 09:34
 */
@Log4j2
@Data
@Component
public class CommonConfig {

  @Value("${spring.application.name}")
  private String applicationName;
  @Value("${spring.profiles.active}")
  private String profile;

  private String hostAddress = "0.0.0.0";

  public CommonConfig() {
    try {
      hostAddress = InetAddress.getLocalHost().getHostAddress();
    } catch (Exception e) {
      log.debug(hostAddress, e);
    }
  }
}
