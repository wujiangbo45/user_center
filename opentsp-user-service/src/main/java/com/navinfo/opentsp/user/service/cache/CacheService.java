package com.navinfo.opentsp.user.service.cache;

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
public interface CacheService {

    /**
     *
     * set value
     *
     * @param key
     * @param data
     */
    public void set(String key, Serializable data);

    /**
     *
     * @param key
     * @param data
     * @param expire
     */
    public void set(String key, Serializable data, long expire, TimeUnit unit);

    /**
     *
     * @param key
     * @param expire
     */
    public void expire(String key, long expire, TimeUnit unit);

    public Serializable get(String key);

    public boolean exists(String key);

    public void delete(String key);

    public void hset(String key, String hkey, Serializable value);

    public Serializable hget(String key, String hkey);

    public void hdelete(String key, String hkey);

    public Long increase(String key, long increment);

    public Long hincrease(String key, String hkey, long increment);

    public Set<String> hkeys(String key);

}
