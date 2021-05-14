package com.vekai.workflow.nopub.kit;

import org.springframework.stereotype.Component;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 11:41 20/06/2018
 */
@Component
public class DateTools {

//    //获取本周的开始时间
//    public static Date getMondayOfWeek(Date curDate) {
//        Calendar cal = Calendar.getInstance();
//        if (null != curDate)
//            cal.setTime(curDate);
//        cal.setTime(curDate);
//        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
//        if (dayOfWeek == 1) {
//            dayOfWeek += 7;
//        }
//        cal.add(Calendar.DATE, 2 - dayOfWeek);
//        return getDayStartTime(cal.getTime());
//    }
//
//    //获取本周的结束时间
//    public static Date getSundayOfWeek(Date curDate) {
//        Calendar cal = Calendar.getInstance();
//        if (null != curDate)
//            cal.setTime(curDate);
//        cal.setTime(getMondayOfWeek(curDate));
//        cal.add(Calendar.DAY_OF_WEEK, 6);
//        Date weekEndSta = cal.getTime();
//        return getDayEndTime(weekEndSta);
//    }
//
//    //获取本月的开始时间
//    public static Date getFirstDayOfMonth(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (null != date)
//            calendar.setTime(date);
//        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, 1);
//        return getDayStartTime(calendar.getTime());
//    }
//
//    //获取本月的结束时间
//    public static Date getLastDayOfMonth(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (null != date)
//            calendar.setTime(date);
//        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, 1);
//        int day = calendar.getActualMaximum(5);
//        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, day);
//        return getDayEndTime(calendar.getTime());
//    }
//
//    //获取某个日期的开始时间
//    private static Date getDayStartTime(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (null != date)
//            calendar.setTime(date);
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return new Timestamp(calendar.getTimeInMillis());
//    }
//
//
//    //获取某个日期的结束时间
//    private static Date getDayEndTime(Date date) {
//        Calendar calendar = Calendar.getInstance();
//        if (null != date)
//            calendar.setTime(date);
//        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//            calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
//        calendar.set(Calendar.MILLISECOND, 999);
//        return new Timestamp(calendar.getTimeInMillis());
//    }
//
//
//
//    //获取某年某月的第一天日期
//    public static Date getStartMonthDate(int year, int month) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month - 1, 1);
//        return getDayStartTime(calendar.getTime());
//    }
//
//    //获取某年某月的最后一天日期
//    public static Date getEndMonthDate(int year, int month) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month - 1, 1);
//        int day = calendar.getActualMaximum(5);
//        calendar.set(year, month - 1, day);
//        return getDayEndTime(calendar.getTime());
//    }
//
//
//
//    //获取本年的开始时间
//    public static Date getBeginDayOfYear(Date date) {
//        Calendar cal = Calendar.getInstance();
//        if (null != date)
//            cal.setTime(date);
//        cal.set(Calendar.YEAR, DateKit.getYear(date));
//        cal.set(Calendar.MONTH, Calendar.JANUARY);
//        cal.set(Calendar.DATE, 1);
//        return getDayStartTime(cal.getTime());
//    }
//
//    //获取本年的结束时间
//    public static Date getEndDayOfYear(Date date) {
//        Calendar cal = Calendar.getInstance();
//        if (null != date)
//            cal.setTime(date);
//        cal.set(Calendar.YEAR, DateKit.getYear(date));
//        cal.set(Calendar.MONTH, Calendar.DECEMBER);
//        cal.set(Calendar.DATE, 31);
//        return getDayEndTime(cal.getTime());
//    }
}

