package com.navinfo.opentsp.user.service.session;

import com.navinfo.opentsp.user.service.cache.CacheService;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public class DefaultSessionImpl extends SessionMeta implements Session {

    private CacheService cacheService;

    public DefaultSessionImpl(SessionMeta sessionMeta, CacheService cacheService){
        this.cacheService = cacheService;
        this.setId(sessionMeta.getId());
        this.setCreationTime(sessionMeta.getCreationTime());
        this.setLastAccessTime(sessionMeta.getLastAccessTime());
    }

    @Override
    public void setAttribute(String name, Serializable value) {
        this.cacheService.hset(this.getId(), name, value);
    }

    @Override
    public Serializable getAttribute(String name) {
        return this.cacheService.hget(this.getId(), name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        Set<String> names = this.cacheService.hkeys(this.getId());
        final Iterator<String> iterator = names.iterator();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
    }

    @Override
    public Serializable removeAttribute(String name) {
        Serializable serializable = this.cacheService.hget(this.getId(), name);
        this.cacheService.hdelete(this.getId(), name);
        return serializable;
    }

    @Override
    public void invalidate() {
        this.cacheService.delete(this.getId());
    }
}
