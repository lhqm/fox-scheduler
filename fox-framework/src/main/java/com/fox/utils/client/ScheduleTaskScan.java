package com.fox.utils.client;

import com.alibaba.fastjson.JSONObject;
import com.fox.annotation.FoxSchedule;
import com.fox.entity.AnnotatedMethod;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 11:35
 * 扫描指定的包路径下包含某个注解的方法和
 */
public class ScheduleTaskScan {

    public static List<AnnotatedMethod> processAnnotations(String packageName) throws ClassNotFoundException, IOException {
        List<AnnotatedMethod> annotatedMethods = new ArrayList<>();
        List<Class<?>> classes = PackageScanner.scanPackage(packageName);

        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(FoxSchedule.class)) {
                    FoxSchedule annotation = method.getAnnotation(FoxSchedule.class);
                    JSONObject values=new JSONObject();
                    values.put("delay",annotation.delay());
                    values.put("taskName",annotation.taskName());
                    values.put("twice",annotation.twice());
                    values.put("type",annotation.type());
                    values.put("cron",annotation.cron());
                    values.put("fresh",annotation.fresh());
                    AnnotatedMethod annotatedMethod = new AnnotatedMethod(clazz, method,values);
                    annotatedMethods.add(annotatedMethod);
                }
            }
        }
        return annotatedMethods;
    }
}
