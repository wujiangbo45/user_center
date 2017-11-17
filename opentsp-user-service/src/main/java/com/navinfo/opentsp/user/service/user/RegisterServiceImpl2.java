package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.common.util.string.HexUtils;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;
import com.navinfo.opentsp.user.service.cache.CacheService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByEmailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.register.RegisterResult;
import com.navinfo.opentsp.user.service.security.AES;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import com.navinfo.opentsp.user.service.security.Md5;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-30
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class RegisterServiceImpl2 extends RegisterServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl2.class);

    @Autowired
    private CacheService cacheService;

    /**
     *
     * 这个注册， 不创建帐号
     *
     * @param registerByPhoneParam
     * @return
     */
    @Override
    public CommonResult<RegisterResult> registerByEmail(RegisterByEmailParam registerByPhoneParam) {
        /**
         * 检查并删除图形验证码, cacheService
         */
        if(!this.verifyCodeService.checkVerifyCode(Functions.REGISTER, VerifyTypes.CAPTCHA,
                registerByPhoneParam.getProduct(), registerByPhoneParam.getEmail(), registerByPhoneParam.getCaptcha(), true)) {
            return new CommonResult<RegisterResult>().fillResult(ResultCode.VERIFY_CODE_ERROR);
        }

        if(this.userEntityDao.findUserByEmail(registerByPhoneParam.getEmail()) != null) {
            return new CommonResult<RegisterResult>().fillResult(ResultCode.EMAIL_ALREADY_EXISTS);
        }

        Long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_EMAIL_TIMEOUT,
                registerByPhoneParam.getProduct(), ConfigPropertyEntity.Types.register));

        String key = EncryptUtil.HMAC_SHA1(registerByPhoneParam.getEmail(),
                this.configPropertyService.getValue(ConfigPropertyService.PROP_SIMPLE_AES_KEY, ConfigPropertyEntity.Types.system));
        registerByPhoneParam.setCreateTime(System.currentTimeMillis());
        this.cacheService.set("register_" + key, registerByPhoneParam, timeout, TimeUnit.SECONDS);

        SendEmailParam sendEmailParam = new SendEmailParam();
        sendEmailParam.setEmail(registerByPhoneParam.getEmail());
        sendEmailParam.setType(Functions.REGISTER.func());
        sendEmailParam.setProduct(registerByPhoneParam.getProduct());
        sendEmailParam.setIp(registerByPhoneParam.getIp());
        sendEmailParam.setCreateTime(registerByPhoneParam.getCreateTime());
        sendEmailParam.setRedirectUrl(registerByPhoneParam.getRedirectUrl());
        CommonResult<String> result1 = this.emailService.sendEmail(sendEmailParam);

        logger.debug("send register email complete ! {}:{}", result1.getCode(), result1.getMessage());

        /**
         * create result
         */
        RegisterResult result = new RegisterResult();
        result.setEmail(sendEmailParam.getEmail());
        return new CommonResult<RegisterResult>().fillResult(ResultCode.SUCCESS).setData(result);
    }

    @Override
    public CommonResult<String> activeAccountByEmail(String encoded) {

        /**
         * 获取到 配置的 AES 的key
         */
        String aesKey = this.configPropertyService.getValue(ConfigPropertyService.PROP_SIMPLE_AES_KEY, ConfigPropertyEntity.Types.system);

        /**
         * 解密
         */
        String json = AES.decode(HexUtils.fromHexString(encoded), EncryptUtil.HMAC_SHA1(aesKey, Md5.md5(aesKey)));
        SendEmailParam param = JsonUtil.fromJson(json, SendEmailParam.class);
        //看看解密出来是不是少了啥零件
        if(param == null || StringUtils.isEmpty(param.getEmail()) || StringUtils.isEmpty(param.getProduct()) || param.getCreateTime() == 0) {
            logger.info("required param missing !");
            return new CommonResult<String>().fillResult(ResultCode.FORBIDDEN);
        }
        //获取过期时间
        Long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_EMAIL_TIMEOUT,
                param.getProduct(), ConfigPropertyEntity.Types.register));
        //先看看连接本身是不是过期了
        if(param.getCreateTime() + timeout * 1000 <= System.currentTimeMillis()) {//已过期
            logger.info("active link expired !");
            return new CommonResult<String>().fillResult(ResultCode.VERIFY_CODE_EXPIRED).setMessage("此链接已过期!");
        }

        String key = EncryptUtil.HMAC_SHA1(param.getEmail(), aesKey);
        RegisterByEmailParam emailParam = (RegisterByEmailParam) this.cacheService.get("register_" + key);
        if(emailParam == null) {
            return new CommonResult<String>().fillResult(ResultCode.VERIFY_CODE_EXPIRED).setMessage("此链接已过期!");
        }

        if(emailParam.getCreateTime() != param.getCreateTime()) {
            return new CommonResult<String>().fillResult(ResultCode.VERIFY_CODE_EXPIRED).setMessage("此链接已过期!");
        }

        this.cacheService.delete("register_" + key);

        /**
         * save user entity
         */
        UserEntity userEntity = this.userComponent.userEntity(emailParam.getProduct(), emailParam.getPassword());
        userEntity.setAccountActived(UserEntity.ACCOUNT_ACTIVED);
        userEntity.setEmail(emailParam.getEmail());
        this.userEntityDao.save(userEntity);
        logger.debug("save user entity success ! {}", userEntity);

        /**
         * save user profile
         */
        UserProfileEntity profileEntity = this.userComponent.userProfileEntity(userEntity, emailParam);
        profileEntity.setEmailActived(UserProfileEntity.EMAIL_ACTIVED);
        this.profileDao.save(profileEntity);
        logger.debug("save user profile success ! {}", profileEntity);

        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }
}
