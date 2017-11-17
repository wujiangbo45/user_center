package com.navinfo.opentsp.user.service.session;

import java.io.Serializable;
import java.util.Enumeration;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-14
 * @modify
 * @copyright Navi Tsp
 */
public interface Session {

    public String getId();

    public long getCreationTime();

    public long getLastAccessTime();

    public void setAttribute(String name, Serializable value);

    public Serializable getAttribute(String name);

    public Enumeration<String> getAttributeNames();

    public Serializable removeAttribute(String name);

    public void invalidate();

}
