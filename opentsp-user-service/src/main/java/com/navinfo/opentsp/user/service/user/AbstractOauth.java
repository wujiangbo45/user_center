package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.dao.UserProfileDao;
import com.navinfo.opentsp.user.dal.dao.UserThirdLinkDao;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;
import com.navinfo.opentsp.user.dal.entity.UserThirdLinkEntity;
import com.navinfo.opentsp.user.service.appinfo.AppInfoService;
import com.navinfo.opentsp.user.service.file.UploadFileService;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.resultdto.login.OauthResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public abstract class AbstractOauth implements Oauth {
    private static final Logger logger = LoggerFactory.getLogger(AbstractOauth.class);

    @Autowired
    private UserThirdLinkDao thirdLinkDao;
    @Autowired
    private UserEntityDao userEntityDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private UserComponent userComponent;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private UploadFileService fileService;

    @Override
    public boolean validateAccessToken(OauthLoginParam oauthLoginParam) {
        return this.getUser(oauthLoginParam) == null;
    }

    @Override
    public UserEntity getUser(OauthLoginParam oauthLoginParam) {
        /**
         * 获取到 此 product 的 appid  和 appkey
         */
        AppIDKeyEntity appIDKeyEntity = this.appInfoService.findAppInfo(oauthLoginParam.getType(), oauthLoginParam.getProduct());
        if(appIDKeyEntity == null) {
            appIDKeyEntity = this.appInfoService.findAppInfo(oauthType(), GlobalConstans.DEFAULT_PRODUCT);
        }

        OauthResult oauthResult = null;
        try {//从第三方获取信息
            oauthResult = this.getOauthResult(oauthLoginParam, appIDKeyEntity);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        if(oauthResult == null)// 如果没有获取到信息
            return null;

        /**
         * 查找是否有关联的用户
         */
        UserEntity userEntity = null;
        UserThirdLinkEntity linkEntity = this.thirdLinkDao.findByTypeAndUnionId(oauthLoginParam.getType(),oauthLoginParam.getUnionid());//.findByTypeAndId(oauthLoginParam.getType(), oauthLoginParam.getOpenId());
        logger.error("type:"+oauthLoginParam.getType()+"unionid:"+oauthLoginParam.getUnionid()+"--------------"+linkEntity);
        if (linkEntity != null && linkEntity.getIsValid() == GlobalConstans.IS_VALID_N) {
            this.thirdLinkDao.deleteById(linkEntity.getId());
            linkEntity = null;
        }

        if(linkEntity != null) {// 如果找到了用户， 说明此openId 已经登陆过
            userEntity = this.userEntityDao.findById(linkEntity.getUserId());
        } else {// 没找到 则需要注册
            /**
             * 保存用户信息
             */
            userEntity = this.userComponent.userEntity(oauthLoginParam.getProduct(), null);
            userEntity.setNickname(oauthResult.nickname());
            this.userEntityDao.save(userEntity);
            logger.info("save user entity success !");

            /**
             * 保存用户属性
             */
            UserProfileEntity userProfileEntity = this.userComponent.userProfileEntity(userEntity, oauthLoginParam);
            userProfileEntity.setGender(oauthResult.gender());

            if(!StringUtils.isEmpty(oauthResult.imgUrl()))
                this.fileService.saveThridPlatformImg(userEntity.getId(), oauthResult.imgUrl());
//            userProfileEntity.setImgUrl();
            this.userProfileDao.save(userProfileEntity);
            logger.info("save user profile success !");

            /**
             * 保存第三方关联
             */
            linkEntity = this.userComponent.userThirdLinkEntity(userEntity, appIDKeyEntity, oauthLoginParam.getProduct());
            linkEntity.setAccessToken(oauthLoginParam.getAccessToken());
            linkEntity.setRefreshToken(oauthLoginParam.getRefreshToken());
            linkEntity.setThirdOpenId(oauthLoginParam.getOpenId());
            linkEntity.setUnionid(oauthLoginParam.getUnionid());
            this.thirdLinkDao.save(linkEntity);
            logger.info("save user third link success !");
        }

        return userEntity;
    }

    public abstract OauthResult getOauthResult(OauthLoginParam oauthLoginParam, AppIDKeyEntity appIDKeyEntity) throws IllegalAccessException;

}
