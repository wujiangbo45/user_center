package com.navinfo.opentsp.user.service.product;

import com.navinfo.opentsp.user.dal.dao.ProductDao;
import com.navinfo.opentsp.user.dal.entity.ProductEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Profile("enable-backend")
@Service
public class ProductServiceImpl2 implements ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl2.class);

    @Autowired
    private ProductDao productDao;

    @Override
    public ProductEntity getProduct(String productId) {
        return this.productDao.findById(productId);
    }

    @Override
    public List<ProductEntity> findAll() {
        return this.productDao.findAll();
    }

    @Override
    public void save(ProductEntity entity) {
        this.productDao.save(entity);
    }

    @Override
    public void update(ProductEntity entity) {
        this.productDao.updateByIdSelective(entity);
    }

    @Override
    public void delete(String id) {
        if (StringUtils.isEmpty(id))
            return;

        int i = this.productDao.deleteById(id);
    }

}
