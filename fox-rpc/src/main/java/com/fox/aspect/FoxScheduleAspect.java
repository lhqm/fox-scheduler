package com.fox.aspect;

import com.fox.client.InitInvocation;
import com.fox.entity.TaskedMethod;
import com.fox.utils.ScheduleTaskScan;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.Invocation;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    //    配置调度方案名称
    @Value(value = "${fox.schedule.name:未命名调度客户端}")
    private String clientName;
    //    配置主机远端地址
    @Value(value = "${fox.server.address:127.0.0.1}")
    private String addr;
    //    配置主机接收端口
    @Value(value = "${fox.server.port:8080}")
    private Integer port;
    @Autowired
    private InitInvocation initInvocation;
//    自动化配置逻辑，织入主启动类之后

    public void InitScanAndSend(String name,String appPath) {
        // 在应用程序启动后执行相关方法的逻辑
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
                log.info("fox-scheduler扫描到客户端可控调度方法:{},配置参数为:{}",item.getMethod(),item.getAnnotationValue());
            });
            log.info("扫描完毕,请仔细查看上述任务调度方法以及参数是否有错误或遗漏");
            Thread thread=new Thread(()->{
                initInvocation.sendTasks(methodList,addr,port,clientName);
                log.info("所有任务已全部同步到远端");
            });
            thread.start();
        } catch (Exception e) {
            log.error("应用启动失败...");
            throw new RuntimeException(e);
        }
    }


    private static class ClientHandler extends SimpleChannelInboundHandler<Object> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            // 处理服务端响应
            System.out.println(msg.toString());
        }
    }
}
