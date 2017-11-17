package com.navinfo.opentsp.user.password;

import com.navinfo.opentsp.user.common.util.uuid.UUIDUtil;
import com.navinfo.opentsp.user.service.security.BCryptPasswordEncoder;
import com.navinfo.opentsp.user.service.security.PasswordEncoder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-09
 * @modify
 * @copyright Navi Tsp
 */
public class RandomPassword {


    @Test
    public void oldNewPasswordMatch() {
        String oldSource = "password";
        String oldEncoder = "$2a$10$4t0xLFEqGzwJfPzbUen.GeRnXcoXynkmIzhLhHpppHcDiW5gWV1Dq";

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(oldSource, oldEncoder);
        Assert.assertTrue(matches);

        oldSource = "123456";
        oldEncoder = "$2a$08$8KayfRJ120Ljqw4QmJBDB.B9aI/kS83HFGT7GzbXTEvs.aTPimcqe";
        matches = encoder.matches(oldSource, oldEncoder);
        Assert.assertTrue(matches);
    }

    public String salt(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 5; i++) {
            sb.append((char) random.nextInt(255));
        }

        return sb.toString();
    }

    @Test
    public void test() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String salt = this.salt();
        System.out.println("using salt : " + salt);

        String password = this.salt() + this.salt();


        String encoded = encoder.encode(salt + password);
        System.out.println("random password : [" + encoded + "], using salt : ["+salt+"]");

        System.out.println(UUIDUtil.randomUUID());
    }

}
