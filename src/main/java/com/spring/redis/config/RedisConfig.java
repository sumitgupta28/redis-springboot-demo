package com.spring.redis.config;

import com.spring.redis.consumer.ProductRequestInventoryConsumer;
import com.spring.redis.consumer.ProductRequestProcessConsumer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static com.spring.redis.constant.CommonConstants.PRODUCT_MESSAGE_CHANNEL;

@Configuration
public class RedisConfig {

    /**
     * Redis configuration for Messages Provider and Database (in-memory)
     */

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // or other serializers
        return template;
    }


    /**
     * Redis Cache Configuration
     */
    /*@Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Set default TTL to 10 minutes
                .disableCachingNullValues();
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config)..build();
    }
*/
    @Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> {
            Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
            configurationMap.put("users", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(20)));
            builder.withInitialCacheConfigurations(configurationMap);
        };
    }


    /**
     * Redis configuration for Consumer
     */

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   @Qualifier("productRequestInventoryConsumerMessageListenerAdapter") MessageListenerAdapter productRequestInventoryConsumer,
                                                   @Qualifier("productRequestProcessConsumerMessageListenerAdapter") MessageListenerAdapter productRequestProcessConsumer) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(productRequestInventoryConsumer, new ChannelTopic(PRODUCT_MESSAGE_CHANNEL)); // Define your channel
        container.addMessageListener(productRequestProcessConsumer, new ChannelTopic(PRODUCT_MESSAGE_CHANNEL)); // Define your channel
        return container;
    }

    @Bean("productRequestInventoryConsumerMessageListenerAdapter")
    public MessageListenerAdapter productRequestInventoryConsumer(@Qualifier("productRequestInventoryConsumer") ProductRequestInventoryConsumer productRequestInventoryConsumer) {
        return new MessageListenerAdapter(productRequestInventoryConsumer);
    }

    @Bean("productRequestProcessConsumerMessageListenerAdapter")
    public MessageListenerAdapter productRequestProcessConsumer(@Qualifier("productRequestProcessConsumer") ProductRequestProcessConsumer productRequestProcessConsumer) {
        return new MessageListenerAdapter(productRequestProcessConsumer);
    }


}
