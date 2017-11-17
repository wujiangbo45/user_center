package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.service.param.register.CheckEmailParam;
import com.navinfo.opentsp.user.service.param.register.CheckMobileParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByEmailParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByPhoneParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.register.RegisterResult;
import com.navinfo.opentsp.user.service.user.RegisterService;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterService registerService;

    @RequestMapping(value = "/mobileRegister", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public CommonResult<RegisterResult> registerByPhone(HttpServletRequest request, @RequestBody RegisterByPhoneParam registerByPhoneParam) {
        registerByPhoneParam.setIp(HttpRequestUtil.getIpAddr(request));
        return this.registerService.registerByPhone(registerByPhoneParam);
    }

    @RequestMapping(value = "/mailRegister", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public CommonResult<RegisterResult> registerByEmail(HttpServletRequest request, @RequestBody RegisterByEmailParam registerByEmailParam) {
        registerByEmailParam.setIp(HttpRequestUtil.getIpAddr(request));
        return this.registerService.registerByEmail(registerByEmailParam);
    }

    @RequestMapping(value = "/mailRegister2", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public CommonResult<RegisterResult> registerByEmail2(HttpServletRequest request, @RequestBody RegisterByEmailParam registerByEmailParam) {
        registerByEmailParam.setIp(HttpRequestUtil.getIpAddr(request));
        return this.registerService.registerByEmail(registerByEmailParam);
    }

    @RequestMapping(value = "/activeByMail/{encoded}", method = {RequestMethod.GET}, produces = {"application/json; charset=utf-8"})
    public Object activeByEmail(@PathVariable("encoded") String encoded, HttpServletResponse response){
        try {
            encoded = URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }

        CommonResult<String> result = this.registerService.activeAccountByEmail(encoded);
        return result;
    }

    @RequestMapping(value = "/checkEmail", method = {RequestMethod.GET}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> emailValid(CheckEmailParam email){
        if(this.registerService.isEmailAvailable(email.getEmail()))
            return new CommonResult<String>().fillResult(ResultCode.EMAIL_NOT_EXISTS);
        return new CommonResult<String>().fillResult(ResultCode.EMAIL_ALREADY_EXISTS);
    }

    @RequestMapping(value = "/checkMobile", method = {RequestMethod.GET}, produces = {"application/json; charset=utf-8"})
    public CommonResult<String> phoneValid(CheckMobileParam phone){
        if(this.registerService.isPhoneAvailable(phone.getMobile())) {
            return new CommonResult<String>().fillResult(ResultCode.PHONE_NOT_EXISTS);
        }
        return new CommonResult<String>().fillResult(ResultCode.PHONE_ALREADY_EXISTS);
    }

}
