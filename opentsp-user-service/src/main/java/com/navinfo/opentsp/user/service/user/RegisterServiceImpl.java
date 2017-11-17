package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.common.util.string.HexUtils;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.dao.UserLoginLogDao;
import com.navinfo.opentsp.user.dal.dao.UserProfileDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.dal.entity.TokenEntity;
import com.navinfo.opentsp.user.dal.entity.UserEntity;
import com.navinfo.opentsp.user.dal.entity.UserProfileEntity;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.email.EmailService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.param.login.QuickLoginParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByEmailParam;
import com.navinfo.opentsp.user.service.param.register.RegisterByPhoneParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.register.RegisterResult;
import com.navinfo.opentsp.user.service.security.AES;
import com.navinfo.opentsp.user.service.security.EncryptUtil;
import com.navinfo.opentsp.user.service.security.Md5;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.var.ConfigPropertyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
//@Service
public class RegisterServiceImpl implements RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Autowired
    protected UserEntityDao userEntityDao;
    @Autowired
    protected UserProfileDao profileDao;
    @Autowired
    protected VerifyCodeService verifyCodeService;
    @Autowired
    protected TokenService tokenService;
    @Autowired
    protected UserLoginLogDao loginLogDao;
    @Autowired
    protected EmailService emailService;
    @Autowired
    protected ConfigPropertyService configPropertyService;
    @Autowired
    protected UserComponent userComponent;

    @Transactional
    @Override
    public CommonResult<RegisterResult> registerByPhone(RegisterByPhoneParam registerByPhoneParam) {
        /**
         * 检查短信验证码是否正确
         */
        if(!verifyCodeService.checkVerifyCode(Functions.REGISTER, VerifyTypes.SMS,
                registerByPhoneParam.getProduct(), registerByPhoneParam.getMobile(), registerByPhoneParam.getSmsCode())) {
            //短信验证码不对
            return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, RegisterResult.class);
        }
        /**
         * 短信验证码校验通过后 删除验证码
         */
        this.verifyCodeService.getVerifyCode(Functions.REGISTER, VerifyTypes.SMS,
                registerByPhoneParam.getProduct(), registerByPhoneParam.getMobile(), true);
        /**
         * 检查手机号是否已经存在
         */
        if(userEntityDao.findUserByMobile(registerByPhoneParam.getMobile()) != null) {
            return new CommonResult<RegisterResult>().fillResult(ResultCode.PHONE_ALREADY_EXISTS);
        }

        /**
         * save user entity
         */
        UserEntity userEntity = this.userComponent.userEntity(registerByPhoneParam.getProduct(), registerByPhoneParam.getPassword());
        userEntity.setMobile(registerByPhoneParam.getMobile());
        this.userEntityDao.save(userEntity);
        logger.debug("save user entity success ! {}", userEntity);

        /**
         * save user profile
         */
        UserProfileEntity profileEntity = this.userComponent.userProfileEntity(userEntity, registerByPhoneParam);
        profileEntity.setEmailActived(UserProfileEntity.EMAIL_NO_ACTIVED);
        profileEntity.setMobileActived(UserProfileEntity.MOBILE_ACTIVED);
        this.profileDao.save(profileEntity);
        logger.debug("save user profile success ! {}", profileEntity);

        /**
         * generate result
         */
        RegisterResult result = new RegisterResult();
        result.setUserId(userEntity.getId());
        result.setEmail(userEntity.getEmail());
        result.setMobile(userEntity.getMobile());
        result.setNickname(userEntity.getNickname());
        TokenEntity tokenEntity = tokenService.createToken(userEntity, registerByPhoneParam,
                registerByPhoneParam.getIp(), registerByPhoneParam.getProduct(), registerByPhoneParam.getMobile());
        result.setToken(tokenEntity.getToken());

        /**
         * save login log
         */
        this.loginLogDao.save(userComponent.userLoginLogEntity(userEntity, tokenEntity));

        return CommonResult.newInstance(ResultCode.SUCCESS, RegisterResult.class).setData(result);
    }

    @Transactional
    @Override
    public UserEntity quikRegisterByPhone(QuickLoginParam registerByPhoneParam) {
        /**
         * save user entity
         */
        UserEntity userEntity = this.userComponent.userEntity(registerByPhoneParam.getProduct(), null);
        userEntity.setMobile(registerByPhoneParam.getMobile());
        this.userEntityDao.save(userEntity);
        logger.debug("save user entity success ! {}", userEntity);

        /**
         * save user profile
         */
        UserProfileEntity profileEntity = this.userComponent.userProfileEntity(userEntity, registerByPhoneParam);
        profileEntity.setEmailActived(UserProfileEntity.EMAIL_NO_ACTIVED);
        profileEntity.setMobileActived(UserProfileEntity.MOBILE_ACTIVED);
        this.profileDao.save(profileEntity);
        logger.debug("save user profile success ! {}", profileEntity);

        return userEntity;
    }

    @Transactional
    @Override
    public CommonResult<RegisterResult> registerByEmail(RegisterByEmailParam registerByPhoneParam) {
        /**
         * 检查并删除图形验证码
         */
        if(!this.verifyCodeService.checkVerifyCode(Functions.REGISTER, VerifyTypes.CAPTCHA,
                registerByPhoneParam.getProduct(), registerByPhoneParam.getEmail(), registerByPhoneParam.getCaptcha(), true)) {
            return new CommonResult<RegisterResult>().fillResult(ResultCode.VERIFY_CODE_ERROR);
        }

        if(this.userEntityDao.findUserByEmail(registerByPhoneParam.getEmail()) != null) {
            return new CommonResult<RegisterResult>().fillResult(ResultCode.EMAIL_ALREADY_EXISTS);
        }
        /**
         * save user entity
         */
        UserEntity userEntity = this.userComponent.userEntity(registerByPhoneParam.getProduct(), registerByPhoneParam.getPassword());
        userEntity.setAccountActived(UserEntity.ACCOUNT_NO_ACTIVED);
        userEntity.setEmail(registerByPhoneParam.getEmail());
        this.userEntityDao.save(userEntity);
        logger.debug("save user entity success ! {}", userEntity);

        /**
         * save user profile
         */
        UserProfileEntity profileEntity = this.userComponent.userProfileEntity(userEntity, registerByPhoneParam);
        this.profileDao.save(profileEntity);
        logger.debug("save user profile success ! {}", profileEntity);

        SendEmailParam sendEmailParam = new SendEmailParam();
        sendEmailParam.setEmail(registerByPhoneParam.getEmail());
        sendEmailParam.setType(Functions.REGISTER.func());
        sendEmailParam.setProduct(registerByPhoneParam.getProduct());
        sendEmailParam.setIp(registerByPhoneParam.getIp());
        sendEmailParam.setRedirectUrl(registerByPhoneParam.getRedirectUrl());
        CommonResult<String> result1 = this.emailService.sendEmail(sendEmailParam);
        logger.debug("send register email complete ! {}:{}", result1.getCode(), result1.getMessage());

        /**
         * create result
         */
        RegisterResult result = new RegisterResult();
        result.setUserId(userEntity.getId());
        result.setEmail(userEntity.getEmail());
        result.setMobile(userEntity.getMobile());
        result.setNickname(userEntity.getNickname());
        return new CommonResult<RegisterResult>().fillResult(ResultCode.SUCCESS);
    }

    @Transactional
    @Override
    public CommonResult<String> activeAccountByEmail(String encoded) {
        /**
         * 获取到 配置的 AES 的key
         */
        String aesKey = this.configPropertyService.getValue(ConfigPropertyService.PROP_SIMPLE_AES_KEY, ConfigPropertyEntity.Types.system);

        /**
         * 解密
         */
//        String json = AES.decode(.decodeFromString(encoded), EncryptUtil.HMAC_SHA1(aesKey, Md5.md5(aesKey)));
        String json = AES.decode(HexUtils.fromHexString(encoded), EncryptUtil.HMAC_SHA1(aesKey, Md5.md5(aesKey)));
        SendEmailParam param = JsonUtil.fromJson(json, SendEmailParam.class);

        if(param == null || StringUtils.isEmpty(param.getEmail()) || StringUtils.isEmpty(param.getProduct()) || param.getCreateTime() == 0) {
            logger.info("required param missing !");
            return new CommonResult<String>().fillResult(ResultCode.FORBIDDEN);
        }

        Long timeout = Long.valueOf(this.configPropertyService.getValue(ConfigPropertyService.PROP_REGISTER_EMAIL_TIMEOUT,
                param.getProduct(), ConfigPropertyEntity.Types.register));

        if(param.getCreateTime() + timeout * 1000 <= System.currentTimeMillis()) {//已过期
            logger.info("active link expired !");
            return new CommonResult<String>().fillResult(ResultCode.VERIFY_CODE_EXPIRED).setMessage("此链接已过期!");
        }

        UserEntity userEntity = this.userEntityDao.findUserByEmail(param.getEmail());
        if(userEntity == null) {
            return new CommonResult<String>().fillResult(ResultCode.EMAIL_NOT_EXISTS);
        }

        UserProfileEntity profile = this.profileDao.findById(userEntity.getId());
        if(profile.getEmailActived().byteValue() == UserProfileEntity.EMAIL_ACTIVED) {
            return new CommonResult<String>().fillResult(ResultCode.ACTIVE_LINK_INVALID);//链接已经失效
        }

        userEntity.setAccountActived(UserEntity.ACCOUNT_ACTIVED);
        profile.setEmailActived(UserProfileEntity.EMAIL_ACTIVED);
        this.userEntityDao.updateByIdSelective(userEntity);
        this.profileDao.updateByIdSelective(profile);

        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

    @Override
    public boolean isEmailAvailable(String email) {
        if(StringUtils.isEmpty(email)) {
            return false;
        }
        return this.userEntityDao.findUserByEmail(email) == null;
    }

    @Override
    public boolean isPhoneAvailable(String phone) {
        if(StringUtils.isEmpty(phone)) {
            return false;
        }

        return this.userEntityDao.findUserByMobile(phone) == null;
    }
}
