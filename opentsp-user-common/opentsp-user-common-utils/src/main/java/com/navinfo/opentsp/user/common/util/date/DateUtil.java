package com.navinfo.opentsp.user.common.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    public static final String date_pattern = "yyyy-MM-dd";
    public static final String month_pattern = "yyyy-MM";
    public static final String year_pattern = "yyyy";
    public static final String time_pattern = "yyyy-MM-dd HH:mm:ss";

    public static String formatDate(Date date) {
        if (date == null)
            return null;
        return new SimpleDateFormat(date_pattern).format(date);
    }

    public static String format(String pattern, Date date) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static String formatTime(Date date) {
        if (date == null)
            return "";
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static Date parseDate(String datestr) {
        try {
            return new SimpleDateFormat(date_pattern).parse(datestr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Date parseTime(String time) {
        Date result = null;
        if (time == null || "".equals(time)) {
            return null;
        }

        try {
            result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Date add(Date date, int mount, int field) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(field, mount);
        return cal.getTime();
    }

    /**
     * date 2 - date1
     *
     * @param date1
     * @param date2
     * @param type  {@link java.util.Calendar}
     * @return
     */
    public static int diff(Date date1, Date date2, int type) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        int d1 = cal.get(type);
        cal.setTime(date2);
        return cal.get(type) - d1;
    }

    public static String timeStr(long time) {
        Date date = new Date(time);
        return getDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String timeStr2(long time) {
        Date date = new Date(time);
        return getDateFormat(date_pattern).format(date);
    }

    public static SimpleDateFormat getDateFormat(String pattern) {
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        sim.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
//		sim.setTimeZone(TimeZone.getTimeZone("Asia/ShangHai"));
        return sim;
    }

    /**
     * 字符串转LONG
     *
     * @param time
     * @return
     */
    public static Long strTimeChangeLong(String time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calBegin = new GregorianCalendar();
        try {
            calBegin.setTime(format.parse(time));
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        long beginTime = calBegin.getTimeInMillis();
        return beginTime;
    }

    public static final List<String> betweenDays(String startDate, String endDate, boolean containsStart, boolean containsEnd) {
        List<String> list = new ArrayList<>();
        if (containsStart)
            list.add(startDate);
        Date date1 = parseDate(startDate);
        Date date2 = parseDate(endDate);
        if (date1.getTime() > date2.getTime()) {
            throw new IllegalArgumentException("start date must be less than or equals with end date !");
        } else if (date1.getTime() == date2.getTime()) {
            if (containsEnd)
                list.add(endDate);
            return list;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
//        calendar.setFirstDayOfWeek(Calendar.MONDAY);
//        calendar.setMinimalDaysInFirstWeek(7);
        String date = startDate;
        while (!endDate.equals(date)) {
            if (!startDate.equals(date)) {
                list.add(date);
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
            date = formatDate(calendar.getTime());
        }

        if (containsEnd)
            list.add(endDate);
        return list;
    }

    public static final List<Integer> betweenWeeks(String startDate, String endDate, boolean containsStart, boolean containsEnd) {
        List<Integer> list = new ArrayList<>();

        Date date1 = parseDate(startDate);
        Date date2 = parseDate(endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);

        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        calendar.setTime(date2);
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);

        if (containsStart) {
            list.add(startWeek);
        }

        if (date1.getTime() > date2.getTime()) {
            throw new IllegalArgumentException("start date must be less than or equals with end date !");
        } else if (date1.getTime() == date2.getTime()) {
            if (containsEnd)
                list.add(endWeek);
            return list;
        }

        calendar.setTime(date1);
        int i = startWeek + 1;
        while (i != endWeek) {
            if (i >= calendar.getMaximum(Calendar.WEEK_OF_YEAR)) {
                i = 0;
            } else {
                list.add(i);
            }
            i++;
        }

        if (containsEnd)
            list.add(endWeek);
        return list;
    }

    public static List<String> betweenMonths(String startDate, String endDate, boolean containsStart, boolean containsEnd) {

        List<String> list = new ArrayList<>();
        Date date1 = parseDate(startDate);
        Date date2 = parseDate(endDate);
        String startMonth = format(month_pattern, date1);
        String endMonth = format(month_pattern, date2);

        if (containsStart) {
            list.add(startMonth);
        }

        if (date1.getTime() > date2.getTime()) {
            throw new IllegalArgumentException("start date must be less than or equals with end date !");
        } else if (date1.getTime() == date2.getTime()) {
            if (containsEnd)
                list.add(endMonth);
            return list;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        String month = startMonth;
        while (!endMonth.equals(month)) {
            if (!startMonth.equals(month)) {
                list.add(month);
            }

            calendar.add(Calendar.MONTH, 1);
            month = format(month_pattern, calendar.getTime());
        }

        if (containsEnd)
            list.add(endMonth);
        return list;
    }

    public static List<Integer> betweenYear(String startDate, String endDate, boolean containsStart, boolean containsEnd) {
        List<Integer> list = new ArrayList<>();
        Date date1 = parseDate(startDate);
        Date date2 = parseDate(endDate);
        int startYear = Integer.valueOf(format(year_pattern, date1));
        int endYear = Integer.valueOf(format(year_pattern, date2));

        if (containsStart) {
            list.add(startYear);
        }

        if (date1.getTime() > date2.getTime()) {
            throw new IllegalArgumentException("start date must be less than or equals with end date !");
        } else if (date1.getTime() == date2.getTime()) {
            if (containsEnd)
                list.add(endYear);
            return list;
        }

        for (int i = startYear + 1; i < endYear; i++ ) {
            list.add(i);
        }

        if (containsEnd)
            list.add(endYear);
        return list;
    }

    public static void main(String[] args) {

        System.out.println(format("M月d日", parseDate("2014-12-24")));

        System.out.println(Arrays.toString(betweenDays("2016-01-02", "2016-01-22", true, false).toArray(new String[0])));
        System.out.println(Arrays.toString(betweenWeeks("2016-01-02", "2016-01-22", true, true).toArray(new Integer[0])));
        System.out.println(Arrays.toString(betweenMonths("2015-01-02", "2016-01-22", true, true).toArray(new String[0])));
        System.out.println(Arrays.toString(betweenYear("2012-01-02", "2016-01-22", true, true).toArray(new Integer[0])));

    }
}