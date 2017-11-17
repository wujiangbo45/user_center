package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.UserEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface UserEntityDao extends BaseDAO<com.navinfo.opentsp.user.dal.entity.UserEntity, String> {

    /**
     * 根据手机号查找用户
     * @param mobile
     * @return
     */
    public UserEntity findUserByMobile(String mobile);

    /**
     * 根据邮箱查找用户
     * @param email
     * @return
     */
    public UserEntity findUserByEmail(String email);

    /**
     *
     * @param identifier 唯一标识， 手机号或者邮箱或者userId
     * @return
     */
    public UserEntity findUserByIdentifier(String identifier);

    /**
     * 将手机号置空
     * @param id
     * @return
     */
    public int   updateMobileIsNullById(String id);

    /**
     * 将邮箱置空
     * @param id
     * @return
     */
    public int  updateEmailIsNullById(String id);

    /**
     *
     * @param key
     * @param startDate
     * @param endDate
     * @param bind 0 - all, 1 - no, 2 - wechat, 3 - qq, 4 - wechat or qq
     * @return
     */
    public List<UserEntity> query(String key, String startDate, String endDate, String bind, int pageNum, int pageSize,String order,String orderType);

    /**
     * count of method query
     * @param key
     * @param startDate
     * @param endDate
     * @param bind 0 - all, 1 - no, 2 - wechat, 3 - qq, 4 - wechat or qq
     * @return
     */
    public long queryCount(String key, String startDate, String endDate, String bind);
}
