package com.navinfo.opentsp.user.service.var;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface ConfigPropertyService {

    /**
     * 注册时候，图形验证码有效时间
     */
    String PROP_REGISTER_CAPTCHA_TIMEOUT = "captcha_timeout";

    /**
     * 注册时候 短信验证码有效时间
     */
    String PROP_REGISTER_SMS_TIMEOUT = "sms_timeout";

    /**
     * 注册的时候  邮件验证码的有效时间(秒)
     */
    String PROP_REGISTER_EMAIL_TIMEOUT = "email_timeout";

    /**
     * 短信验证码的长度
     */
    String PROP_SMS_CODE_LENGTH = "sms_code_length";

    /**
     * token的有效时间，分钟
     */
    String PROP_TOKEN_EXPIRE_TIME = "token_expire_time";

    /**
     * web 的token失效时间, 分钟
     */
    String PROP_TOKEN_WEB_EXPIRE_TIME = "token_web_expire_time";

    /**
     * session 有效时间 分钟
     */
    String PROP_SESSION_EXPIRE_TIME = "session_expire_time";

    /**
     * 简单的aes加密的key
     */
    String PROP_SIMPLE_AES_KEY = "simple_aes_key";

    /**
     * 登录失败时间， 分钟 。 在 ${login_failed_time}分钟 内 登录失败 ${login_failed_count} 次，需要验证码
     */
    String PROP_LOGIN_FAILED_TIME = "login_failed_time";

    /**
     * 登录失败次数。在 ${login_failed_time}分钟 内 登录失败 ${login_failed_count} 次，需要验证码
     */
    String PROP_LOGIN_FAILED_COUNT = "login_failed_count";

    /**
     * 每个ip一天最多发几条
     */
    String PROP_SMS_IP_MAX = "sms_ip_max";

    /**
     * 每个手机号一天最多发几条
     */
    String PROP_SMS_PHONE_MAX = "sms_phone_max";

    /**
     * 每个手机号，相同内容每天最多发几次
     */
    String PROP_SMS_PHONE_CONTENT_MAX = "sms_phone_content_max";


    /**
     * 查找配置的属性
     * @param name
     * @param product
     * @param type
     * @return
     */
    public String getValue(String name, String product, ConfigPropertyEntity.Types type);

    public String getValue(String name, ConfigPropertyEntity.Types type);

    public void reload();

}
