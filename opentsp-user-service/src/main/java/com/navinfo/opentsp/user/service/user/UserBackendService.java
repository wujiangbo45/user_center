package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.service.param.backend.QueryUserListParam;
import com.navinfo.opentsp.user.service.param.backend.ResetPasswordParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.PageResult;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryUserListResult;

/**
 *
 * Created by wupeng on 11/3/15.
 */
public interface UserBackendService {

    public PageResult<QueryUserListResult> query(QueryUserListParam param);

    public CommonResult<String> resetPassword(ResetPasswordParam passwordParam);

}
