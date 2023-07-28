package com.foxrpc.config;

import com.foxrpc.annotation.EnableFoxScheduler;
import com.foxrpc.aspect.FoxScheduleAspect;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Configuration
@EnableAspectJAutoProxy
public class FoxScheduleAnnotationConfiguration {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private FoxScheduleAspect foxScheduleAspect;

    @PostConstruct
    public void executeOnAnnotation() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableFoxScheduler.class);
        if (beansWithAnnotation.size()==0){return;}
        for (Object bean : beansWithAnnotation.values()) {
//            获取类所在包路径
            String appPackage = bean.getClass().getPackage().getName();
//            获取注解上的写入值
            EnableFoxScheduler annotation = bean.getClass().getAnnotation(EnableFoxScheduler.class);
            if (annotation != null) {
                String packageName = annotation.packageName();
                // 使用获取到的注解值进行后续操作
                // 执行自动注册
                foxScheduleAspect.InitScanAndSend(packageName,appPackage);
            }
        }
    }
    @Bean
    public AspectJExpressionPointcutAdvisor customAnnotationAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("@annotation(com.foxrpc.annotation.EnableFoxScheduler)");
        advisor.setAdvice((MethodInterceptor) methodInvocation -> {
            Object result = methodInvocation.proceed();
            return result;
        });
        return advisor;
    }
}