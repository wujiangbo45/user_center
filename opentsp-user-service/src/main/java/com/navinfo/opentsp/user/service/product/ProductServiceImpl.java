package com.navinfo.opentsp.user.service.product;

import com.navinfo.opentsp.user.dal.dao.ProductDao;
import com.navinfo.opentsp.user.dal.entity.ProductEntity;
import com.navinfo.opentsp.user.service.cache.CacheReloadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Profile("!enable-backend")
@Service
public class ProductServiceImpl implements ProductService, CacheReloadable, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private static Map<String, ProductEntity> cache = new HashMap<>();
    //TODO use redis instead
    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    @Autowired
    private ProductDao productDao;

    @Override
    public ProductEntity getProduct(String productId) {
        lock.readLock().lock();
        try {
            return cache.get(productId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<ProductEntity> findAll() {
        return new LinkedList<>(cache.values());
    }

    @Override
    public void save(ProductEntity entity) {
        this.productDao.save(entity);
        lock.writeLock().lock();
        try {
            cache.put(entity.getProductId(), entity);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void update(ProductEntity entity) {
        this.productDao.updateByIdSelective(entity);
        lock.writeLock().lock();
        try {
            cache.put(entity.getProductId(), this.productDao.findById(entity.getProductId()));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isEmpty(id))
            return;

        int i = this.productDao.deleteById(id);
        lock.writeLock().lock();
        try {
            if (i > 0)
                cache.remove(id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void reload() {
        logger.info("reloading cached product information !");
        List<ProductEntity> entities = productDao.findAll();
        if(entities.size() == 0)
            return;

        lock.writeLock().lock();
        try {
            cache.clear();
            for(ProductEntity entity : entities) {
                cache.put(entity.getProductId(), entity);
            }
        } finally {
            lock.writeLock().unlock();
        }
        logger.info("reloading cached product information success !");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reload();
    }
}
