package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.SendSmsLogEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface SendSmsLogEntityMapper extends MybatisMapper<SendSmsLogEntity, String>{


    int updateByPrimaryKeyWithBLOBs(SendSmsLogEntity record);


    long countByIp(@Param("ip")String ip, @Param("date")Date date);

    long countByPhone(@Param("phone")String phone, @Param("date")Date date);

    long countByPhoneAndContent(@Param("phone")String phone, @Param("content")String content, @Param("date")Date date);

    SendSmsLogEntity findLatestLog(String phone);

    SendSmsLogEntity findLatestLogByIp(String ip);
}