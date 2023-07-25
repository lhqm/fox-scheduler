//package com.fox.register;
//
//import com.fox.entity.TaskedMethod;
////import com.fox.register.initClient.InitInvocation;
//import com.fox.utils.ScheduleTaskScan;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.event.ApplicationStartedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
///**
// * @author 离狐千慕
// * @version 1.0
// * @date 2023/7/24 15:02
// */
//@Configuration
//@Slf4j
//public class FoxSchedulerRegistrar implements ApplicationListener<ApplicationStartedEvent> {
////    配置扫描路径
//    @Value(value = "${fox.schedule.path:com}")
//    private String packageName;
////    配置主机远端地址
//    @Value(value = "${fox.server.address:127.0.0.1}")
//    private String addr;
////    配置主机接收端口
//    @Value(value = "${fox.server.port:8080}")
//    private Integer port;
////    注入发送依赖
//    @Override
//    public void onApplicationEvent(ApplicationStartedEvent event) {
//        // 在应用程序启动后执行相关方法的逻辑
//        // 可以根据需要执行初始化、调用其他方法等操作
//        log.info("客户端主体程序已启动,开始注册调度方法");
//        log.info("调度扫描路径为:{},如果不在预期之内，请修改配置后重新启动",packageName);
//        try {
////            扫描包体，递归获取所有带有该注解的类
//            List<TaskedMethod> methodList = ScheduleTaskScan.processAnnotations(packageName);
////            循环打印方法，提示客户端可控调度方案读取状况
//            methodList.forEach(item->{
//                log.info("fox-scheduler扫描到客户端可控调度方法:{},配置参数为:{}",item.getMethod().getName(),item.getAnnotationValue());
//            });
//            log.info("扫描完毕,请仔细查看上述任务调度方法以及参数是否有错误或遗漏");
////            initInvocation.sendTasks(methodList,addr,port);
//            log.info("所有任务已全部同步到远端");
//        } catch (Exception e) {
//            log.error("扫描失败!请检查扫描路径是否配置正确!");
//            throw new RuntimeException(e);
//        }
//    }
//
//}