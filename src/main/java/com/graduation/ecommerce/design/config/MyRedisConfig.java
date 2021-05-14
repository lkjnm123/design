package com.graduation.ecommerce.design.config;

import com.graduation.ecommerce.design.entity.Customer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class MyRedisConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        return container;
    }

    @Bean
    public RedisTemplate<Object, Customer> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, Customer> template = new RedisTemplate<Object,Customer>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Customer> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Customer>(Customer.class);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        return template;
    }
    @Bean
    public RedisCacheManager userRedisCacheManager(RedisConnectionFactory redisConnectionFactory){
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Customer> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Customer>(Customer.class);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer));

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
        //RedisCacheManager redisCacheManager = new RedisCacheManager(userRedisTemplate); //WRONG SpringBoot 1.x
        //RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(Objects.requireNonNull(userRedisTemplate.getConnectionFactory()));
        //RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith((RedisSerializationContext.SerializationPair<User>) userRedisTemplate.getValueSerializer());
        //return new RedisCacheManager(redisCacheWriter,redisCacheConfiguration);
    }

}
