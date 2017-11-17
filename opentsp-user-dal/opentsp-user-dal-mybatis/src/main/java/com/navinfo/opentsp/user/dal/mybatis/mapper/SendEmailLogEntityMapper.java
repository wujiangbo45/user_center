package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.SendEmailLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface SendEmailLogEntityMapper extends MybatisMapper<SendEmailLogEntity, String>{

    int updateByPrimaryKeyWithBLOBs(SendEmailLogEntity record);

    long countByIp(@Param("ip")String ip, @Param("date")Date date);

    long countByEmail(@Param("email")String email, @Param("date")Date date);

    SendEmailLogEntity findLatestLog(String email);

    SendEmailLogEntity findLatestLogByIp(String ip);
}