package com.navinfo.opentsp.user.sso.constant;

/**
 * Created by duxj on 2016/3/17.
 */
public class SSOConstant {
    //动态配置的常量
    //格式：项目名称_JSESSIONID
    public final static String PROJECT_SESSION_NAME = "_JSESSIONID_";
    //格式：项目名称_SSO
    public final static String REDIS_KEY_PREFIX = "_SSO_";

    /**redis模拟session用户的属性字段**/
    public final static String REDIS_PROPERTY_USERID = "userId";
    public final static String REDIS_PROPERTY_TOKEN = "token";
    public final static String REDIS_PROPERTY_NICKNAME = "nickname";
    public final static String REDIS_PROPERTY_JESSIONID = "jSessionId";
    public final static String REDIS_PROPERTY_EMAIL = "email";
    /**redis中key的过期时间***/
    public final static int REDIS_EXPIRE_TIME = 600 ;
    
    /**redis模拟session用户的属性字段**/

    /**SSO存放request的属性字段**/
    public final static String SSO_USER_ID = "userId";
    public final static String SSO_TOKEN = "token";
    public final static String SSO_NICKNAME = "nickname";
    /**SSO存放request的属性字段**/
    public final static String PARAM_NAME_EXCLUSIONS = "exclusions";
    public final static String PARAM_NAME_VALIDATE_URL = "validateTokenUrl";

}
