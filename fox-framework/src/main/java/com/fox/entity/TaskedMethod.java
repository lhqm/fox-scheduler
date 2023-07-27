package com.fox.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TaskedMethod implements Serializable {
    private String clazz;
    private String method;
    private JSONObject annotationValue;

    // 反序列化构造器
    public static TaskedMethod fromJsonString(String jsonString) {
        JSONObject json = JSONObject.parseObject(jsonString);
        String className = json.getString("clazz");
        String methodName = json.getString("method");
        JSONObject annotationValue = json.getJSONObject("annotationValue");
        return new TaskedMethod(className, methodName, annotationValue);
    }

    // 序列化构造器
    public String toJsonString() {
        JSONObject json = new JSONObject();
        json.put("className", clazz);
        json.put("methodName", method);
        json.put("annotationValue", annotationValue);
        return json.toJSONString();
    }
}