package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.UserLoginLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface UserLoginLogEntityMapper extends MybatisMapper<UserLoginLogEntity, String>{

    List<UserLoginLogEntity> latestLogs(@Param("userId") String userId, @Param("loginDate")Date loginDate, @Param("num") int num);

}