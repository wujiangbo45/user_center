package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.service.appinfo.AppInfoService;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.email.EmailService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.param.setting.*;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.resultdto.login.WebWeixinTokenInfo;
import com.navinfo.opentsp.user.service.setting.AccountService;
import com.navinfo.opentsp.user.service.sms.SmsService;
import com.navinfo.opentsp.user.service.user.WebWeixinOauth;
import com.navinfo.opentsp.user.service.valimpl.EmailImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify
 * @copyright Navi Tsp
 */
@Controller
@RequestMapping("/user")
public class AccountBindController {
    private static final Logger logger = LoggerFactory.getLogger(AccountBindController.class);
    @Autowired
    private AccountService accountService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private EmailService emailService;

    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private EmailImplementor emailImplementor;

    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private WebWeixinOauth webWeixinOauth;
    /**
     * 申请手机绑定/更换绑定接口（同时发解绑短信）
     *
     * @param param
     * @return
     */
    @RequestMapping("/applyBindMobile")
    @ResponseBody
    public CommonResult<String> applyBindMobile(@RequestBody ApplyBindMobileParam param) {
        if (!GlobalConstans.BIND_TYPE.equals(param.getType()) && !GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }
        //第二步，验证短信验证码
        SendSmsParam smsParam = new SendSmsParam();
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            smsParam.setType(Functions.BIND.toString());
        }
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            smsParam.setType(Functions.CHANGEBIND.toString());
        }
        smsParam.setProduct(param.getProduct());
        smsParam.setMobile(param.getMobile());
        smsParam.setIp(param.getIp());
        CommonResult result = smsService.sendSms(smsParam);
        return result;
    }

    /**
     * 手机绑定/更换绑定接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/bindMobile")
    @ResponseBody
    public CommonResult<String> bindMobile(@RequestBody BindMobileParam param) {

        if (!GlobalConstans.BIND_TYPE.equals(param.getType()) && !GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }
        //绑定
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            //验证短信验证码
            Functions functions = Functions.valuesOf(GlobalConstans.BIND_TYPE);
            boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.SMS, param.getProduct(), param.getMobile(), param.getSmsCode());
            //短信校验失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, String.class);
            }
        }
        //更改绑定
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            if (StringUtils.isEmpty(param.getOldMobile()) || StringUtils.isEmpty(param.getOldSmsCode())) {
                return CommonResult.newInstance(ResultCode.BAD_REQUEST, String.class);
            }
            //新手机号短信验证码校验
            Functions functions = Functions.valuesOf(GlobalConstans.BIND_TYPE);
            boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.SMS, param.getProduct(), param.getMobile(), param.getSmsCode());
            //短信校验失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, String.class);
            }
            //旧手机号短信验证码校验
            functions = Functions.valuesOf(GlobalConstans.CHANGE_BIND_TYPE);
            bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.SMS, param.getProduct(), param.getOldMobile(), param.getOldSmsCode());
            //短信校验失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, String.class);
            }
        }
        return accountService.bindMobile(param);
    }

    /**
     * 更换绑定验证接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/checkChangeBindMobile")
    @ResponseBody
    public CommonResult<String> checkChangeBindMobile(@RequestBody CheckChangeBindMobileParam param) {
        //第二步，验证短信验证码
        Functions functions = Functions.valuesOf(GlobalConstans.CHANGE_BIND_TYPE);
        boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.SMS, param.getProduct(), param.getMobile(), param.getSmsCode());
        //短信校验失败，直接返回
        if (!bool) {
            return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, String.class);
        }
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    /**
     * 更换绑定验证接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/checkChangeBindEmail")
    @ResponseBody
    public CommonResult<String> checkChangeBindEmail(@RequestBody CheckChangeBindEmailParam param) {
        //第二步，验证短信验证码
        Functions functions = Functions.valuesOf(GlobalConstans.CHANGE_BIND_TYPE);
        boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.EMAIL, param.getProduct(), param.getEmail(), param.getVerifyCode());
        //短信校验失败，直接返回
        if (!bool) {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
        }
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    /**
     * 1.3.22	申请邮箱绑定/更改接口（同时发绑定邮件）
     *
     * @param param
     * @return
     */
    @RequestMapping("/applyBindEmail")
    @ResponseBody
    public CommonResult<String> applyBindEmail(@RequestBody ApplyBindEmailParam param) {

        if (!GlobalConstans.BIND_TYPE.equals(param.getType()) && !GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }
        SendEmailParam sendEmailParam = new SendEmailParam();
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            sendEmailParam.setType(Functions.BIND.toString());
        }
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            sendEmailParam.setType(Functions.CHANGEBIND.toString());
        }
        sendEmailParam.setProduct(param.getProduct());
        sendEmailParam.setEmail(param.getEmail());
        sendEmailParam.setIp(param.getIp());
        return emailService.sendEmail(sendEmailParam);
    }

    /**
     * 邮箱绑定/解绑接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/bindEmail")
    @ResponseBody
    public CommonResult<String> bindEmail(@RequestBody BindEmailParam param) {

        if (!GlobalConstans.BIND_TYPE.equals(param.getType()) && !GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
        }
        //绑定
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            //邮箱验证码验证
            Functions functions = Functions.valuesOf(GlobalConstans.BIND_TYPE);
            boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.EMAIL, param.getProduct(), param.getEmail(), param.getVerifyCode());
            //验证失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
            }
        }
        //更改绑定
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {

            if (!emailImplementor.doValidate(param.getOldEmail()) || StringUtils.isEmpty(param.getOldVerifyCode())) {
                return CommonResult.newInstance(ResultCode.BAD_REQUEST, String.class);
            }

            Functions functions = Functions.valuesOf(GlobalConstans.BIND_TYPE);
            boolean bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.EMAIL, param.getProduct(), param.getEmail(), param.getVerifyCode());
            //新邮箱验证码校验失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
            }
            //旧邮箱验证码校验
            functions = Functions.valuesOf(GlobalConstans.CHANGE_BIND_TYPE);
            bool = verifyCodeService.checkVerifyCode(functions, VerifyTypes.EMAIL, param.getProduct(), param.getOldEmail(), param.getOldVerifyCode());
            //校验失败，直接返回
            if (!bool) {
                return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, String.class);
            }
        }

        return accountService.bindEmail(param);
    }


    /**
     * 第三方绑定接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/bindThridPlatform")
    @ResponseBody
    public CommonResult<String> bindThridPlatform(@RequestBody BindThriatformParam param) {
        logger.info(param.getUnionid()+"---"+param.getOpenId());
        return accountService.bindThridPlatform(param);
    }

    /**
     * 第三方绑定接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/webBindThridPlatform")
    @ResponseBody
    public CommonResult<String> webBindThridPlatform(@RequestBody WebBindThriatformParam param) {

        AppIDKeyEntity appIDKeyEntity = appInfoService.findAppInfo(param.getType(), param.getProduct());
        if (appIDKeyEntity == null) {
            return CommonResult.newInstance(ResultCode.LOGIN_FAILED, String.class);
        }
        WebWeixinTokenInfo webWeixinTokenInfo = webWeixinOauth.getOauthResult(param.getCode(), appIDKeyEntity);
        if (webWeixinTokenInfo == null) {
            return CommonResult.newInstance(ResultCode.LOGIN_FAILED, String.class);
        }
        BindThriatformParam bindThriatformParam = new BindThriatformParam();
        bindThriatformParam.setProduct(param.getProduct());
        bindThriatformParam.setAccessToken(webWeixinTokenInfo.getAccess_token());
        bindThriatformParam.setRefreshToken(webWeixinTokenInfo.getRefresh_token());
        bindThriatformParam.setType(param.getType());
        bindThriatformParam.setOpenId(webWeixinTokenInfo.getOpenid());
        bindThriatformParam.setToken(param.getToken());
        bindThriatformParam.setUser(param.getUser());
        bindThriatformParam.setUnionid(webWeixinTokenInfo.getUnionid());
        return accountService.bindThridPlatform(bindThriatformParam);
    }

    /**
     * 第三方解绑接口
     *
     * @param param
     * @return
     */
    @RequestMapping("/unbindThridPlatform")
    @ResponseBody
    public CommonResult<String> unbindThridPlatform(@RequestBody UnbindThriatformParam param) {

        return accountService.unbindThridPlatform(param);
    }

}
