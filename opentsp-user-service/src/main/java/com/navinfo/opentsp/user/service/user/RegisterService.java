package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.service.param.login.QuickLoginParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByEmailParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByPhoneParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.resultdto.register.RegisterResult;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface RegisterService {

    public CommonResult<RegisterResult> registerByPhone(RegisterByPhoneParam registerByPhoneParam);

    /**
     * 快速注册
     * @param registerByPhoneParam
     * @return
     */
    public UserEntity quikRegisterByPhone(QuickLoginParam registerByPhoneParam);

    public CommonResult<RegisterResult> registerByEmail(RegisterByEmailParam registerByPhoneParam);

    public CommonResult<String> activeAccountByEmail(String encoded);

    public boolean isEmailAvailable(String email);

    public boolean isPhoneAvailable(String phone);
}
