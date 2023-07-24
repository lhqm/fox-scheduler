package com.fox.register;

import com.fox.annotation.EnableFoxScheduler;
import com.fox.entity.AnnotatedMethod;
import com.fox.utils.client.PackageScanner;
import com.fox.utils.client.ScheduleTaskScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
@Slf4j
public class FoxSchedulerRegistrar implements ApplicationListener<ApplicationStartedEvent> {
    @Value(value = "${fox.schedule.path:}")
    private String packageName;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        // 在应用程序启动后执行相关方法的逻辑
        // 可以根据需要执行初始化、调用其他方法等操作
        log.info("客户端主体程序已启动,开始注册调度方法");
        log.info("调度扫描路径为:{},如果不在预期之内，请修改配置后重新启动",packageName);
        try {
            List<AnnotatedMethod> methodList = ScheduleTaskScan.processAnnotations(packageName);
            methodList.forEach(item->{
                System.out.println("========================");
                System.out.println(item.getMethod().getName());
                System.out.println(Arrays.toString(item.getMethod().getParameterTypes()));
                System.out.println(item.getMethod().getReturnType().getName());
                System.out.println("--数值列表--");
                System.out.println(item.getAnnotationValue());
            });
        } catch (Exception e) {
            log.error("扫描失败!请检查扫描路径是否配置正确!");
            throw new RuntimeException(e);
        }
    }

}