package com.sumancloud.wx.mp;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringCloudApplication
@ComponentScan(basePackages = "com.sumancloud")
public class WxMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxMpApplication.class, args);
    }
}
