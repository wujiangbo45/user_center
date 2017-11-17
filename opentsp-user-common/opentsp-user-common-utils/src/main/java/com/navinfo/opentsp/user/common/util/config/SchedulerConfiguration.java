package com.navinfo.opentsp.user.common.util.config;

import com.navinfo.opentsp.user.common.util.scheduler.AbstractScheduler;
import com.navinfo.opentsp.user.common.util.scheduler.OpenTspScheduler;
import com.navinfo.opentsp.user.common.util.scheduler.StdMethodInvokingJobDetailFactoryBean;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-27
 * @modify
 * @copyright Navi Tsp
 */
//TODO make this work(currently I used spring schedule component)
//@Configuration
public class SchedulerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired(required = false)
    @Bean
    public List<CronTriggerFactoryBean> triggerFactoryBeans(List<AbstractScheduler> schedulers) {
        if(schedulers == null || schedulers.size() == 0) {
            return null;
        }

        List<CronTriggerFactoryBean> triggerFactoryBeans = new LinkedList<>();

        for(OpenTspScheduler scheduler : schedulers) {
            logger.info("creating job details for scheduler : {} ", scheduler.getClass());
            StdMethodInvokingJobDetailFactoryBean jobDetailFactoryBean = new StdMethodInvokingJobDetailFactoryBean();
            jobDetailFactoryBean.setTargetObject(scheduler);
            jobDetailFactoryBean.setTargetMethod("schedule");
            jobDetailFactoryBean.setConcurrent(false);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(jobDetailFactoryBean);

            CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
            triggerFactoryBean.setJobDetail(jobDetailFactoryBean.getObject());
            triggerFactoryBean.setCronExpression(jobDetailFactoryBean.getCron());
            triggerFactoryBeans.add(triggerFactoryBean);

        }

        return triggerFactoryBeans;
    }

    @Autowired(required = false)
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(List<CronTriggerFactoryBean> triggerFactoryBeans) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();

        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.jmx.export", "false");
        properties.setProperty("org.quartz.scheduler.skipUpdateCheck", "true");
        schedulerFactoryBean.setQuartzProperties(properties);

        List<Trigger> triggers = new LinkedList<>();
        if(triggerFactoryBeans != null) {
            for(CronTriggerFactoryBean factoryBean : triggerFactoryBeans) {
                triggers.add(factoryBean.getObject());
            }
        }

        schedulerFactoryBean.setTriggers(triggers.toArray(new Trigger[0]));
        return schedulerFactoryBean;
    }

}
