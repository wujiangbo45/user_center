package com.navinfo.opentsp.user.dal.dao;

import com.navinfo.opentsp.user.dal.entity.RegisterStaticsEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface RegisterStaticsDao extends BaseDAO<RegisterStaticsEntity, String> {

    public int analyzeRegister(Date dayStart, Date dayEnd);

    public int analyzeOne(String product, String registerSrc, Date dayStart, Date dayEnd);

    public int deleteAnalyzer(Date dayStart, Date dayEnd, String product, String registerSrc);

    /**
     * 后端页面的那个统计列表
     * @param product
     * @return
     */
    List<Map<String, Object>> queryList(String product, String date);

    /**
     * 查询折线图
     * @param startDate
     * @param endDate
     * @param column
     * @return
     */
    List<Map<String, Object>> query4Statics(String product, String startDate, String endDate, String column);

}
