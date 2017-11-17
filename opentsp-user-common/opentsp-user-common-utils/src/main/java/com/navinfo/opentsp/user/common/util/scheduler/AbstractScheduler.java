package com.navinfo.opentsp.user.common.util.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-27
 * @modify
 * @copyright Navi Tsp
 */
public abstract class AbstractScheduler extends CronTriggerImpl implements OpenTspScheduler {
    @Override
    public abstract String getCronExpression();

    @Override
    public void execute() throws JobExecutionException {
        this.execute(null);
    }

    @Override
    public abstract void execute(JobExecutionContext context) throws JobExecutionException;
}
