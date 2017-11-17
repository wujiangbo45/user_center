package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.service.param.setting.DeleteUserCarParam;
import com.navinfo.opentsp.user.service.param.setting.UpdateUserCarParam;
import com.navinfo.opentsp.user.service.param.setting.UpdateUserParam;
import com.navinfo.opentsp.user.service.param.setting.UserDetailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.resultdto.setting.UpdateUserCarResult;
import com.navinfo.opentsp.user.service.setting.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author wanliang
 * @version 1.0
 * @date 2015/10/20
 * @modify
 * @copyright Navi Tsp
 */
@Controller
@RequestMapping("/user")
public class AccountController {

    @Autowired
    private AccountService accountService;

    /**
     * 获取当前用户资料
     *
     * @param request
     * @param param
     * @return
     */
    @RequestMapping("/userDetail")
    @ResponseBody
    public CommonResult<AccountResult> userDetail(HttpServletRequest request, @RequestBody UserDetailParam param) {

        return accountService.userDateil(param.getToken());
    }

    /**
     * 修改用户
     *
     * @param param
     * @return
     */
    @RequestMapping("/updateUser")
    @ResponseBody
    public CommonResult<String> updateUser(@RequestBody UpdateUserParam param) {

        if (param.getBirthday() == null
                && param.getGender() == null
                && StringUtils.isEmpty(param.getLiveCity())
                && StringUtils.isEmpty(param.getLiveDistrict())
                && StringUtils.isEmpty(param.getLiveProvince())
                && StringUtils.isEmpty(param.getNickname())
                ) {
            return new CommonResult<String>().fillResult(ResultCode.BAD_REQUEST).setMessage("nothing to update !");
        }

        if (!StringUtils.isEmpty(param.getNickname()) && param.getNickname().length() > 14) {
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR);
        }

        return accountService.updateUser(param);
    }

    /**
     * 修改车辆信息
     * @param param
     * @return
     */
    @RequestMapping("/updateUserCar")
    @ResponseBody
    public CommonResult<UpdateUserCarResult> updateUserCar(@RequestBody UpdateUserCarParam param) {
        return accountService.updateUserCar(param);
    }

    @RequestMapping(value = "/deleteUserCar")
    @ResponseBody
    public CommonResult<String> deleteUserCar(@RequestBody DeleteUserCarParam deleteUserCarParam) {
        return accountService.deleteUserCar(deleteUserCarParam);
    }
}
