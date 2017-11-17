package com.navinfo.opentsp.user.service.cache;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-15
 * @modify
 * @copyright Navi Tsp
 */
public class CustomSerializationRedisSerializer extends JdkSerializationRedisSerializer {

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    @Override
    public Object deserialize(byte[] bytes) {
        try {
            return super.deserialize(bytes);
        } catch (SerializationException exception) {
            return stringRedisSerializer.deserialize(bytes);
        }
    }
}
