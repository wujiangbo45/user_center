package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.device.DeviceManaged;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
public interface TokenGenerator {

    public String generate(UserEntity userEntity, DeviceManaged deviceManaged);

}
