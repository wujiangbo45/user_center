package com.navinfo.opentsp.user.dal.mybatis.listener;

import com.navinfo.opentsp.user.common.util.event.ClassScanEvent;
import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.stereotype.Component;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
@Component
@Mybatis
public class ClassScanListener extends AbstractOpentspListener<ClassScanEvent> {
    @Override
    public boolean async() {
        return true;
    }

    @Override
    public void onEvent(ClassScanEvent event) {

    }


}
