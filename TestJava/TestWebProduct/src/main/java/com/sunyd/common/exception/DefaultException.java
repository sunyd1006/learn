package com.sunyd.common.exception;


import com.sunyd.common.Status;

public final class DefaultException extends RuntimeException {

  private static final long serialVersionUID = -8618092465858207782L;

  private Status code;
  private String message;

  public DefaultException(Status code) {
    this.code = code;
    this.message = code.message();
  }

  public DefaultException(String message) {
    this.code = ExceptionStatus.INTERNAL_ERROR;
    this.message = message;
  }

  public DefaultException(Status code, String message) {
    this.code = code;
    this.message = message;
  }

  public DefaultException(Status code, Throwable cause) {
    super(cause);
    this.code = code;
    this.message = code.message();
  }

  public DefaultException(Status code, String message, Throwable cause) {
    super(cause);
    this.code = code;
    this.message = message;
  }

  public Status getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}