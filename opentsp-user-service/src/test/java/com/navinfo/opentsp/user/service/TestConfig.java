package com.navinfo.opentsp.user.service;

import com.navinfo.opentsp.user.common.util.UtilScan;
import com.navinfo.opentsp.user.dal.mybatis.MybatisScan;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackageClasses = {UtilScan.class, MybatisScan.class, ServiceScan.class})
public class TestConfig {



}
