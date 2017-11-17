package com.navinfo.opentsp.user.web.backend;

import com.navinfo.opentsp.user.common.util.date.DateUtil;
import com.navinfo.opentsp.user.service.param.statics.AnalyzeStaticsParam;
import com.navinfo.opentsp.user.service.param.statics.QueryRegisterListParam;
import com.navinfo.opentsp.user.service.param.statics.QueryStaticsParam;
import com.navinfo.opentsp.user.service.product.ProductService;
import com.navinfo.opentsp.user.service.result.CommonResult;
import com.navinfo.opentsp.user.service.result.ResultCode;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryListResult;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryStaticsResult;
import com.navinfo.opentsp.user.service.statics.RegisterStaticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-28
 * @modify
 * @copyright Navi Tsp
 */
@Profile("enable-backend")
@RestController
@RequestMapping("/backend/statics")
public class StaticsController {
    private static final Logger logger = LoggerFactory.getLogger(StaticsController.class);

    @Autowired
    private RegisterStaticsService staticsService;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/analyze", produces = {"application/json; charset=utf-8"})
    public CommonResult<String> analyze(AnalyzeStaticsParam param) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Date dayStart = sdf.parse(param.getDayStart());
        Date dayEnd = sdf.parse(param.getDayEnd());
        boolean override = "Y".equalsIgnoreCase(param.getOverride());
        int i = 0;

        String message = null;
        try {
            if(StringUtils.isEmpty(param.getProduct())) {
                i = staticsService.analyzeRegister(dayStart, dayEnd, override);
            } else {
                i = staticsService.analyzeRegister(param.getProduct(), param.getSrc(), dayStart, dayEnd, override);
            }

            message = String.valueOf(i);
        } catch (Exception e ) {
            message = e.getMessage();
            logger.error(e.getMessage(), e);
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class).setData(message);
    }

    @RequestMapping(value = "/queryList", produces = {"application/json; charset=utf-8"})
    public CommonResult<QueryListResult> list(@RequestBody QueryRegisterListParam param) {
        if (!StringUtils.isEmpty(param.getProduct()) && productService.getProduct(param.getProduct()) == null) {
            return new CommonResult<QueryListResult>().fillResult(ResultCode.BAD_REQUEST);
        }
        return new CommonResult<QueryListResult>().fillResult(ResultCode.SUCCESS).setData(staticsService.queryListResult(param));
    }

    @RequestMapping(value = "/query", produces = {"application/json; charset=utf-8"})
    public CommonResult<QueryStaticsResult> queryStatics(@RequestBody QueryStaticsParam param) {
        Date date1 = DateUtil.parseDate(param.getStartDate());
        Date date2 = DateUtil.parseDate(param.getEndDate());

        if (date1 == null || date2 == null || (date1.getTime() > date2.getTime())) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, QueryStaticsResult.class);
        }

        return new CommonResult<QueryStaticsResult>().fillResult(ResultCode.SUCCESS).setData(this.staticsService.queryStaticsResult(param));
    }

}
