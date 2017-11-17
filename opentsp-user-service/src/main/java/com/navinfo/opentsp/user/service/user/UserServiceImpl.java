package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.dao.UserLoginLogDao;
import com.navinfo.opentsp.user.dal.entity.*;
import com.navinfo.opentsp.user.service.cache.VerifyCodeService;
import com.navinfo.opentsp.user.service.email.EmailService;
import com.navinfo.opentsp.user.service.enums.Functions;
import com.navinfo.opentsp.user.service.enums.VerifyTypes;
import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.param.login.LoginParam;
import com.navinfo.opentsp.user.service.param.login.LogoutParam;
import com.navinfo.opentsp.user.service.param.login.QuickLoginParam;
import com.navinfo.opentsp.user.service.param.login.RefreshTokenParam;
import com.navinfo.opentsp.user.service.param.password.ChangePasswdParam;
import com.navinfo.opentsp.user.service.param.password.FindPasswordByEmailParam;
import com.navinfo.opentsp.user.service.param.password.FindPasswordBySmsParam;
import com.navinfo.opentsp.user.service.param.password.ResetPasswordParam;
import com.navinfo.opentsp.user.service.param.sms.SendSmsParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.LoginResult;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.resultdto.setting.CarInfo;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdatePasswordResult;
import com.navinfo.opentsp.user.service.security.PasswordService;
import com.navinfo.opentsp.user.service.session.SessionAttrConstant;
import com.navinfo.opentsp.user.service.session.SessionManager;
import com.navinfo.opentsp.user.service.sms.SmsService;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.valimpl.EmailImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-13
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public static final String TOKEN_REFRESH_EXTEND = "extend";
    public static final String TOKEN_REFRESH_CHANGE = "change";

    @Autowired
    private UserEntityDao userEntityDao;
    @Autowired
    private PasswordService passwordService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private LoginFailedTimer failedTimer;
    @Autowired
    private UserComponent userComponent;
    @Autowired
    private UserLoginLogDao loginLogDao;
    @Autowired
    private VerifyCodeService verifyCodeService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private EmailImplementor emailImplementor;
    @Autowired
    private EmailService emailService;
    @Autowired
    private SessionManager sessionManager;

    /**
     * token刷新策略： 直接换还是延长有效期
     */
    @Value("${opentsp.token.refresh.strategy:extend}")
    private String tokenRefreshStrategy;


    @Transactional
    @Override
    public CommonResult<LoginResult> login(LoginParam loginParam) {
        String loginName = loginParam.getLoginName();
        UserEntity userEntity = this.userEntityDao.findUserByIdentifier(loginName);
        /**
         * user exists
         */
        if(userEntity == null) {
            logger.info("login name not found ! {} ", loginName);
            return CommonResult.newInstance(ResultCode.NAME_OR_PWD_ERROR, LoginResult.class);
        }

        /**
         * 如果需要图片验证码
         */
        if(failedTimer.needCaptcha(userEntity.getId(), loginParam.getProduct())) {
            if(StringUtils.isEmpty(loginParam.getCaptcha())) {
                return CommonResult.newInstance(ResultCode.NEED_VERIFY_CODE, LoginResult.class);//需要验证码
            }
            //校验图片验证码
            if(!this.verifyCodeService.checkVerifyCode(Functions.LOGIN, VerifyTypes.CAPTCHA,
                    loginParam.getProduct(), loginParam.getLoginName(), loginParam.getCaptcha(), true)) {
                return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, LoginResult.class);//验证码错误
            }
        }

        /**
         * password not match
         */
        if(!this.passwordService.matches(loginParam.getPassword(), userEntity.getSalt(), userEntity.getPassword())) {
            logger.info("login failed ! password not match !");
            failedTimer.incrFailed(userEntity.getId(), loginParam.getProduct());
            UserLoginLogEntity logEntity = this.userComponent.failedLoginLog(loginParam, userEntity.getProductId(),
                    loginParam.getLoginName(), loginParam.getProduct(), loginParam.getIp());
            logEntity.setLoginResult("wrong password !");
            this.loginLogDao.save(logEntity);
            return CommonResult.newInstance(ResultCode.NAME_OR_PWD_ERROR, LoginResult.class);
        }

        /**
         * 检查一下帐号状态
         */
        ResultCode resultCode = this.userComponent.validateUserStatus(userEntity);
        if(resultCode != ResultCode.SUCCESS) {
            return CommonResult.newInstance(resultCode, LoginResult.class);
        }

        /**
         * login success, create token
         */
        TokenEntity tokenEntity = this.tokenService.createToken(userEntity, loginParam, loginParam.getIp(), loginParam.getProduct(), loginName);

        UserLoginLogEntity logEntity = this.userComponent.userLoginLogEntity(userEntity, tokenEntity);
        this.loginLogDao.save(logEntity);

        /**
         * 清除登录失败次数
         */
        failedTimer.clearFailed(userEntity.getId());

        sessionManager.getSession(tokenEntity.getToken()).setAttribute(SessionAttrConstant.ATTR_USER_ENTITY, userEntity);
        LoginResult loginResult = new LoginResult();
        loginResult.setNickname(userEntity.getNickname());
        loginResult.setToken(tokenEntity.getToken());
        loginResult.setUserId(userEntity.getId());
        return CommonResult.newInstance(ResultCode.SUCCESS, LoginResult.class).setData(loginResult);
    }

    @Override
    public CommonResult<LoginResult> refreshToken(RefreshTokenParam refreshTokenParam) {
        TokenEntity token = refreshTokenParam.getTokenEntity();//this.tokenService.getToken(refreshTokenParam.getToken());
        /**
         * 旧的token不存在
         */
        if(token == null) {
            logger.info("token was not exists !");
            return CommonResult.newInstance(ResultCode.TOKEN_INVALID, LoginResult.class);
        }

        /**
         * token 已经过期
         */
        if(!this.tokenService.isTokenValid(token)) {
            logger.info("token has bean expired !");
            return CommonResult.newInstance(ResultCode.TOKEN_EXPIRED, LoginResult.class);
        }

        /**
         * token 和 登录名 不匹配
         */
        if(!refreshTokenParam.getLoginName().equals(token.getLoginName())) {
            logger.info("login name and token not match !");
            return CommonResult.newInstance(ResultCode.TOKEN_NOT_MATCH, LoginResult.class);
        }

        /**
         *  检查deviceType
         */
        if(!token.getDeviceType().equals(refreshTokenParam.getDeviceTypeString())) {
            logger.info("token device type and passed device type not match !");
            return CommonResult.newInstance(ResultCode.TOKEN_NOT_MATCH, LoginResult.class);
        }

        UserEntity userEntity = refreshTokenParam.getUser();//this.userEntityDao.findById(token.getUserId());

        LoginResult loginResult = new LoginResult();
        if (TOKEN_REFRESH_CHANGE.equals(tokenRefreshStrategy)) {
            this.sessionManager.getSession(token.getToken()).invalidate();//旧的session失效

            TokenEntity newToken = this.tokenService.createToken(userEntity, refreshTokenParam,
                    refreshTokenParam.getIp(), token.getOpProductId(), refreshTokenParam.getLoginName());

            UserLoginLogEntity loginLogEntity = this.userComponent.userLoginLogEntity(userEntity, newToken);
            loginLogEntity.setLoginResult("refresh token success ! ");
            this.loginLogDao.save(loginLogEntity);

            this.sessionManager.getSession(newToken.getToken()).setAttribute(SessionAttrConstant.ATTR_USER_ENTITY, userEntity);

            loginResult.setUserId(userEntity.getId());
            loginResult.setToken(newToken.getToken());
            loginResult.setNickname(userEntity.getNickname());
        } else if (TOKEN_REFRESH_EXTEND.equals(tokenRefreshStrategy)) {
            this.tokenService.extendTokenExpireTime(token.getToken());

            UserLoginLogEntity loginLogEntity = this.userComponent.userLoginLogEntity(userEntity, token);
            loginLogEntity.setLoginResult("refresh token success ! ");
            this.loginLogDao.save(loginLogEntity);

            loginResult.setUserId(userEntity.getId());
            loginResult.setToken(token.getToken());
            loginResult.setNickname(userEntity.getNickname());
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, LoginResult.class).setData(loginResult);
    }

    @Override
    public List<UserLoginLogEntity> latestLoginLog(String userId, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);//将日期设置到本月刚开始的时候

        return this.loginLogDao.latestLogs(userId, calendar.getTime(), num);
    }

    @Override
    public boolean isTokenValid(String token) {
        if(StringUtils.isEmpty(token))
            return false;

        TokenEntity tokenEntity = this.tokenService.getToken(token);
        if(tokenEntity == null)
            return false;

        return this.tokenService.isTokenValid(tokenEntity);
    }

    @Transactional
    @Override
    public CommonResult<LoginResult> quickLogin(QuickLoginParam quickLoginParam) {
        //检查短信验证码
        if(!this.verifyCodeService.checkVerifyCode(Functions.QUIK_LOGIN, VerifyTypes.SMS,
                quickLoginParam.getProduct(), quickLoginParam.getMobile(), quickLoginParam.getSmsCode())) {
            logger.info("sms code error ! ");
            return CommonResult.newInstance(ResultCode.SMS_CODE_ERROR, LoginResult.class);
        }
        /**
         * 短信验证码验证通过后，删除缓存的验证码
         */
        this.verifyCodeService.checkVerifyCode(Functions.QUIK_LOGIN, VerifyTypes.SMS,
                quickLoginParam.getProduct(), quickLoginParam.getMobile(), quickLoginParam.getSmsCode(), true);

        UserEntity userEntity = this.userEntityDao.findUserByMobile(quickLoginParam.getMobile());
        if(userEntity == null) {// 手机号未注册
            // 注册
            userEntity = registerService.quikRegisterByPhone(quickLoginParam);
        }

        /**
         * 创建token， 登录
         */
        ResultCode resultCode = this.userComponent.validateUserStatus(userEntity);//校验用户状态
        if(resultCode == ResultCode.SUCCESS) { //成功
            TokenEntity tokenEntity = this.tokenService.createToken(userEntity, quickLoginParam,
                    quickLoginParam.getIp(), quickLoginParam.getProduct(), quickLoginParam.getMobile());

            // save login log
            UserLoginLogEntity logEntity = this.userComponent.userLoginLogEntity(userEntity, tokenEntity);
            logEntity.setLoginResult("quick login success!");
            this.loginLogDao.save(logEntity);
            return CommonResult.newInstance(resultCode, LoginResult.class).setData(new LoginResult(
                    userEntity.getId(), userEntity.getNickname(), tokenEntity.getToken()));
        } else {//
            return new CommonResult<LoginResult>().fillResult(resultCode);
        }
    }

    @Override
    public CommonResult<String> findPasswordBySms(FindPasswordBySmsParam param) {
        UserEntity userEntity = this.userEntityDao.findUserByMobile(param.getMobile());
        if(userEntity == null) {
            logger.info("phone {} not exists !", param.getMobile());
            return CommonResult.newInstance(ResultCode.PHONE_NOT_EXISTS, String.class);
        }

        SendSmsParam sendSmsParam = new SendSmsParam();
        sendSmsParam.setProduct(param.getProduct());
        sendSmsParam.setIp(param.getIp());
        sendSmsParam.setMobile(param.getMobile());
        sendSmsParam.setType(Functions.FIND_PASSWORD.func());
        return this.smsService.sendSms(sendSmsParam);
    }

    @Override
    public CommonResult<String> findPasswordByEmail(FindPasswordByEmailParam param) {
        UserEntity userEntity = this.userEntityDao.findUserByEmail(param.getEmail());
        if(userEntity == null) {
            return CommonResult.newInstance(ResultCode.EMAIL_NOT_EXISTS, String.class);
        }

        SendEmailParam sendEmailParam = new SendEmailParam();
        sendEmailParam.setEmail(param.getEmail());
        sendEmailParam.setProduct(param.getProduct());
        sendEmailParam.setType(Functions.FIND_PASSWORD.func());
        sendEmailParam.setIp(param.getIp());
        return this.emailService.sendEmail(sendEmailParam);
    }

    @Override
    public CommonResult<LoginResult> resetPassword(ResetPasswordParam passwordParam) {
        VerifyTypes type = null;
        /**
         * 判断是否为邮箱
         */
        if(this.emailImplementor.doValidate(passwordParam.getIdentifier())) {
            type = VerifyTypes.EMAIL;
        } else {
            type = VerifyTypes.SMS;
        }

        /**
         * 校验验证码
         */
        if(!this.verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, type, passwordParam.getProduct(),
                passwordParam.getIdentifier(), passwordParam.getVerifyCode())) {
            return CommonResult.newInstance(ResultCode.VERIFY_CODE_ERROR, LoginResult.class);
        }

        /**
         * 删除验证码
         */
        this.verifyCodeService.checkVerifyCode(Functions.FIND_PASSWORD, type, passwordParam.getProduct(),
                passwordParam.getIdentifier(), passwordParam.getVerifyCode(), true);

        UserEntity userEntity = null;
        if(type == VerifyTypes.EMAIL)
            userEntity = this.userEntityDao.findUserByEmail(passwordParam.getIdentifier());
        else
            userEntity = this.userEntityDao.findUserByMobile(passwordParam.getIdentifier());

        /**
         * 如果没有找到用户
         */
        if(userEntity == null)
            return CommonResult.newInstance(ResultCode.USER_NOT_EXISTS, LoginResult.class);

        //设置新密码
        userEntity.setPassword(this.passwordService.encodePassword(passwordParam.getNewPassword(), userEntity.getSalt()));
        this.userEntityDao.updateByIdSelective(userEntity);

        //delete all exists token
        this.tokenService.removeUserToken(userEntity.getId());

        // 进行登录
        LoginParam loginParam = new LoginParam();
        loginParam.setLoginName(userEntity.getMobile());
        loginParam.setPassword(passwordParam.getNewPassword());
        loginParam.setProduct(passwordParam.getProduct());

        return this.login(loginParam);
    }

    @Override
    public CommonResult<UpdatePasswordResult> updatePassword(ChangePasswdParam passwdParam) {
        UserEntity userEntity = passwdParam.getUser();

        if(!this.passwordService.matches(passwdParam.getOldPassword(), userEntity.getSalt(), userEntity.getPassword())) {
            logger.info("old password invalid !");
            return CommonResult.newInstance(ResultCode.PASSWORD_ERROR, UpdatePasswordResult.class);
        }

        userEntity.setPassword(this.passwordService.encodePassword(passwdParam.getNewPassword(), userEntity.getSalt()));
        this.userEntityDao.updateByIdSelective(userEntity);

        TokenEntity tokenEntity = passwdParam.getTokenEntity();
        this.tokenService.removeUserToken(userEntity.getId());

        tokenEntity = this.tokenService.createToken(userEntity, passwdParam, passwdParam.getIp(), tokenEntity.getOpProductId(), tokenEntity.getLoginName());
        this.sessionManager.getSession(tokenEntity.getToken()).setAttribute(SessionAttrConstant.ATTR_USER_ENTITY, userEntity);

        return CommonResult.newInstance(ResultCode.SUCCESS, UpdatePasswordResult.class).setData(new UpdatePasswordResult(tokenEntity.getToken()));
    }

    @Override
    public CommonResult<String> logout(LogoutParam logoutParam) {
        this.sessionManager.getSession(logoutParam.getToken()).invalidate();
        this.tokenService.removeToken(logoutParam.getToken());
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @Override
    public CommonResult<UserEntity> userDetailById(String userId) {
        UserEntity userEntity = userEntityDao.findById(userId);
        return new CommonResult<UserEntity>().fillResult(ResultCode.SUCCESS).setData(userEntity);
    }

}
