package com.spring.redis.controller;

import com.spring.redis.entity.Product;
import com.spring.redis.publisher.RedisPublisher;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.spring.redis.constant.CommonConstants.PRODUCT_MESSAGE_CHANNEL;

@RestController
@RequiredArgsConstructor
@Tag(name = "Redis Pub/Sub ", description = """
        Below APIs Demonstrate how Redis can be used for Publishing the Messages. This message will be consumed by 2 Consumers
        - ProductRequestInventoryConsumer and ProductRequestProcessConsumer. 
        """
)
public class RedisPubSubController {

    private final RedisPublisher redisPublisher;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Product product) {
        redisPublisher.publishProductInformation(PRODUCT_MESSAGE_CHANNEL, product);
        return "Message published!";
    }
}
