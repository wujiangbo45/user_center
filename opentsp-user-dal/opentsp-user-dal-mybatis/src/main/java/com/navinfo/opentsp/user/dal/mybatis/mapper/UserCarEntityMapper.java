package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserCarEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCarEntityMapper extends MybatisMapper<UserCarEntity, String>{

    public List<UserCarEntity> selectByUserId(String userId);

    int deleteByCarId(String id);

    UserCarEntity findByUserIdAndCarNo(@Param("userId")String userId,
                                       @Param("carNo")String carNo);
}