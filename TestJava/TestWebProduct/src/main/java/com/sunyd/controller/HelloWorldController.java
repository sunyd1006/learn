package com.sunyd.controller;

import com.sunyd.common.response.BaseResponse;
import com.sunyd.common.response.ResponseStatus;
import com.sunyd.service.BackService;
import com.sunyd.service.NotBeanProcessor;
import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api")
public class HelloWorldController {
  @Autowired
  BackService service;

  @GetMapping("/create")
  public BaseResponse<String> getDataByService() throws Exception {
    try {
      service.throwExceptionRandom();
    } catch (Exception e) {
      System.out.println("ControllerTryAndPrint----------------");
      throw e;
    }

    // 可能会被统一拦截处理, 以下代码可能不会被执行
    System.out.println("getDataByService.RunSuccess");
    return BaseResponse.with(ResponseStatus.SUCCESS, "操作成功");
  }

  @Timed(value = "create_request_duration", description = "The delay of the sdk ", histogram = true)
  @GetMapping("/start/{processName}")
  public BaseResponse start(@PathVariable("processName") String name) {
    // log.debug("call start");
    NotBeanProcessor processor = new NotBeanProcessor(service, name);
    return BaseResponse.with(ResponseStatus.SUCCESS, "end", processor);
  }
}
