package com.navinfo.opentsp.user.common.util.event;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public abstract class AbstractOpentspEvent<T> implements OpentspEvent<T> {
    @Override
    public String getEventId() {
        return UUIDUtil.randomUUID();
    }
}
