package com.navinfo.opentsp.user.service.task;

import com.navinfo.opentsp.user.service.statics.RegisterStaticsService;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * 每天凌晨零点1分， 分析前一天的注册数量
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-27
 * @modify
 * @copyright Navi Tsp
 */
@EnableScheduling
@Component
public class RegisterStaticsTask extends MultiHostScheduler {
    private static final Logger logger = LoggerFactory.getLogger(RegisterStaticsTask.class);

    private static final String CRON = "0 1 0 * * ?";

    @Autowired
    private RegisterStaticsService staticsService;

    @Override
    public String getCronExpression() {
        return CRON;
    }

    /**
     * entry point of task
     * @throws JobExecutionException
     */
    @Scheduled(cron = CRON)
    @Override
    public void execute() throws JobExecutionException {
        super.execute();
    }

    @Override
    public String executeInternal() {
        String message = null;
        logger.info("begin to analyze register statics !");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());//2015-10-21 00:01:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        Date dayEnd = calendar.getTime();//2015-10-21 00:00:00
        calendar.add(Calendar.DAY_OF_YEAR, -1);//2015-10-20 00:00:00
        Date dayStart = calendar.getTime();

        try {
            int i = staticsService.analyzeRegister(dayStart, dayEnd, false);
            message = "statics success ! count : " + i;
            logger.info("statics success ! count : {}", i);
        } catch (Exception e) {
            message = e.getMessage();
            logger.error("statics failed !");
            logger.error(e.getMessage(), e);
        }

        return message;
    }

    @Override
    public String id() {
        return RegisterStaticsTask.class.getName();
    }
}
