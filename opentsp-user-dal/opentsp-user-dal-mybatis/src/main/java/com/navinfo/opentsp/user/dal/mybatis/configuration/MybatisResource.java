package com.navinfo.opentsp.user.dal.mybatis.configuration;

import org.springframework.core.io.Resource;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-28
 * @modify
 * @copyright Navi Tsp
 */
public class MybatisResource {

    private Resource[] mapperResources;

    public MybatisResource(){}

    public MybatisResource(Resource[] mapperResources){
        this.mapperResources = mapperResources;
    }

    public Resource[] getMapperResources() {
        return mapperResources;
    }

    public void setMapperResources(Resource[] mapperResources) {
        this.mapperResources = mapperResources;
    }
}
