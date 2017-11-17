package com.navinfo.opentsp.user.service.var;

import com.navinfo.opentsp.user.dal.dao.ConfigPropertyDao;
import com.navinfo.opentsp.user.dal.entity.ConfigPropertyEntity;
import com.navinfo.opentsp.user.service.cache.CacheReloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * service for system email
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-10-08
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class ConfigPropertyServiceImpl implements ConfigPropertyService, InitializingBean, CacheReloadable {
    private static final Logger logger = LoggerFactory.getLogger(ConfigPropertyServiceImpl.class);

    private static final Map<String, ConfigPropertyEntity> map = new HashMap<>();
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private ConfigPropertyDao dao;

    @Override
    public String getValue(String name, String product, ConfigPropertyEntity.Types type) {
        ConfigPropertyEntity entity = null;

        lock.readLock().lock();
        try {
            entity = map.get(this.keys(name, product, type));
            if(entity == null) {// if this product has no email, try default product
                entity = map.get(this.keys(name, ConfigPropertyEntity.DEFAULT_PRODUCT, type));
            }
        } finally {
            lock.readLock().unlock();
        }

        if(entity != null) {
            return StringUtils.isEmpty(entity.getPropValue()) ? entity.getDefaultVal() : entity.getPropValue();
        }

        return null;
    }

    @Override
    public String getValue(String name, ConfigPropertyEntity.Types type) {
        return this.getValue(name, ConfigPropertyEntity.DEFAULT_PRODUCT, type);
    }

    private String keys(String name, String product, ConfigPropertyEntity.Types type){
        return new StringBuilder(name).append(".").append(product).append(".").append(type.name()).toString();
    }

    @Override
    public void reload() {
        logger.info("prepare to reloading ... ");
        lock.writeLock().lock();
         try {
            map.clear();
             List<ConfigPropertyEntity> entities = this.dao.findAll();
             for(ConfigPropertyEntity entity : entities) {
                map.put(this.keys(entity.getPropName(), entity.getProductId(),
                        Enum.valueOf(ConfigPropertyEntity.Types.class, entity.getType())), entity);
             }
         } finally {
             lock.writeLock().unlock();
         }
        logger.info("reloading complete ! ");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reload();
    }
}
