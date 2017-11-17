package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.password.*;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdatePasswordResult;
import com.navinfo.opentsp.user.service.user.UserService;
import com.navinfo.opentsp.user.web.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-19
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class PasswordController {

    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private UserService userService;


    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/findPasswordBySms", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> findPasswordBySms(@RequestBody FindPasswordBySmsParam param) {

        if (!verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, VerifyTypes.CAPTCHA, param.getProduct(),
                param.getMobile(), param.getCaptcha(), true)) {//验证码错误
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
        }

        return this.userService.findPasswordBySms(param);
    }

    @RequestMapping(value = "/findPasswordByEmail", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> findPasswordByEmail(@RequestBody FindPasswordByEmailParam param) {

        if (!verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, VerifyTypes.CAPTCHA, param.getProduct(),
                param.getEmail(), param.getCaptcha(), true)) {//验证码错误
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
        }

        return this.userService.findPasswordByEmail(param);
    }

    @RequestMapping(value = "/validateFindPasswordSms", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> validateFindPasswordSms(@RequestBody ValidateFindPasswdSmsParam param) {
        if (!verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, VerifyTypes.SMS, param.getProduct(),
                param.getMobile(), param.getSmsCode(), false)) {//验证码错误
            return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, String.class);
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping(value = "/validateFindPasswordEmail", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> validateFindPasswordEmail(@RequestBody ValidateFindPasswdEmailParam param) {

        if (!verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, VerifyTypes.EMAIL, param.getProduct(),
                param.getEmail(), param.getVerifyCode(), false)) {//验证码错误
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping(value = "/resetPassword", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<LoginResult> resetPassword(@RequestBody ResetPasswordParam param){
        return this.userService.resetPassword(param);
    }

    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<UpdatePasswordResult> changePassword(@RequestBody ChangePasswdParam passwdParam, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        CommonResult<UpdatePasswordResult> result = this.userService.updatePassword(passwdParam);
        String sessionId = SessionUtil.getSessionId(request);
        if (sessionId != null && result.getCode() == ResultCode.SUCCESS.code()) {
            String token = cacheService.get(SessionUtil.SESSION_ID_PREFIX + sessionId) + "";
            String logoutUrl = (String) cacheService.get(SessionUtil.SSO_LOGOUT_PREFIX + token);
            cacheService.delete(SessionUtil.SSO_LOGOUT_PREFIX + token);
            cacheService.set(SessionUtil.SESSION_ID_PREFIX + sessionId, result.getData().getToken(), 1800, TimeUnit.SECONDS);
            cacheService.set(SessionUtil.SSO_LOGOUT_PREFIX + sessionId, logoutUrl, 1800, TimeUnit.SECONDS);
        }
        return result;
    }


}
