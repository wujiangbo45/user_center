package com.navinfo.opentsp.user.service.setting;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.dal.dao.UserCarDao;
import com.navinfo.opentsp.user.dal.dao.UserEntityDao;
import com.navinfo.opentsp.user.dal.dao.UserProfileDao;
import com.navinfo.opentsp.user.dal.dao.UserThirdLinkDao;
import com.navinfo.opentsp.user.dal.entity.*;
import com.navinfo.opentsp.user.service.appinfo.AppInfoService;
import com.navinfo.opentsp.user.service.event.UpdateUserEvent;
import com.navinfo.opentsp.user.service.file.UploadFileService;
import com.navinfo.opentsp.user.service.param.login.OauthLoginParam;
import com.navinfo.opentsp.user.service.param.setting.*;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.login.QQUserInfo;
import com.navinfo.opentsp.user.service.resultdto.login.WeixinUserInfo;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.resultdto.setting.CarInfo;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdateUserCarResult;
import com.navinfo.opentsp.user.service.token.TokenService;
import com.navinfo.opentsp.user.service.user.QQOauth;
import com.navinfo.opentsp.user.service.user.UserComponent;
import com.navinfo.opentsp.user.service.user.WeixinOauth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wanliang
 * @version 1.0
 * @date 2015/10/19
 * @modify 账号设置业务实现类
 * @copyright Navi Tsp
 */
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserCarDao userCarDao;
    @Autowired
    private UserProfileDao userProfileDao;
    @Autowired
    private UserEntityDao userEntityDao;
    @Autowired
    private UserThirdLinkDao userThirdLinkDao;
    @Autowired
    private AppInfoService appInfoService;

    private static final String THRID_TYPE_WEIXIN = "weixin";

    private static final String THRID_TYPE_QQ = "qq";

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private WeixinOauth weixinOauth;

    @Autowired
    private QQOauth qqOauth;

    @Autowired
    private EventPublisher eventPublisher;
    @Autowired
    private UserComponent userComponent;
    /**
     * 获取当前用户的账号信息
     *
     * @param token
     * @return
     */
    @Override
    public CommonResult<AccountResult> userDateil(String token) {
        TokenEntity tokenEntity = tokenService.getToken(token);
        String userId = tokenEntity.getUserId();
        return userDetailById(userId);
    }

    public CommonResult<AccountResult> userDetailById(String userId) {
        UserEntity userEntity = userEntityDao.findById(userId);
        UserProfileEntity userProfileEntity = userProfileDao.findById(userId);
        List<UserCarEntity> userCarEntityList = userCarDao.findByUserId(userId);
        AccountResult result = new AccountResult();
        result.setUserId(userId);
        result.setNickname(userEntity.getNickname());
        result.setNickEditable(userProfileEntity.getNicknameModifiable());
        result.setMobile(userEntity.getMobile());
        result.setEmail(userEntity.getEmail());
        result.setBindInfo(userProfileEntity.getRegisterSrc());
        result.setGender(userProfileEntity.getGender());
        if (userProfileEntity.getBirthday() != null) {
            result.setBirthday(new SimpleDateFormat("yyyy-MM-dd").format(userProfileEntity.getBirthday()));
        }
        result.setLiveProvince(userProfileEntity.getProvince());
        result.setLiveCity(userProfileEntity.getCity());
        result.setLiveDistrict(userProfileEntity.getDistrict());
        List<CarInfo> carInfos = new ArrayList<>();
        for (UserCarEntity userCarEntity : userCarEntityList) {
            CarInfo carInfo = new CarInfo();
            carInfo.setCarId(userCarEntity.getId());
            carInfo.setCarBuyDate(userCarEntity.getBuyDate());
            carInfo.setCarModelId(userCarEntity.getCarModelId());
            carInfo.setEngineNo(userCarEntity.getEngineNo());
            carInfo.setLisenceNo(userCarEntity.getCarNo());
            carInfo.setCarIcon(userCarEntity.getCarIcon());
            carInfos.add(carInfo);
        }
        List<String> bindInfos = userThirdLinkDao.selectByUserId(userId, GlobalConstans.IS_VALID_Y);
        StringBuffer bindInfo = new StringBuffer();
        for (String bindType : bindInfos) {
            bindInfo.append(bindType + ",");
        }
        result.setBindInfo(bindInfo.toString());
        result.setCarInfo(carInfos);
        return new CommonResult<AccountResult>().fillResult(ResultCode.SUCCESS).setData(result);
    }

    /**
     * 修改用户信息
     *
     * @return
     */
    @Transactional
    public CommonResult<String> updateUser(UpdateUserParam param) {
        UserEntity userEntity = param.getUser();
        String userId = userEntity.getId();
        UserProfileEntity userProfileEntity = userProfileDao.findById(userId);
        if (userEntity == null) {
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);
        }
        if (userProfileEntity == null) {
            userProfileEntity = this.userComponent.userProfileEntity(userEntity, param);
            this.userProfileDao.save(userProfileEntity);
        }
        //判断用户昵称是否允许被修改
        if (!StringUtils.isEmpty(param.getNickname())
                && UserProfileEntity.NICKNAME_CAN_MODIFY == userProfileEntity.getNicknameModifiable().byteValue()) {
            userEntity.setNickname(param.getNickname());
            userProfileEntity.setNicknameModifiable(UserProfileEntity.NICKNAME_CANNOT_MODIFY);
            userEntityDao.updateById(userEntity);
        }
        if (!StringUtils.isEmpty(param.getGender())) {
            userProfileEntity.setGender(param.getGender());
        }
        if (!StringUtils.isEmpty(param.getBirthday())) {
            userProfileEntity.setBirthday(param.getBirthday());
        }
        if (!StringUtils.isEmpty(param.getLiveCity())) {
            if ("null".equalsIgnoreCase(param.getLiveDistrict())) {
                userProfileEntity.setCity(null);
            } else {
                userProfileEntity.setCity(param.getLiveCity());
            }
        }
        if (!StringUtils.isEmpty(param.getLiveDistrict())) {
            if ("null".equalsIgnoreCase(param.getLiveDistrict())) {
                userProfileEntity.setDistrict(null);
            } else {
                userProfileEntity.setDistrict(param.getLiveDistrict());
            }
        }
        if (!StringUtils.isEmpty(param.getLiveProvince())) {
            userProfileEntity.setProvince(param.getLiveProvince());
        }
        if (StringUtils.isEmpty(userProfileEntity.getUserId()) && StringUtils.isEmpty(userProfileEntity.getId())) {
            userProfileEntity.setUserId(userId);
            userProfileEntity.setId(userId);
            userProfileDao.save(userProfileEntity);
        } else {
            userProfileDao.updateById(userProfileEntity);
        }
        eventPublisher.publishEvent(new UpdateUserEvent(userEntity,param.getToken()));
        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

    /**
     * 修改用户车辆信息
     *
     * @param param
     * @return
     */
    public CommonResult<UpdateUserCarResult> updateUserCar(UpdateUserCarParam param) {
        UserEntity userEntity = param.getUser();//userEntityDao.findById(userId);
        if (userEntity == null) {
            return new CommonResult<UpdateUserCarResult>().fillResult(ResultCode.USER_NOT_EXISTS);
        }
        UserCarEntity userCarEntity = new UserCarEntity();
        userCarEntity.setId(param.getCarId());
        userCarEntity.setUserId(userEntity.getId());
        userCarEntity.setBuyDate(param.getCarBuyDate());
        userCarEntity.setEngineNo(param.getEngineNo());
        userCarEntity.setCarNo(param.getLisenceNo());
        userCarEntity.setOpProductId(param.getProduct());
        userCarEntity.setCarModelId(param.getCarModelId());
        userCarEntity.setCarIcon(param.getCarIcon());
        userCarEntity.setIsValid(true);
        if (StringUtils.isEmpty(param.getCarId())) { //新增  insert
            if (StringUtils.isEmpty(userCarEntity.getCarModelId())) {
                return CommonResult.newInstance(ResultCode.BAD_REQUEST, UpdateUserCarResult.class);
            }
          UserCarEntity userCarEntity1=  this.userCarDao.findByUserIdAndCarNo(userEntity.getId(), param.getLisenceNo());
            if (!StringUtils.isEmpty(param.getLisenceNo())
                    &&  userCarEntity1!= null&&userCarEntity1.getIsValid()) {// if has carNo
                return CommonResult.newInstance(ResultCode.CAR_ALREADY_EXISTS, UpdateUserCarResult.class);
            }

            userCarDao.save(userCarEntity);
        } else {
             UserCarEntity userCarEntity1=  this.userCarDao.findByUserIdAndCarNo(userEntity.getId(), param.getLisenceNo());
            if (!StringUtils.isEmpty(param.getLisenceNo())
                    &&userCarEntity1!= null&&!param.getCarId().equals(userCarEntity1.getId())) {// if has carNo
                return CommonResult.newInstance(ResultCode.CAR_ALREADY_EXISTS, UpdateUserCarResult.class);
            }
            // 更新  update
            userCarDao.updateByIdSelective(userCarEntity);
        }

        return new CommonResult<UpdateUserCarResult>().fillResult(ResultCode.SUCCESS).setData(new UpdateUserCarResult(userCarEntity.getId()));
    }

    /**
     * 绑定手机号 或解绑手机号
     *
     * @param param
     * @return
     */
    @Override
    public CommonResult<String> bindMobile(BindMobileParam param) {
        UserEntity userEntity = param.getUser();
        String userId = userEntity.getId();
        if (userEntity == null) {
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);
        }
        //绑定处理
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            if (!StringUtils.isEmpty(userEntity.getMobile())) {
                return CommonResult.newInstance(ResultCode.PHONE_BIND_EXISTS, String.class);
            }
        }
        //更改绑定
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            if (StringUtils.isEmpty(param.getOldMobile())) {
                return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
            }
            if (!param.getOldMobile().equals(userEntity.getMobile())) {
                return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
            }
            if (param.getMobile().equals(userEntity.getMobile())) {
                return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
            }
        }
        UserEntity user = userEntityDao.findUserByMobile(param.getMobile());
        if (user != null) {
            return CommonResult.newInstance(ResultCode.PHONE_ALREADY_EXISTS, String.class);
        }
        userEntity.setMobile(param.getMobile());
        userEntityDao.updateById(userEntity);
        eventPublisher.publishEvent(new UpdateUserEvent(userEntity,param.getToken()));
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    /**
     * 绑定邮箱 或解绑邮箱
     *
     * @param param
     * @return
     */
    @Override
    public CommonResult<String> bindEmail(BindEmailParam param) {
        UserEntity userEntity = param.getUser();
        String userId = userEntity.getId();
        if (userEntity == null) {
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);
        }
        //绑定处理
        if (GlobalConstans.BIND_TYPE.equals(param.getType())) {
            if (!StringUtils.isEmpty(userEntity.getEmail())) {
                return CommonResult.newInstance(ResultCode.EMAIL_ALREADY_EXISTS, String.class);
            }
        }
        //更改绑定
        if (GlobalConstans.CHANGE_BIND_TYPE.equals(param.getType())) {
            if (StringUtils.isEmpty(param.getEmail())) {
                return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
            }
            if (!param.getOldEmail().equals(userEntity.getEmail())) {
                return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);
            }
        }
        UserEntity user = userEntityDao.findUserByEmail(param.getEmail());
        if (user != null) {
            return CommonResult.newInstance(ResultCode.EMAIL_ALREADY_EXISTS, String.class);
        }
        userEntity.setEmail(param.getEmail());
        userEntityDao.updateById(userEntity);
        eventPublisher.publishEvent(new UpdateUserEvent(userEntity,param.getToken()));
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }


    /**
     * 第三方账号绑定
     *
     * @param param
     * @return
     */
    @Transactional
    public CommonResult<String> bindThridPlatform(BindThriatformParam param) {
        UserEntity userEntity = param.getUser();
        String userId = userEntity.getId();
        if (userEntity == null) {
            return new CommonResult<String>().fillResult(ResultCode.USER_NOT_EXISTS);
        }
        UserThirdLinkEntity    thirdEntity=userThirdLinkDao.findByTypeAndUnionId(param.getType(), param.getUnionid());
        if(thirdEntity!=null){
            return new CommonResult<String>().fillResult(ResultCode.THIRD_USER_ALREADY_BIND_FAILED);
        }
        String thirdId= userThirdLinkDao.selectIdByUserIdAndType(userId, param.getType(), GlobalConstans.IS_VALID_Y);
        if(!StringUtils.isEmpty(thirdId)){
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        }

        OauthLoginParam oauthLoginParam = new OauthLoginParam();
        oauthLoginParam.setAccessToken(param.getAccessToken());
        oauthLoginParam.setProduct(param.getProduct());
        oauthLoginParam.setType(param.getType());
        oauthLoginParam.setRefreshToken(param.getRefreshToken());
        oauthLoginParam.setUnionid(param.getUnionid());
        AppIDKeyEntity appIDKeyEntity = appInfoService.findAppInfo(param.getType(), param.getProduct());
        if (appIDKeyEntity == null) {
            return CommonResult.newInstance(ResultCode.THIRD_USER_PROFILE_FAILED, String.class);
        }
        UserThirdLinkEntity  userThirdLinkEntity=userThirdLinkDao.findByTypeAndOpenId(param.getType(), param.getOpenId());

        if(userThirdLinkEntity!=null){
            return CommonResult.newInstance(ResultCode.THIRD_USER_ALREADY_BIND_FAILED, String.class);
        }

        userThirdLinkEntity = new UserThirdLinkEntity();
        userThirdLinkEntity.setUserId(userId);
        userThirdLinkEntity.setAccessToken(param.getAccessToken());
        userThirdLinkEntity.setOpProductId(param.getProduct());
        userThirdLinkEntity.setIsValid(GlobalConstans.IS_VALID_Y);
        userThirdLinkEntity.setThirdType(THRID_TYPE_WEIXIN);
        userThirdLinkEntity.setThirdAuthInfo(appIDKeyEntity.getId());
        userThirdLinkEntity.setUnionid(oauthLoginParam.getUnionid());
        if (THRID_TYPE_WEIXIN.equals(param.getType())) {
            WeixinUserInfo weixinUserInfo = null;
            try {
                weixinUserInfo = (WeixinUserInfo) weixinOauth.getOauthResult(oauthLoginParam, appIDKeyEntity);
                userThirdLinkEntity.setThirdOpenId(weixinUserInfo.getOpenid());
                if (StringUtils.isEmpty(userEntity.getNickname())) {
                    userEntity.setNickname(weixinUserInfo.getNickname());
                    userEntityDao.updateByIdSelective(userEntity);
                }
                UserProfileEntity userProfileEntity = userProfileDao.findById(userId);
                if (StringUtils.isEmpty(userProfileEntity.getGender())) {
                    userProfileEntity.setGender(weixinUserInfo.gender());
                }
                if (StringUtils.isEmpty(userProfileEntity.getImgUrl())) {
                    CommonResult result = uploadFileService.saveThridPlatformImg(userId, weixinUserInfo.imgUrl());
                    if (result.getCode() != ResultCode.SUCCESS.code()) {
                        return result;
                    }
                }
                userProfileDao.updateByIdSelective(userProfileEntity);
            } catch (IllegalAccessException e) {
                return new CommonResult<String>().fillResult(ResultCode.THIRD_USER_PROFILE_FAILED);
            }
            if (weixinUserInfo == null) {
                return CommonResult.newInstance(ResultCode.THIRD_USER_PROFILE_FAILED, String.class);
            }
        }

        if (THRID_TYPE_QQ.equals(param.getType())) {
            try {
                QQUserInfo qqUserInfo = (QQUserInfo) qqOauth.getOauthResult(oauthLoginParam, appIDKeyEntity);
                if (StringUtils.isEmpty(userEntity.getNickname())) {
                    userEntity.setNickname(qqUserInfo.getNickname());
                    userEntityDao.updateByIdSelective(userEntity);
                }
                UserProfileEntity userProfileEntity = userProfileDao.findById(userId);
                if (StringUtils.isEmpty(userProfileEntity.getGender())) {
                    userProfileEntity.setGender(qqUserInfo.gender());
                }
                if (StringUtils.isEmpty(userProfileEntity.getImgUrl())) {
                    CommonResult result = uploadFileService.saveThridPlatformImg(userId, qqUserInfo.imgUrl());
                    if (result.getCode() != ResultCode.SUCCESS.code()) {
                        return result;
                    }
                }
                userProfileDao.updateByIdSelective(userProfileEntity);
            } catch (IllegalAccessException e) {
                return CommonResult.newInstance(ResultCode.THIRD_USER_PROFILE_FAILED, String.class);
            }
        }

        userThirdLinkDao.save(userThirdLinkEntity);


        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    /**
     * 第三方解绑
     *
     * @param param
     * @return
     */
    @Override
    public CommonResult<String> unbindThridPlatform(UnbindThriatformParam param) {
        UserEntity userEntity = param.getUser();
        String userId = userEntity.getId();
        if (userEntity == null) {
            return CommonResult.newInstance(ResultCode.USER_NOT_EXISTS, String.class
            );
        }
        userEntity=userEntityDao.findById(userId);
        if (StringUtils.isEmpty(userEntity.getEmail()) && StringUtils.isEmpty(userEntity.getMobile())) {
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        }
        String id = userThirdLinkDao.selectIdByUserIdAndType(userId, param.getType(), GlobalConstans.IS_VALID_Y);
        if (StringUtils.isEmpty(id)) {
            return CommonResult.newInstance(ResultCode.UNBIND_NOT_SATISFIED, String.class);
        }

        userThirdLinkDao.deleteById(id);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @Override
    public CommonResult<String> deleteUserCar(DeleteUserCarParam deleteUserCarParam) {
        int i = this.userCarDao.deleteCarById(deleteUserCarParam.getId());
        if (i == 0) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage("车不存在!");
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }


}


