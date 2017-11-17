package com.navinfo.opentsp.user.common.util.listener;

import com.navinfo.opentsp.user.common.util.Order;
import com.navinfo.opentsp.user.common.util.event.OpentspEvent;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
public interface OpentspListener<T extends OpentspEvent<?>> extends Order{

    /**
     * true 表示异步， false表示同步
     * @return
     */
    public boolean async();

    public void onEvent(T event);

}
