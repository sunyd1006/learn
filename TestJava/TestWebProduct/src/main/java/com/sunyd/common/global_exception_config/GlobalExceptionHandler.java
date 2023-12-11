package com.sunyd.common.global_exception_config;


import com.sunyd.common.exception.DefaultException;
import com.sunyd.common.response.BaseResponse;
import com.sunyd.common.response.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice   // 该注解 = @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {

  // HttpResponse是之前文章中设计的统一Response类
  // 指定要处理的异常类，可以是一个数组
  @ExceptionHandler(Exception.class)
  public BaseResponse<String> ExceptionHandler(Exception e) {
    BaseResponse<String> response;
    if (e instanceof DefaultException) {
      response = BaseResponse.with(ResponseStatus.INTERNAL_ERROR, "");
    } else {
      response = BaseResponse.with(ResponseStatus.EXCEPTION_ERROR, e.getMessage());
    }
    return response;
  }

}
