package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.service.param.IpManaged;
import com.navinfo.opentsp.user.service.validator.ParamValidatable;
import com.navinfo.opentsp.user.web.interceptor.HttpHolder;
import com.navinfo.opentsp.user.web.util.HttpRequestUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-15
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class IpParamValidator implements OpentspValidator {
    @Override
    public boolean validate(ParamValidatable... args) {

        for (ParamValidatable arg : args ) {
            if(arg instanceof IpManaged) {
                IpManaged ip = (IpManaged) arg;
                if(StringUtils.isEmpty(ip.getIp())) {
                    ip.setIp(HttpRequestUtil.getIpAddr(HttpHolder.getRequest()));
                }
            }
        }

        return true;
    }
}
