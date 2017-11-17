package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.service.exception.UserNotLoginException;
import com.navinfo.opentsp.user.service.exception.ValidatorException;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.token.TokenManaged;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.user.UserComponent;
import com.navinfo.opentsp.user.service.validator.ParamValidatable;
import com.navinfo.opentsp.user.web.autologin.AutoLoginHelper;
import com.navinfo.opentsp.user.web.interceptor.HttpHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * 同时执行自动登录的功能
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class TokenValidator implements OpentspValidator {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserComponent userComponent;
    @Autowired
    private AutoLoginHelper autoLoginHelper;

    @Override
    public boolean validate(ParamValidatable... args) {
        for(ParamValidatable arg : args) {
            if(arg instanceof TokenManaged) {
                if(!this.check((TokenManaged) arg)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean check(TokenManaged baseTokenParam){
        String token = baseTokenParam.getToken();
        if(StringUtils.isEmpty(token)) {//如果参数没有传
            token = HttpHolder.getRequest().getHeader(GlobalConstans.TOKEN_HEADER_NAME);//则从header里面取
            if(!StringUtils.isEmpty(token)) {//header里面有
                baseTokenParam.setToken(token);
            }
        }

        if(!StringUtils.isEmpty(token)) {
            TokenEntity tokenEntity = this.tokenService.getToken(token);
            if(this.tokenService.isTokenValid(tokenEntity)) {//如果token 有效
                baseTokenParam.setUser(userComponent.getUserFromToken(token));
                baseTokenParam.setTokenEntity(tokenEntity);
                return true;
            }
        }

        CommonResult<LoginResult> loginResultCommonResult = this.autoLoginHelper.autoLogin(HttpHolder.getRequest(), HttpHolder.getResponse());
        if(loginResultCommonResult.getCode() == ResultCode.SUCCESS.code()) {//如果登录成功了
            token = loginResultCommonResult.getData().getToken();
            HttpHolder.getResponse().setHeader(GlobalConstans.TOKEN_HEADER_NAME, token);
            TokenEntity tokenEntity = this.tokenService.getToken(token);
            baseTokenParam.setUser(userComponent.getUserFromToken(token));
            baseTokenParam.setTokenEntity(tokenEntity);
            baseTokenParam.setToken(token);
            return true;
        }

        //如果有token， 但是上面的条件都未通过，则认为token是无效的
        if(!StringUtils.isEmpty(token)) {
            throw new ValidatorException("token is invalid !", ResultCode.TOKEN_INVALID);
        }

        throw new UserNotLoginException("invalid token !");
    }
}
