package com.navinfo.opentsp.user.service.cache.impl;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.service.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class CacheServiceImpl4Redis implements CacheService {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    private ValueOperations<String, Serializable> opsForValue(){
        return redisTemplate.opsForValue();
    }

    private HashOperations<String, String, Serializable> opsForHash(){
        return redisTemplate.opsForHash();
    }

    @Override
    public void set(String key, Serializable data) {
        this.opsForValue().set(keys(key), data);
    }

    private String keys(String key){
        return GlobalConstans.REDIS_KEY_PREFIX + key;
    }

    @Override
    public void set(String key, Serializable data, long expire, TimeUnit unit) {
        opsForValue().set(keys(key), data, expire, unit);
    }

    @Override
    public void expire(String key, long expire, TimeUnit unit) {
        redisTemplate.expire(keys(key), expire, unit);
    }

    @Override
    public Serializable get(String key) {
        return opsForValue().get(keys(key));
    }

    @Override
    public boolean exists(String key) {
        return this.get(key) == null;
    }

    @Override
    public void delete(String key) {
        this.redisTemplate.delete(keys(key));
    }

    @Override
    public void hset(String key, String hkey, Serializable value) {
        this.opsForHash().put(keys(key), hkey, value);
    }

    @Override
    public Serializable hget(String key, String hkey) {
        return this.opsForHash().get(keys(key), hkey);
    }

    @Override
    public void hdelete(String key, String hkey) {
        this.opsForHash().delete(keys(key), hkey);
    }

    @Override
    public Long increase(String key, long increment) {
        return opsForValue().increment(keys(key), increment);
    }

    @Override
    public Long hincrease(String key, String hkey, long increment) {
        return this.opsForHash().increment(keys(key), hkey, increment);
    }

    @Override
    public Set<String> hkeys(String key) {
        return this.opsForHash().keys(this.keys(key));
    }
}
