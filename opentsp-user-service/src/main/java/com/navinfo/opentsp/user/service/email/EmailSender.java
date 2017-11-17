package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;

import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
public interface EmailSender {

    public void sendEmail(String template, Map<String, String> params, SendEmailLogEntity entity);

}
