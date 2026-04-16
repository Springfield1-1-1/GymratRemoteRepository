package com.springfield.gymrat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Slf4j
@Configuration
public class RedisCacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        log.info("初始化 Redis CacheManager...");

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))   // 缓存有效期 10 分钟
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) // 键转成字符串存储
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))  // 值转成JSON存储
                .disableCachingNullValues();    // 如果结果是 null，就不存进缓存（防止缓存穿透）

        log.info("Redis 连接成功，开始配置缓存管理器");

        return RedisCacheManager.builder(connectionFactory) // 创建缓存管理器
                .cacheDefaults(config)  // 默认缓存配置
                .withCacheConfiguration("equipmentListCache", config.entryTtl(Duration.ofMinutes(30)))  //  equipmentListCache 缓存有效期 30 分钟
                .transactionAware() // 开启事务
                .build();   // 创建缓存管理器并返回
    }
}
