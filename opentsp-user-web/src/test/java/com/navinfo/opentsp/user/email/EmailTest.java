package com.navinfo.opentsp.user.email;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/3/11
 * @modify
 * @copyright Navi Tsp
 */
public class EmailTest {

    public static void main(String [] args)throws  Exception{
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("mail.mapbar.com");
        javaMailSender.setUsername("wdjenkins@mapbar.com");
        javaMailSender.setPassword("AnVDdxRQ12rmkRo");
        Properties properties=new Properties();
        properties.setProperty("mail.smtp.auth","true");
        javaMailSender.setJavaMailProperties(properties);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true,"utf-8");
        messageHelper.setTo(new InternetAddress("wanliang@mapbar.com"));
        messageHelper.setFrom("wdjenkins@mapbar.com");
        messageHelper.setSubject("c测试信息1");
        messageHelper.setText("<html><head></head><body><h1>hello!!zhangjian</h1></body></html>", true);
        javaMailSender.send(mimeMessage);
    }
}