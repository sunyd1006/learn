package com.datastruct;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
    public static String toJson(Object object){
        // 将 Java 对象转成 JSON 字符串
        return  JSONObject.toJSONString(object);

    }
    public static <T> T fromJson(String jsonStr, Class<T> clazz){
        return (T) JSONObject.parseObject(jsonStr, clazz);
    }
}
