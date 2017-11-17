package com.navinfo.opentsp.user.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-23
 * @modify
 * @copyright Navi Tsp
 */
public abstract class OpenTspFilterAdaptor implements OpenTspFilter {

    static String[] patterns = new String[]{"/*"};

    @Override
    public String[] urlPattern() {
        return patterns;
    }

    @Override
    public Integer order() {
        return null;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
