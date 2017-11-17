package com.navinfo.opentsp.user.service.statics;

import com.navinfo.opentsp.user.common.util.date.DateUtil;
import com.navinfo.opentsp.user.dal.dao.RegisterStaticsDao;
import com.navinfo.opentsp.user.service.device.DeviceType;
import com.navinfo.opentsp.user.service.param.statics.QueryRegisterListParam;
import com.navinfo.opentsp.user.service.param.statics.QueryStaticsParam;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryListResult;
import com.navinfo.opentsp.user.service.resultdto.backend.QueryStaticsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wupeng
 * @version 1.0
 * @date 2015-10-28
 * @modify
 * @copyright Navi Tsp
 */
@Service
public class RegisterStaticsService {

    @Autowired
    private RegisterStaticsDao registerStaticsDao;

    public int analyzeRegister(Date dayStart, Date dayEnd, boolean override) {
        Assert.notNull(dayStart);
        Assert.notNull(dayEnd);

        if(override) {
            this.registerStaticsDao.deleteAnalyzer(dayStart, dayEnd, null, null);
        }

        return this.registerStaticsDao.analyzeRegister(dayStart, dayEnd);
    }

    public int analyzeRegister(String product, String registerSrc, Date dayStart, Date dayEnd, boolean override) {
        Assert.notNull(dayStart, "start time can not be null !");
        Assert.notNull(dayEnd, "end time can not be null !");
        Assert.notNull(product, "product can not be null !");

        if(override) {
            this.registerStaticsDao.deleteAnalyzer(dayStart, dayEnd, product, registerSrc);
        }

        return this.registerStaticsDao.analyzeOne(product, registerSrc, dayStart, dayEnd);
    }

    public QueryListResult.ListItem parse(List<Map<String, Object>> mapList) {
        QueryListResult.ListItem item = new QueryListResult.ListItem();

        for (Map<String, Object> map : mapList) {
            String type = String.valueOf(map.get("register_src"));
            String num = String.valueOf(map.get("num"));
            int value = StringUtils.isEmpty(num) ? 0 : Integer.valueOf(num);
            switch (type) {
                case "web" :
                    item.setWeb(value);
                    break;
                case "tbox" :
                    item.setTbox(value);
                    break;
                case "android" :
                case "ios" :
                    item.setMobile(item.getMobile() + value);
                    break;
                default :
            }
        }

        return item;
    }

    public QueryListResult queryListResult(QueryRegisterListParam param) {
        QueryListResult result = new QueryListResult();

        List<Map<String, Object>> mapList = registerStaticsDao.queryList(param.getProduct(), null);
        result.setTotal(this.parse(mapList));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, -1);

        mapList = registerStaticsDao.queryList(param.getProduct(), sdf.format(calendar.getTime()));
        result.setYesterday(this.parse(mapList));

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        mapList = registerStaticsDao.queryList(param.getProduct(), sdf.format(calendar.getTime()));
        result.setBeforeYesterday(this.parse(mapList));

        return result;
    }

    public QueryStaticsResult queryStaticsResult(QueryStaticsParam param) {
        String column = null;
        if (!StringUtils.isEmpty(param.getUnit())) {
            switch (param.getUnit()) {//day, week, month, year
                case QueryStaticsParam.day:
                    column = "register_date";
                    break;
                case QueryStaticsParam.week:
                    column = "register_week";
                    break;
                case QueryStaticsParam.month:
                    column = "register_month";
                    break;
                case QueryStaticsParam.year:
                    column = "register_year";
                    break;
                default:
                    column = "register_date";
            }
        } else {
            column = "register_date";
        }

        QueryStaticsResult result = new QueryStaticsResult();
        List<Map<String, Object>> mapList = this.registerStaticsDao.query4Statics(param.getProduct(), param.getStartDate(), param.getEndDate(), column);

        List<QueryStaticsResult.StaticsItem> staticsItems = new LinkedList<>();
        for (DeviceType deviceType : DeviceType.values()) {
            QueryStaticsResult.StaticsItem item = new QueryStaticsResult.StaticsItem();
            item.setName(deviceType.name());
            staticsItems.add(item);
        }

        Map<String, Map<String, Integer>> mapMap = this.parseResult4Statics(mapList);
        for (Map<String, Object> listItemMap : mapList) {
            String xName = String.valueOf(listItemMap.get("x_name"));
            result.addXAxis(xName);

            Map<String, Integer> map = mapMap.get(xName);
            for (QueryStaticsResult.StaticsItem item : staticsItems) {
                Integer value = map.get(item.getName());
                item.addData(value == null ? 0 : value);
            }
        }

        result.setSeries(staticsItems);
        this.fillEmptyData(result, param, param.getUnit());
        return result;
    }

    private void fillEmptyData(QueryStaticsResult result, QueryStaticsParam param, String column) {
        List<String> all = new ArrayList<>();

        boolean containsStart = true;
        boolean containsEnd = true;
        switch (column) {//day, week, month, year
            case QueryStaticsParam.day:
                List<String> days = DateUtil.betweenDays(param.getStartDate(), param.getEndDate(), containsStart, containsEnd);
                all.addAll(days);
                break;
            case QueryStaticsParam.week:
                List<Integer> weeks = DateUtil.betweenWeeks(param.getStartDate(), param.getEndDate(), containsStart, containsEnd);
                for (Integer i : weeks) {
                    all.add(String.valueOf(i));
                }
                break;
            case QueryStaticsParam.month:
                List<String> months = DateUtil.betweenMonths(param.getStartDate(), param.getEndDate(), containsStart, containsEnd);
                all.addAll(months);
                break;
            case QueryStaticsParam.year:
                List<Integer> years = DateUtil.betweenYear(param.getStartDate(), param.getEndDate(), containsStart, containsEnd);
                for (Integer i : years) {
                    all.add(String.valueOf(i));
                }
                break;
            default:
                throw new IllegalArgumentException("illegal argument of column !! ");
        }

        for (int i = 0; i < all.size(); i++) {
            String item = all.get(i);
            if (result.getxAxis().contains(item)) {
                continue;
            } else {
                if (i == all.size() - 1) {
                    result.getxAxis().add(item);
                    for (QueryStaticsResult.StaticsItem staticsItem : result.getSeries()) {
                        staticsItem.getData().add(0);
                    }
                } else {
                    result.getxAxis().add(i, item);
                    for (QueryStaticsResult.StaticsItem staticsItem : result.getSeries()) {
                        staticsItem.getData().add(i, 0);
                    }
                }
            }
        }
    }

    //              date,   register_src, count
    private Map<String, Map<String, Integer>> parseResult4Statics(List<Map<String, Object>> mapList) {
        Map<String, Map<String, Integer>> mapMap = new HashMap<>();
        for (Map<String, Object> map : mapList) {
            String xName = String.valueOf(map.get("x_name"));
            Map<String, Integer> map1 = mapMap.get(xName);
            if (map1 == null) {
                map1 = new HashMap<>();
                mapMap.put(xName, map1);
            }

            String valueStr = String.valueOf(map.get("num"));
            Integer count = 0;
            if (!StringUtils.isEmpty(valueStr)) {
                count = Integer.valueOf(valueStr);
            }

            map1.put(String.valueOf(map.get("register_src")), count);
        }

        return mapMap;
    }

}
