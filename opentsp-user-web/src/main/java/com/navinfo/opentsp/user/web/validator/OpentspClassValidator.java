package com.navinfo.opentsp.user.web.validator;

/**
 * Created by wupeng on 11/3/15.
 */
public interface OpentspClassValidator {

    public boolean validate(Object instance, String methodName, Object[] args);

    public Class support();

}
