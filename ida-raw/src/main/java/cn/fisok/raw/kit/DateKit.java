/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.raw.kit;

import cn.fisok.raw.holder.DateHolder;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:48
 * @desc : 日期处理
 */
public abstract class DateKit {
    public static final String DATE_TIME_MS_FORMAT="yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT="yyyy-MM-dd";
    public static final String TIME_FORMAT="HH:mm:ss";

    public static final String X_DATE_TIME_MS_FORMAT="yyyyMMdd HH:mm:ss.SSS";
    public static final String X_DATE_TIME_FORMAT="yyyyMMdd HH:mm:ss";
    public static final String X_DATE_FORMAT="yyyyMMdd";


    public static Date now(){
        Date now = DateHolder.getDate();
        if(now == null){
            now = new Date();
        }
        return now;
    }

    /**
     * 当前日期的字串值
     * @return  如: 2020-08-01
     */
    public static String strNowDate(){
        return format(now(),DATE_FORMAT);
    }

    /**
     * 当前日期的日期+时间值
     * @return 如  2020-08-01 08:34:16
     */
    public static String strNowDateTime(){
        return format(now(),DATE_TIME_FORMAT);
    }

    /**
     * 当前时间值
     * @return 如 08:34:16
     */
    public static String strNowTime(){
        return format(now(),TIME_FORMAT);
    }

    /**
     * @deprecated
     * @param str
     * @return
     */
    public static Date parse(String str){
        String fixedStr = StringKit.nvl(str,"");
        //如果是数字字串，则直接转下
        if(fixedStr.matches("\\d+")){
            return parse(Long.parseLong(str));
        }
        if(fixedStr.indexOf("/")>0){
            fixedStr = fixedStr.replaceAll("/+","-");
        }
        fixedStr = fixedStr.replaceAll("-+","-");

        try{
            DateTimeFormatter format = DateTimeFormat.forPattern(DATE_TIME_MS_FORMAT);
            return DateTime.parse(fixedStr,format).toDate();
        }catch(IllegalArgumentException e){
            try{
                DateTimeFormatter format = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
                return DateTime.parse(fixedStr,format).toDate();
            }catch(IllegalArgumentException e1){
                DateTimeFormatter format = DateTimeFormat.forPattern(DATE_FORMAT);
                return DateTime.parse(fixedStr,format).toDate();
            }
        }
    }

    public static Date xparse(String str){
        String fixedStr = StringKit.nvl(str,"");
        //如果是数字字串，则直接转下
        if(fixedStr.matches("\\d+")){
            return parse(Long.parseLong(str));
        }
        fixedStr = fixedStr.replaceAll("/+","");
        fixedStr = fixedStr.replaceAll("-+","");

        try{
            DateTimeFormatter format = DateTimeFormat.forPattern(X_DATE_TIME_MS_FORMAT);
            return DateTime.parse(fixedStr,format).toDate();
        }catch(IllegalArgumentException e){
            try{
                DateTimeFormatter format = DateTimeFormat.forPattern(X_DATE_TIME_FORMAT);
                return DateTime.parse(fixedStr,format).toDate();
            }catch(IllegalArgumentException e1){
                DateTimeFormatter format = DateTimeFormat.forPattern(X_DATE_FORMAT);
                return DateTime.parse(fixedStr,format).toDate();
            }
        }
    }

    public static Date parse(Long value){
        return new Date(value);
    }

    public static int getYear(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getYear();
    }

    public static int getMonth(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getMonthOfYear();
    }

    public static int getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    public static int getHour(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getHourOfDay();
    }

    public static int getMinute(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getMinuteOfHour();
    }

    public static int getDayOfMonth(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfMonth();
    }

    public static int getDayOfWeek(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getDayOfWeek();
    }

    public static int getYearOfCentury(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.getYearOfCentury();
    }

    public static String format(Date date){
//        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_MS_FORMAT);
        return format(date,DATE_TIME_FORMAT);
    }

