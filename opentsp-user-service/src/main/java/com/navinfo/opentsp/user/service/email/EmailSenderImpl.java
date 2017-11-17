package com.navinfo.opentsp.user.service.email;

import com.navinfo.opentsp.user.common.util.email.MailSendBackend;
import com.navinfo.opentsp.user.common.util.file.FileUtil;
import com.navinfo.opentsp.user.common.util.json.JsonUtil;
import com.navinfo.opentsp.user.dal.dao.SendEmailLogDao;
import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-12
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class EmailSenderImpl implements EmailSender {
    private static final Logger logger = LoggerFactory.getLogger(EmailSenderImpl.class);

    @Autowired
    private SendEmailLogDao logDao;
    @Autowired
    private MailSendBackend backend;

    @Override
    public void sendEmail(String template, Map<String, String> params, SendEmailLogEntity entity) {
        String html = null;
        try {
            String name = "templates/email/" + template + ".html";
            html = FileUtil.readString(name);
            html = FileUtil.replace(html, params);
            entity.setContent(new StringBuilder("name : ").append(name)
                    .append(", json : ").append(JsonUtil.toJson(params)).toString());
            logger.info("send mail json : {}", html);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        if (!StringUtils.isEmpty(html)) {
            this.backend.sendHtmlEmail(entity.getEmail(), entity.getSubject(), html);

            entity.setResponse("success");
            this.logDao.save(entity);
        }
    }
}
