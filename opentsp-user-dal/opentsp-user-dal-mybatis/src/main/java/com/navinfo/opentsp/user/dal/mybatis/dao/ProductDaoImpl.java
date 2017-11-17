package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.dao.ProductDao;
import com.navinfo.opentsp.user.dal.entity.ProductEntity;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import com.navinfo.opentsp.user.dal.mybatis.mapper.ProductEntityMapper;
import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Repository
@Mybatis
public class ProductDaoImpl extends BaseDAOImpl<ProductEntity, String> implements ProductDao {

    @Autowired
    private ProductEntityMapper mapper;

    @Override
    protected MybatisMapper<ProductEntity, String> getMapper() {
        return mapper;
    }

    @Override
    public List<ProductEntity> findAll() {
        return this.mapper.findAll();
    }
}
