package com.navinfo.opentsp.user.service.valimpl;

import com.navinfo.opentsp.user.common.util.constants.GlobalConstans;
import com.navinfo.opentsp.user.dal.entity.ProductEntity;
import com.navinfo.opentsp.user.service.product.ProductService;
import com.navinfo.opentsp.user.service.validator.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-10
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class ProductImplmentor implements ValidatorImplmentor{
    private static final Logger logger = LoggerFactory.getLogger(ProductImplmentor.class);

    @Autowired
    private ProductService productService;

    @Override
    public boolean doValidate(Object arg) {
        if (arg instanceof String) {
           String product = (String) arg;
            if(!StringUtils.isEmpty(product)) {
                ProductEntity entity = productService.getProduct(product);
                if (entity != null && entity.getStatus() == GlobalConstans.IS_VALID_N) {//对于产品渠道，0是启用，1是禁用
                    return true;
                }
            }
        }

        logger.debug("invalid product : {}", arg);
        return false;
    }

    @Override
    public Class supportValidation() {
        return Product.class;
    }
}
