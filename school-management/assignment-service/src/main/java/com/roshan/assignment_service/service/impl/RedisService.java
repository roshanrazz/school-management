package com.roshan.assignment_service.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private static final Logger log = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T get(String queueName, Class<T> entityClass) {
        try {
            Object data = redisTemplate.opsForValue().get(queueName);
            return objectMapper.convertValue(data, entityClass);

        } catch (Exception e) {
            log.info("Exception getting queue: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public <T> List<T> getList(String queueName, Class<T> type) {
        try {
            Object data = redisTemplate.opsForValue().get(queueName);
            return objectMapper.convertValue(data, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("Error getting list from Redis for key {}: {}", queueName, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void set(String queueName, Object value, Long ttl) {
        try {
            redisTemplate.opsForValue().set(queueName, value, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info("Exception setting queue: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Error deleting key {} from Redis: {}", key, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
