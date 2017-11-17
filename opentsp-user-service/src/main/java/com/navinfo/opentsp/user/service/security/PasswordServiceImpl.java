package com.navinfo.opentsp.user.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class PasswordServiceImpl implements PasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String encodePassword(String original, String salt) {
        return passwordEncoder.encode(salt + original);
    }

    @Override
    public boolean matches(String original, String salt, String encodedPassword) {
        return passwordEncoder.matches(salt + original, encodedPassword);
    }
}
