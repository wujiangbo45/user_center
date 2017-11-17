package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;

import java.util.Date;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserLoginLogDao extends BaseDAO<UserLoginLogEntity, String> {

    /**
     * 查询最近记录
     * @param userId
     * @param date 在这个日期之后
     * @param num 最新的多少条
     * @return
     */
    public List<UserLoginLogEntity> latestLogs(String userId, Date date, int num);

}
