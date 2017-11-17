package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserEntityMapper extends MybatisMapper<UserEntity, String> {

    /**
     * 根据手机号查找用户
     *
     * @param mobile
     * @return
     */
    public UserEntity findUserByMobile(String mobile);

    /**
     * 根据邮箱查找用户
     *
     * @param email
     * @return
     */
    public UserEntity findUserByEmail(String email);

    /**
     * 根据唯一标识查找用户
     *
     * @param identifier 唯一标识， 手机号或者邮箱或者userId
     * @return
     */
    public UserEntity findUserByIdentifier(String identifier);

    /**
     * 将手机号置空
     *
     * @param id
     * @return
     */
    public int updateMobileIsNullById(String id);

    /**
     * 将邮箱置空
     *
     * @param id
     * @return
     */
    public int updateEmailIsNullById(String id);

    /**
     * 查询
     *
     * @param key       关键字
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @param bind      绑定信息, 0 - all, 1 - no, 2 - wechat, 3 - qq, 4 - wechat or qq
     * @param offset
     * @param pageSize
     * @return
     */
    public List<UserEntity> query(@Param("key") String key,
                                  @Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate,
                                  @Param("bind") String bind,
                                  @Param("offset") int offset,
                                  @Param("pageSize") int pageSize,
                                  @Param("order")String order,
                                  @Param("orderType")String orderType);

    long queryCount(@Param("key") String key,
                    @Param("startDate") Date startDate,
                    @Param("endDate") Date endDate,
                    @Param("bind") String bind);
}