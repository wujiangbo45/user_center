package com.navinfo.opentsp.user.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
public class HttpHolder {

    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();

    public static void setResponse(HttpServletResponse response){
        responseHolder.set(response);
    }

    public static HttpServletResponse getResponse(){
        return responseHolder.get();
    }

    public static void setRequest(HttpServletRequest request){
        requestHolder.set(request);
    }

    public static HttpServletRequest getRequest(){
        return requestHolder.get();
    }
}
