package com.fox.utils;

import com.fox.annotation.FoxSchedule;
import com.alibaba.fastjson.JSONObject;
import com.fox.entity.TaskedMethod;
import com.fox.utils.client.PackageScanner;

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
    public static List<TaskedMethod> processAnnotations(String packageName) throws ClassNotFoundException, IOException {
        List<TaskedMethod> taskedMethods = new ArrayList<>();
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
                    TaskedMethod taskedMethod = new TaskedMethod(clazz.getName(), method.getName(),values);
                    taskedMethods.add(taskedMethod);
                }
            }
        }
        return taskedMethods;
    }
}
