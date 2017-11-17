package com.navinfo.opentsp.user.service.appinfo;

import com.navinfo.opentsp.user.dal.dao.AppIDKeyDao;
import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.service.cache.CacheReloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-16
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class AppInfoServiceImpl implements AppInfoService, InitializingBean, CacheReloadable {
    private static final Logger logger = LoggerFactory.getLogger(AppInfoServiceImpl.class);
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    //TODO use redis instead
    private static final Map<String, AppIDKeyEntity> cache = new HashMap<>();

    @Autowired
    private AppIDKeyDao appDao;

    @Override
    public AppIDKeyEntity findAppInfo(String thirdType, String product) {
        String key = product + "." + thirdType;
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void reload() {
        logger.info("reloading appid and appkey info....");
        List<AppIDKeyEntity> list = this.appDao.findAll();
        lock.writeLock().lock();
        try {
            for(AppIDKeyEntity appIDKeyEntity : list) {
                cache.put(appIDKeyEntity.getProductId() + "." + appIDKeyEntity.getThirdType(), appIDKeyEntity);
            }
        } finally {
            lock.writeLock().unlock();
        }
        logger.info("reloading appid and appkey info success !");
    }

    @Override
    public void save(AppIDKeyEntity entity) {
        this.appDao.save(entity);
        lock.writeLock().lock();
        try {
            cache.put(entity.getProductId() + "." + entity.getThirdType(), entity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<AppIDKeyEntity> findAll() {
        return new LinkedList<>(cache.values());
    }

    @Override
    public void delete(String product, String thirdType) {
        AppIDKeyEntity entity = this.findAppInfo(thirdType, product);
        if (entity == null) {
            return;
        }

        lock.writeLock().lock();
        try {
            cache.remove(entity.getProductId() + "." + entity.getThirdType());
        } finally {
            lock.writeLock().unlock();
        }

        this.appDao.deleteById(entity.getId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reload();
    }
}
