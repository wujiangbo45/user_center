package com.navinfo.opentsp.user.web.backend;

import com.navinfo.opentsp.user.service.param.backend.QueryUserListParam;
import com.navinfo.opentsp.user.service.param.backend.ResetPasswordParam;
import com.navinfo.opentsp.user.service.param.setting.UserDetailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.PageResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryUserListResult;
import com.navinfo.opentsp.user.service.resultdto.setting.AccountResult;
import com.navinfo.opentsp.user.service.setting.AccountService;
import com.navinfo.opentsp.user.service.user.UserBackendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Created by wupeng on 11/3/15.
 */
@Profile("enable-backend")
@RestController
@RequestMapping("/backend/user")
public class UserBackendController {
    private static final Logger logger = LoggerFactory.getLogger(UserBackendController.class);

    @Autowired
    private UserBackendService userService;
    @Autowired
    private AccountService accountService;

    @Value("${opentsp.admin.resetpwd.disable:false}")
    private boolean disbleResetPwd;

    @RequestMapping(value ="/query")
    public PageResult<QueryUserListResult> queryList(@RequestBody  QueryUserListParam param) {
        return this.userService.query(param);
    }

    @RequestMapping("/resetPassword")
    public CommonResult<String> resetPasswd(@RequestBody ResetPasswordParam passwordParam) {
        if (disbleResetPwd) {
            return new CommonResult<String>().fillResult(ResultCode.FORBIDDEN).setMessage("此功能已被关闭!");
        }
        try {
            return this.userService.resetPassword(passwordParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new CommonResult<String>().fillResult(ResultCode.SERVER_ERROR);
        }
    }

    @RequestMapping("/userDetail")
    public CommonResult<AccountResult> userDetail(HttpServletRequest request, @RequestBody UserDetailParam param) {
        String userId = param.getUserId();
        if (StringUtils.isEmpty(userId)) {
            return CommonResult.newInstance(ResultCode.BAD_REQUEST, AccountResult.class);
        }

        return accountService.userDetailById(userId);
    }

}
