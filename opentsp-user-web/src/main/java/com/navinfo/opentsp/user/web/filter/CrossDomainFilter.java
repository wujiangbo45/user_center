package com.navinfo.opentsp.user.web.filter;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-23
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class CrossDomainFilter extends OpenTspFilterAdaptor {
    private static final Logger logger = LoggerFactory.getLogger(CrossDomainFilter.class);

    private static final String methods = "POST, GET, OPTIONS, DELETE, PATCH, HEAD, TRACE, OPTIONS, CONNECT";
    private static final String headers;

    static {
        headers = "origin, content-type, accept, x-requested-with,"
                + GlobalConstans.TOKEN_HEADER_NAME + ","
                + GlobalConstans.HEADER_DEVICE_ID + ","
                + GlobalConstans.HEADER_DEVICE_TYPE + ","
                + GlobalConstans.HEADER_APP_VERSION;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        servletResponse.setHeader("Access-Control-Allow-Origin", origin);//Access-Control-Allow-Credentials 设置为true， 此项不可设置为 *
        servletResponse.setHeader("Access-Control-Max-Age", "1728000");
        servletResponse.setHeader("Access-Control-Allow-Methods", methods);
        servletResponse.setHeader("Access-Control-Expose-Headers", GlobalConstans.TOKEN_HEADER_NAME);
        servletResponse.setHeader("Access-Control-Allow-Headers", headers);

        chain.doFilter(request, response);
    }
}
