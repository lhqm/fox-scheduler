package com.foxrpc.config;

import com.foxrpc.annotation.EnableFoxServer;
import com.foxrpc.aspect.FoxServerAspect;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
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
    @Value("${fox.server.port:-1}")
    private int port;

    @PostConstruct
    public void executeOnAnnotation() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(EnableFoxServer.class);
        if (beansWithAnnotation.size()==0){return;}
        boolean started=true;
        for (Object bean : beansWithAnnotation.values()) {
//            获取注解上的写入值
//            该注解执运行一次这个方法，其他地方加了也无效化
            EnableFoxServer annotation = bean.getClass().getAnnotation(EnableFoxServer.class);
            if (annotation != null && started) {
                int port = annotation.port();
                // 使用获取到的注解值进行后续操作
                //        配置文件没配置或者非法，读注解
                if (this.port<0){
//            注解上有且合法，直接读注解
                    if (port>0){
                        this.port=port;
                    }else {
//                全都失效，配置到8000端口
                        this.port=8000;
                    }
                }
                // 执行自动注册
//                自动注册挂起一个守护线程来确保服务一直在线。
                Thread thread = new Thread(() -> {
                    try {
                        FoxServerAspect.startNetty(this.port);
                    } catch (InterruptedException e) {
                        log.info("Netty启动失败");
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
        advisor.setExpression("@annotation(com.foxrpc.annotation.EnableFoxServer)");
        advisor.setAdvice((MethodInterceptor) methodInvocation -> {
            Object result = methodInvocation.proceed();
            return result;
        });
        return advisor;
    }
}
