package com.navinfo.opentsp.user.service.task;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.common.util.scheduler.AbstractScheduler;
import com.navinfo.opentsp.user.dal.dao.ScheduleTaskDao;
import com.navinfo.opentsp.user.dal.entity.ScheduledTaskHistory;
import com.navinfo.opentsp.user.service.cache.CacheService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-24
 * @modify
 * @copyright Navi Tsp
 */
public abstract class MultiHostScheduler extends AbstractScheduler {
    private static final Logger logger = LoggerFactory.getLogger(MultiHostScheduler.class);

    @Autowired
    private ScheduleTaskDao taskDao;

    @Autowired
    private CacheService cacheService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String key = GlobalConstans.TASK_LOCK_KEY + this.id();
        long lock = cacheService.increase(key, 1L);//try to lock
        if (lock != 1L) {//already locked by other service
            cacheService.increase(key, -1L);// release tried lock
            logger.warn("other host was already executing the task ! I am out !");
            return;
        }

        //locked
        try {
            ScheduledTaskHistory history = new ScheduledTaskHistory();//TODO add this code to a AOP
            history.setCreateTime(new Date());
            history.setTaskClass(this.id());
            history.setTaskMethod("executeInternal");
            history.setNote(this.executeInternal());//execute
            history.setEndTime(new Date());
            this.taskDao.save(history);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            cacheService.increase(key, -1L);//release lock
        }
    }

    public abstract String executeInternal() throws Exception;

    public abstract String id();

}
