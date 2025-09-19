package com.spring.redis.publisher;

import com.spring.redis.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publishProductInformation(String channel, Product message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
