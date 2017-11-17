package com.navinfo.opentsp.user.service.task;

import com.navinfo.opentsp.user.common.util.event.EventPublisher;
import com.navinfo.opentsp.user.common.util.scheduler.AbstractScheduler;
import com.navinfo.opentsp.user.service.event.ReloadConfigEvent;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * 由于有一部分数据是缓存在内存里面的, @see ProductServiceImpl or AppInfoServiceImpl， 在负载均衡的部署方式下会出现问题
 * 例如A、B机器均部署了此服务， 用户在A机器修改了产品渠道信息， 那么A机器和数据库均做了修改， 但是B机器不知道此改变， 导致A、B机器
 * 上面缓存的数据不一致， 这里可以考虑存入redis。 但是由于这类信息实时性要求不高， 所以这里我先做一个简单方案， 每五分钟定时查一下库就好了。
 *
 *
 * @author wupeng
 * @version 1.0
 * @date 2016-01-20
 * @modify
 * @copyright Navi Tsp
 */
@EnableScheduling
@Component
public class CacheRefreshTask extends AbstractScheduler {
    private static final Logger logger = LoggerFactory.getLogger(CacheRefreshTask.class);

    private static final String CRON = "0 0/5 * * * ?";

    @Autowired
    private EventPublisher eventPublisher;

    @Override
    public String getCronExpression() {
        return CRON;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }

    @Scheduled(cron = CRON)
    public void executeInternal() {
        logger.info("begin to reloading cache !");
        ReloadConfigEvent event = new ReloadConfigEvent();
        this.eventPublisher.publishEvent(event);
        logger.info("reloading cache complete !");
    }
}
