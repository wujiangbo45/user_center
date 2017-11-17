package com.navinfo.opentsp.user.common.util.constants;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface GlobalConstans {

    /**
     * 存放redis的前缀
     */
    String REDIS_KEY_PREFIX = "op_usr.";

    String REDIS_KEY_SESSION = "session.";

    String REDIS_KEY_USER = "user.";

    /**
     * 任务锁定的key
     */
    String TASK_LOCK_KEY = REDIS_KEY_PREFIX + "lock.";

    /**
     * device_id  的  header名称
     */
    String HEADER_DEVICE_ID = "device-id";

    /**
     * device_type 的 header 名称
     */
    String HEADER_DEVICE_TYPE = "device-type";

    /**
     * app_version  的  header 名称
     */
    String HEADER_APP_VERSION = "app-version";

    /**
     * 默认的产品
     */
    String DEFAULT_PRODUCT = "-1";

    byte IS_VALID_Y = 1;
    byte IS_VALID_N = 0;

    String TOKEN_HEADER_NAME = "X-Auth-Token";

    static final String BIND_TYPE = "bind";
    static final String CHANGE_BIND_TYPE = "changeBind";

    static final String SSO_SESSION_NAME="SSOSESSIONID";
}
