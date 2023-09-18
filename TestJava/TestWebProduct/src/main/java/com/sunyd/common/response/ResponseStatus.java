package com.sunyd.common.response;

import com.sunyd.common.Status;

public enum ResponseStatus implements Status {
  SUCCESS(200, "Operation Success"),
  PARAM_ERROR(400, "Invalid parameters"),
  NOT_FOUND(404, "Resource not found"),
  INTERNAL_ERROR(500, "Application internal error"),
  EXCEPTION_ERROR(500, "Unexpected exception happens");

  private final int code;
  private final String message;

  ResponseStatus(int code, String message) {
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
