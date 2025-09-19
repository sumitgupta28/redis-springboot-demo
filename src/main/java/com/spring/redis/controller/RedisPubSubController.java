package com.spring.redis.controller;

import com.spring.redis.entity.Product;
import com.spring.redis.publisher.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.spring.redis.constant.CommonConstants.PRODUCT_MESSAGE_CHANNEL;

@RestController
@RequiredArgsConstructor
public class RedisPubSubController {

    private final RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Product product) {
        redisPublisher.publishProductInformation(PRODUCT_MESSAGE_CHANNEL, product);
        return "Message published!";
    }
}
