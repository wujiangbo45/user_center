package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public interface Oauth {

    /**
     * which type of oauth login. eg.qq,weixin
     *
     * @return
     */
    String oauthType();

    /**
     * 检查 accessToken 是否正确
     * @param oauthLoginParam
     * @return
     */
    boolean validateAccessToken(OauthLoginParam oauthLoginParam);

    /**
     * 获取并创建用户
     * @param oauthLoginParam
     * @return
     */
    UserEntity getUser(OauthLoginParam oauthLoginParam);

}
