package com.navinfo.opentsp.user.common.util.event;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
public interface OpentspEvent<T> {

    public String getEventId();

    public T getData();

}
