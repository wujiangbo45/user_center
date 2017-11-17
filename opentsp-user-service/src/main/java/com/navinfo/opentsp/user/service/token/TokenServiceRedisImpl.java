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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 *
 * <p>
 *     token 存在于redis， redis中找不到则认为token已经失效.
 * </p>
 * <p>
 *     谨慎起见， 提供了一个开关（默认关闭的），可以将token存储于mysql， 但仅仅是存储， 不参与查询。 查询只在redis
 * </p>
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-22
 * @modify
 * @copyright Navi Tsp
 */
@Profile("!persistToken")
@Service
public class TokenServiceRedisImpl implements TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenServiceRedisImpl.class);

    @Value("${opentsp.token.persist:false}")
    private boolean persistToken;

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private TokenDao tokenDao;
    @Autowired
    private ConfigPropertyService configPropertyService;

    @Override
    public void removeToken(String token) {
        userCacheService.removeTokenFromCache(token);
    }

    @Override
    public void removeUserToken(String userId) {
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

        logger.debug("save token success ! token : {} ", tokenData.getToken());
        this.userCacheService.putTokenIntoCache(tokenData);

        if(persistToken) {
            this.tokenDao.save(tokenData);
        }

        return tokenData;
    }

    @Override
    public void extendTokenExpireTime(String token) {
        if (StringUtils.isEmpty(token))
            return;

        TokenEntity tokenEntity = this.getToken(token);
        if (tokenEntity != null) {
            this.userCacheService.putTokenIntoCache(tokenEntity);
        }
    }

    @Override
    public TokenEntity getToken(String token) {
        logger.debug("token data not in redis !");
        return (TokenEntity) this.userCacheService.getTokenFromCache(token);
    }

    @Override
    public TokenEntity getLatestAvailableToken(String userId, DeviceType deviceType) {
        return this.userCacheService.getTokenFromCache(userId, deviceType);
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
        return valid;
    }
}
