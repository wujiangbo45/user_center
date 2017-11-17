package com.navinfo.opentsp.user.web.backend;

import com.navinfo.opentsp.user.dal.entity.AppIDKeyEntity;
import com.navinfo.opentsp.user.service.appinfo.AppInfoService;
import com.navinfo.opentsp.user.service.param.backend.DeleteAppInfoParam;
import com.navinfo.opentsp.user.service.param.backend.SaveAppEntityParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-09-29
 * @modify
 * @copyright Navi Tsp
 */
@Profile("enable-backend")
@RestController
@RequestMapping("/backend/appInfo")
public class AppInfoController implements InitializingBean{

    @Autowired
    private AppInfoService appInfoService;

    @RequestMapping("/save")
    public CommonResult<String> save(@RequestBody SaveAppEntityParam entityParam){
        if (this.appInfoService.findAppInfo(entityParam.getThirdType(), entityParam.getProductId()) != null) {
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR).setMessage("第三方信息已存在！");
        }

        AppIDKeyEntity entity = new AppIDKeyEntity();
        entity.setAppid(entityParam.getAppid());
        entity.setAppkey(entityParam.getAppkey());
        entity.setProductId(entityParam.getProductId());
        entity.setThirdName(entityParam.getThirdName());
        entity.setThirdType(entityParam.getThirdType());
        appInfoService.save(entity);
        return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
    }

    @RequestMapping("/query")
    public CommonResult<List<AppIDKeyEntity>> query() {
        return new CommonResult<List<AppIDKeyEntity>>().fillResult(ResultCode.SUCCESS).setData(appInfoService.findAll());
    }

    @RequestMapping("/delete")
    public CommonResult<String> delete(@RequestBody DeleteAppInfoParam param) {
        if (this.appInfoService.findAppInfo(param.getThirdType(), param.getProduct()) == null) {
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR).setMessage("第三方信息不存在!");
        }
        this.appInfoService.delete(param.getProduct(), param.getThirdType());
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
