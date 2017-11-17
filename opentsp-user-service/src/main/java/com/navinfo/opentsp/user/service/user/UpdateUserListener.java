package com.navinfo.opentsp.user.service.user;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.service.event.UpdateUserEvent;
import com.navinfo.opentsp.user.service.session.SessionAttrConstant;
import com.navinfo.opentsp.user.service.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-12-21
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class UpdateUserListener extends AbstractOpentspListener<UpdateUserEvent> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateUserListener.class);

    @Autowired
    private SessionManager sessionManager;

    @Override
    public void onEvent(UpdateUserEvent event) {
        if (StringUtils.isEmpty(event.token()) || event.getData() == null) {
            logger.info("update user session failed ! token : {}, userEntity : {}", event.token(), event.getData());
            return;
        }

        this.sessionManager.getSession(event.token()).setAttribute(SessionAttrConstant.ATTR_USER_ENTITY, event.getData());
        logger.info("update user session success !");
    }
}
