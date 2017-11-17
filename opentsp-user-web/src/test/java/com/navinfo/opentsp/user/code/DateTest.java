package com.navinfo.opentsp.user.code;

import org.junit.Test;

import java.util.Calendar;

/**
 * @author wupeng
 * @version 1.0
 * @date 2016-01-11
 * @modify
 * @copyright Navi Tsp
 */
public class DateTest {

    @Test
    public void test() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setMinimalDaysInFirstWeek(7);
        int i = calendar.get(Calendar.WEEK_OF_YEAR);
        System.out.println(i);


    }

}
