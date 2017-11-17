package com.navinfo.opentsp.user.web.util;


import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/3/15
 * @modify
 * @copyright Navi Tsp
 */
public class SessionUtil {


    public static String SESSION_ID_PREFIX = "ssosessionid_";

    public static String SSO_LOGOUT_PREFIX = "ssologout_";

    /**
     * 获取客户端的jsessionId
     * @param request
     * @return
     */
    public static String getSessionId(HttpServletRequest request){

        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if(GlobalConstans.SSO_SESSION_NAME.equalsIgnoreCase(cookie.getName()))
                    sessionId = cookie.getValue();
            }
        }
        if(sessionId == null )
            sessionId =	(String) request.getAttribute(GlobalConstans.SSO_SESSION_NAME);
        return  sessionId;
    }

    /**
     * 在cookie中创建sessionId
     * @param request
     * @param response
     * @return sessionId
     */
    public static String createSessionId(HttpServletRequest request,HttpServletResponse response){
        String sessionId =UUIDUtil.randomUUID();
        //request.setAttribute("APPSTORESESSIONID", sessionId);
        /****写cookie****/
        Cookie cookie = new Cookie(GlobalConstans.SSO_SESSION_NAME, sessionId);
        cookie.setPath("/");
     //   cookie.setMaxAge(864000);//设置过期10天
        response.addCookie(cookie);
        /****写cookie****/
        return sessionId;
    }




}
