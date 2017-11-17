package com.navinfo.opentsp.user.web.filter;

import org.springframework.context.annotation.Lazy;

import javax.servlet.Filter;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-23
 * @modify
 * @copyright Navi Tsp
 */
@Lazy
public interface OpenTspFilter extends Filter {

    public String[] urlPattern();

    public Integer order();

}
