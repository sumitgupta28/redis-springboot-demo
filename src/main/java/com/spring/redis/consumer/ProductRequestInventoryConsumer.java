package com.spring.redis.consumer;

import com.spring.redis.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@Component("productRequestInventoryConsumer")
@Slf4j
public class ProductRequestInventoryConsumer implements MessageListener {

    private final GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("[ProductRequestInventoryConsumer]Received message on Channel : {} " , new String(message.getChannel()));
            // Deserialize the message body back to your object type
            Product receivedMessage = (Product) serializer.deserialize(message.getBody());
            log.info("[ProductRequestInventoryConsumer]Received message: {} " , receivedMessage);
        } catch (Exception e) {
            log.error("[ProductRequestInventoryConsumer]Error deserializing message: {} " , e.getMessage(),e);
        }
    }
}
