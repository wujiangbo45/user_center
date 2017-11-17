package com.navinfo.opentsp.user.service.valimpl;

import com.navinfo.opentsp.user.dal.dao.CommonSmsProductDao;
import com.navinfo.opentsp.user.dal.entity.CommomSmsProductEntity;
import com.navinfo.opentsp.user.service.validator.CommSmsProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-28
 * @modify
 * @copyright Navi Tsp
 */
@Component
public class CommonSmsProductImplementor implements ValidatorImplmentor, InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(CommonSmsProductImplementor.class);

    @Value("${comm.sms.products:}")
    private String products;

    @Autowired
    private CommonSmsProductDao productDao;

    private Set<String> productSet;

    @Override
    public boolean doValidate(Object arg) {
        if (arg == null)
            return false;

        String product = String.valueOf(arg);
        if (!productSet.contains(product)) {
            if (productDao.findById(product) == null) {
                logger.info("product dose not exists !");
                return false;
            }
        }
        return true;
    }

    @Override
    public Class supportValidation() {
        return CommSmsProduct.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        productSet = new HashSet<>();
        productSet.addAll(Arrays.asList(products.split(",")));
        for (String product : productSet) {
            if (productDao.findById(product) == null) {
                CommomSmsProductEntity entity = new CommomSmsProductEntity();
                entity.setProductId(product);
                entity.setCreateTime(new Date());
                entity.setProductName(product);
                entity.setDescription("auto created according config file !");
                productDao.save(entity);
            }
        }
    }
}
