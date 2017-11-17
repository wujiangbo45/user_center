package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.service.param.email.SendEmailParam;
import com.navinfo.opentsp.user.service.result.CommonResult;

import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
public interface EmailService {

    /**
     * 发送邮件
     * @param emailParam
     * @return
     */
    public CommonResult<String> sendEmail(SendEmailParam emailParam);

    /**
     * 内部调用， 直接发送
     *
     * called internal, not save anything, not check send count, send ip and  etc..
     *
     * @param email
     * @param template
     * @param attrs
     * @return
     */
    public boolean notify(String email, String subject, String template, Map<String, String> attrs);

}
