package com.sunyd.common.response;

import lombok.Getter;

import java.io.Serializable;

/**
 * 统一 Response 设计指南
 * NOTE: https://mawen.work/java/2020/07/03/unify-response/
 */
@Getter
public final class BaseResponse<T> implements Serializable {
  private static final long serialVersionUID = 3886133510113334083L;
  private ResponseStatus code;
  private String message;
  private T data;

  // 无参构造方法中将响应码置为DefaultStatus中的SUCCESS
  public BaseResponse() {
    this.setCode(ResponseStatus.SUCCESS);
    this.message = ResponseStatus.SUCCESS.message();
  }

  public BaseResponse(T data) {
    this();
    this.data = data;
  }

  public BaseResponse<T> setCode(ResponseStatus code) {
    this.code = code;
    this.message = code.message();
    return this;
  }

  public BaseResponse<T> setMessage(String message) {
    this.message = message;
    return this;
  }

  public BaseResponse<T> setData(T data) {
    this.data = data;
    return this;
  }

  public static <T> BaseResponse<T> with(ResponseStatus code, String message) {
    BaseResponse<T> response = new BaseResponse<>();
    response.code = code;
    response.message = message != null && !message.isEmpty() ? message : code.message();
    return response;
  }

  public static <T> BaseResponse<T> with(ResponseStatus code, String message, T data) {
    BaseResponse<T> response = new BaseResponse<>();
    response.code = code;
    response.message = message;
    response.data = data;
    return response;
  }

  @Override
  public String toString() {
    return "BaseResponse{" +
            "code=" + code.code() +
            ", message='" + message + '\'' +
            ", data=" + data +
            '}';
  }
}