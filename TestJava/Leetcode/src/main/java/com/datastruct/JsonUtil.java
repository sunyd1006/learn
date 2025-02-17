package com.datastruct;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    // 创建 ObjectMapper 实例
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void serializeAndPrint(Object object, String defaultValue) {
        System.out.println(serialize(object, defaultValue));
    }

    // 将对象序列化为 JSON 字符串
    public static String serialize(Object object, String defaultValue) {
        try {
            // 使用 ObjectMapper 将对象转换为 JSON 字符串
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    // 将 JSON 字符串反序列化为对象
    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            // 使用 ObjectMapper 将 JSON 字符串转换为指定的对象
            return objectMapper.readValue(json, clazz);
        } catch (JsonMappingException e) {
            e.printStackTrace();
            return null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
