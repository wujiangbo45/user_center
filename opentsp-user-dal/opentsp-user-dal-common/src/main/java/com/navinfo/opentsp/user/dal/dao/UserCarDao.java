package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.UserCarEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserCarDao extends BaseDAO<UserCarEntity, String>{

    /**
     * 根据userId获取车信息
     * @param userId
     * @return
     */
    public List<UserCarEntity> findByUserId(String userId);

    /**
     * 根据用户id和车牌号查询
     * @param userId
     * @param carNo
     * @return
     */
    public UserCarEntity findByUserIdAndCarNo(String userId, String carNo);

    int deleteCarById(String id);
}
