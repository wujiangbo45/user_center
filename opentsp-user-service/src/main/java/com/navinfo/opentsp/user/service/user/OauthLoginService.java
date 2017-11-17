package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
public interface OauthLoginService {

    public CommonResult<LoginResult> oauthLogin(OauthLoginParam oauthLoginParam);

}
