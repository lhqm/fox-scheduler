package com.fox;

import com.fox.entity.AnnotatedMethod;
import com.fox.utils.client.ScheduleTaskScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 13:55
 */
@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SpringApplication.run(ClientApplication.class,args);
        List<AnnotatedMethod> methodList = ScheduleTaskScan.processAnnotations(null);
        methodList.forEach(item->{
            System.out.println("========================");
            System.out.println(item.getMethod().getName());
            System.out.println(Arrays.toString(item.getMethod().getParameterTypes()));
            System.out.println(item.getMethod().getReturnType().getName());
            System.out.println("--数值列表--");
            System.out.println(item.getAnnotationValue());
        });
    }
}
