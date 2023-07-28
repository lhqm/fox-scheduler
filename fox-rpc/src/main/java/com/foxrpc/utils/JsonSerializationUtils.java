package com.foxrpc.utils;

import com.alibaba.fastjson.JSON;

/**
 * @Author 离狐千慕
 * @Date 2023-07-25 09:24:12
 * JSON序列化和反序列化解析器
 */
public class JsonSerializationUtils {

  public static String serialize(Object obj) {
    return JSON.toJSONString(obj);
  }

  public static <T> T deserialize(String json, Class<T> clazz) {
    return JSON.parseObject(json, clazz);
  }
}