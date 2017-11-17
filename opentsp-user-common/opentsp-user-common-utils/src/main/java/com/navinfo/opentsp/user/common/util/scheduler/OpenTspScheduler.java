package com.navinfo.opentsp.user.common.util.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionException;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-27
 * @modify
 * @copyright Navi Tsp
 */
public interface OpenTspScheduler extends Job {

    public abstract String getCronExpression();

    public void execute() throws JobExecutionException;

}
