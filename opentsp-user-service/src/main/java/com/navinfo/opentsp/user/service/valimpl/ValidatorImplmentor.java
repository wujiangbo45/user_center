package com.navinfo.opentsp.user.service.valimpl;

/**
 *
 * 校验的实现者
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-09
 * @modify
 * @copyright Navi Tsp
 */
public interface ValidatorImplmentor {

    /**
     *
     * 执行校验
     *
     * @param arg
     * @return true 表示校验通过， false表示不通过
     */
    public boolean doValidate(Object arg);

    /**
     *
     * 支持哪个校验， 例如 NotNull ..etc
     *
     * @return
     */
    public Class supportValidation();

}
