package com.fox.aspect;

import com.fox.annotation.EnableFoxScheduler;
import com.fox.entity.TaskedMethod;
import com.fox.utils.ScheduleTaskScan;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/26 17:03
 */
@Aspect
@Slf4j
@Component
public class FoxScheduleAspect {
    //    配置扫描路径
    @Value(value = "${fox.schedule.path:com}")
    private String packageName;
    //    配置主机远端地址
    @Value(value = "${fox.server.address:127.0.0.1}")
    private String addr;
    //    配置主机接收端口
    @Value(value = "${fox.server.port:8080}")
    private Integer port;
//    自动化配置逻辑，织入主启动类之后

    public void InitScanAndSend(String name,String appPath) throws Throwable {
        // 在应用程序启动后执行相关方法的逻辑
        // 通过切点仲裁包体逻辑
//        没有设置外置包名，通过仲裁获取注解值的包名
        log.info("等待注册...");
        log.info("客户端主体程序已启动,开始注册调度方法");
        if (packageName.equals("com")){
//            获取注解，通过注解来判断是否有包名，如果还没有，就直接扫描全包
            log.warn("未检索到文件配置中的扫描路径配置,fox-scheduler将向下搜索...");
//            不为以下三种不可被扫描的状况，赋值
            if(!name.equals("") && !name.equals("\\") && !name.equals("/")){
                log.info("检索到注解配置packageName={}",name);
                packageName=name;
            }else {
                log.warn("未检索到任何配置！将扫描启动器路径:{}",appPath);
                packageName=appPath;
            }
        }
        log.info("调度扫描路径为:{},如果不在预期之内，请修改配置后重新启动",packageName);
        try {
//            扫描包体，递归获取所有带有该注解的类
            List<TaskedMethod> methodList = ScheduleTaskScan.processAnnotations(packageName);
//            循环打印方法，提示客户端可控调度方案读取状况
            methodList.forEach(item->{
                log.info("fox-scheduler扫描到客户端可控调度方法:{},配置参数为:{}",item.getMethod().getName(),item.getAnnotationValue());
            });
            log.info("扫描完毕,请仔细查看上述任务调度方法以及参数是否有错误或遗漏");
//            initInvocation.sendTasks(methodList,addr,port);
            log.info("所有任务已全部同步到远端");
        } catch (Exception e) {
            log.error("扫描失败!请检查扫描路径是否配置正确!");
            throw new RuntimeException(e);
        }
    }
}