package com.navinfo.opentsp.user.service.configuration;

import com.navinfo.opentsp.user.service.cache.CustomSerializationRedisSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
* @author wupeng
* @version 1.0
* @date 2015-10-08
* @modify
* @copyright Navi Tsp
*/
@Configuration
public class RedisConfiguration {

    @Autowired
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new CustomSerializationRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new CustomSerializationRedisSerializer());
        return template;
    }
}

