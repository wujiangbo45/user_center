package com.navinfo.opentsp.user.service.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Profile("md5")
@Service
public class Md5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(String rawPassword) {
        return Md5.md5(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        if(StringUtils.isEmpty(encodedPassword ))
            return false;

        return encodedPassword.equals(this.encode(rawPassword));
    }
}
