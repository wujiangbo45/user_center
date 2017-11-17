package com.navinfo.opentsp.user.dal.dao;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface BaseDAO<T, ID> {

    /**
     * insert
     * @param entity
     * @return
     */
    public ID save(T entity);

    /**
     * find one by id
     * @param id
     * @return
     */
    public T findById(ID id);

    /**
     * update a entity
     * @param entity
     * @return
     */
    public int updateById(T entity);

    /**
     * update not null properties
     * @param entity
     * @return
     */
    public int updateByIdSelective(T entity);

    public int deleteById(ID id);

}
