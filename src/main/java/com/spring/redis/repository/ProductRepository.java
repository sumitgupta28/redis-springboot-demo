package com.spring.redis.repository;

import com.spring.redis.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static final String HASH_KEY = "Product";
    private final RedisTemplate<String, Object> redisTemplate;

    public void save(Product product) {
        redisTemplate.opsForHash().put(HASH_KEY, product.getId(), product);
    }


    public Product findById(String id) {
        return (Product) redisTemplate.opsForHash().get(HASH_KEY, id);
    }


    public void deleteById(String id) {
        redisTemplate.opsForHash().delete(HASH_KEY, id);
    }
}
