package com.sumancloud.index;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RedissonConfig {


    @Value("${spring.redis.host}")
    private String host;

     @Value("${spring.redis.port}")
     private String port;

     @Value("${spring.redis.password}")
     private String password;

    @Bean
    public RedissonClient redisson() throws IOException {
        System.out.println("-----------------读取配置信息------------------------------");
        // 本例子使用的是yaml格式的配置文件，读取使用Config.fromYAML，如果是Json文件，则使用Config.fromJSON
        //Config config = Config.fromYAML(RedissonConfig.class.getClassLoader().getResource("redisson-config.yml22"));
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);



        System.out.println("-----------------信息读取完毕------------------------------");

        return Redisson.create(config);
    }
}
