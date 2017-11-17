package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.resultdto.login.OauthResult;
import com.navinfo.opentsp.user.service.resultdto.login.QQUserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 * @see http://wiki.connect.qq.com/get_user_info
 */
@Component
public class QQOauth extends AbstractOauth {
    private static final Logger logger = LoggerFactory.getLogger(QQOauth.class);

    public static final String type = "qq";

    @Value("${opentsp.qq.userInfo.url}")
    private String userInfoUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String oauthType() {
        return type;
    }

//    @Override
//    public boolean validateAccessToken(OauthLogin oauthLogin) {
//        return this.getUser(oauthLogin) == null;
//    }
//
//    @Override
//    public UserEntity getUser(OauthLogin oauthLogin) {
//        /**
//         * 获取到 此 product 的 appid  和 appkey
//         */
//        AppIDKeyEntity appIDKeyEntity = this.appInfoService.findAppInfo(oauthType(), oauthLogin.getProduct());
//        if(appIDKeyEntity == null) {
//            appIDKeyEntity = this.appInfoService.findAppInfo(oauthType(), GlobalConstans.DEFAULT_PRODUCT);
//        }
//
//        String openId = oauthLogin.getOpenId();
//        String accessToken = oauthLogin.getAccessToken();
//        logger.info("check openId and accessToken for qq login, openId : {}, token : {}", openId, accessToken);
//
//        /**
//         * 从第三方获取用户信息, 校验openId 和 accessToken 是否有效
//         */
//        String url = userInfoUrl + "?openid=" + openId + "&access_token=" + accessToken + "&oauth_consumer_key=" + appIDKeyEntity.getAppid();
//        logger.info("user info url : {}", url);
//
//        String str = this.restTemplate.getForObject(url, String.class);
//        QQUserInfo qqUserInfo = JsonUtil.fromJson(str, QQUserInfo.class);
//        if(!"0".equals(qqUserInfo.getRet())) {   // 未成功获取到用户信息
//            logger.error("QQ server response with code: {} and body: \n{}", qqUserInfo.getRet(), qqUserInfo.getMsg());
//            return null;
//        }
//
//        /**
//         * 查找是否有关联的用户
//         */
//        UserEntity userEntity = null;
//        UserThirdLinkEntity linkEntity = this.thirdLinkDao.findByTypeAndId(type, openId);
//        if(linkEntity != null) {// 如果找到了用户， 说明此openId 已经登陆过
//            userEntity = this.userEntityDao.findById(linkEntity.getUserId());
//        } else {// 没找到 则需要注册
//            /**
//             * 保存用户信息
//             */
//            userEntity = this.userComponent.userEntity(oauthLogin.getProduct());
//            userEntity.setNickname(qqUserInfo.getNickname());
//            this.userEntityDao.save(userEntity);
//            logger.info("save user entity success !");
//
//            /**
//             * 保存用户属性
//             */
//            UserProfileEntity userProfileEntity = this.userComponent.userProfileEntity(userEntity, oauthLogin);
//            userProfileEntity.setGender("男".equals(qqUserInfo.getGender()) ? UserProfileEntity.GENDER_MAN : UserProfileEntity.GENDER_WOMAN);
////          userProfileEntity.setImgUrl();
//            this.userProfileDao.save(userProfileEntity);
//            logger.info("save user profile success !");
//
//            /**
//             * 保存第三方关联
//             */
//            linkEntity = this.userComponent.userThirdLinkEntity(userEntity, appIDKeyEntity, oauthLogin.getProduct());
//            linkEntity.setAccessToken(oauthLogin.getAccessToken());
//            linkEntity.setRefreshToken(oauthLogin.getRefreshToken());
//            linkEntity.setThirdOpenId(oauthLogin.getOpenId());
//            this.thirdLinkDao.save(linkEntity);
//            logger.info("save user third link success !");
//        }
//
//        return userEntity;
//    }

    public OauthResult getOauthResult(OauthLoginParam oauthLoginParam, AppIDKeyEntity appIDKeyEntity)
            throws IllegalAccessException {

        String openId = oauthLoginParam.getOpenId();
        String accessToken = oauthLoginParam.getAccessToken();
        logger.info("check openId and accessToken for qq login, openId : {}, token : {}", openId, accessToken);

        /**
         * 从第三方获取用户信息, 校验openId 和 accessToken 是否有效
         */
        String url = userInfoUrl + "?openid=" + openId + "&access_token=" + accessToken + "&oauth_consumer_key=" + appIDKeyEntity.getAppid();
        logger.info("user info url : {}", url);

        String str = this.restTemplate.getForObject(url, String.class);
        QQUserInfo qqUserInfo = JsonUtil.fromJson(str, QQUserInfo.class);
        if(!"0".equals(qqUserInfo.getRet())) {   // 未成功获取到用户信息
            logger.error("QQ server response with code: {} and body: \n{}", qqUserInfo.getRet(), qqUserInfo.getMsg());
            return null;
        }

        return qqUserInfo;
    }
}
