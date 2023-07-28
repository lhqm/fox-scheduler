package com.foxrpc.annotation;


import java.lang.annotation.*;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/27 16:37
 * 服务端监听端口
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
//@Import({FoxServerAspect.class})
public @interface EnableFoxServer {
    int port() default 8000; //本地监听端口。将通过该端口来进行通信
}