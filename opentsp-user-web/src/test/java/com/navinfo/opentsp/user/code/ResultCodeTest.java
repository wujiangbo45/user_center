package com.navinfo.opentsp.user.code;

import com.navinfo.opentsp.user.service.result.ResultCode;
import org.junit.Test;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-21
 * @modify
 * @copyright Navi Tsp
 */
public class ResultCodeTest {

    @Test
    public void testLen(){
        System.out.println("阿萨德是".length());
        System.out.println("asdf".length());
    }

//    @Test
    public void test(){
        for(ResultCode code : ResultCode.values()) {
            System.out.println("|" + code.code() + "|" + code.httpCode() + "|" + code.message());
        }
    }

}
