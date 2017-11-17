package com.navinfo.opentsp.user.service.token;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.dao.TokenDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * <p>
 *     token 持久化于mysql， 同时使用redis做缓存
 * </p>
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
@Profile("persistToken")
@Service
public class TokenServiceImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private ConfigPropertyService configPropertyService;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private ExecutorService executorService;

    @Override
    public void removeToken(String token) {
        this.tokenDao.removeToken(token);
        userCacheService.removeTokenFromCache(token);
    }

    @Override
    public void removeUserToken(String userId) {
        this.tokenDao.removeAllToken(userId);
        this.userCacheService.removeAllToken(userId);
    }

    @Override
    public void removeUserTokenExcept(String token) {
        TokenEntity tokenEntity = this.getToken(token);
        if(tokenEntity != null) {
            String userId = tokenEntity.getUserId();
            String deviceType = tokenEntity.getDeviceType();
            for(DeviceType type : DeviceType.values()) {
                if(!type.name().equals(deviceType)) {
                    this.removeUserTokenByDeviceType(userId, type);
                }
            }
        }
    }

    @Override
    public void removeUserTokenByDeviceType(String userId, DeviceType deviceType) {
        this.tokenDao.removeTokenByDeviceType(userId, deviceType.name());
        this.userCacheService.removeTokenFromCache(userId, deviceType);
    }

    @Override
    public TokenEntity createToken(UserEntity userEntity, DeviceManaged deviceManaged, String clientIp, String productId, String loginName) {
        /**
         * 首先删除此设备类型的所有token
         */
        this.removeUserTokenByDeviceType(userEntity.getId(), deviceManaged.getDeviceType());

        TokenEntity tokenData = new TokenEntity();
        tokenData.setToken(tokenGenerator.generate(userEntity, deviceManaged));
        tokenData.setUserId(userEntity.getId());
        tokenData.setAppVersion(deviceManaged.getAppVersion());
        tokenData.setDeviceId(deviceManaged.getDeviceId());
        tokenData.setDeviceType(deviceManaged.getDeviceType().name());
        tokenData.setClientIp(clientIp);
        tokenData.setOpProductId(productId);
        tokenData.setLoginName(loginName);
        tokenData.setIsValid(GlobalConstans.IS_VALID_Y);

        this.tokenDao.save(tokenData);
        logger.debug("save token success ! token : {} ", tokenData.getToken());
        this.userCacheService.putTokenIntoCache(tokenData);
        return tokenData;
    }

    @Override
    public void extendTokenExpireTime(String token) {
        //TODO implement it
    }

    @Override
    public TokenEntity getToken(String token) {
        logger.debug("token data not in redis !");
        TokenEntity tokenEntity = (TokenEntity) this.userCacheService.getTokenFromCache(token);
        if(tokenEntity == null) {
            tokenEntity = this.tokenDao.getToken(token);
            if(this.isTokenValid(tokenEntity)) {
                this.userCacheService.putTokenIntoCache(tokenEntity);
            }
        }

        return tokenEntity;
    }

    @Override
    public TokenEntity getLatestAvailableToken(String userId, DeviceType deviceType) {
        return this.tokenDao.getLatestAvailableToken(userId, deviceType.name());
    }

    @Override
    public boolean isTokenValid(TokenEntity tokenEntity) {
        if(tokenEntity == null)
            return false;

        if(tokenEntity.getIsValid() == GlobalConstans.IS_VALID_N) {
            return false;
        }

        long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_TOKEN_EXPIRE_TIME,
            tokenEntity.getOpProductId(), ConfigPropertyEntity.Types.session));

        if(DeviceType.web.name().equals(tokenEntity.getDeviceType())) {
            timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_TOKEN_WEB_EXPIRE_TIME,
                    tokenEntity.getOpProductId(), ConfigPropertyEntity.Types.session));
        }

        boolean valid = tokenEntity.getCreateTime().getTime() + TimeUnit.MINUTES.toMillis(timeout) >= System.currentTimeMillis();
        if(!valid) {// 如果token 已经失效了， 则将他删除
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    removeToken(tokenEntity.getToken());
                }
            });
        }

        return valid;
    }
}
