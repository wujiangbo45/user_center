package com.navinfo.opentsp.user.web.controller;

import com.navinfo.opentsp.user.dal.entity.LocationEntity;
import com.navinfo.opentsp.user.service.location.LocationService;
import com.navinfo.opentsp.user.service.param.open.QueryLocationParam;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.open.SnameMappingResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-11-05
 * @modify
 * @copyright Navi Tsp
 */
@RestController
@RequestMapping("/location")
public class LocationController implements InitializingBean{
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    private List<SnameMappingResult> cache;

    @RequestMapping(value = "/query", method = {RequestMethod.GET}, produces = {"application/json; charset=utf-8"})
    public CommonResult<List<LocationEntity>> query(QueryLocationParam param) {
        return new CommonResult<List<LocationEntity>>().fillResult(ResultCode.SUCCESS).setData(this.locationService.findCity(param.getParentId(), param.getName()));
    }

    @RequestMapping("/lisenceNoMappings")
    public CommonResult<List<SnameMappingResult>> lisenceNoMappings() {
        return new CommonResult<List<SnameMappingResult>>().fillResult(ResultCode.SUCCESS).setData(Collections.unmodifiableList(cache));
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        BufferedReader br = null;
        InputStream is = null;
        List<SnameMappingResult> list = new LinkedList<>();
        try {
            Resource resource = new ClassPathResource("city/sname_mapping.txt");
            is = resource.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                String[] name = str.split("=");
                list.add(new SnameMappingResult(name[0], name[1]));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (is != null)
                is.close();
            if (br != null)
                br.close();
        }

        cache = Collections.unmodifiableList(list);
    }
}
