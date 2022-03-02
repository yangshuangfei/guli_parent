package com.stitch.service.base.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        System.out.println("创建自定义序列化的RedisTemplate");
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());//key的序列化
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());//value的序列化
        return redisTemplate;
    }

    /**
     * 两个factory都行
     * @param factory
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        System.out.println("创建自定义序列化的CacheManager");
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                //过期时间
                .entryTtl(Duration.ofSeconds(600))
                //配置序列化
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();//取出来的数据是null就不存入缓存
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .cacheDefaults(configuration)
                .build();
        return cacheManager;
    }
}
