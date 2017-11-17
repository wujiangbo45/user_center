package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.captcha.AuthCode;
import com.navinfo.opentsp.user.service.param.captcha.CheckCaptcha;
import com.navinfo.opentsp.user.service.param.captcha.GetCaptcha;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/user")
public class CaptchaController {
    private static Logger logger = LoggerFactory.getLogger(CaptchaController.class);

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private ConfigPropertyService configPropertyService;

    @Value("${opentsp.verify.picture.url}")
    private String verifyPictureUrl;

    /**
     * 获取验证码接口
     *
     * @return
     */
    @RequestMapping(value = "/getCaptcha", method = RequestMethod.GET)
    @ResponseBody
    public Object getCaptcha(HttpServletResponse response, GetCaptcha captcha) {

        try {
            ResponseEntity<AuthCode> code = restTemplate.getForEntity(verifyPictureUrl, AuthCode.class);
            if (code.getStatusCode() == HttpStatus.OK) {
                AuthCode authCode = code.getBody();

                long timeout = Long.valueOf(configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_CAPTCHA_TIMEOUT,
                        captcha.getProduct(), ConfigPropertyEntity.Types.register));

                verifyCodeService.setVerifyCode(Functions.valuesOf(captcha.getType()), VerifyTypes.CAPTCHA, captcha.getProduct(),
                        captcha.getIdentifier(), authCode.getCode(), timeout);

                String img64 = authCode.getImg64();

                // 设置图像不要缓存
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setContentType("image/jpeg");
                logger.debug(authCode.getCode());
                response.getOutputStream().write(Base64.decodeBase64(img64));
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }


    @RequestMapping(value = "/checkCaptcha", produces = {"application/json; charset=utf-8"})
    public CommonResult<String> checkCaptcha(@RequestBody CheckCaptcha checkCaptcha){

        if(!this.verifyCodeService.checkVerifyCode(Functions.valuesOf(checkCaptcha.getType()),
                VerifyTypes.CAPTCHA, checkCaptcha.getProduct(), checkCaptcha.getIdentifier(), checkCaptcha.getVerifyCode())) {
            return new CommonResult<String>().fillResult(ResultCode.VERIFY_CODE_ERROR);
        }

        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

}
