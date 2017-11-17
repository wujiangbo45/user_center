package com.navinfo.opentsp.user.dal.mongo.dao;

import com.navinfo.opentsp.user.dal.dao.ScheduleTaskDao;
import com.navinfo.opentsp.user.dal.entity.ScheduledTaskHistory;
import org.springframework.stereotype.Repository;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-09
 * @modify
 * @copyright Navi Tsp
 */
@Repository
public class ScheduleTaskDaoMongoImpl extends BaseMongoDaoImpl<ScheduledTaskHistory, String> implements ScheduleTaskDao {
}
