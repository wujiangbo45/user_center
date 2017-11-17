package com.navinfo.opentsp.user.common.util.uuid;

import java.util.UUID;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
public class UUIDUtil {

    public static final String randomUUID(){
        return UUID.randomUUID().toString().replace("-","").toLowerCase();
    }

}