    public static String format(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 取两个日期间隔的天数
     *
     * @param date1 date1
     * @param date2 date2
     * @return int
     */
    public static int getRangeDays(Date date1, Date date2){
        DateTime begin = new DateTime(date1);
        DateTime end = new DateTime(date2);
        Period period = new Period(begin,end, PeriodType.days());
        return period.getDays();
    }
    /**
     * 取两个日期间隔的月数
     *
     * @param date1 date11
     * @param date2 date2
     * @return int
     */
    public static int getRangeMonths(Date date1, Date date2){
        DateTime begin = new DateTime(date1);
        DateTime end = new DateTime(date2);
        Period period = new Period(begin,end, PeriodType.months());
        return period.getMonths();
    }

    /**
     * 取两个日期间隔的年数
     *
     * @param date1 date1
     * @param date2 date2
     * @return int
     */
    public static int getRangeYears(Date date1, Date date2){
        DateTime begin = new DateTime(date1);
        DateTime end = new DateTime(date2);
        Period period = new Period(begin,end, PeriodType.years());
        return period.getYears();
    }

    /**
     * 判断两个日期是否是同一天
     *
     * @param startDate startDate
     * @param endDate endDate
     * @return boolean
     */
    public static boolean isSameDay(Date startDate, Date endDate) {
        if (getRangeDays(startDate, endDate) == 0)
            return true;
        else
            return false;
    }

    public static Date plusYears(Date startDate, int plusValue) {
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = oStartDate.plusMonths(plusValue*12);
        return oEndDate.toDate();
    }
    public static Date plusMonths(Date startDate, int plusValue) {
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = oStartDate.plusMonths(plusValue);
        return oEndDate.toDate();
    }
    public static Date plusDays(Date startDate, int plusValue) {
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = oStartDate.plusDays(plusValue);
        return oEndDate.toDate();
    }
    public static Date minusDays(Date startDate, int plusValue) {
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = oStartDate.minusDays(plusValue);
        return oEndDate.toDate();
    }
    public static Date minusYears(Date startDate, int plusValue) {
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = oStartDate.minusYears(plusValue);
        return oEndDate.toDate();
    }

    /**
     * 只保留日期的年月日
     * @param date
     * @return
     */
    public static Date getOnlyYMD(Date date){
        String strDate = DateKit.format(date,DATE_FORMAT);
        return DateKit.parse(strDate);
    }

    /**
     * 指定时间是否在指定区间内
     *
     * @param date date
     * @param startDate startDate
     * @param endDate endDate
     * @return boolean
     */
    public static boolean inRange(Date date,Date startDate,Date endDate){
        DateTime oDate = new DateTime(date);
        DateTime oStartDate = new DateTime(startDate);
        DateTime oEndDate = new DateTime(endDate);
        Interval interval = new Interval(oStartDate,oEndDate);
        return interval.contains(oDate);
    }

    /**
     * 两个日期之间是否相关整月，如1月1号 至 3月1号为true,
     *
     * @param startDate startDate
     * @param endDate endDate
     * @return boolean
     */
    public static boolean rangIsFullMonth(Date startDate, Date endDate){
        int rangMonths = getRangeMonths(startDate,endDate);
        Date oEndDate = plusMonths(startDate, rangMonths);
        return isSameDay(endDate, oEndDate);
    }

    /**
     * 指定日期区间是否在另一个日期区间内
     *
     * @param innerStartDate 内层日期区间开始日期
     * @param innerEndDate 内层日期区间结束日期
     * @param outerStartDate 外层日期区间开始日期
     * @param outerEndDate 外层日期区间结束日期
     * @param containsEnd 是否算尾期
     * @return boolean
     */
    public static boolean inRange(Date innerStartDate,Date innerEndDate,Date outerStartDate,Date outerEndDate,boolean containsEnd){
        if(containsEnd){
            return inRange(innerStartDate,outerStartDate,outerEndDate) && (inRange(innerEndDate,outerStartDate,outerEndDate)|| compareLimitToDay(innerEndDate,outerEndDate) == 0);
        }else{
            return inRange(innerStartDate,outerStartDate,outerEndDate) && (inRange(innerEndDate,outerStartDate,outerEndDate));
        }
    }

    /**
     * 指定日期区间是否在另一个日期区间内
     *
     * @param innerStartDate 内层日期区间开始日期
     * @param innerEndDate 内层日期区间结束日期
     * @param outerStartDate 外层日期区间开始日期
     * @param outerEndDate 外层日期区间结束日期
     * @return boolean
     */
    public static boolean inRange(Date innerStartDate,Date innerEndDate,Date outerStartDate,Date outerEndDate){
        return inRange(innerStartDate, innerEndDate,outerStartDate, outerEndDate,true);
    }

    /**
     * 两个日期比较（到天）如果date1>date2返回1，date1=date2返回0，date1<date2返回-1
     *
     * @param date1 date1
     * @param date2 date2
     * @return int
     */
    public static int compareLimitToDay(Date date1,Date date2){
        DateTime startDate = new DateTime(date1);
        DateTime endDate = new DateTime(date2);
        int year1 = startDate.getYear();
        int year2 = endDate.getYear();
        if(year1>year2){
            return 1;
        }else if(year1<year2){
            return -1;
        }else {
            int days1 = startDate.getDayOfYear();
            int days2 = endDate.getDayOfYear();
            if(days1 > days2){
                return 1;
            }else if(days1 < days2){
                return -1;
            }else{
                return 0;
            }
        }
    }

    /**
     * 获取指定日期当月的天数
     *
     * @param date date
     * @return int
     */
    public static int getMonthDays(Date date){
        DateTime oDate = new DateTime(date);
        Integer days = oDate.dayOfMonth().getMaximumValue();
        return days;
    }

    /**
     * 判断日期date是否在referDate之后
     *
     * @param referDate 前一个日期
     * @param date 比较的后一个日期
     * @param containEquals 如果是同一天，是否包含
     * @return boolean
     */
    public static boolean isAfter(Date referDate,Date date,boolean containEquals){
        int r = compareLimitToDay(date,referDate);
        if(r == 1){
            return true;
        }else if(r == 0){
            if(containEquals)return true;
            else return false;
        }else{
            return false;
        }
    }

    /**
     * 判断日期date是否在referDate之后,相等返会false
     *
     * @param referDate referDate
     * @param date date
     * @return boolean
     */
    public static boolean isAfter(Date referDate,Date date){
        return isAfter(referDate,date,false);
    }

    /**
     * 获取指定年的天数
     *
     * @param date date
     * @return int
     */
    public static int getYearDays(Date date){
        DateTime oDate = new DateTime(date);
        Integer days = oDate.dayOfYear().getMaximumValue();
        return days;
    }

    /**
     * 获取指定月份的最后一天
     *
     * @param year  year
     * @param month month
     * @return date
     */
    public static Date getLastDayOfMonth(int year,int month){
        LocalDate localDate = LocalDate.of(year, month,1);
        LocalDate now = localDate.with(TemporalAdjusters.lastDayOfMonth());
        Date.from(now.atStartOfDay(ZoneId.systemDefault()).plusDays(1L).minusNanos(1L).toInstant());
        return localDate2Date(now);
    }

    public static LocalDate date2LocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        LocalDate localDate = localDateTime.toLocalDate();
        return localDate;
    }

    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    public static Date localDate2Date(LocalDate localDate){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime){
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public java.time.Period between(Date beginDate,Date endDate){
        LocalDate begin = date2LocalDate(beginDate);
        LocalDate end = date2LocalDate(endDate);
        java.time.Period period = java.time.Period.between(begin,end);
        return period;
    }



    /**
     * 获取本周的开始时间
     *
     * @param curDate curDate
     * @return date
     */
    public static Date getMondayOfWeek(Date curDate) {
        Calendar cal = Calendar.getInstance();
        if (null != curDate)
            cal.setTime(curDate);
        cal.setTime(curDate);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            dayOfWeek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayOfWeek);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本周的结束时间
     *
     * @param curDate curDate
     * @return date
     */
    public static Date getSundayOfWeek(Date curDate) {
        Calendar cal = Calendar.getInstance();
        if (null != curDate)
            cal.setTime(curDate);
        cal.setTime(getMondayOfWeek(curDate));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    /**
     * 获取本月的开始时间
     *
     * @param date date
     * @return date
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
            calendar.setTime(date);
        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取本月的结束时间
     *
     * @param date date
     * @return date
     */
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
            calendar.setTime(date);
        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(DateKit.getYear(date), DateKit.getMonth(date) - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取某个日期的开始时间
     *
     * @param date date
     * @return date
     */
    private static Date getDayStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
            calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }


    /**
     * 获取某个日期的结束时间
     *
     * @param date date
     * @return date
     */
    private static Date getDayEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (null != date)
            calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }



    /**
     * 获取某年某月的第一天日期
     *
     * @param year  year
     * @param month month
     * @return date
     */
    public static Date getStartMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return getDayStartTime(calendar.getTime());
    }

    /**
     * 获取某年某月的最后一天日期
     *
     * @param year  year
     * @param month month
     * @return date
     */
    public static Date getEndMonthDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int day = calendar.getActualMaximum(5);
        calendar.set(year, month - 1, day);
        return getDayEndTime(calendar.getTime());
    }

    /**
     * 获取本年的开始时间
     *
     * @param date date
     * @return date
     */
    public static Date getBeginDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (null != date)
            cal.setTime(date);
        cal.set(Calendar.YEAR, DateKit.getYear(date));
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);
        return getDayStartTime(cal.getTime());
    }

    /**
     * 获取本年的结束时间
     *
     * @param date date
     * @return date
     */
    public static Date getEndDayOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        if (null != date)
            cal.setTime(date);
        cal.set(Calendar.YEAR, DateKit.getYear(date));
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DATE, 31);
        return getDayEndTime(cal.getTime());
    }

}
