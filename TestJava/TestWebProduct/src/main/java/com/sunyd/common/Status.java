package com.sunyd.common;

import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

public interface Status extends Serializable {
  @JsonValue
  int code(); // 序列化时只显示code，message以BaseResponse中的为准

  String message();
}
