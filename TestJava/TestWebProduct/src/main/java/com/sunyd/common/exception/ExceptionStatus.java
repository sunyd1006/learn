package com.sunyd.common.exception;

import com.sunyd.common.Status;

public enum ExceptionStatus implements Status {
  PARAMETER_MISSING(400001, "Missing required parameter"),
  REQUEST_THIRD_PARTY_FAILED(400002, "Request third-party system failed"),
  PARAMETER_INVALID(404002, "Invalid parameter"),
  RESOURCE_NOT_FOUND(404001, "Resource not found"),

  INTERNAL_ERROR(500001, "Internal error");

  private final int code;
  private final String message;

  ExceptionStatus(int code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public int code() {
    return code;
  }

  @Override
  public String message() {
    return message;
  }

  @Override
  public String toString() {
    return String.valueOf(this.code);
  }
}