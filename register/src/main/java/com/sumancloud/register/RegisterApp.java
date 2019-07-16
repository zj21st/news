package com.sumancloud.register;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringCloudApplication
@EnableEurekaServer
public class RegisterApp {
    public static void main(String[] args) {
        SpringApplication.run(RegisterApp.class,args);
    }
}