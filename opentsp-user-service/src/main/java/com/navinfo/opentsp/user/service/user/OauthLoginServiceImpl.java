package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.dal.dao.UserLoginLogDao;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.token.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */

@Service
public class OauthLoginServiceImpl implements OauthLoginService {
    private static final Logger logger = LoggerFactory.getLogger(OauthLoginServiceImpl.class);

    private final Map<String, Oauth> oauthMap;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserComponent userComponent;
    @Autowired
    private UserLoginLogDao loginLogDao;

    @Autowired
    public OauthLoginServiceImpl(List<Oauth> oauths) {
        Map<String, Oauth> oauthMap1 = new HashMap<>();
        for (Oauth oauth : oauths) {
            oauthMap1.put(oauth.oauthType(), oauth);
            logger.info("loading oauth : {}", oauth.getClass());
        }

        oauthMap = Collections.unmodifiableMap(oauthMap1);
    }

    @Override
    public CommonResult<LoginResult> oauthLogin(OauthLoginParam oauthLoginParam) {
        String type = oauthLoginParam.getType();
        Oauth oauth = oauthMap.get(type);
        if(oauth == null) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, LoginResult.class);
        }

        UserEntity userEntity = oauth.getUser(oauthLoginParam);
        if(userEntity == null) {
            logger.error("oauth login error !! param : {}", oauthLoginParam.toString());
            return CommonResult.newInstance(ResultCode.THIRD_USER_PROFILE_FAILED, LoginResult.class);
        }

        TokenEntity tokenEntity = this.tokenService.createToken(userEntity, oauthLoginParam, oauthLoginParam.getIp(),
                oauthLoginParam.getProduct(), oauthLoginParam.getOpenId());

        UserLoginLogEntity loginLogEntity = this.userComponent.userLoginLogEntity(userEntity, tokenEntity);
        loginLogEntity.setLoginResult("oauth login success !");
        this.loginLogDao.save(loginLogEntity);

        return CommonResult.newInstance(ResultCode.SUCCESS, LoginResult.class)
                .setData(new LoginResult(userEntity.getId(), userEntity.getNickname(), tokenEntity.getToken()));
    }
}
