package com.foxrpc.config;

import com.foxrpc.annotation.EnableFoxServer;
import com.foxrpc.aspect.FoxServerAspect;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 16:43
 * 服务端自动配置类
 */
@Configuration
@EnableAspectJAutoProxy
@Slf4j
public class FoxServerAnnotationConfiguration {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private FoxServerAspect foxServerAspect;

    @PostConstruct
    public void executeOnAnnotation() throws Throwable {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableFoxServer.class);
        boolean started=true;
        for (Object bean : beansWithAnnotation.values()) {
//            获取注解上的写入值
//            该注解执运行一次这个方法，其他地方加了也无效化
            EnableFoxServer annotation = bean.getClass().getAnnotation(EnableFoxServer.class);
            if (annotation != null && started) {
                int port = annotation.port();
                // 使用获取到的注解值进行后续操作
                // 执行自动注册
//                自动注册挂起一个守护线程来确保服务一直在线。
                Thread thread = new Thread(() -> {
                    try {
                        foxServerAspect.startNetty(port);
                    } catch (InterruptedException e) {
                        log.info("netty服务器启动失败....");
                        throw new RuntimeException(e);
                    }
                });
                thread.setDaemon(true);
                thread.start();

                started=false;
            }
        }
    }
    @Bean
    public AspectJExpressionPointcutAdvisor foxServerAnnotationAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("@annotation(com.fox.annotation.EnableFoxServer)");
        advisor.setAdvice((MethodInterceptor) methodInvocation -> {
            Object result = methodInvocation.proceed();
            return result;
        });
        return advisor;
    }
}
