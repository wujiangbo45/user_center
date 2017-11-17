package com.navinfo.opentsp.user.dal.dao;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
public interface BaseMongoDao<T, TD> extends BaseDAO<T, TD> {

    public List<T> findByExample(T example);

    public List<T> findByExampleSelective(T example);

}
