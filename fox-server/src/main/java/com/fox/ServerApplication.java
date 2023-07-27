package com.fox;

import com.fox.annotation.EnableFoxServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 离狐千慕
 * @version 1.0
 * @date ${DATE} ${TIME}
 */
@SpringBootApplication
@EnableFoxServer
public class ServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class,args);
    }
}