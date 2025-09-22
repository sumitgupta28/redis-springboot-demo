package com.spring.redis.stream.config;

import com.spring.redis.stream.service.BillingDeptPurchaseEventConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.nio.charset.StandardCharsets;

import static com.spring.redis.constant.CommonConstants.GROUP_NAME_BILLING_DEPT;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class RedisStreamConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisTemplate<String, String> redisTemplate;
    @Value("${redis-stream.stream-key}")
    private String streamKey;

    // Bean to create the consumer group on startup
    @Bean("streamMessageListenerContainerForBillingDept")
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainerForBillingDept() {
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(redisConnectionFactory);
        try {
            // Check if the consumer group exists, create if not
            redisConnectionFactory.getConnection().streamCommands()
                    .xInfoGroups(streamKey.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            redisConnectionFactory.getConnection().streamCommands()
                    .xGroupCreate(streamKey.getBytes(StandardCharsets.UTF_8), GROUP_NAME_BILLING_DEPT, ReadOffset.from("0-0"), true);
            log.debug("Created consumer group: " + GROUP_NAME_BILLING_DEPT);
        }

        // Add the stream listener to the container
        container.receive(
                Consumer.from(GROUP_NAME_BILLING_DEPT, "BillingDeptPurchaseEventConsumer-Consumer-1"), // consumer-1 is the name of this specific consumer instance
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                new BillingDeptPurchaseEventConsumer(redisTemplate));
        container.start();
        return container;
    }


}
