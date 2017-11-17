package com.navinfo.opentsp.user.service.param;

import com.navinfo.opentsp.user.service.validator.ParamValidatable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface IpManaged extends ParamValidatable{
    /**
     * 设置ip
     * @param ip
     */
    public void setIp(String ip);

    /**
     * 获取ip
     * @return
     */
    public String getIp();


}
