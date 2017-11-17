package com.navinfo.opentsp.user.service.cache;

import com.navinfo.opentsp.user.common.util.listener.AbstractOpentspListener;
import com.navinfo.opentsp.user.service.event.ReloadConfigEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class ReloadConfigListener  extends AbstractOpentspListener<ReloadConfigEvent> {

    @Autowired
    private List<CacheReloadable> reloadables;

    @Override
    public boolean async() {
        return false;
    }

    @Override
    public void onEvent(ReloadConfigEvent event) {
        for (CacheReloadable reloadable : reloadables) {
            reloadable.reload();
        }
    }
}
