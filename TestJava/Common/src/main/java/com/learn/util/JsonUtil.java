package com.learn.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
  private static final ObjectMapper JSONIZER_POOL;

  static {
    JSONIZER_POOL = createJsonizer();
  }

  private static ObjectMapper createJsonizer() {
    return JsonMapper.builder()
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .build();
  }

  public static String serialize(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    return JSONIZER_POOL.writeValueAsString(object);
  }

  public static String serialize(Object object, String defaultJsonString) {
    if (object == null) {
      return defaultJsonString;
    }
    try {
      return JSONIZER_POOL.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Serialization error: ", e);
      return defaultJsonString;
    }
  }

}
