package com.vekai.crops.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

  public static String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

  public static String[] MONTHS_ZHN = { "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" };
  /**
   * Number of milliseconds in a standard second.
   * 
   * @since 2.1
   */
  public static final long MILLIS_PER_SECOND = DateUtils.MILLIS_PER_SECOND;

  /**
   * Number of milliseconds in a standard minute.
   * 
   * @since 2.1
   */
  public static final long MILLIS_PER_MINUTE = DateUtils.MILLIS_PER_MINUTE;

  /**
   * Number of milliseconds in a standard hour.
   * 
   * @since 2.1
   */
  public static final long MILLIS_PER_HOUR = DateUtils.MILLIS_PER_HOUR;

  /**
   * Number of milliseconds in a standard day.
   * 
   * @since 2.1
   */
  public static final long MILLIS_PER_DAY = DateUtils.MILLIS_PER_DAY;

  /**
   * This is half a month, so this represents whether a date is in the top or
   * bottom half of the month.
   */
  public final static int SEMI_MONTH = DateUtils.SEMI_MONTH;

  /**
   * A week range, starting on Sunday.
   */
  public final static int RANGE_WEEK_SUNDAY = DateUtils.RANGE_WEEK_SUNDAY;

  /**
   * A week range, starting on Monday.
   */
  public final static int RANGE_WEEK_MONDAY = DateUtils.RANGE_WEEK_MONDAY;

  /**
   * A week range, starting on the day focused.
   */
  public final static int RANGE_WEEK_RELATIVE = DateUtils.RANGE_WEEK_RELATIVE;

  /**
   * A week range, centered around the day focused.
   */
  public final static int RANGE_WEEK_CENTER = DateUtils.RANGE_WEEK_CENTER;

  /**
   * A month range, the week starting on Sunday.
   */
  public final static int RANGE_MONTH_SUNDAY = DateUtils.RANGE_MONTH_SUNDAY;

  /**
   * A month range, the week starting on Monday.
   */
  public final static int RANGE_MONTH_MONDAY = DateUtils.RANGE_MONTH_MONDAY;

  public static final String FORMAT_DATE = "yyyy-MM-dd"//
      , FORMAT_DATE_COMPACT = "yyyyMMdd" //
      , FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss"//
      , FORMAT_DATE_TIME_COMPACT = "yyyyMMddHHmmss"//
      , FORMAT_DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss SSS"//
      , FORMAT_DATE_TIME_MS_COMPACT = "yyyyMMddHHmmssSSS"//
  ;

  /**
   * <p>
   * <code>DateUtils</code> instances should NOT be constructed in standard
   * programming. Instead, the class should be used as
   * <code>DateUtils.parse(str);</code>.
   * </p>
   * 
   * <p>
   * This constructor is public to permit tools that require a JavaBean instance
   * to operate.
   * </p>
   */
  public DateUtil() {
  }

  /**
   * 将日期字符按指定格式转换为Date对象
   * 
   * @param datestring
   *          日期字符串
   * @param format
   *          指定日期转换的格式
   * @return
   */
  public static Date parseString2Date(String datestring, String format) throws Exception {
    try {
      String sDate = "";
      if (datestring.length() == 7) {
        sDate = datestring + "/01";
      } else if (datestring.length() == 4) {
        sDate = datestring + "/01/01";
      } else {
        sDate = datestring;
      }
      Date date = new SimpleDateFormat(format).parse(sDate);
      return date;
    } catch (Exception e) {
      throw new Exception("字符'" + datestring + "'转换异常" + e);
    }
  }

  /**
   * 以默认的"yyyy-MM-dd"来格式化日期字符串
   * 
   * @param datestring
   *          日期字符串
   * @return 经过格式化操作的日期对象
   * @throws Exception
   */
  public static Date parseString2Date(String datestring) throws Exception {
    return parseString2Date(datestring, "yyyy-MM-dd");
  }

  /**
   * 将日期对象(date)按提供的格式(sFormat)格式化
   * 
   * @param date
   *          日期对象
   * @param sFormat
   *          目的格式
   * @return
   */
  public static String parseDateFormat(Date date, String sFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    return sdf.format(gc.getTime());
  }
  /**
   * "yyyy-m-d" 转换为"yyyy-mm-dd"
   * @param dateStr
   * @return
   */
  public static String getFormatyyyyMMdd(String dateStr){
      String[] dateStrs = dateStr.split("-");
      if(dateStrs.length==3){
          String month = dateStrs[1];
          String day = dateStrs[2];
          if(dateStrs[1].length()==1){
              month = "0"+dateStrs[1];
          }
          if(dateStrs[2].length()==1){
              day = "0"+dateStrs[2];
          }
          dateStr = dateStrs[0]+month+day;
      }
      return dateStr;
  }
  /**
   * 针对日期对象的日期推移计算，如昨天、上月等
   * 
   * @param date
   *          传入的日期对象
   * @param iYear
   *          年偏移量
   * @param iMonth
   *          月偏移量
   * @param iDate
   *          日偏移量
   * @param sFormat
   *          返回日期字符串的格式
   * @return
   */
  public static String getRelativeDate(Date date, int iYear, int iMonth, int iDate, String sFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(sFormat); // 定义格式
    GregorianCalendar gc = new GregorianCalendar(); //

    gc.setTime(date); // 设置时间

    gc.add(Calendar.YEAR, iYear); // 算术求和
    gc.add(Calendar.MONTH, iMonth);
    gc.add(Calendar.DATE, iDate);

    return sdf.format(gc.getTime());
  }

  /**
   * 针对日期字符串的日期推移计算，如昨天、上月等
   * 将日期字符串按默认格式'yyyy-MM-dd'来格式化日期对象之后推算日期，输出以sFormat格式指定的日期字符串
   * 
   * @param sDate
   * @param iYear
   * @param iMonth
   * @param iDate
   * @param sFormat
   * @return
   * @throws Exception
   */
  public static String getRelativeDate(String sDate, int iYear, int iMonth, int iDate, String sFormat) throws Exception {
    if (sFormat.equals("")) {
      sFormat = "yyyy-MM-dd";
    }
    Date date = parseString2Date(sDate, sFormat);
    return getRelativeDate(date, iYear, iMonth, iDate, sFormat);
  }

  public static String getMonthName(int iMonth) throws Exception {
    int i = iMonth % 12;
    if (i == 0) {
      i = 12;
    }
    return MONTHS[i - 1];
  }

  /**
   * 返回默认格式的日期字符串
   * 
   * @param date
   * @param iYear
   * @param iMonth
   * @param iDate
   * @return
   */
  public static String getRelativeDate(Date date, int iYear, int iMonth, int iDate) throws Exception {
    return getRelativeDate(date, iYear, iMonth, iDate, "yyyy-MM-dd");
  }

  public static String getRelativeDate(int iYear, int iMonth, int iDate) throws Exception {
    return getRelativeDate(new Date(), iYear, iMonth, iDate, "yyyy-MM-dd");
  }

  public static String getRelativeDate(String sDate, int iYear, int iMonth, int iDate) throws Exception {
    return getRelativeDate(sDate, iYear, iMonth, iDate, "yyyy-MM-dd");
  }

  /**
   * 针对日期对象的日期推移计算，如昨天、上月等
   * 
   * @param date
   *          传入的日期对象
   * @param iYear
   *          年偏移量
   * @param iMonth
   *          月偏移量
   * @param iDate
   *          日偏移量
   * @param sFormat
   *          返回日期字符串的格式
   * @return
   */
  public static String getRelativeTime(Date date, int iYear, int iMonth, int iDate, int iHour, int iMinute, String sFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(sFormat); // 定义格式
    GregorianCalendar gc = new GregorianCalendar(); //

    gc.setTime(date); // 设置时间

    gc.add(Calendar.YEAR, iYear); // 算术求和
    gc.add(Calendar.MONTH, iMonth);
    gc.add(Calendar.DATE, iDate);
    gc.add(Calendar.HOUR, iHour);
    gc.add(Calendar.MINUTE, iMinute);

    return sdf.format(gc.getTime());
  }

  public static String getCurrentRelativeTime(int iYear, int iMonth, int iDate, int iHour, int iMinute) {
    return getRelativeTime(new Date(), iYear, iMonth, iDate, iHour, iMinute);
  }

  public static String getRelativeTime(int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond) {
    return getRelativeTime(new Date(), iYear, iMonth, iDate, iHour, iMinute, iSecond, "");
  }

  public static String getRelativeTime(Date date, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond, String sFormat) {
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, iSecond, 0, sFormat);
  }

  public static String getRelativeTimeOfMilliSecond(String sDate, int iMilliSecond) throws Exception {
    return getRelativeTimeOfMilliSecond(sDate, iMilliSecond, FORMAT_DATE_TIME_MS_COMPACT);
  }

  public static String getRelativeTimeOfMilliSecond(String sDate, int iMilliSecond, String sFormat) throws Exception {
    return getRelativeTime(sDate, 0, 0, 0, 0, 0, 0, iMilliSecond, sFormat);
  }

  public static String getRelativeTime(String sDate, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond, int iMilliSecond, String sFormat)
      throws Exception {
    Date date = parseString2Date(sDate, sFormat);
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, iSecond, iMilliSecond, sFormat);
  }

  public static String getRelativeTime(Date date, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond, int iMilliSecond, String sFormat) {
    if (CommonUtil.ifIsEmpty(sFormat))
      sFormat = "yyyy-MM-dd HH:mm:ss";
    SimpleDateFormat sdf = new SimpleDateFormat(sFormat); // 定义格式
    GregorianCalendar gc = new GregorianCalendar(); //
    gc.setTime(date); // 设置时间

    gc.add(Calendar.YEAR, iYear); // 算术求和
    gc.add(Calendar.MONTH, iMonth);
    gc.add(Calendar.DATE, iDate);
    gc.add(Calendar.HOUR, iHour);
    gc.add(Calendar.MINUTE, iMinute);
    gc.add(Calendar.SECOND, iSecond);
    gc.add(Calendar.MILLISECOND, iMilliSecond);

    return sdf.format(gc.getTime());
  }

  /**
   * 针对日期字符串的日期推移计算，如昨天、上月等
   * 将日期字符串按默认格式'yyyy-MM-dd'来格式化日期对象之后推算日期，输出以sFormat格式指定的日期字符串
   * 
   * @param sDate
   * @param iYear
   * @param iMonth
   * @param iDate
   * @param sFormat
   * @return
   * @throws Exception
   */
  public static String getRelativeTime(String sDate, int iYear, int iMonth, int iDate, int iHour, int iMinute, String sFormat) throws Exception {
    Date date = parseString2Date(sDate, "yyyy-MM-dd");
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, sFormat);
  }

  public static String getRelativeTime(String sDate, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond, String sFormat) throws Exception {
    Date date = parseString2Date(sDate, sFormat);
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, iSecond, sFormat);
  }

  public static String getRelativeTime(Date date, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond) throws Exception {
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, iSecond, "yyyy-MM-dd HH:mm:ss");
  }

  public static String getRelativeTime(String sDate, int iYear, int iMonth, int iDate, int iHour, int iMinute, int iSecond) throws Exception {
    return getRelativeTime(sDate, iYear, iMonth, iDate, iHour, iMinute, iSecond, "yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 返回默认格式的日期字符串
   * 
   * @param date
   * @param iYear
   * @param iMonth
   * @param iDate
   * @return
   */
  public static String getRelativeTime(Date date, int iYear, int iMonth, int iDate, int iHour, int iMinute) {
    return getRelativeTime(date, iYear, iMonth, iDate, iHour, iMinute, "yyyy-MM-dd HH:mm");
  }

  public static String getRelativeTime(String sDate, int iYear, int iMonth, int iDate, int iHour, int iMinute) throws Exception {
    return getRelativeTime(sDate, iYear, iMonth, iDate, iHour, iMinute, "yyyy-MM-dd HH:mm");
  }

  public static String getRelativeMonth(Date date, int iYear, int iMonth, String s) {
    return getRelativeDate(date, iYear, iMonth, 0, s);
  }

  public static String getRelativeMonth(String sDate, int iYear, int iMonth, String s) throws Exception {
    return getRelativeDate(sDate, iYear, iMonth, 0, s);
  }

  public static String getRelativeMonth(Date date, int iYear, int iMonth) {
    return getRelativeDate(date, iYear, iMonth, 0, "yyyy/MM");
  }

  public static String getRelativeMonth(String sDate, int iYear, int iMonth) throws Exception {
    return getRelativeDate(sDate, iYear, iMonth, 0, "yyyy/MM");
  }

  /**
   * 判断sEndDate是否为当月月末
   * 
   * @param sEndDate
   * @return
   * @throws Exception
   */
  public static boolean isMonthEnd(String sEndDate) throws Exception {
    if (getRelativeDate(sEndDate, 0, 0, 1).substring(8, 10).equals("01")) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * 将日期从"yyyyMMdd"转换为format指定的日期输出格式
   * 
   * @param sDate
   *          格式为'yyyyMMdd'的日期字符串
   * @param format
   *          输出格式
   * @return
   * @throws Exception
   *           日期转换异常
   */
  public static String formatDate(String sDate, String format) throws Exception {
    if (CommonUtil.ifIsEmpty(sDate)) {
      return getToday();
    } else {
      int iLength = sDate.length();
      if (8 == iLength) {
        sDate = parseDateFormat(parseString2Date(sDate, FORMAT_DATE_COMPACT), format);
      } else if (14 == iLength) {
        sDate = parseDateFormat(parseString2Date(sDate, FORMAT_DATE_TIME_COMPACT), format);
      }
    }
    return sDate;
  }

  public static String formatDate(String sDate) throws Exception {
    return formatDate(sDate, "yyyy-MM-dd");
  }

  /**
   * 按照format指定格式返回当前时间
   * 
   * @param format
   *          时间格式
   * @return
   */
  public static String getNowTime(String format) {
    return parseDateFormat(new Date(), format);
  }

  /**
   * 获取当前时间，精确到秒
   * 
   * @return
   */
  public static String getNowTime() {
    return getNowTime("yyyy-MM-dd HH:mm:ss");
  }

  /**
   * 获取当前时间，精确到毫秒
   * 
   * @return
   */
  public static String getNow() {
    return getNowTime("yyyy-MM-dd HH:mm:ss SSS");
  }

  public static String getToday(String sFormat) {
    return getNowTime(sFormat);
  }

  public static String getToday() {
    return getToday("yyyy-MM-dd");
  }

  public static String getYesterday(String sFormat) {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(date);
    gc.add(Calendar.DATE, -1);
    return sdf.format(gc.getTime());
  }

  public static String getYesterday() {
    return getYesterday("yyyy-MM-dd");
  }

  /**
   * 获得报表的填报日期
   * 
   * @param sdate
   *          传入的处理日期 yyyy-MM-dd
   * @return
   * @throws Exception
   */
  public static String getFillInDate(String sdate) throws Exception {
    Date date = parseString2Date(sdate);
    String sFillinDate = "";
    switch (sdate.length()) {
    case 7:
      sFillinDate = parseDateFormat(date, "yyyy 年 MM 月");
      break;
    case 10:
      sFillinDate = parseDateFormat(date, "yyyy 年 MM 月 dd 日");
      break;
    default:
    }
    return sFillinDate;
  }

  /**
   * 根据iScal返回相应位置的数组值
   * 
   * @param sDate
   * @param iScal
   * @return
   */
  public static String getFillinParam(String sDate, int iScal) {
    if (iScal <= 0) {
      return sDate;
    }
    String[] sDateInfo = StringUtils.split(sDate, "/");
    if (sDateInfo.length >= iScal) {
      return sDateInfo[iScal - 1];
    }
    return sDate;
  }

  /**
   * 根据分隔符分解字符串为字符串数组
   * 
   * @param datetime
   *          日期字符串('yyyy-MM-dd')
   * @param seperator
   *          分隔符
   * @return 字符串数组
   */
  public static String[] getQuaterTime(String datetime, String seperator) {
    String[] sOriginTime = StringUtils.split(datetime, seperator); // 取年月
    int iMonth = Integer.parseInt(sOriginTime[1]);
    int iQuarter = (iMonth + 2) / 3; // 月份所处季度
    String[] sQuarterTime = new String[2];
    System.arraycopy(sOriginTime, 0, sQuarterTime, 0, 2);

    switch (iQuarter) {
    case 1:
      sQuarterTime[1] = "03";
      break;
    case 2:
      sQuarterTime[1] = "06";
      break;
    case 3:
      sQuarterTime[1] = "09";
      break;
    case 4:
      sQuarterTime[1] = "12";
      break;
    }
    return sQuarterTime;
  }

  /**
   * 以默认的分隔符'/'分解日期字符串为字符串数组
   * 
   * @param datetime
   *          日期字符串
   * @return 字符串数组
   */
  public static String[] getQuarterTime(String datetime) {
    return getQuaterTime(datetime, "/");
  }

  /**
   * 根据传入的月份('/'分割)字符串返回格式为'yyyy年第一(二/三/四)季度'的报表填报季度时间
   * 传入的日期取前7位组成年月数组，如果缺少月日则默认为第一季度，
   * 
   * @param datetime
   * @return
   */
  public static String getQuaterName(String datetime) {
    String[] sDateInfo = getQuarterTime(datetime);
    String sQuarter = "";
    switch (Integer.parseInt(sDateInfo[1]) / 3) {
    case 1:
      sQuarter = "第 一 季度";
      break;
    case 2:
      sQuarter = "第 二 季度";
      break;
    case 3:
      sQuarter = "第 三 季度";
      break;
    case 4:
      sQuarter = "第 四 季度";
      break;
    }
    return sDateInfo[0] + " 年 " + sQuarter; // 'yyyy年第一(二/三/四)季度'
  }

  /**
   * 获取季度信息 根据offset指定的偏移量返回对应的季度信息
   * 
   * @param datetime
   * @param offset
   * @return
   */
  public static String getQuaterInfo(String datetime, int offset) throws Exception {
    String[] sDateInfo = getQuarterTime(datetime);
    String sMonthRange = sDateInfo[0] + "/" + sDateInfo[1];
    int iStep = offset * 3;
    String sQuaterValue = getRelativeMonth(sMonthRange + "/01", 0, iStep);

    return sQuaterValue;
  }

  /**
   * 返回出入的日期出于那个半年范围内 根据offset指定的偏移量返回对应的半年度时点
   * 
   * @param datetime
   *          需判断的日期
   * @param offset
   *          半年偏移量
   * @return
   * @throws Exception
   */
  public static String getYearInfo(String datetime, int offset) throws Exception {
    String[] sDateInfo = getQuarterTime(datetime);
    if (sDateInfo[1].compareTo("06") > 0) {
      sDateInfo[1] = "12";
    } else {
      sDateInfo[1] = "06";
    }
    int iStep = offset * 6;
    return getRelativeMonth(sDateInfo[0] + "/" + sDateInfo[1] + "/01", 0, iStep);
  }

  /**
   * 判断返回传入参数是否为正确的日期字符
   * 
   * @param date
   * @return
   */
  public static boolean isDateString(String date) {
    int[] iaMonthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    String[] iaDate = new String[3];
    int year, month, day;
    // 识别三种日期格式yyyy-MM-dd,yyyyMMdd,yyyy-MM-dd
    if (!date.matches("[0-9]{4}[/|-]?[0-1]{1}[0-9]{1}[/|-]?[0-3]{1}[0-9]{1}")) {
      return false;
    }
    if (date.length() == 8) {
      iaDate[0] = date.substring(0, 4);
      iaDate[1] = date.substring(4, 6);
      iaDate[2] = date.substring(6, 8);
    } else {
      iaDate[0] = date.substring(0, 4);
      iaDate[1] = date.substring(5, 7);
      iaDate[2] = date.substring(8, 10);
    }
    year = Integer.parseInt(iaDate[0]);
    month = Integer.parseInt(iaDate[1]);
    day = Integer.parseInt(iaDate[2]);

    if (year < 1900 || year > 2100) {
      return false;
    }
    if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
      iaMonthDays[1] = 29;
    }
    if (month < 1 || month > 12) {
      return false;
    }
    if (day < 1 || day > iaMonthDays[month - 1]) {
      return false;
    }
    return true;
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Checks if two date objects are on the same day ignoring time.
   * </p>
   * 
   * <p>
   * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002
   * 13:45 and 12 Mar 2002 13:45 would return false.
   * </p>
   * 
   * @param date1
   *          the first date, not altered, not null
   * @param date2
   *          the second date, not altered, not null
   * @return true if they represent the same day
   * @throws IllegalArgumentException
   *           if either date is <code>null</code>
   * @since 2.1
   */
  public static boolean isSameDay(Date date1, Date date2) {
    return DateUtils.isSameDay(date1, date2);
  }

  /**
   * <p>
   * Checks if two calendar objects are on the same day ignoring time.
   * </p>
   * 
   * <p>
   * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true. 28 Mar 2002
   * 13:45 and 12 Mar 2002 13:45 would return false.
   * </p>
   * 
   * @param cal1
   *          the first calendar, not altered, not null
   * @param cal2
   *          the second calendar, not altered, not null
   * @return true if they represent the same day
   * @throws IllegalArgumentException
   *           if either calendar is <code>null</code>
   * @since 2.1
   */
  public static boolean isSameDay(Calendar cal1, Calendar cal2) {
    return DateUtils.isSameDay(cal1, cal2);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Checks if two date objects represent the same instant in time.
   * </p>
   * 
   * <p>
   * This method compares the long millisecond time of the two objects.
   * </p>
   * 
   * @param date1
   *          the first date, not altered, not null
   * @param date2
   *          the second date, not altered, not null
   * @return true if they represent the same millisecond instant
   * @throws IllegalArgumentException
   *           if either date is <code>null</code>
   * @since 2.1
   */
  public static boolean isSameInstant(Date date1, Date date2) {
    return DateUtils.isSameInstant(date1, date2);
  }

  /**
   * <p>
   * Checks if two calendar objects represent the same instant in time.
   * </p>
   * 
   * <p>
   * This method compares the long millisecond time of the two objects.
   * </p>
   * 
   * @param cal1
   *          the first calendar, not altered, not null
   * @param cal2
   *          the second calendar, not altered, not null
   * @return true if they represent the same millisecond instant
   * @throws IllegalArgumentException
   *           if either date is <code>null</code>
   * @since 2.1
   */
  public static boolean isSameInstant(Calendar cal1, Calendar cal2) {
    return DateUtils.isSameInstant(cal1, cal2);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Checks if two calendar objects represent the same local time.
   * </p>
   * 
   * <p>
   * This method compares the values of the fields of the two objects. In
   * addition, both calendars must be the same of the same type.
   * </p>
   * 
   * @param cal1
   *          the first calendar, not altered, not null
   * @param cal2
   *          the second calendar, not altered, not null
   * @return true if they represent the same millisecond instant
   * @throws IllegalArgumentException
   *           if either date is <code>null</code>
   * @since 2.1
   */
  public static boolean isSameLocalTime(Calendar cal1, Calendar cal2) {
    return DateUtils.isSameLocalTime(cal1, cal2);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Parses a string representing a date by trying a variety of different
   * parsers.
   * </p>
   * 
   * <p>
   * The parse will try each parse pattern in turn. A parse is only deemed
   * sucessful if it parses the whole of the input string. If no parse patterns
   * match, a ParseException is thrown.
   * </p>
   * 
   * @param str
   *          the date to parse, not null
   * @param parsePatterns
   *          the date format patterns to use, see SimpleDateFormat, not null
   * @return the parsed date
   * @throws IllegalArgumentException
   *           if the date string or pattern array is null
   * @throws ParseException
   *           if none of the date patterns were suitable
   */
  public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
    return DateUtils.parseDate(str, parsePatterns);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Round this date, leaving the field specified as the most significant field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this
   * was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was
   * passed with MONTH, it would return 1 April 2002 0:00:00.000.
   * </p>
   * 
   * <p>
   * For a date in a timezone that handles the change to daylight saving time,
   * rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight
   * saving time begins at 02:00 on March 30. Rounding a date that crosses this
   * time would produce the following values:
   * <ul>
   * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
   * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
   * </ul>
   * </p>
   * 
   * @param date
   *          the date to work with
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Date round(Date date, int field) {
    return DateUtils.round(date, field);
  }

  /**
   * <p>
   * Round this date, leaving the field specified as the most significant field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this
   * was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was
   * passed with MONTH, it would return 1 April 2002 0:00:00.000.
   * </p>
   * 
   * <p>
   * For a date in a timezone that handles the change to daylight saving time,
   * rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight
   * saving time begins at 02:00 on March 30. Rounding a date that crosses this
   * time would produce the following values:
   * <ul>
   * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
   * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
   * </ul>
   * </p>
   * 
   * @param date
   *          the date to work with
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date (a different object)
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Calendar round(Calendar date, int field) {
    return DateUtils.round(date, field);
  }

  /**
   * <p>
   * Round this date, leaving the field specified as the most significant field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if this
   * was passed with HOUR, it would return 28 Mar 2002 14:00:00.000. If this was
   * passed with MONTH, it would return 1 April 2002 0:00:00.000.
   * </p>
   * 
   * <p>
   * For a date in a timezone that handles the change to daylight saving time,
   * rounding to Calendar.HOUR_OF_DAY will behave as follows. Suppose daylight
   * saving time begins at 02:00 on March 30. Rounding a date that crosses this
   * time would produce the following values:
   * <ul>
   * <li>March 30, 2003 01:10 rounds to March 30, 2003 01:00</li>
   * <li>March 30, 2003 01:40 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:10 rounds to March 30, 2003 03:00</li>
   * <li>March 30, 2003 02:40 rounds to March 30, 2003 04:00</li>
   * </ul>
   * </p>
   * 
   * @param date
   *          the date to work with, either Date or Calendar
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ClassCastException
   *           if the object type is not a <code>Date</code> or
   *           <code>Calendar</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Date round(Object date, int field) {
    return DateUtils.round(date, field);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Truncate this date, leaving the field specified as the most significant
   * field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you
   * passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was
   * passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
   * </p>
   * 
   * @param date
   *          the date to work with
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Date truncate(Date date, int field) {
    return DateUtils.truncate(date, field);
  }

  /**
   * <p>
   * Truncate this date, leaving the field specified as the most significant
   * field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you
   * passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was
   * passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
   * </p>
   * 
   * @param date
   *          the date to work with
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date (a different object)
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Calendar truncate(Calendar date, int field) {
    return DateUtils.truncate(date, field);
  }

  /**
   * <p>
   * Truncate this date, leaving the field specified as the most significant
   * field.
   * </p>
   * 
   * <p>
   * For example, if you had the datetime of 28 Mar 2002 13:45:01.231, if you
   * passed with HOUR, it would return 28 Mar 2002 13:00:00.000. If this was
   * passed with MONTH, it would return 1 Mar 2002 0:00:00.000.
   * </p>
   * 
   * @param date
   *          the date to work with, either <code>Date</code> or
   *          <code>Calendar</code>
   * @param field
   *          the field from <code>Calendar</code> or <code>SEMI_MONTH</code>
   * @return the rounded date
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ClassCastException
   *           if the object type is not a <code>Date</code> or
   *           <code>Calendar</code>
   * @throws ArithmeticException
   *           if the year is over 280 million
   */
  public static Date truncate(Object date, int field) {
    return DateUtils.truncate(date, field);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * This constructs an <code>Iterator</code> that will start and stop over a
   * date range based on the focused date and the range style.
   * </p>
   * 
   * <p>
   * For instance, passing Thursday, July 4, 2002 and a
   * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that
   * starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002.
   * 
   * @param focus
   *          the date to work with
   * @param rangeStyle
   *          the style constant to use. Must be one of the range styles listed
   *          for the {@link #iterator(Calendar, int)} method.
   * 
   * @return the date iterator
   * @throws IllegalArgumentException
   *           if the date is <code>null</code> or if the rangeStyle is not
   */
  public static Iterator iterator(Date focus, int rangeStyle) {
    return DateUtils.iterator(focus, rangeStyle);
  }

  /**
   * <p>
   * This constructs an <code>Iterator</code> that will start and stop over a
   * date range based on the focused date and the range style.
   * </p>
   * 
   * <p>
   * For instance, passing Thursday, July 4, 2002 and a
   * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that
   * starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002.
   * 
   * @param focus
   *          the date to work with
   * @param rangeStyle
   *          the style constant to use. Must be one of
   *          {@link DateUtils#RANGE_MONTH_SUNDAY},
   *          {@link DateUtils#RANGE_MONTH_MONDAY},
   *          {@link DateUtils#RANGE_WEEK_SUNDAY},
   *          {@link DateUtils#RANGE_WEEK_MONDAY},
   *          {@link DateUtils#RANGE_WEEK_RELATIVE},
   *          {@link DateUtils#RANGE_WEEK_CENTER}
   * @return the date iterator
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   */
  public static Iterator iterator(Calendar focus, int rangeStyle) {
    return DateUtils.iterator(focus, rangeStyle);
  }

  /**
   * <p>
   * This constructs an <code>Iterator</code> that will start and stop over a
   * date range based on the focused date and the range style.
   * </p>
   * 
   * <p>
   * For instance, passing Thursday, July 4, 2002 and a
   * <code>RANGE_MONTH_SUNDAY</code> will return an <code>Iterator</code> that
   * starts with Sunday, June 30, 2002 and ends with Saturday, August 3, 2002.
   * </p>
   * 
   * @param focus
   *          the date to work with, either <code>Date</code> or
   *          <code>Calendar</code>
   * @param rangeStyle
   *          the style constant to use. Must be one of the range styles listed
   *          for the {@link #iterator(Calendar, int)} method.
   * @return the date iterator
   * @throws IllegalArgumentException
   *           if the date is <code>null</code>
   * @throws ClassCastException
   *           if the object type is not a <code>Date</code> or
   *           <code>Calendar</code>
   */
  public static Iterator iterator(Object focus, int rangeStyle) {
    return DateUtils.iterator(focus, rangeStyle);
  }

  // =================================================================

  /**
   * ISO8601 formatter for date-time without time zone. The format used is
   * <tt>yyyy-MM-dd'T'HH:mm:ss</tt>.
   */
  public static final FastDateFormat ISO_DATETIME_FORMAT = DateFormatUtils.ISO_DATETIME_FORMAT;

  /**
   * ISO8601 formatter for date-time with time zone. The format used is
   * <tt>yyyy-MM-dd'T'HH:mm:ssZZ</tt>.
   */
  public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT;

  /**
   * ISO8601 formatter for date without time zone. The format used is
   * <tt>yyyy-MM-dd</tt>.
   */
  public static final FastDateFormat ISO_DATE_FORMAT = DateFormatUtils.ISO_DATE_FORMAT;

  /**
   * ISO8601-like formatter for date with time zone. The format used is
   * <tt>yyyy-MM-ddZZ</tt>. This pattern does not comply with the formal ISO8601
   * specification as the standard does not allow a time zone without a time.
   */
  public static final FastDateFormat ISO_DATE_TIME_ZONE_FORMAT = DateFormatUtils.ISO_DATE_TIME_ZONE_FORMAT;

  /**
   * ISO8601 formatter for time without time zone. The format used is
   * <tt>'T'HH:mm:ss</tt>.
   */
  public static final FastDateFormat ISO_TIME_FORMAT = DateFormatUtils.ISO_TIME_FORMAT;

  /**
   * ISO8601 formatter for time with time zone. The format used is
   * <tt>'T'HH:mm:ssZZ</tt>.
   */
  public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = DateFormatUtils.ISO_TIME_TIME_ZONE_FORMAT;

  /**
   * ISO8601-like formatter for time without time zone. The format used is
   * <tt>HH:mm:ss</tt>. This pattern does not comply with the formal ISO8601
   * specification as the standard requires the 'T' prefix for times.
   */
  public static final FastDateFormat ISO_TIME_NO_T_FORMAT = DateFormatUtils.ISO_TIME_NO_T_FORMAT;

  /**
   * ISO8601-like formatter for time with time zone. The format used is
   * <tt>HH:mm:ssZZ</tt>. This pattern does not comply with the formal ISO8601
   * specification as the standard requires the 'T' prefix for times.
   */
  public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = DateFormatUtils.ISO_TIME_NO_T_TIME_ZONE_FORMAT;

  /**
   * SMTP (and probably other) date headers. The format used is
   * <tt>EEE, dd MMM yyyy HH:mm:ss Z</tt> in US locale.
   */
  public static final FastDateFormat SMTP_DATETIME_FORMAT = DateFormatUtils.SMTP_DATETIME_FORMAT;

  /**
   * <p>
   * Format a date/time into a specific pattern using the UTC time zone.
   * </p>
   * 
   * @param millis
   *          the date to format expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @return the formatted date
   */
  public static String formatUTC(long millis, String pattern) {
    return DateFormatUtils.formatUTC(millis, pattern);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern using the UTC time zone.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @return the formatted date
   */
  public static String formatUTC(Date date, String pattern) {
    return DateFormatUtils.formatUTC(date, pattern);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern using the UTC time zone.
   * </p>
   * 
   * @param millis
   *          the date to format expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String formatUTC(long millis, String pattern, Locale locale) {
    return DateFormatUtils.formatUTC(millis, pattern, locale);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern using the UTC time zone.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String formatUTC(Date date, String pattern, Locale locale) {
    return DateFormatUtils.formatUTC(date, pattern, locale);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern.
   * </p>
   * 
   * @param millis
   *          the date to format expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @return the formatted date
   */
  public static String format(long millis, String pattern) {
    return DateFormatUtils.format(millis, pattern);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @return the formatted date
   */
  public static String format(Date date, String pattern) {
    return DateFormatUtils.format(date, pattern);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a time zone.
   * </p>
   * 
   * @param millis
   *          the time expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @param timeZone
   *          the time zone to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(long millis, String pattern, TimeZone timeZone) {
    return DateFormatUtils.format(millis, pattern, timeZone);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a time zone.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @param timeZone
   *          the time zone to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(Date date, String pattern, TimeZone timeZone) {
    return DateFormatUtils.format(date, pattern, timeZone);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a locale.
   * </p>
   * 
   * @param millis
   *          the date to format expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(long millis, String pattern, Locale locale) {
    return DateFormatUtils.format(millis, pattern, locale);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a locale.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(Date date, String pattern, Locale locale) {
    return DateFormatUtils.format(date, pattern, locale);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a time zone and locale.
   * </p>
   * 
   * @param millis
   *          the date to format expressed in milliseconds
   * @param pattern
   *          the pattern to use to format the date
   * @param timeZone
   *          the time zone to use, may be <code>null</code>
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(long millis, String pattern, TimeZone timeZone, Locale locale) {
    return DateFormatUtils.format(millis, pattern, timeZone, locale);
  }

  /**
   * <p>
   * Format a date/time into a specific pattern in a time zone and locale.
   * </p>
   * 
   * @param date
   *          the date to format
   * @param pattern
   *          the pattern to use to format the date
   * @param timeZone
   *          the time zone to use, may be <code>null</code>
   * @param locale
   *          the locale to use, may be <code>null</code>
   * @return the formatted date
   */
  public static String format(Date date, String pattern, TimeZone timeZone, Locale locale) {
    return DateFormatUtils.format(date, pattern, timeZone, locale);
  }

  // ========================================================================

  /**
   * <p>
   * Pattern used with <code>FastDateFormat</code> and
   * <code>SimpleDateFormat</code> for the ISO8601 period format used in
   * durations.
   * </p>
   * 
   */
  public static final String ISO_EXTENDED_FORMAT_PATTERN = DurationFormatUtils.ISO_EXTENDED_FORMAT_PATTERN;

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Get the time gap as a string.
   * </p>
   * 
   * <p>
   * The format used is ISO8601-like: <i>H</i>:<i>m</i>:<i>s</i>.<i>S</i>.
   * </p>
   * 
   * @param durationMillis
   *          the duration to format
   * @return the time as a String
   */
  public static String formatDurationHMS(long durationMillis) {
    return DurationFormatUtils.formatDurationHMS(durationMillis);
  }

  /**
   * <p>
   * Get the time gap as a string.
   * </p>
   * 
   * <p>
   * The format used is the ISO8601 period format.
   * </p>
   * 
   * <p>
   * This method formats durations using the days and lower fields of the ISO
   * format pattern, such as P7D6H5M4.321S.
   * </p>
   * 
   * @param durationMillis
   *          the duration to format
   * @return the time as a String
   */
  public static String formatDurationISO(long durationMillis) {
    return DurationFormatUtils.formatDurationISO(durationMillis);
  }

  /**
   * <p>
   * Get the time gap as a string, using the specified format, and padding with
   * zeros and using the default timezone.
   * </p>
   * 
   * <p>
   * This method formats durations using the days and lower fields of the format
   * pattern. Months and larger are not used.
   * </p>
   * 
   * @param durationMillis
   *          the duration to format
   * @param format
   *          the way in which to format the duration
   * @return the time as a String
   */
  public static String formatDuration(long durationMillis, String format) {
    return DurationFormatUtils.formatDuration(durationMillis, format);
  }

  /**
   * <p>
   * Get the time gap as a string, using the specified format. Padding the left
   * hand side of numbers with zeroes is optional and the timezone may be
   * specified.
   * </p>
   * 
   * <p>
   * This method formats durations using the days and lower fields of the format
   * pattern. Months and larger are not used.
   * </p>
   * 
   * @param durationMillis
   *          the duration to format
   * @param format
   *          the way in which to format the duration
   * @param padWithZeros
   *          whether to pad the left hand side of numbers with 0's
   * @return the time as a String
   */
  public static String formatDuration(long durationMillis, String format, boolean padWithZeros) {
    return DurationFormatUtils.formatDuration(durationMillis, format, padWithZeros);
  }

  /**
   * <p>
   * Format an elapsed time into a plurialization correct string.
   * </p>
   * 
   * <p>
   * This method formats durations using the days and lower fields of the format
   * pattern. Months and larger are not used.
   * </p>
   * 
   * @param durationMillis
   *          the elapsed time to report in milliseconds
   * @param suppressLeadingZeroElements
   *          suppresses leading 0 elements
   * @param suppressTrailingZeroElements
   *          suppresses trailing 0 elements
   * @return the formatted text in days/hours/minutes/seconds
   */
  public static String formatDurationWords(long durationMillis, boolean suppressLeadingZeroElements, boolean suppressTrailingZeroElements) {
    return DurationFormatUtils.formatDurationWords(durationMillis, suppressLeadingZeroElements, suppressTrailingZeroElements);
  }

  // -----------------------------------------------------------------------
  /**
   * <p>
   * Get the time gap as a string.
   * </p>
   * 
   * <p>
   * The format used is the ISO8601 period format.
   * </p>
   * 
   * @param startMillis
   *          the start of the duration to format
   * @param endMillis
   *          the end of the duration to format
   * @return the time as a String
   */
  public static String formatPeriodISO(long startMillis, long endMillis) {
    return DurationFormatUtils.formatPeriodISO(startMillis, endMillis);
  }

  /**
   * <p>
   * Get the time gap as a string, using the specified format. Padding the left
   * hand side of numbers with zeroes is optional.
   * 
   * @param startMillis
   *          the start of the duration
   * @param endMillis
   *          the end of the duration
   * @param format
   *          the way in which to format the duration
   * @return the time as a String
   */
  public static String formatPeriod(long startMillis, long endMillis, String format) {
    return DurationFormatUtils.formatPeriod(startMillis, endMillis, format);
  }

  /**
   * <p>
   * Get the time gap as a string, using the specified format. Padding the left
   * hand side of numbers with zeroes is optional and the timezone may be
   * specified.
   * 
   * @param startMillis
   *          the start of the duration
   * @param endMillis
   *          the end of the duration
   * @param format
   *          the way in which to format the duration
   * @param padWithZeros
   *          whether to pad the left hand side of numbers with 0's
   * @param timezone
   *          the millis are defined in
   * @return the time as a String
   */
  public static String formatPeriod(long startMillis, long endMillis, String format, boolean padWithZeros, TimeZone timezone) {
    return DurationFormatUtils.formatPeriod(startMillis, endMillis, format, padWithZeros, timezone);
  }

  public static long getDateSubDays(String sDate1, String sDate2, String sFormat) throws Exception {
    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTime(new SimpleDateFormat(sFormat).parse(sDate1));
    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(new SimpleDateFormat(sFormat).parse(sDate2));

    long timethis = calendar1.getTimeInMillis();
    long timeend = calendar2.getTimeInMillis();
    long theday = (timethis - timeend) / (1000 * 60 * 60 * 24);
    return theday;
  }
  /**
   * 获取当前天
   * @return
   * @author Space
   * new add
   */
  public static int getDay() {
      Calendar cal = Calendar.getInstance();
      int year = cal.get(Calendar.DAY_OF_MONTH);
      return year;
    }
  public static int getYear() {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    return year;
  }

  public static int getMonth() {
    Calendar cal = Calendar.getInstance();
    int month = cal.get(Calendar.MONTH) + 1;
    return month;
  }

  public static int getMonth(String sTime) throws Exception {
    Date date = getDateByString(sTime);
    int iMonth = date.getMonth() + 1;
    return iMonth;
  }
  /**
   * 获取最近几个月“年月”
   * @param iMonth 几个月
   * @param dateFormat 格式 如：201909/2019-02
   * @return
   * @author Space
   * @DateTime 20190704 new add
   */
  public static List<String> getLastNMonths(int iMonth, String dateFormat){
     List<String> monthList = new ArrayList();
     if("".equals(dateFormat)|| dateFormat == null){
         dateFormat = "yyyyMM";
     }
     for(int i = 0; i<iMonth; i++){
         SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
         Calendar c = Calendar.getInstance();
         c.setTime(new Date());
         c.add(Calendar.MONTH, -i);
         Date m = c.getTime();
         monthList.add(sdf.format(m));
     }
     return monthList;
  }
  public static String getQuarter(int month) {
    if (month > 0 && month <= 3) {
      return "SP";
    } else if (month > 3 && month <= 6) {
      return "SU";
    } else if (month > 6 && month <= 9) {
      return "AU";
    } else {
      return "WI";
    }
  }

  public static String getSemiAnnual(int month) {
    if (month > 0 && month <= 6) {
      return "S";
    } else {
      return "X";
    }
  }

  public static String getEndDate(String sStartDate, String sDateType, int iDate) throws Exception {
    int iYear = 0, iMonth = 0, iDay = 0, iHour = 0, iMinute = 0, iSecond = 0;
    if ("10".equals(sDateType)) {
      iYear = iDate;
    } else if ("20".equals(sDateType)) {
      iMonth = iDate;
    } else if ("30".equals(sDateType)) {
      iDay = iDate;
    }
    return getRelativeTime(sStartDate, iYear, iMonth, iDate, iHour, iMinute, iSecond);
  }

  public static long getBetween(String sFromDate, String sToDate) throws Exception {
    return getBetween(sFromDate, sToDate, "", "");
  }

  public static long getBetween(String sFromDate, String sToDate, String sFormat, String sTimeType) throws Exception {
    if (CommonUtil.ifIsEmpty(sFormat))
      sFormat = FORMAT_DATE_TIME_MS;
    return getBetween(parseString2Date(sFromDate, sFormat), parseString2Date(sToDate, sFormat), sTimeType);
  }

  /**
   * 根据传入时间类型sTimeType，返回两个日期之间相差的数量
   * 
   * @param fromDate
   * @param toDate
   * @param sTimeType
   *          year-年；month-月；day-日；hour-时；min-fen；sec-秒
   * @return
   * @throws Exception
   */
  public static long getBetween(Date fromDate, Date toDate, String sTimeType) throws Exception {
    long l1 = fromDate.getTime();
    long l2 = toDate.getTime();
    if ("year".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / (365l * 24 * 3600000));
    } else if ("month".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / (31l * 24 * 3600000));
    } else if ("day".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / (24l * 3600000));
    } else if ("hour".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / 3600000);
    } else if ("min".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / 60000);
    } else if ("sec".equalsIgnoreCase(sTimeType)) {
      return Math.round(1.0 * (l2 - l1) / 1000);
    } else {
      return Math.round(1.0 * (l2 - l1));
    }
  }

  /**
   * 日期字符转日期对象
   * 
   * @param sTime
   *          格式为yyyy-MM-dd或yyyy-MM-dd hh:mm:ss
   * @return
   * @throws Exception
   */
  /*
   * public static Date getDateByString(String sTime) throws Exception {
   * DateFormat df; if (sTime.length() == 10) { df = new
   * SimpleDateFormat("yyyy-MM-dd"); } else { df = new SimpleDateFormat(
   * "yyyy-MM-dd hh:mm:ss"); } return df.parse(sTime); }
   */
  public static Date getDateByString(String sTime) throws Exception {
    Date dReturn = null;
    if (CommonUtil.ifIsEmpty(sTime)) {
      return dReturn;
    }
    try {
      String sDateFormar = "";
      if (sTime.indexOf("-") > 0) {
        if (sTime.length() == 10) {
          sDateFormar = "yyyy-MM-dd";
        } else if (sTime.length() == 19) {
          sDateFormar = "yyyy-MM-dd hh:mm:ss";
        } else {
          throw new Exception("不支持的日期格式：" + sTime);
        }
      } else if (sTime.indexOf("/") > 0) {
        if (sTime.length() == 10) {
          sDateFormar = "yyyy/MM/dd";
        } else if (sTime.length() == 19) {
          sDateFormar = "yyyy/MM/dd hh:mm:ss";
        } else {
          throw new Exception("不支持的日期格式：" + sTime);
        }
      } else {
        if (sTime.length() == 8) {
          sDateFormar = "yyyyMMdd";
        } else {
          throw new Exception("不支持的日期格式：" + sTime);
        }
      }
      DateFormat df = new SimpleDateFormat(sDateFormar);
      dReturn = df.parse(sTime);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    /*
     * if (sTime.length() == 10) { DateFormat df = new
     * SimpleDateFormat(sDateFormar); sReturn = df.parse(sTime); } else if
     * (sTime.length() == 19) { DateFormat df = new SimpleDateFormat(
     * "yyyy-MM-dd hh:mm:ss"); sReturn = df.parse(sTime); } else { throw new
     * Exception("不支持的日期格式：" + sTime); }
     */
    return dReturn;
  }

  /**
   * 获取指定日期的N个月后的日期
   * 
   * @param inputDate
   *          指定日期
   * @param iMonth
   *          N个月
   * @param sInputFormat
   *          输入日期格式
   * @param sOutputFormat
   *          输出日期格式
   * @return {String}
   */
  public static String getAfterMonthDate(String inputDate, int iMonth, String sInputFormat, String sOutputFormat) {
    Calendar c = Calendar.getInstance();
    if (CommonUtil.ifIsEmpty(sInputFormat))
      sInputFormat = "yyyy-MM-dd";
    if (CommonUtil.ifIsEmpty(sOutputFormat))
      sOutputFormat = "yyyy-MM-dd";
    SimpleDateFormat inputFormat = new SimpleDateFormat(sInputFormat);
    SimpleDateFormat outputFormat = new SimpleDateFormat(sOutputFormat);
    Date date = null;
    try {
      date = inputFormat.parse(inputDate);
    } catch (Exception e) {

    }
    c.setTime(date);// 设置日历时间
    c.add(Calendar.MONTH, iMonth);
    String sOutDate = outputFormat.format(c.getTime());
    return sOutDate;
  }
  
  /**
   * 获得时间间隔向上取整
   * @param fromDate
   * @param toDate
   * @param sTimeType
   * @return
   * @throws Exception
   */
  public static double getBetweenCeil(Date fromDate, Date toDate, String sTimeType) throws Exception {
    long l1 = fromDate.getTime();
    long l2 = toDate.getTime();
    if ("year".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/(365l*24*3600000));
    } else if ("month".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/(31l*24*3600000));
    } else if ("day".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/(24l*3600000));
    } else if ("hour".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/3600000);
    } else if ("min".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/60000);
    } else if ("sec".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1)/1000);
    } else if ("sss".equalsIgnoreCase(sTimeType)) {
      return Math.ceil(1.0*(l2 - l1));
    } else {
      throw new Exception("不支持的时间类型：" + sTimeType);
    }
  }

  //获取给出日期的前多少天的日期数组,包括当天
  public static String[] getBeforeDates(int iDays) throws Exception {
    return getBeforeDates(getNowTime("yyyy-MM-dd"), iDays, "yyyy-MM-dd");
  }
  
  public static String[] getBeforeDates(int iDays, String sFormat) throws Exception {
    return getBeforeDates(getNowTime("yyyy-MM-dd"), iDays, sFormat);
  }
  
  public static String[] getBeforeDates(String inputDate, int iDays) throws Exception {
    return getBeforeDates(inputDate, iDays, "yyyy-MM-dd");
  }

  public static String[] getBeforeDates(String inputDate, int iDays, String sFormat) throws Exception {
    String[] a = new String[iDays];
    for (int i = 0; i < iDays; i++) {
      a[i] = getRelativeDate(inputDate, 0, 0, -i, "yyyy-MM-dd");
    }
    return a;
  }

  public static String[] getAfterDates(int iDays) throws Exception {
    return getAfterDates(getNowTime("yyyy-MM-dd"), iDays, "yyyy-MM-dd");
  }
  
  public static String[] getAfterDates(int iDays, String sFormat) throws Exception {
    return getAfterDates(getNowTime("yyyy-MM-dd"), iDays, sFormat);
  }

  public static String[] getAfterDates(String inputDate, int iDays) throws Exception {
    return getAfterDates(inputDate, iDays, "yyyy-MM-dd");
  }

  public static String[] getAfterDates(String inputDate, int iDays, String sFormat) throws Exception {
    String[] a = new String[iDays];
    for (int i = 0; i < iDays; i++) {
      a[i] = getRelativeDate(inputDate, 0, 0, i, "yyyy-MM-dd");
    }
    return a;
  }
  /**
   * 获取月份的天数集合
   * 如果是当月就获取到当前天为止
   * @param yearParam
   * @param monthParam 月份参数，输入7：获得8月数据
   * @return
   * @author Space
   * new add
   * @DateTime 2019-08-20
   */
  public static List<String> getDayByMonth(int yearParam, int monthParam){
      List<String> list = new ArrayList();
      Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
      aCalendar.set(yearParam,monthParam,1);
      int year = aCalendar.get(Calendar.YEAR);//年份
      int month = aCalendar.get(Calendar.MONTH) + 1;//月份
      int day = aCalendar.getActualMaximum(Calendar.DATE);
      if(month == DateUtil.getMonth()){
          day = DateUtil.getDay();
      }
      for (int i = 1; i <= day; i++) {
          String aDate=null;
          if(month<10&&i<10){
                   aDate = String.valueOf(year)+"-0"+month+"-0"+i;
                   }
          if(month<10&&i>=10){
                      aDate = String.valueOf(year)+"-0"+month+"-"+i;
                      }
          if(month>=10&&i<10){
                      aDate = String.valueOf(year)+"-"+month+"-0"+i;
                      }
          if(month>=10&&i>=10){
              aDate = String.valueOf(year)+"-"+month+"-"+i;
              }

          list.add(aDate);
      }
      return list ;
  }
}
