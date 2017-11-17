package com.navinfo.opentsp.user.dal.dao;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public interface Identified<ID> {

    public void generateID();

    public void setId(ID id);

    public ID getId();
}
