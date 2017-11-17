package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.entity.*;
import com.navinfo.opentsp.user.service.device.DeviceManaged;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.security.PasswordService;
import com.navinfo.opentsp.user.service.session.Session;
import com.navinfo.opentsp.user.service.session.SessionAttrConstant;
import com.navinfo.opentsp.user.service.session.SessionManager;
import com.navinfo.opentsp.user.service.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Random;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class UserComponent {
    private static final Logger logger = LoggerFactory.getLogger(UserComponent.class);

    private final Random random = new Random();

    @Autowired
    private PasswordService passwordService;
    @Autowired
    private SessionManager sessionManager;
    @Autowired
    private UserEntityDao userEntityDao;
    @Autowired
    private TokenService tokenService;

    public UserEntity getUserFromToken(String token){
        Session session = this.sessionManager.getSession(token);
        UserEntity userEntity = (UserEntity) session.getAttribute(SessionAttrConstant.ATTR_USER_ENTITY);
        if(userEntity == null) {
            TokenEntity tokenEntity = tokenService.getToken(token);
            userEntity = this.userEntityDao.findById(tokenEntity.getUserId());
            session.setAttribute(SessionAttrConstant.ATTR_USER_ENTITY, userEntity);
        }

        return userEntity;
    }

    public UserLoginLogEntity userLoginLogEntity(UserEntity userEntity, TokenEntity tokenEntity){
        UserLoginLogEntity userLoginLogEntity = new UserLoginLogEntity();
        userLoginLogEntity.setUserId(userEntity.getId());
        userLoginLogEntity.setToken(tokenEntity.getToken());
        userLoginLogEntity.setOpProductId(tokenEntity.getOpProductId());
        userLoginLogEntity.setAppVersion(tokenEntity.getAppVersion());
        userLoginLogEntity.setDeviceId(tokenEntity.getDeviceId());
        userLoginLogEntity.setDeviceType(tokenEntity.getDeviceType());
        userLoginLogEntity.setLoginIp(tokenEntity.getClientIp());
        userLoginLogEntity.setLoginName(tokenEntity.getLoginName());
        userLoginLogEntity.setLoginResult("success");
        userLoginLogEntity.setLoginTime(new Date());
        userLoginLogEntity.setProductId(userEntity.getProductId());

        return userLoginLogEntity;
    }

    public UserLoginLogEntity failedLoginLog(DeviceManaged deviceManaged, String product, String loginName, String opProduct, String ip){
        UserLoginLogEntity userLoginLogEntity = new UserLoginLogEntity();
        userLoginLogEntity.setOpProductId(opProduct);
        userLoginLogEntity.setAppVersion(deviceManaged.getAppVersion());
        userLoginLogEntity.setDeviceId(deviceManaged.getDeviceId());
        userLoginLogEntity.setDeviceType(deviceManaged.getDeviceType().name());
        userLoginLogEntity.setLoginIp(ip);
        userLoginLogEntity.setLoginName(loginName);
        userLoginLogEntity.setLoginResult("failed");
        userLoginLogEntity.setLoginTime(new Date());
        userLoginLogEntity.setProductId(product);

        return userLoginLogEntity;
    }

    public ResultCode validateUserStatus(UserEntity userEntity){
        /**
         * 帐号没激活
         */
        if(userEntity.getAccountActived() == null || userEntity.getAccountActived().byteValue() == UserEntity.ACCOUNT_NO_ACTIVED) {
            return ResultCode.ACCOUNT_NOT_ACTIVED;
        }

        /**
         * 是否过期
         */
        if(userEntity.getAccountNonExpired() == null || userEntity.getAccountNonExpired() == UserEntity.ACCOUNT_EXPIRED) {
            return ResultCode.ACCOUNT_EXPIRED;
        }

        /**
         * 帐号已锁定
         */
        if(userEntity.getAccountNonLocked() == null || userEntity.getAccountNonLocked() == UserEntity.ACCOUNT_LOCKED) {
            return ResultCode.ACCOUNT_LOCKED;
        }

        /**
         * 帐号已经禁用
         */
        if(userEntity.getEnable() == null || userEntity.getEnable() == UserEntity.ACCOUNT_DISABLE) {
            return ResultCode.ACCOUNT_DISABLED;
        }

        return ResultCode.SUCCESS;
    }

    public String salt(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 5; i++) {
            sb.append((char) random.nextInt(255));
        }

        return sb.toString();
    }

    /**
     * 创建对象， 创建完成之后， 需要调用者 设置 mobile  或者  email 属性值
     * @param product
     * @return
     */
    public UserEntity userEntity(String product, String password) {
        /**
         * save user entity
         */
        UserEntity userEntity = new UserEntity();
        userEntity.setAccountActived(UserEntity.ACCOUNT_ACTIVED);
        userEntity.setEnable(GlobalConstans.IS_VALID_Y);
        userEntity.setAccountNonExpired(UserEntity.ACCOUNT_NO_EXPIRED);
        userEntity.setAccountNonLocked(UserEntity.ACCOUNT_NO_LOCKED);
        userEntity.setProductId(product);
        userEntity.setSalt(this.salt());
        userEntity.setEnable(UserEntity.ACCOUNT_ENABLE);
        userEntity.setCreateTime(new Date());
        userEntity.setUpdateTime(userEntity.getCreateTime());
        if(StringUtils.isEmpty(password)) {
            password = this.salt();
        }
        userEntity.setPassword(passwordService.encodePassword(password,
                userEntity.getSalt()));

        return userEntity;
    }

    /**
     * 创建对象，创建完成之后，需要调用者设置 emailActived mobileActived 属性值
     * @param userEntity
     * @param deviceManaged
     * @return
     */
    public UserProfileEntity userProfileEntity(UserEntity userEntity, DeviceManaged deviceManaged){
        /**
         * save user profile
         */
        UserProfileEntity profileEntity = new UserProfileEntity();
        profileEntity.setUserId(userEntity.getId());
        profileEntity.setEmailActived(UserProfileEntity.EMAIL_NO_ACTIVED);
        profileEntity.setMobileActived(UserProfileEntity.MOBILE_NO_ACTIVED);
        profileEntity.setNicknameModifiable(UserProfileEntity.NICKNAME_CAN_MODIFY);
        profileEntity.setRegisterAppVersion(deviceManaged.getAppVersion());
        profileEntity.setRegisterDeviceId(deviceManaged.getDeviceId());
        profileEntity.setRegisterDeviceType(deviceManaged.getDeviceType().name());
        profileEntity.setRegisterSrc(deviceManaged.getDeviceType().name());

        return profileEntity;
    }

    /**
     * 创建第三方关联对象， 创建完成后， 需要调用者设置  accessToken, openId, refreshToken 信息
     * @param userEntity
     * @param appIDKeyEntity
     * @return
     */
    public UserThirdLinkEntity userThirdLinkEntity(UserEntity userEntity, AppIDKeyEntity appIDKeyEntity, String opProductId){
        UserThirdLinkEntity thirdLinkEntity = new UserThirdLinkEntity();
        thirdLinkEntity.setUpdateTime(new Date());
        thirdLinkEntity.setCreateTime(thirdLinkEntity.getUpdateTime());
        thirdLinkEntity.setUserId(userEntity.getId());
        thirdLinkEntity.setOpProductId(opProductId);
        thirdLinkEntity.setIsValid(GlobalConstans.IS_VALID_Y);
        thirdLinkEntity.setThirdAuthInfo(appIDKeyEntity.getId());
        thirdLinkEntity.setThirdType(appIDKeyEntity.getThirdType());

        return thirdLinkEntity;
    }

}
