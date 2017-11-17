package com.navinfo.opentsp.user.dal.mybatis.dao;

import com.navinfo.opentsp.user.dal.dao.BaseDAO;
import com.navinfo.opentsp.user.dal.dao.Identified;
import com.navinfo.opentsp.user.dal.mybatis.mapper.MybatisMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
public abstract class BaseDAOImpl<T extends Identified<ID>, ID> implements BaseDAO<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(BaseDAOImpl.class);

    @Override
    public ID save(T entity) {
        if(entity.getId() == null || "".equals(entity.getId()))
            entity.generateID();
        int i = this.getMapper().insert(entity);
        logger.debug("save class {}, effected rows {}", entity.getClass().getName(), i);
        return entity.getId();
    }

    @Override
    public T findById(ID id) {
        return this.getMapper().selectByPrimaryKey(id);
    }

    @Override
    public int updateById(T entity) {
        return this.getMapper().updateByPrimaryKey(entity);
    }

    @Override
    public int updateByIdSelective(T entity) {
        return this.getMapper().updateByPrimaryKeySelective(entity);
    }

    @Override
    public int deleteById(ID id) {
        return this.getMapper().deleteByPrimaryKey(id);
    }

    protected abstract MybatisMapper<T, ID> getMapper();
}
