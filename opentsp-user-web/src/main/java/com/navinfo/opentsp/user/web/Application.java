package com.navinfo.opentsp.user.web;

import com.navinfo.opentsp.user.common.util.UtilScan;
import com.navinfo.opentsp.user.dal.mongo.MongoScan;
import com.navinfo.opentsp.user.dal.mybatis.MybatisScan;
import com.navinfo.opentsp.user.dal.mybatis.configuration.MybatisConfig;
import com.navinfo.opentsp.user.service.ServiceScan;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.TimeZone;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
@Configuration
@EnableAutoConfiguration
@Import({MybatisConfig.class})
@MapperScan(basePackageClasses = {MybatisScan.class})
@ComponentScan(basePackageClasses = {UtilScan.class, MybatisScan.class, MongoScan.class, ServiceScan.class, Application.class})
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));//BeiJing TimeZone
        SpringApplication.run(Application.class, args);

        /**
         * 出现这个吉祥马就是程序启动成功的标识， 勿删 ！！
         */
        logger.error(" application startup success !\n" +
                "                           _(\\_/) \n" +
                "                         ,((((^`\\\n" +
                "                        ((((  (6 \\ \n" +
                "                      ,((((( ,    \\\n" +
                "  ,,,_              ,(((((  /\"._  ,`,\n" +
                " ((((\\\\ ,...       ,((((   /    `-.-'\n" +
                " )))  ;'    `\"'\"'\"\"((((   (      \n" +
                "(((  /            (((      \\\n" +
                " )) |                      |\n" +
                "((  |        .       '     |\n" +
                "))  \\     _ '      `t   ,.')\n" +
                "(   |   y;- -,-\"\"'\"-.\\   \\/  \n" +
                ")   / ./  ) /         `\\  \\\n" +
                "   |./   ( (           / /'\n" +
                "   ||     \\\\          //'|\n" +
                "   ||      \\\\       _//'||\n" +
                "   ||       ))     |_/  ||\n" +
                "   \\_\\     |_/          ||\n" +
                "   `'\"                  \\_\\\n" +
                "                        `'\"");
    }

}
