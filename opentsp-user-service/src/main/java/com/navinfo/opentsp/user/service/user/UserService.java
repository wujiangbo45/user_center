package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import com.navinfo.opentsp.user.service.param.login.LoginParam;
import com.navinfo.opentsp.user.service.param.login.LogoutParam;
import com.navinfo.opentsp.user.service.param.login.QuickLoginParam;
import com.navinfo.opentsp.user.service.param.login.RefreshTokenParam;
import com.navinfo.opentsp.user.service.param.password.ChangePasswdParam;
import com.navinfo.opentsp.user.service.param.password.FindPasswordByEmailParam;
import com.navinfo.opentsp.user.service.param.password.FindPasswordBySmsParam;
import com.navinfo.opentsp.user.service.param.password.ResetPasswordParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdatePasswordResult;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserService {

    /**
     * 登录
     * @param loginParam
     * @return
     */
    public CommonResult<LoginResult> login(LoginParam loginParam);

    /**
     * 刷新token
     * @param refreshTokenParam
     * @return
     */
    public CommonResult<LoginResult> refreshToken(RefreshTokenParam refreshTokenParam);

    /**
     * 查询最近的登陆记录
     * @param userId
     * @param num 最近多少条
     * @return
     */
    public List<UserLoginLogEntity> latestLoginLog(String userId, int num);

    /**
     * 检查token是否有效
     * @param token
     * @return
     */
    public boolean isTokenValid(String token);

    /**
     * 快速登录
     * @param quickLoginParam
     * @return
     */
    public CommonResult<LoginResult> quickLogin(QuickLoginParam quickLoginParam);

    /**
     * 找回密码  并发送短信
     * @param param
     * @return
     */
    public CommonResult<String> findPasswordBySms(FindPasswordBySmsParam param);

    /**
     * 找回密码  并发送邮件
     * @param param
     * @return
     */
    public CommonResult<String> findPasswordByEmail(FindPasswordByEmailParam param);

    /**
     * 重置密码
     * @param passwordParam
     * @return
     */
    public CommonResult<LoginResult> resetPassword(ResetPasswordParam passwordParam);

    /**
     * 更新密码
     * @param passwdParam
     * @return
     */
    public CommonResult<UpdatePasswordResult> updatePassword(ChangePasswdParam passwdParam);

    public CommonResult<String> logout(LogoutParam logoutParam);

    public CommonResult<UserEntity> userDetailById(String userId);
}
