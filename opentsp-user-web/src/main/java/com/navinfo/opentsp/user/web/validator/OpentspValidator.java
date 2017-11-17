package com.navinfo.opentsp.user.web.validator;

import com.navinfo.opentsp.user.service.validator.ParamValidatable;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
public interface OpentspValidator {

    /**
     *
     * 在进入到controller之前执行，校验参数
     *
     * @param args  controller的方法参数
     * @return true表示参数校验成功，false表示参数校验失败
     */
    public boolean validate(ParamValidatable... args);

}
