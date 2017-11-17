package com.navinfo.opentsp.user.dal.mybatis.mapper;

import com.navinfo.opentsp.user.dal.entity.RegisterStaticsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RegisterStaticsEntityMapper extends MybatisMapper<RegisterStaticsEntity, String>{

    /**
     * 分析一端时间内的每个产品的每个渠道每天的注册量
     * @param dayStart
     * @param dayEnd
     * @return
     */
    int analyzeRegister(@Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    /**
     * 分析一端时间内， 指定产品指定渠道的每天的注册量
     * @param product
     * @param registerSrc
     * @param dayStart
     * @param dayEnd
     * @return
     */
    int analyzeOne(@Param("product") String product, @Param("registerSrc") String registerSrc,
                   @Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    /**
     * 删除分析的结果
     * @param product
     * @param registerSrc
     * @param dayStart
     * @param dayEnd
     * @return
     */
    int deleteAnalyzer(@Param("product") String product, @Param("registerSrc") String registerSrc,
                   @Param("dayStart") Date dayStart, @Param("dayEnd") Date dayEnd);

    List<Map<String,Object>> queryList(@Param("product") String product, @Param("date") String date);

    /**
     * 查询统计结果
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Object>> queryStatics(@Param("product") String product, @Param("startDate") String startDate,
                                           @Param("endDate") String endDate, @Param("column") String column);
}