package com.vekai.crops.etl.util;

import cn.fisok.raw.kit.DateKit;

import java.util.Date;

public class DateUtil {

    /**
     * 给年份和月份获取月份的最后一天
     * @param year
     * @param month
     * @return
     */
    public static String getLastDayOfMonth(int year,int month){
        //获取目标日期
        Date lastDayOfMonth = DateKit.getLastDayOfMonth(year, month);
        //保留目标日期年月日
        String strDate = DateKit.format(lastDayOfMonth,DateKit.DATE_FORMAT);
        return strDate;
    }

    /**
     * 获取日期年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        return DateKit.getYear(date);
    }

    /**
     * 获取日期月份
     * @param date
     * @return
     */
    public static int getMonth(Date date){
        return DateKit.getMonth(date);
    }

    /**
     * 获取和给定日期相差几个月的时间
     * @param startDate
     * @param plusValue
     * @return
     */
    public static Date plusMonth(Date startDate, int plusValue){
        return DateKit.plusMonths(startDate,plusValue);
    }

    /**
     * 给日期算该日期当月的最后一天
     * @param date
     * @return
     */
    public static String getLastDayOfCurrentMonth(Date date){
        return getLastDayOfMonth(getYear(date),getMonth(date));
    }

    /**
     * 给日期算该日期前一个月的最后一天
     * @param date
     * @return
     */
    public static String getLastDayOfPreviousMonth(Date date){
        Date previousDate = plusMonth(date, -1);
        return getLastDayOfMonth(getYear(previousDate),getMonth(previousDate));
    }

    /**
     * 给日期算该日期前几个月的最后一天
     * @param date
     * @param periods
     * @return
     */
    public static String[] getLastDayOfPreviousMonths(Date date, int periods){
        periods = Math.abs(periods);
        String[] strDates = new String[periods];
        Date previousDate = null;
        for(int i=0;i<periods;i++){
            previousDate = plusMonth(date,-(i+1));
            strDates[i] = getLastDayOfMonth(getYear(previousDate),getMonth(previousDate));
        }
        return strDates;
    }

    /**
     * 给日期算该日期前几个季度的最后一天
     * @param date
     * @param periods
     * @return
     */
    public static String[] getLastDayOfPreviousQuarters(Date date, int periods){
        periods = Math.abs(periods);
        int month = getMonth(date);
        int x = month%3;
        if(x == 0) x = 3;
        String[] strDates = new String[periods];
        Date previousDate = null;
        for(int i=0;i<periods;i++){
            previousDate = plusMonth(date,-(i*3+x));
            strDates[i] = getLastDayOfMonth(getYear(previousDate),getMonth(previousDate));
        }
        return strDates;
    }

    /**
     * 给日期算给日期前几年的最后一天
     * @param date
     * @param periods
     * @return
     */
    public static String[] getLastDayOfPreviousYears(Date date,int periods){
        periods = Math.abs(periods);
        String[] strDates = new String[periods];
        for(int i=0;i<periods;i++){
            strDates[i] = getLastDayOfMonth(getYear(date)-i-1,12);
        }
        return strDates;
    }
}
