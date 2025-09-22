package com.spring.redis.stream.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import static com.spring.redis.constant.CommonConstants.GROUP_NAME_BILLING_DEPT;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingDeptPurchaseEventConsumer implements StreamListener<String, MapRecord<String, String, String>> {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${redis-stream.stream-key}")
    private String streamKey;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        log.info("Received message ID: {} ", message.getId());
        log.info("Received message data: {} ", message.getValue());

        // Acknowledge the message to remove it from the Pending Entries List (PEL)
        redisTemplate.opsForStream().acknowledge(GROUP_NAME_BILLING_DEPT, message);
    }
}
