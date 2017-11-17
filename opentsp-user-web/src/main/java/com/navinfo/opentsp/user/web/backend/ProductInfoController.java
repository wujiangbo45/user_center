package com.navinfo.opentsp.user.web.backend;

import com.navinfo.opentsp.user.dal.entity.ProductEntity;
import com.navinfo.opentsp.user.service.param.backend.DeleteProductParam;
import com.navinfo.opentsp.user.service.param.backend.SaveProductParam;
import com.navinfo.opentsp.user.service.param.backend.UpdateProductParam;
import com.navinfo.opentsp.user.service.product.ProductService;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * Created by wupeng on 11/3/15.
 */
@Profile("enable-backend")
@RestController
@RequestMapping("/backend/product")
public class ProductInfoController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/save")
    public CommonResult<String> save(@RequestBody SaveProductParam param) {
        if (this.productService.getProduct(param.getProductId()) != null ) {
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR).setMessage("产品已存在！");
        }

        ProductEntity entity = new ProductEntity();
        entity.setProductId(param.getProductId());
        entity.setDescription(param.getDescription());
        entity.setProductName(param.getProductName());

        this.productService.save(entity);
        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }


    @RequestMapping("/query")
    public CommonResult<List<ProductEntity>> find() {
        return new CommonResult<List<ProductEntity>>().fillResult(ResultCode.SUCCESS).setData(this.productService.findAll());
    }

    @RequestMapping("/update")
    public CommonResult<String> update(@RequestBody UpdateProductParam param) {
        ProductEntity entity = this.productService.getProduct(param.getProductId());
        if (entity == null)
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR).setMessage("product no exists !");

        if (StringUtils.isEmpty(param.getProductName()) && StringUtils.isEmpty(param.getDescription()))
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR).setMessage("nothing to change !");

        if (!StringUtils.isEmpty(param.getDescription()))
            entity.setDescription(param.getDescription());
        if (!StringUtils.isEmpty(param.getProductName()))
            entity.setProductName(param.getProductName());
        entity.setStatus(param.getStatus());

        this.productService.update(entity);
        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

    @RequestMapping("/delete")
    public CommonResult<String> delete(@RequestBody DeleteProductParam param) {
        this.productService.delete(param.getId());
        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

}
