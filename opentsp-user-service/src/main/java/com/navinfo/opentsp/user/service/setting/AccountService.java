package com.navinfo.opentsp.user.service.setting;

import com.navinfo.opentsp.user.service.param.setting.*;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdateUserCarResult;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/16
 * @modify 账号设置
 * @copyright Navi Tsp
 */
public interface AccountService {

    public CommonResult<AccountResult> userDateil(String token);

    public CommonResult<AccountResult> userDetailById(String userId);

    public CommonResult<String> updateUser(UpdateUserParam param);

    public CommonResult<UpdateUserCarResult> updateUserCar(UpdateUserCarParam param);

    public CommonResult<String> bindMobile(BindMobileParam param);

    public CommonResult<String> bindEmail(BindEmailParam param);

    public CommonResult<String> bindThridPlatform(BindThriatformParam param);

    public CommonResult<String> unbindThridPlatform(UnbindThriatformParam param);

    CommonResult<String> deleteUserCar(DeleteUserCarParam deleteUserCarParam);
}
