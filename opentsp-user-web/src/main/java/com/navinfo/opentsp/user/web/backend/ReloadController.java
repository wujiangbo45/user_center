package com.navinfo.opentsp.user.web.backend;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.service.event.ReloadConfigEvent;
import com.navinfo.opentsp.user.service.location.LocationService;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-20
 * @modify
 * @copyright Navi Tsp
 */
@Profile("enable-backend")
@RestController
@RequestMapping("/backend/config")
public class ReloadController {

    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private LocationService locationService;

    @RequestMapping(value = "/reload")
    public CommonResult<String> reload() {
        ReloadConfigEvent event = new ReloadConfigEvent();
        this.eventPublisher.publishEvent(event);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping(value = "/reloadLocation")
    public CommonResult<String> reloadLocation() {
        locationService.reload();
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

}
