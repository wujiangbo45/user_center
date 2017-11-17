package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class TokenGeneratorImpl implements TokenGenerator {

    private static final String SPLITER = ";";

    private Random random = new Random();

    /**
     * userid + devicetype + deviceId + current time millis
     *
     * @param userEntity
     * @param deviceManaged
     * @return
     */
    @Override
    public String generate(UserEntity userEntity, DeviceManaged deviceManaged) {
        StringBuilder content = new StringBuilder(userEntity.getId()).append(SPLITER);
        /**
         * deviceType, default is web
         */
        if (StringUtils.isEmpty(deviceManaged.getDeviceType())) {
            content.append(DeviceType.web.name()).append(SPLITER);
        } else {
            content.append(deviceManaged.getDeviceType().name()).append(SPLITER);
            content.append(deviceManaged.getDeviceId());
        }

        content.append(System.currentTimeMillis()).append(random.nextDouble());

        String key = new StringBuilder(userEntity.getSalt()).append(SPLITER)
                .append(userEntity.getCreateTime().getTime()).toString();
        return EncryptUtil.HMAC_SHA1(content.toString(), key);
    }
}
