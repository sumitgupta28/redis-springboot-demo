package com.spring.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedisSpringBootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisSpringBootDemoApplication.class, args);
    }

}
