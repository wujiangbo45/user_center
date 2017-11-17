package com.navinfo.opentsp.user.service.session;

import java.io.Serializable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public class SessionMeta implements Serializable {
    static final String META_KEY = "_meta_";

    private String id;
    private long creationTime;
    private long lastAccessTime;

    public SessionMeta(){
        this.creationTime = System.currentTimeMillis();
        this.lastAccessTime = this.creationTime;
    }

    public SessionMeta(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void updateLastAccessTime() {
        this.setLastAccessTime(System.currentTimeMillis());
    }

}
