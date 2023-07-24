package com.fox.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AnnotatedMethod implements Serializable {
    private Class<?> clazz;
    private Method method;
    private JSONObject annotationValue;
}