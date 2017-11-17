package com.navinfo.opentsp.user.service.product;

import com.navinfo.opentsp.user.dal.entity.ProductEntity;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
public interface ProductService {

    public ProductEntity getProduct(String productId);

    public List<ProductEntity> findAll();

    public void save(ProductEntity entity);

    public void update(ProductEntity entity);

    public void delete(String id);

}
