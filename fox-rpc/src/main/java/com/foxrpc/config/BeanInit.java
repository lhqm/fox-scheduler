package com.foxrpc.config;

import com.foxrpc.aspect.FoxScheduleAspect;
import com.foxrpc.client.InitInvocation;
import com.foxrpc.client.InitInvocationImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/28 11:33
 * 手动注入IOC
 */
@Configuration
public class BeanInit {
    @Bean
    public FoxScheduleAspect foxScheduleAspect() {
        return new FoxScheduleAspect();
    }
    @Bean
    public InitInvocation initInvocation() {
        return new InitInvocationImpl();
    }
}
