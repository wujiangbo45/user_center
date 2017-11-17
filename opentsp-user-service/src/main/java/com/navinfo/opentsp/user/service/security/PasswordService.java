package com.navinfo.opentsp.user.service.security;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface PasswordService {

    /**
     * 加密
     * @param original
     * @param salt
     * @return
     */
    public String encodePassword(String original, String salt);

    /**
     * 是否匹配
     * @param original
     * @param salt
     * @param encodedPassword
     * @return
     */
    public boolean matches(String original, String salt, String encodedPassword);

}
