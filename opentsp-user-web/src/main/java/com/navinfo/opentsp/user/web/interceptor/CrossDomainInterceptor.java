package com.navinfo.opentsp.user.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
//cross domain operation must in Filter!
//@Component
@Deprecated
public class CrossDomainInterceptor implements OpentspInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(CrossDomainInterceptor.class);

    @Override
    public String urlMapping() {
        return "/**";
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        HttpServletRequest servletRequest = (HttpServletRequest) request;

        String origin = servletRequest.getHeader("Origin");//find origin header, allow cross domain
        logger.info("find Origin : {}", origin);
        if(StringUtils.isEmpty(origin)) {// if no origin header
            origin = servletRequest.getHeader("Referer");//
            logger.info("find Referer : {}", origin);
            if(StringUtils.isEmpty(origin)) {
                origin = "*";
            } else {

            }
        }

        servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        servletResponse.setHeader("Access-Control-Allow-Origin", "*");
//        servletResponse.setHeader("Access-Control-Max-Age", "1728000");
        servletResponse.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, x-requested-with, sid, mycustom, smuser");
        servletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
