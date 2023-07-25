package com.fox;

import com.fox.annotation.EnableFoxScheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author 离狐千慕
 * @version 1.0
 * @date 2023/7/24 13:55
 */
@SpringBootApplication
@EnableFoxScheduler
public class ClientApplication {
    public static void main(String[] args){
        SpringApplication.run(ClientApplication.class,args);
    }
}
