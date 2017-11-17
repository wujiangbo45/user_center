package com.navinfo.opentsp.user.service.param;

import com.navinfo.opentsp.user.service.validator.ParamValidatable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface HttpHeaderParamable extends ParamValidatable {

    /**
     *
     * 头信息和参数信息的映射关系.  eg. new String[][]{{"fieldName", "headerName"},{"fieldName", "headerName"}}
     *
     * @return
     */
    public String[][] fieldHeaderMapping();

}
