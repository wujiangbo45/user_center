package com.navinfo.opentsp.user.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
public interface OpentspInterceptor extends HandlerInterceptor {

    public String urlMapping();

}
