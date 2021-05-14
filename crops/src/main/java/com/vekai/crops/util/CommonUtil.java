package com.vekai.crops.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.*;

public class CommonUtil {
  public static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
  public static String IV = "F27D5C9927726BCEFE7510B1BDD3D137";
  public static String SALT = "3FF2EC019C627B945225DEBAD71A01B6985FE84C95A70EB132882F88C0A59A55";
  private static final int KEY_SIZE = 128;
  private static final int ITERATION_COUNT = 1;
  final static int BUFFER_SIZE = 4096;
  private static final Charset UTF_8 = Charset.forName("UTF-8");
  public static final String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
      "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C",
      "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

  public CommonUtil() {
    // mapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
  }

  /**** Log_OPERATION_begin ********************************************************************************************/
  /*
   * public static void logTrade(String sUser, String sAction, String sResult) {
   * logApp(sUser, "trade", "", "", sAction, sResult); }
   *
   * public static void logError(String sUser, String sAction, String sResult) {
   * logApp(sUser, "error", "", "", sAction, sResult); }
   *
   * public static void logApp(String sUser, String sType, String sObject,
   * String sFunction, String sAction, String sResult) { try { String t =
   * ConfigUtil.App + "_log_" + sType; String sJson = "{'a':'" + sUser +
   * "','b':'" + getTodayNow() + "','d':'" + sObject + "','e':'" + sFunction +
   * "','f':'" + StringUtils.replace(sAction, "\'", "\\\'") + "','g':'" +
   * StringUtils.replace(sResult, "\'", "\\\'") + "'}"; ObjectUtil.insert(t,
   * sJson, ""); } catch (Throwable e) { e.printStackTrace(); logger.error(
   * "log failed. Action=" + sAction + " Result=" + sResult + "\n" + e); } }
   *
   * public static void log(String sUser, String sType, String sContent) {
   * log(sUser, sType, "", "", sContent); }
   *
   * public static void log(String sUser, String sType, String sObject, String
   * sFunction, String sContent) { try { String t = ConfigUtil.App + "_log_all";
   * String sJson = "{'a':'" + sUser + "','b':'" + getTodayNow() + "','c':'" +
   * sType + "','d':'" + sObject + "','e':'" + sFunction + "','f':'" +
   * StringUtils.replace(sContent, "\'", "\\\'") + "'}"; ObjectUtil.insert(t,
   * sJson, ""); } catch (Throwable e) { logger.error("log failed. " + sContent
   * + "\n" + e); } }
   */

  /**** Log_OPERATION_end **********************************************************************************************/

  public static void callShell(String shellString) {
    try {
      Process process = Runtime.getRuntime().exec(shellString);
      int exitValue = process.waitFor();
      if (0 != exitValue) {
        logger.error("call shell failed. error code is :" + exitValue);
      }
    } catch (Throwable e) {
      logger.error("call shell failed. " + e);
    }
  }

  /*
   * public static String getNowTime(String format) { return parseDateFormat(new
   * Date(), format); }
   *
   * public static String parseDateFormat(Date date, String sFormat) {
   * SimpleDateFormat sdf = new SimpleDateFormat(sFormat); GregorianCalendar gc
   * = new GregorianCalendar(); gc.setTime(date); return
   * sdf.format(gc.getTime()); }
   */

  public static Map cloneMap(Map map) {
    Map o = new HashMap();
    if (map != null) {
      Iterator it = map.entrySet().iterator();
      while (it.hasNext()) {
        Entry<String, Object> entry = (Entry<String, Object>) it.next();
        o.put(entry.getKey(), "" + entry.getValue());
      }
    }
    return o;
  }

  @Deprecated
  public static boolean isContain(String s1, String s2) {
    if (s1 == null || s2 == null) {
      return false;
    } else {
      String[] a1 = StringUtils.splitPreserveAllTokens(s1, ",");
      String[] a2 = StringUtils.splitPreserveAllTokens(s2, ",");

      boolean bContain = false;
      for (int i = 0; i < a1.length; i++) {
        for (int j = 0; j < a2.length; j++) {
          if (a1[i].equals(a2[j])) {
            return true;
          }
        }
      }
      return bContain;
    }
  }

  @Deprecated
  public boolean ifContain(String s1, String s2) {
    if (s1 == null || s2 == null) {
      return false;
    } else {
      String[] a1 = StringUtils.splitPreserveAllTokens(s1, ",");
      String[] a2 = StringUtils.splitPreserveAllTokens(s2, ",");

      boolean bContain = false;
      for (int i = 0; i < a1.length; i++) {
        for (int j = 0; j < a2.length; j++) {
          if (a1[i].equals(a2[j])) {
            return true;
          }
        }
      }
      return bContain;
    }
  }

  public static int hashCode(String sInput) {
    return sInput.hashCode();
  }

  /**
   * str1是否包含str2
   *
   * @param sInput1
   * @param sInput2
   *          防重复标识号
   * @return
   */
  public static boolean ifIsIndexOf(String sInput1, String sInput2) {
    return ifIsIndexOf(sInput1, sInput2, ",");
  }

  public static boolean ifIsIndexOf(String sInput1, String sInput2, String separator) {
    boolean bReturn = false;
    if (sInput1 != null && sInput2 != null && !"".equals(sInput1) && !"".equals(sInput2)) {
      if ((separator + sInput1 + separator).indexOf(separator + sInput2 + separator) >= 0) {
        bReturn = true;
      }
    }
    return bReturn;
  }

  /**
   * 校验数字是100的整数倍
   *
   * @param n
   * @return
   */
  public static boolean isNumberBy100(double n) {
    n = (int) n;
    if (n <= 0) {
      return false;
    }
    if (n % 100 == 0) {
      return true;
    } else {
      return false;
    }
  }

  public static String getBetweenString(String s, String s1, String s2) {
    return getBeforeString(getAfterString(s, s1), s2);
  }

  public static String getBeforeString(String s, String s1) {
    if (isNull(s)) {
      return "";
    } else {
      int i = s.indexOf(s1);
      if (i > -1) {
        return s.substring(0, i).toString().trim();
      } else {
        return "";
      }
    }
  }

  public static String getAfterString(String s, String s1) {
    if (isNull(s)) {
      return "";
    } else {
      int i = s.indexOf(s1);
      if (i > -1) {
        return s.substring(i + s1.length()).toString().trim();
      } else {
        return "";
      }
    }
  }

  public static String getBeforeString1(String s, String s1) {
    if (isNull(s)) {
      return "";
    } else {
      int i = s.indexOf(s1);
      if (i > -1) {
        return s.substring(0, i).toString().trim();
      } else {
        return s;
      }
    }
  }

  public static String getAfterString1(String s, String s1) {
    if (isNull(s)) {
      return "";
    } else {
      int i = s.indexOf(s1);
      if (i > -1) {
        return s.substring(i + s1.length()).toString().trim();
      } else {
        return "";
      }
    }
  }

  public static String getNotNullString(Object o) {
    if (isNull(o)) {
      return "";
    } else {
      return o.toString();
    }
  }

  public static String getNotNullString(String o) {
    if (isNull(o)) {
      return "";
    } else {
      return o;
    }
  }

  /**
   * 判断是否为空对象
   *
   * @param o
   * @return
   */
  @Deprecated
  public static boolean isNull(Object o) {
    if (o == null || o.equals("") || StringUtils.isBlank("" + o) || o.equals("null") || o.equals("undefined")) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断是否为空对象
   *
   * @param o
   * @return
   */
  @Deprecated
  public static boolean isNotNull(Object o) {
    return !isNull(o);
  }

  /**
   * 判断对象是否为null
   *
   * @param input
   *          输入参数
   * @return {Boolean}
   */
  public static boolean ifIsNull(Object input) {
    if (null == input)
      return true;
    return false;
  }

  /**
   * 判断对象是否不为null
   *
   * @param input
   *          输入参数
   * @return {Boolean}
   */
  public static boolean ifIsNotNull(Object input) {
    return !ifIsNull(input);
  }

  /**
   * 根据对象类型判断是否为empty
   * <ul>
   * <li>String: null、""、" "、\t(制表符)、\n(换行符)、\f(换页符)、\r(回车符)</li>
   * <li>Integer: null、0</li>
   * <li>Double: null、0</li>
   * <li>Float: null、0</li>
   * <li>Array: null、size=0</li>
   * <li>List: null、size=0</li>
   * <li>Set: null、size=0</li>
   * <li>Map: null、size=0</li>
   * <li>JsonNode: null、{}、""、""""、[]</li>
   * </ul>
   *
   * @param <T>
   * @param input
   *          输入参数
   * @return {Boolean}
   */
  public static <T> boolean ifIsEmpty(T input) {
    return ifIsEmpty(input, null);
  }

  /**
   * 根据对象类型或指定对象类型判断是否为empty
   * <ol>
   * <li>若指定对象类型ifIsNotEmpty,优先处理</li>
   * </ol>
   * <ul>
   * <li>String: null、""、" "、\t(制表符)、\n(换行符)、\f(换页符)、\r(回车符)</li>
   * <li>Integer: null、0</li>
   * <li>Double: null、0</li>
   * <li>Float: null、0</li>
   * <li>Array: null、size=0</li>
   * <li>List: null、size=0</li>
   * <li>Set: null、size=0</li>
   * <li>Map: null、size=0</li>
   * <li>JsonNode: null、{}、""、""""、[]</li>
   * </ul>
   *
   * @param <T>
   * @param input
   *          输入参数
   * @param sObjectType
   *          指定的对象类型
   * @return {Boolean}
   */
  public static <T> boolean ifIsEmpty(T input, String sObjectType) {
    if (ifIsNull(input))
      return true;
    if (StringUtils.isNotBlank(sObjectType)) {
      if ("JsonNode".equals(sObjectType)) {
        String sInput = input.toString();
        return "{}".equals(sInput) || "".equals(sInput) || "null".equals(sInput) || "\"\"".equals(sInput) || "[]".equals(sInput);
      }
    }
    if (input instanceof String) {
      return (StringUtils.isBlank("" + input) || "''".equals(input));
    } else if (input instanceof Integer) {
      return 0 == (Integer) input;
    } else if (input instanceof Double) {
      return 0 == (Double) input;
    } else if (input instanceof Float) {
      return 0 == (Float) input;
    } else if (input.getClass().isArray()) {
      return Array.getLength(input) == 0;
    } else if (input instanceof Collection) {
      return ((Collection<?>) input).size() == 0;
    } else if (input instanceof List) {
      return ((List<?>) input).size() == 0;
    } else if (input instanceof Set) {
      return ((Set<?>) input).size() == 0;
    } else if (input instanceof Map) {
      return ((Map<?, ?>) input).size() == 0;
    } else if (input instanceof JsonNode) {
      String sInput = input.toString();
      return "{}".equals(sInput) || "".equals(sInput) || "null".equals(sInput) || "\"\"".equals(sInput) || "[]".equals(sInput);
    }
    return false;
  }

  public static boolean ifIsNotEmpty(Object input) {
    return !ifIsEmpty(input);
  }

  public static boolean ifIsNotEmpty(Object input, String sObjectType) {
    return !ifIsEmpty(input, sObjectType);
  }

  public static boolean ifIsEmptyJson(String sJson) {
    return ifIsEmpty(sJson, "JsonNode");
  }

  public static boolean ifIsNotEmptyJson(String sJson) {
    return !ifIsEmpty(sJson, "JsonNode");
  }

  public static boolean ifIsTrue(String sInput) {
    if ("true".equals(sInput))
      return true;
    return false;
  }

  public static boolean ifIsFalse(String sInput) {
    if ("false".equals(sInput))
      return true;
    return false;
  }

  /**
   * 判断是否为空json
   *
   * @param o
   * @return
   */
  @Deprecated
  public static boolean isNullJson(Object o) {
    if (o == null || o.equals("") || StringUtils.isBlank("" + o) || o.equals("null") || o.equals("undefined")
        || "{}".equals(o.toString()) || "\"\"".equals(o.toString())) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断是否为空json
   *
   * @param o
   * @return
   */
  @Deprecated
  public static boolean isNotNullJson(Object o) {
    return !isNullJson(o);
  }

  @Deprecated
  public static boolean isValidPhoneNumber(String nm) {
    if (nm == null)
      return false;
    String n = StringUtils.trim(nm);
    String[] ns = n.split(",");
    boolean v = true;
    for (String x : ns) {
      if (!x.matches("1\\d{10}")) {
        v = false;
        break;
      }
    }
    return v;
  }

  public static boolean ifIsValidPhoneNo(String sInput) {
    String regExp = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(16[6])|(17[3,5,6,7,8])|(19[8,9]))\\d{8}$";
    Pattern pattern = Pattern.compile(regExp);
    Matcher matcher = pattern.matcher(sInput);
    return matcher.matches();
  }

  /**
   * 校验是否为正整数
   *
   * @param sData
   * @return
   */
  public static boolean ifIsPositiveInteger(String sData) {
    String regExp = "^[1-9]\\d*$";
    Pattern pattern = Pattern.compile(regExp);
    Matcher matcher = pattern.matcher(sData);
    return matcher.matches();
  }

  public static boolean ifIsChineseCharacters(String sData) {
    String regExp = "^[\u4e00-\u9fa5]{0,}$";
    Pattern pattern = Pattern.compile(regExp);
    Matcher matcher = pattern.matcher(sData);
    return matcher.matches();
  }

  /**
   * 记录日志
   *
   * @return
   */
  /*
   * public static void log(String sTime, String sUser, String sAction, String
   * sContent) { try { DBUtil.updateObjectNolog("logtrade" + sTime.substring(0,
   * 7), "{'_id':'" + sUser + "'}", "{\"" + sAction + "." + sTime + "\":\"" +
   * StringUtils.replace(sContent, "\"", "\\\"").replace("\n", "\\n") + "\"}",
   * "", "", false, true); } catch (Exception e) { logger.error("logger error "
   * + e); } }
   *
   * public static void logdata(String sTime, String sUser, String sAction,
   * String sContent) { try { DBUtil.addToSetObject("logdata" +
   * sTime.substring(0, 7), "{'_id':'" + sUser + "'}", "{\"" + sAction + "." +
   * sTime + "\":\"" + StringUtils.replace(sContent, "\"", "\\\"").replace("\n",
   * "\\n") + "\"}", sUser); } catch (Exception e) { logger.error(
   * "logger error " + e); } }
   */

  /*  *//**
   * 获取当前时间，精确到毫秒
   *
   * @return
   */
  /*
   * public static String getTodayNow() { Calendar cal = Calendar.getInstance();
   * return sdFormat.format(cal.getTime()); }
   *
   * public static int getCurMonth() { Calendar cal = Calendar.getInstance();
   * int month = cal.get(Calendar.MONTH) + 1; return month; }
   *
   * public static int getCurYear() { Calendar c = Calendar.getInstance();
   *
   * return c.get(Calendar.YEAR); }
   *
   * public static int getCurDay() { Calendar c = Calendar.getInstance();
   *
   * return c.get(Calendar.DAY_OF_MONTH); }
   *//**
   * 获取当前月的第一天，精确到毫秒
   *
   * @return
   */
  /*
   * public static String getCurMonthFirstDay() { Calendar c =
   * Calendar.getInstance(); c.add(Calendar.MONTH, 0);
   * c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天 String first =
   * sdFormat.format(c.getTime()); // logger.info("===============Curfirst:" +
   * first); return first; }
   *//**
   * 获取当前月的最后一天，精确到毫秒
   *
   * @return
   */
  /*
   * public static String getCurMonthLastDay() { Calendar ca =
   * Calendar.getInstance(); ca.set(Calendar.DAY_OF_MONTH,
   * ca.getActualMaximum(Calendar.DAY_OF_MONTH)); String last =
   * sdFormat.format(ca.getTime()); // logger.info("===============Curlast:" +
   * last); return last; }
   *//**
   * 获取接下来几个月的第一天
   *
   * @param addNums
   * @return
   */
  /*
   * public static String getNextMonthFirstDay(int addNums) { Calendar c =
   * Calendar.getInstance(); c.add(Calendar.MONTH, addNums);
   * c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天 String first =
   * sdFormat.format(c.getTime()); // logger.info("===============NextAddfirst:"
   * + first); return first; }
   *//**
   * 获取接下来几个月的最后一天
   *
   * @param addNums
   * @return
   */
  /*
   * public static String getNextMonthLastDay(int addNums) { Calendar ca =
   * Calendar.getInstance(); ca.add(Calendar.MONTH, addNums);
   * ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
   * String last = sdFormat.format(ca.getTime()); //
   * logger.info("===============NextAddlast:" + last); return last; }
   *//**
   * 获取某个月的第一天，精确到毫秒
   *
   * @return
   */
  /*
   * public static String getOtherMonthFirstDay(String year, String month) {
   * Calendar c = Calendar.getInstance(); c = Calendar.getInstance();
   * c.set(Calendar.YEAR, Integer.parseInt(year));// 设置年 c.set(Calendar.MONTH,
   * Integer.parseInt(month) - 1);// 设置月 c.set(Calendar.DAY_OF_MONTH, 1);//
   * 设置为1号,当前日期既为本月第一天 String first = sdFormat.format(c.getTime()); //
   * logger.info("===============Otherfirst:" + first); return first; }
   *//**
   * 获取某个月的最后一天，精确到毫秒
   *
   * @return
   */

  /*
   * public static String getOtherMonthLastDay(String year, String month) {
   * Calendar c = Calendar.getInstance(); c = Calendar.getInstance();
   * c.set(Calendar.YEAR, Integer.parseInt(year));// 设置年 c.set(Calendar.MONTH,
   * Integer.parseInt(month) - 1);// 设置月 c.set(Calendar.DAY_OF_MONTH,
   * c.getActualMaximum(Calendar.DAY_OF_MONTH)); String last =
   * sdFormat.format(c.getTime()); // logger.info("===============Otherlast:" +
   * last); return last; }
   *//**
   * 获取合同时间流水号
   *
   * @return
   */
  /*
   * public static String getContractNo() { return "C" +
   * CommonUtil.getTodayNow().replace("/", "").replace("-", "").replace(":",
   * "").replace(" ", ""); }
   */

  public static String getReplaceStr(String message, String node) throws Exception {
    String[] str = node.split(";");
    String key = "", value = "";
    DecimalFormat myformat = new DecimalFormat();
    myformat.applyPattern("##,###.00");
    for (int i = 0; i < str.length; i++) {
      if (!str[i].contains(","))
        continue;
      key = str[i].split(",")[0];
      if (key == null)
        continue;
      value = str[i].split(",")[1];
      if (value == null)
        continue;
      if ("j".equalsIgnoreCase(key) || "m".equalsIgnoreCase(key)) {
        value = myformat.format(Double.valueOf(value));
      }
      message = message.replace("{" + key + "}", value);
    }
    return message;
  }

  /**
   * 提供精确的加法运算。
   *
   * @param dValue1
   *          被加数
   * @param dValue2
   *          加数
   * @return 两个参数的和
   */
  public static double add(double dValue1, double dValue2) {
    BigDecimal bg1 = new BigDecimal(Double.toString(dValue1));
    BigDecimal bg2 = new BigDecimal(Double.toString(dValue2));
    return bg1.add(bg2).doubleValue();
  }

  /**
   * 提供精确的减法运算。
   *
   * @param dValue1
   *          被减数
   * @param dValue2
   *          减数
   * @return 两个参数的差
   */
  public static double subtract(double dValue1, double dValue2) {
    BigDecimal bg1 = new BigDecimal(Double.toString(dValue1));
    BigDecimal bg2 = new BigDecimal(Double.toString(dValue2));
    return bg1.subtract(bg2).doubleValue();
  }

  /**
   * 提供精确的乘法运算。
   *
   * @param dValue1
   *          被乘数
   * @param dValue2
   *          乘数
   * @return 两个参数的积
   */
  public static double multiply(double dValue1, double dValue2) {
    BigDecimal bg1 = new BigDecimal(Double.toString(dValue1));
    BigDecimal bg2 = new BigDecimal(Double.toString(dValue2));
    return bg1.multiply(bg2).doubleValue();
  }

  /**
   * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
   *
   * @param dValue1
   *          被除数
   * @param dValue2
   *          除数
   * @param scale
   *          表示表示需要精确到小数点以后几位。
   * @return 两个参数的商
   */
  public static double divide(double dValue1, double dValue2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal bg1 = new BigDecimal(Double.toString(dValue1));
    BigDecimal bg2 = new BigDecimal(Double.toString(dValue2));
    return bg1.divide(bg2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  public static int doubleCompare(double dValue1, double dValue2) {
    BigDecimal bg1 = new BigDecimal(Double.toString(dValue1));
    BigDecimal bg2 = new BigDecimal(Double.toString(dValue2));
    return bg1.compareTo(bg2);
  }

  public static String getUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * 修改查询区域格式为对象形式
   *
   * @param sField
   * @return
   */
  public static String changeFieldToObject(String sField) {
    return changeFieldToObject(sField, "1");
  }

  public static String changeFieldToObject(String sField, String sValue) {
    if (!CommonUtil.isNull(sField) && !"{}".equals(sField)) {
      if ((sField.contains(":") && sField.contains("-") && getStrNumbers(sField, "-") % 2 == 0 && getStrNumbers(sField, ":") % 2 == 0)
          || !sField.contains(":")) {
        sField = sField + ",";
        String[] sFields = sField.split(",");
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        int num = sFields.length;
        for (int i = 0; i < num; i++) {
          if (!CommonUtil.isNull(sFields[i])) {
            sb.append("'" + sFields[i] + "':" + sValue);
            if (i < num - 1)
              sb.append(",");
          }
        }
        sb.append("}");
        sField = sb.toString();
      }
    }
    return sField;
  }

  // 查询包含字符个数
  public static int getStrNumbers(String src, String find) {
    int o = 0;
    int index = -1;
    while ((index = src.indexOf(find, index)) > -1) {
      ++index;
      ++o;
    }
    return o;
  }

  public static void checkID(String sID) throws Exception {
    if (isNull(sID)) {
      throw new Exception("ID不能为空");
    }
  }

  public static String getArrayListString(ArrayList list) throws Exception {
    StringBuffer sb = new StringBuffer();
    int i = 0;
    for (Object o : list) {
      if (i != 0) {
        sb.append(",");
      }
      i++;
      sb.append(o);
    }
    return sb.toString();
  }

  /**
   * 对象值增加
   *
   * @param mapObject
   * @param sID
   * @param sField
   * @param dValue
   * @throws Exception
   */
  public static void incDoubleValue(HashMap<String, HashMap<String, Double>> mapObject, String sID, String sField, double dValue)
      throws Exception {
    HashMap<String, Double> map = mapObject.get(sID);
    if (!mapObject.containsKey(sID)) {
      map = new HashMap<String, Double>();
      mapObject.put(sID, map);
    }
    String[] aField = StringUtils.split(sField, ",");
    for (String s : aField) {
      double d = 0;
      if (map.containsKey(s)) {
        d = map.get(s);
      }
      // d += dValue;
      d = CommonUtil.add(d, dValue);
      map.put(s, d);
    }
  }

  /**
   * 对象值增加
   *
   * @param mapObject
   * @param sID
   * @param sField
   * @param dValue
   * @throws Exception
   */
  public static void incIntValue(HashMap<String, HashMap<String, Integer>> mapObject, String sID, String sField, double dValue)
      throws Exception {
    HashMap<String, Integer> map = mapObject.get(sID);
    if (!mapObject.containsKey(sID)) {
      map = new HashMap<String, Integer>();
      mapObject.put(sID, map);
    }
    String[] aField = StringUtils.split(sField, ",");
    for (String s : aField) {
      int d = 0;
      if (map.containsKey(s)) {
        d = map.get(s);
      }
      d += dValue;
      map.put(s, d);
    }
  }

  public static void addStrValue(HashMap<String, HashMap<String, String>> mapUser, String sID, String sField, String sValue)
      throws Exception {
    HashMap<String, String> map = mapUser.get(sID);
    if (!mapUser.containsKey(sID)) {
      map = new HashMap<String, String>();
      mapUser.put(sID, map);
    }
    String[] aField = StringUtils.split(sField, ",");
    for (String s : aField) {
      String ds = sValue;
      map.put(s, ds);
    }
  }

  @Override
  protected Object clone() throws CloneNotSupportedException {
    // TODO Auto-generated method stub
    return super.clone();
  }

  /*
   * public static String GetDateByIdent(String indetityID) throws Exception{
   *
   * return indetityID.substring("") }
   */
  /**
   * 处理日期
   *
   * @param doDate
   * @param addDays
   * @return
   * @throws Exception
   */
  public static String doWithDateAdd(String doDate, int addDays) throws Exception {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date newDate = sdf.parse(doDate);
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis(newDate.getTime());
    c.add(Calendar.DATE, addDays);
    Date reDate = new Date(c.getTimeInMillis()); // 将c转换成Date
    return sdf.format(reDate);
  }

  // 返回列表字符比较之差
  public static String getDiffListString(String s1, String s2) {
    String s = "";
    if (CommonUtil.isNull(s1) && CommonUtil.isNull(s2)) {
    } else if (CommonUtil.isNull(s1)) {
      String[] a = StringUtils.split(s2, ",");
      for (String s_ : a) {
        s += ",+" + s_;
      }
    } else if (CommonUtil.isNull(s2)) {
      String[] a = StringUtils.split(s1, ",");
      for (String s_ : a) {
        s += ",-" + s_;
      }
    } else {
      String[] a = StringUtils.split(s1, ",");
      for (String s_ : a) {
        if ((s2).indexOf("," + s_ + ",") == -1) {
          s += ",-" + s_;
        } else {
          s += "," + s_;
        }
      }
      a = StringUtils.split(s2, ",");
      for (String s_ : a) {
        if ((s1).indexOf("," + s_ + ",") == -1) {
          s += ",+" + s_;
        }
      }
    }
    if (!s.equals("")) {
      s = s.substring(1);
    }
    return s;
  }

  // 返回添加字符列表
  @Deprecated
  public static String appendListString(String s1, String s2) {
    String s = "";
    if (CommonUtil.isNull(s1) && CommonUtil.isNull(s2)) {
    } else if (CommonUtil.isNull(s1)) {
      s = "," + s2 + ",";
    } else if (CommonUtil.isNull(s2)) {
      s = "," + s1 + ",";
    } else {
      s = s1 + "," + s2 + ",";
    }
    return s;
  }

  // 返回删除字符列表
  @Deprecated
  public static String removeListString(String s1, String s2) {
    String s = "";
    if (CommonUtil.isNull(s1)) {
    } else if (CommonUtil.isNull(s2)) {
      s = s1;
    } else {
      if (s1.equals(s2)) {
        s = "";
      } else {
        // s = StringUtils.replace("," + s1 + ",", "," + s2 + ",", ",");
        s = StringUtils.replace(s1, "," + s2 + ",", "");
        // s = s.substring(1, s.length() - 1);
      }
    }
    return s;
  }

  /**
   * 删除sInput1中sInput2字符串，并返回删除后字符串
   *
   * @param sInput1
   * @param sInput2
   *          防重复标识号
   * @return
   */
  public static String removeString(String sInput1, String sInput2) {
    return removeString(sInput1, sInput2, ",");
  }

  public static String removeString(String sInput1, String sInput2, String separator) {
    String sResult = "";
    if (CommonUtil.isNull(sInput1) || sInput1.equals(sInput2)) {

    } else if (CommonUtil.isNull(sInput2)) {
      sResult = sInput1;
    } else {
      sResult = StringUtils.replace(separator + sInput1 + separator, separator + sInput2 + separator, ",");
      sResult = sResult.substring(1, sResult.length() - 1);
    }
    return sResult;
  }

  public static String getTripString(String s1) {
    s1 = StringUtils.replace(s1, "\"", "\\\"");
    s1 = StringUtils.replace(s1, "\n", "");
    s1 = StringUtils.replace(s1, "\r", "");
    return s1;
  }

  public static String md5(String str) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes("UTF-8"));
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return converterToHexString(md.digest()).toUpperCase();
  }

  public static String converterToHexString(byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for (byte b : bytes) {
      String str = Integer.toHexString(0xff & b);
      if (str.length() < 2)
        sb.append(0);
      sb.append(str);
    }
    return sb.toString();
  }

  /**
   * sha1加密
   *
   * @param str
   *          待加密内容
   * @return {String}
   */
  public static String sha1(String str) {
    if (null == str || 0 == str.length()) {
      return null;
    }
    char[] buf = {};
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    try {
      MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
      mdTemp.update(str.getBytes("UTF-8"));

      byte[] md = mdTemp.digest();
      int j = md.length;
      buf = new char[j * 2];
      int k = 0;
      for (int i = 0; i < j; i++) {
        byte byte0 = md[i];
        buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
        buf[k++] = hexDigits[byte0 & 0xf];
      }
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    return new String(buf);
  }

  public static boolean isNum(String str) {
    Pattern pattern = Pattern.compile("[0-9]*");

    return pattern.matcher(str).matches();
  }

  public static boolean isChar(String fstrData) {
    for (int i = 0; i < fstrData.length(); i++) {
      char c = fstrData.charAt(i);

      if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {

      } else {
        return false;
      }
    }
    return true;
  }

  /**
   * hashMap装换为JSONSTRING
   *
   * @param hashmap
   * @return
   */
  public static String getJsonString(HashMap<String, String> hashmap) {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    Iterator<Entry<String, String>> it = hashmap.entrySet().iterator();
    while (it.hasNext()) {
      Entry<String, String> entry = it.next();
      sb.append("\"" + entry.getKey() + "\":\"" + entry.getValue() + "\",");
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("}");
    return sb.toString();
  }

  /**
   * 将InputStream转换成String
   *
   * @param in
   *          InputStream
   * @return String
   * @throws Exception
   *
   */
  public static String InputStreamTOString(InputStream in) throws Exception {

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] data = new byte[BUFFER_SIZE];
    int count = -1;
    while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
      outStream.write(data, 0, count);

    data = null;
    return new String(outStream.toByteArray(), "ISO-8859-1");
  }

  /**
   * 将InputStream转换成某种字符编码的String
   *
   * @param in
   * @param encoding
   * @return
   * @throws Exception
   */
  public static String InputStreamTOString(InputStream in, String encoding) throws Exception {

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] data = new byte[BUFFER_SIZE];
    int count = -1;
    while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
      outStream.write(data, 0, count);

    data = null;
    return new String(outStream.toByteArray(), "ISO-8859-1");
  }

  /**
   * new add time : 201904 new add auther: zjxu base64String 转化为InputStream
   *
   * @param base64string
   * @return
   * @throws IOException
   */
  /*public static InputStream Base64ToInputStream(String base64string) throws IOException {
    ByteArrayInputStream stream = null;

    BASE64Decoder decoder = new BASE64Decoder();

    byte[] bytes1 = decoder.decodeBuffer(base64string);

    stream = new ByteArrayInputStream(bytes1);

    return stream;

  }*/

  /**
   * 将String转换成InputStream
   *
   * @param in
   * @return
   * @throws Exception
   */
  public static InputStream StringTOInputStream(String in) throws Exception {

    ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
    return is;
  }

  /**
   * 将InputStream转换成byte数组
   *
   * @param in
   *          InputStream
   * @return byte[]
   * @throws IOException
   */
  public static byte[] InputStreamTOByte(InputStream in) throws IOException {

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    byte[] data = new byte[BUFFER_SIZE];
    int count = -1;
    while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
      outStream.write(data, 0, count);

    data = null;
    return outStream.toByteArray();
  }

  /**
   * 将byte数组转换成InputStream
   *
   * @param in
   * @return
   * @throws Exception
   */
  public static InputStream byteTOInputStream(byte[] in) throws Exception {

    ByteArrayInputStream is = new ByteArrayInputStream(in);
    return is;
  }

  /**
   * 将byte数组转换成String
   *
   * @param in
   * @return
   * @throws Exception
   */
  public static String byteTOString(byte[] in) throws Exception {

    InputStream is = byteTOInputStream(in);
    return InputStreamTOString(is);
  }

  /**
   * 本算法利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，
   * 然后通过模62操作，结果作为索引取出字符 一千万次生成重复几率很小，满足大部分需求
   *
   * @return
   */
  public static String generateShortUuid() {
    StringBuffer shortBuffer = new StringBuffer();
    String uuid = UUID.randomUUID().toString().replace("-", "");
    for (int i = 0; i < 8; i++) {
      String str = uuid.substring(i * 4, i * 4 + 4);
      int x = Integer.parseInt(str, 16);
      shortBuffer.append(chars[x % 0x3E]);
    }
    return shortBuffer.toString();
  }

  /**
   * 根据.json对象配置获取所有配置的或指定的字段的默认值
   * <p>
   * 对象初始化配置：
   * <ul>
   * 'i':{</br> &ensp;'a':{</br> &emsp;'a':
   * '初始化类型，0：默认值进行初始化，1：采用特定方法进行初始化',</br> &emsp; 'b': '特殊初始化的配置定义
   * 1：getNextSerialNo，2：getToday，3：getNowTime',</br> &emsp;'c':
   * '初始化数据的前缀',</br> &emsp; 'v': '默认值进行初始化的值'</br> &ensp;}, <br>
   * &ensp;'e':''<br>
   * }
   * </ul>
   *
   * @param sObject
   *          对象名称
   * @param sFields
   *          要获取的初始化字段值
   * @param sUserID
   *          用户编号
   * @return JsonNode {'field1':'value', 'field2':'value'}
   * @throws Exception
   */
  /*
   * public static JsonNode getInitFieldValue(String sObject, String sFields,
   * String sUserID) throws Exception { String sFildValue = ""; JsonNode o =
   * ConfigUtil.Tables.get(sObject); Iterator<Entry<String, JsonNode>> it =
   * o.get("i").get("a").fields(); while (it.hasNext()) { Entry<String,
   * JsonNode> entry = it.next(); String sKey = entry.getKey(); if
   * (!isNull(sFields) && !ifIsIndexOf(sFields, sKey)) continue; JsonNode jValue
   * = entry.getValue(); if (CommonUtil.isNullJson(jValue)) continue; String
   * sInitValue = ""; String sInitType = JsonUtil.getJsonStringValue(jValue,
   * "a"); // 0:采用固定默认值，1:采用特殊方法 if ("1".equals(sInitType)) { String
   * sInitFunctionType = JsonUtil.getJsonStringValue(jValue, "b"); String
   * sPrefix = JsonUtil.getJsonStringValue(jValue, "c", ""); if
   * ("1".equals(sInitFunctionType)) { sInitValue = sPrefix +
   * ObjectUtil.getNextSerialNo(sObject, sUserID); } else if
   * ("2".equals(sInitFunctionType)) { sInitValue = sPrefix +
   * DateUtil.getToday(); } else if ("3".equals(sInitFunctionType)) { sInitValue
   * = sPrefix + DateUtil.getNowTime(); } sFildValue += ",'" + sKey + "':'" +
   * sInitValue + "'"; } else { sInitValue = JsonUtil.getJsonStringValue(jValue,
   * "v"); sFildValue += ",'" + sKey + "':'" + sInitValue + "'"; } } if
   * (!CommonUtil.isNull(sFildValue)) { sFildValue = "{" +
   * sFildValue.substring(1) + "}"; } else { sFildValue = "{}"; } return
   * JsonUtil.getJson(sFildValue); }
   */

  /**
   * 产生纯数字的随机数
   *
   * @param length
   * @return add user : wjzhang
   */
  public static String getRandomNumber(int length) {
    String base = "0123456789";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }

  /**
   * 随机数产生
   *
   * @param length
   * @return
   */
  public static String getRandomNum(int length) {
    String base = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }

  public static String getRandomNumberByABC(int length) {
    String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int number = random.nextInt(base.length());
      sb.append(base.charAt(number));
    }
    return sb.toString();
  }

  /**
   * 判断服务器间调用返回信息是否为有效
   *
   * @param sResult
   *          调用返回信息
   * @return {boolean}
   */
  /*
   * public static boolean ifIsSuccessResult(String sResult) { if
   * (CommonUtil.isNull(sResult)) return false; try { JsonNode jResult =
   * JsonUtil.getJson(sResult); return ifIsSuccessResult(jResult); } catch
   * (Exception e) { e.printStackTrace(); return false; } }
   *//**
   * 判断服务器间调用返回信息是否为有效
   *
   * @param sResult
   *          调用返回信息
   * @return {boolean}
   */
  /*
   * public static boolean ifIsSuccessResult(JsonNode jResult) throws Exception
   * { if (CommonUtil.isNullJson(jResult)) { return false; } String sError =
   * JsonUtil.getJsonStringValue(jResult, "s"); if ("0".equals(sError)) { return
   * false; } else { return true; } }
   */

  /**
   * Base64 编码
   *
   * @param bytes
   * @return {String}
   */
  public static String base64(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  /**
   * Base64 解码
   *
   * @param str
   * @return {byte[]}
   */
  public static byte[] base64(String str) {
    return Base64.decodeBase64(str);
  }

  /**
   * Hex 编码
   *
   * @param bytes
   * @return {String}
   */
  public static String hex(byte[] bytes) {
    return Hex.encodeHexString(bytes);
  }

  /**
   * Hex 解码
   *
   * @param str
   * @return {byte[]}
   */
  public static byte[] hex(String str) {
    try {
      return Hex.decodeHex(str.toCharArray());
    } catch (DecoderException e) {
      throw new IllegalStateException(e);
    }
  }

  public static int parseInt(String sInput) {
    return parseInt(sInput, 0);
  }

  /**
   * 将String格式的数字转换为int，若为非法数字格式，则采用默认值进行赋值
   *
   * @param sInput
   *          String 格式的数字
   * @param iDefault
   *          默认值
   * @return {int}
   */
  public static int parseInt(String sInput, int iDefault) {
    int iReturn = 0;
    try {
      iReturn = Integer.parseInt(sInput);
    } catch (Exception e) {
      iReturn = iDefault;
    }
    return iReturn;
  }

  public static double parseDouble(String sInput) {
    return parseDouble(sInput, 0);
  }

  /**
   * 将String格式的数字转换为int，若为非法数字格式，则采用默认值进行赋值
   *
   * @param sInput
   *          String 格式的数字
   * @param dDefault
   *          默认值
   * @return {int}
   */
  public static double parseDouble(String sInput, double dDefault) {
    double dReturn = 0;
    try {
      dReturn = Double.parseDouble(sInput);
    } catch (Exception e) {
      dReturn = dDefault;
    }
    return dReturn;
  }

  /**
   * k=3 c=yesno的是否的是判断
   *
   * @param sInput
   * @return
   */
  // public static boolean ifIsYesOrNoOfYes(String sInput) {
  // return "1".equals(sInput) ? true : false;
  // }

  /**
   * k=3 c=yesno的是否的否判断
   *
   * @param sInput
   * @return
   */
  // public static Boolean ifIsYesOrNoOfNo(String sInput) {
  // return !ifIsYesOrNoOfYes(sInput);
  // }

  /**
   * 获取k=3 c=yesno的是否的[是]值
   *
   * @return {String}
   */
  // public static String getYesOrNoOfYes() {
  // return getYesOrNo(1);
  // }

  /**
   * 获取k=3 c=yesno的是否的[否]值
   *
   * @return {String}
   */
  // public static String getYesOrNoOfNo() {
  // return getYesOrNo(0);
  // }

  /**
   * 获取k=3 c=yesno的是否值
   *
   * @param iType
   *          类型
   *          <ul>
   *          <li>0:否</li>
   *          <li>1:是</li>
   *          </ul>
   * @return {String}
   */
  // private static String getYesOrNo(int iType) {
  // String sYesOrNo = "";
  // switch (iType) {
  // case 1:
  // sYesOrNo = "1";
  // break; // 是
  // default:
  // sYesOrNo = "0";
  // }
  // return sYesOrNo;
  // }

  /**
   * 获取k=3 c=status的状态的[有效]值
   *
   * @return {String}
   */
  // public static String getStatusOfValid() {
  // return "1";
  // }

  /**
   * 获取k=3 c=status的状态的[无效]值
   *
   * @return {String}
   */
  // public static String getStatusOfInValid() {
  // return "0";
  // }
  //
  // public static boolean ifIsStatusOfValid(String sInput) {
  // return "1".equals(sInput) ? true : false;
  // }
  //
  // public static boolean ifIsStatusOfInValid(String sInput) {
  // return !ifIsStatusOfValid(sInput);
  // }

  /**
   * 判断两个字符串内容是否有交集
   *
   * @param sInput1
   *          字符串1
   * @param sInput2
   *          字符串2
   * @return {Boolean}
   */
  public static boolean ifIsContains(String sInput1, String sInput2) {
    return ifIsContains(sInput1, sInput2, ",");
  }

  public static boolean ifIsContains(String sInput1, String sInput2, String separator) {
    return ifIsContains(sInput1, separator, sInput2, separator);
  }

  /**
   * 判断两个字符串内容是否有交集
   *
   * @param sInput1
   *          字符串1
   * @param separator1
   *          字符串1分隔符
   * @param sInput2
   *          字符串2
   * @param separator2
   *          字符串2分隔符
   * @return {Boolean}
   */
  public static boolean ifIsContains(String sInput1, String separator1, String sInput2, String separator2) {
    if (sInput1 == null || sInput2 == null) {
      return false;
    } else {
      String[] aInput1 = StringUtils.splitPreserveAllTokens(sInput1, separator1);
      String[] aInput2 = StringUtils.splitPreserveAllTokens(sInput2, separator2);
      int iLength1 = aInput1.length;
      int iLength2 = aInput2.length;
      boolean bContain = false;
      for (int i = 0; i < iLength1; i++) {
        for (int j = 0; j < iLength2; j++) {
          if (aInput1[i].equals(aInput2[j])) {
            return true;
          }
        }
      }
      return bContain;
    }
  }

  public static boolean ifIsAllContains(String sInput1, String sInput2) {
    return ifIsAllContains(sInput1, sInput2, ",");
  }

  public static boolean ifIsAllContains(String sInput1, String sInput2, String separator2) {
    return ifIsAllContains(sInput1, separator2, sInput2, separator2);
  }

  public static boolean ifIsAllContains(String sInput1, String separator1, String sInput2, String separator2) {
    if (sInput1 == null || sInput2 == null) {
      return false;
    } else {
      String[] aInput1 = StringUtils.splitPreserveAllTokens(sInput1, separator1);
      String[] aInput2 = StringUtils.splitPreserveAllTokens(sInput2, separator2);
      int iLength1 = aInput1.length;
      int iLength2 = aInput2.length;
      boolean bContain = false;
      for (int i = 0; i < iLength1; i++) {
        boolean bThisContrain = false;
        for (int j = 0; j < iLength2; j++) {
          if (aInput1[i].equals(aInput2[j])) {
            bThisContrain = true;
            break;
          } else {
            continue;
          }
        }
        bContain = bThisContrain;
        if (!bContain)
          break;
      }
      return bContain;
    }
  }

  /**
   * 判断json中的key值是否和字符串有交集
   *
   * @param sInput
   *          字符串
   * @param jInput
   *          JsonNode
   * @return {Boolean}
   * @throws Exception
   */
  public static boolean ifisContains(String sInput, JsonNode jInput) throws Exception {
    return ifisContains(sInput, ",", jInput);
  }

  /**
   * 判断json中的key值是否和字符串有交集
   *
   * @param sInput
   *          字符串
   * @param sSeparator
   *          字符串分隔符
   * @param jInput
   *          JsonNode
   * @return {Boolean}
   * @throws Exception
   */
  public static boolean ifisContains(String sInput, String sSeparator, JsonNode jInput) throws Exception {
    if (ifIsEmpty(sSeparator))
      sSeparator = ",";
    if (ifIsEmpty(sInput) || ifIsEmpty(jInput))
      return false;
    String[] aInputs = StringUtils.split(sInput, sSeparator);
    for (String input : aInputs) {
      JsonNode jValue = jInput.get(input);
      if (ifIsNull(jValue))
        continue;
      if (ifIsNotEmpty(jValue.toString()))
        return true;
    }
    return false;
  }

  public static boolean ifIsContains(JsonNode jInput1, JsonNode jInput2) {
    if (ifIsEmpty(jInput1) || ifIsEmpty(jInput2))
      return false;
    Iterator<String> it = jInput2.fieldNames();
    while (it.hasNext()) {
      String sKey = it.next();
      if (CommonUtil.ifIsNotNull(jInput1.get(sKey)))
        return true;
    }
    return false;
  }

  /**
   * 获取两个字符串内容交集
   *
   * @param sInput1
   *          字符串1
   * @param sSeparator1
   *          字符串1分隔符
   * @param sInput2
   *          字符串2
   * @param sSeparator2
   *          字符串2分隔符
   * @return {String}
   */
  public static String getIntersection(String sInput1, String sSeparator1, String sInput2, String sSeparator2) {
    String sResult = "";
    String[] aInputs = StringUtils.split(sInput1, sSeparator1);
    for (String value : aInputs) {
      if (ifIsIndexOf(sInput2, value, sSeparator2)) {
        sResult += "," + value;
      }
    }
    if (isNotNull(sResult))
      sResult = sResult.substring(1);
    return sResult;
  }

  /**
   * 获取两个字符串内容的并集
   *
   * @param sInput1
   *          字符串1
   * @param sInput2
   *          字符串2
   * @param sSeparator
   *          字符串分隔符
   * @return {String}
   */
  public static String getUnion(String sInput1, String sInput2, String sSeparator) {
    String[] aInputs = StringUtils.split(sInput2, sSeparator);
    for (String value : aInputs) {
      if (!ifIsIndexOf(sInput1, value, sSeparator)) {
        if (ifIsEmpty(sInput1)) {
          sInput1 = value;
        } else {
          sInput1 += sSeparator + value;
        }
      }
    }
    return sInput1;
  }

  /**
   * 获取两个字符串(input1到input2)内容的差集
   *
   * @param sInput1
   *          字符串1
   * @param sInput2
   *          字符串2
   * @return {String}
   */
  public static String getSubtraction(String sInput1, String sInput2) {
    return getSubtraction(sInput1, ",", sInput2, ",");
  }

  /**
   * 获取两个字符串(input1到input2)内容的差集
   *
   * @param sInput1
   *          字符串1
   * @param sSeparator1
   *          字符串1分隔符
   * @param sInput2
   *          字符串2
   * @param sSeparator2
   *          字符串2分隔符
   * @return {String}
   */
  public static String getSubtraction(String sInput1, String sSeparator1, String sInput2, String sSeparator2) {
    String sResult = "";
    String[] aInputs = StringUtils.split(sInput1, sSeparator1);
    for (String value : aInputs) {
      if (!ifIsIndexOf(sInput2, value, sSeparator2)) {
        sResult += "," + value;
      }
    }
    if (isNotNull(sResult))
      sResult = sResult.substring(1);
    return sResult;
  }

  /*
   * public static String[] getAddress(String sCode) { String[] aAddress = new
   * String[3]; if (CommonUtil.isNull(sCode) || sCode.length() != 6) return
   * aAddress; String sProvince = "", sCity = "", sDistrict = ""; String sCode1
   * = sCode.substring(0, 2) + "0000"; try { sProvince =
   * ConfigUtil.Options.get("area").get("o").get(sCode1).get("a").asText(); }
   * catch (Exception e) { sProvince = null; } String sCode2 =
   * sCode.substring(0, 4) + "00"; try { sCity = ConfigUtil.Options.get("area_"
   * + sCode1).get("o").get(sCode2).get("a").asText(); } catch (Exception e) {
   * sCity = null; } try { sDistrict = ConfigUtil.Options.get("area_" + sCode1 +
   * "_" + sCode2).get("o").get(sCode).get("a").asText(); } catch (Exception e)
   * { sDistrict = null; } if (CommonUtil.isNotNull(sProvince)) { aAddress[0] =
   * sProvince; } if (CommonUtil.isNotNull(sCity)) { aAddress[1] = sCity; } if
   * (CommonUtil.isNotNull(sDistrict)) { aAddress[2] = sDistrict; } return
   * aAddress; }
   */

  /**
   * 递归压缩文件夹
   *
   * @param srcRootDir
   *          压缩文件夹根目录的子路径
   * @param file
   *          当前递归压缩的文件或目录对象
   * @param zos
   *          压缩文件存储对象
   * @throws Exception
   */
  private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception {
    if (file == null) {
      return;
    }

    // 如果是文件，则直接压缩该文件
    if (file.isFile()) {
      int count, bufferLen = 1024;
      byte data[] = new byte[bufferLen];

      // 获取文件相对于压缩文件夹根目录的子路径
      String subPath = file.getAbsolutePath();
      // int index = subPath.indexOf(srcRootDir);
      // if (index != -1) {
      subPath = subPath.substring(srcRootDir.length() + File.separator.length() - 1);
      // }
      ZipEntry entry = new ZipEntry(subPath);
      zos.putNextEntry(entry);
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      while ((count = bis.read(data, 0, bufferLen)) != -1) {
        zos.write(data, 0, count);
      }
      bis.close();
      zos.closeEntry();
    }
    // 如果是目录，则压缩整个目录
    else {
      // 压缩目录中的文件或子目录
      File[] childFileList = file.listFiles();
      for (int n = 0; n < childFileList.length; n++) {
        childFileList[n].getAbsolutePath().indexOf(file.getAbsolutePath());
        zip(srcRootDir, childFileList[n], zos);
      }
    }
  }

  /**
   * 对文件或文件目录进行压缩
   *
   * @param srcPath
   *          要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
   * @param zipPath
   *          压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
   * @param zipFileName
   *          压缩文件名
   * @throws Exception
   */
  public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
    if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath) || StringUtils.isEmpty(zipFileName)) {
      throw new Exception("参数为空");
    }
    CheckedOutputStream cos = null;
    ZipOutputStream zos = null;
    try {
      File srcFile = new File(srcPath);

      // 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
      if (srcFile.isDirectory() && zipPath.indexOf(srcPath) != -1) {
        throw new Exception("zipPath must not be the child directory of srcPath.");
      }

      // 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
      File zipDir = new File(zipPath);
      if (!zipDir.exists() || !zipDir.isDirectory()) {
        zipDir.mkdirs();
      }

      // 创建压缩文件保存的文件对象
      String zipFilePath = zipPath + File.separator + zipFileName;
      File zipFile = new File(zipFilePath);
      if (zipFile.exists()) {
        // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
        // SecurityManager securityManager = new SecurityManager();
        // securityManager.checkDelete(zipFilePath);
        // 删除已存在的目标文件
        zipFile.delete();
      }

      cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
      zos = new ZipOutputStream(cos);

      // 如果只是压缩一个文件，则需要截取该文件的父目录
      String srcRootDir = srcPath;
      if (srcFile.isFile()) {
        int index = srcPath.lastIndexOf(File.separator);
        if (index != -1) {
          srcRootDir = srcPath.substring(0, index);
        }
      }
      // 调用递归压缩方法进行目录或文件压缩
      zip(srcRootDir, srcFile, zos);
      zos.flush();
    } catch (Exception e) {
      throw e;
    } finally {
      try {
        if (zos != null) {
          zos.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 解压缩zip包
   *
   * @param zipPath
   *          压缩文件保存的路径
   * @param zipFileName
   *          压缩文件名
   * @param files
   *          当前压缩的文件列表
   * @throws Exception
   */
  public static File zip(String zipPath, String zipFileName, List<File> files) throws Exception {
    File zipFile = null;
    if (files == null || files.size() == 0 || zipFileName == null | "".equals(zipFileName)) {
      return zipFile;
    }

    CheckedOutputStream cos = null;
    ZipOutputStream zos = null;
    try {
      // 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
      File zipDir = new File(zipPath);
      if (!zipDir.exists() || !zipDir.isDirectory()) {
        zipDir.mkdirs();
      }

      // 创建压缩文件保存的文件对象
      String zipFilePath = zipPath + File.separator + zipFileName;
      zipFile = new File(zipFilePath);
      if (zipFile.exists()) {
        // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
        SecurityManager securityManager = new SecurityManager();
        securityManager.checkDelete(zipFilePath);
        // 删除已存在的目标文件
        zipFile.delete();
      }

      cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
      zos = new ZipOutputStream(cos);

      // 如果是文件，则直接压缩该文件
      for (int n = 0; n < files.size(); n++) {
        File file = files.get(n);
        int count, bufferLen = 1024;
        byte data[] = new byte[bufferLen];

        ZipEntry entry = new ZipEntry(file.getName());
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        while ((count = bis.read(data, 0, bufferLen)) != -1) {
          zos.write(data, 0, count);
        }
        bis.close();
        zos.closeEntry();
      }

      zos.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (zos != null) {
          zos.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return zipFile;
  }

  /**
   * 解压缩zip包
   *
   * @param zipFilePath
   *          zip文件的全路径
   * @param unzipFilePath
   *          解压后的文件保存的路径
   * @param includeZipFileName
   *          解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含
   */
  @SuppressWarnings("unchecked")
  public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
    if (StringUtils.isEmpty(zipFilePath) || StringUtils.isEmpty(unzipFilePath)) {
      throw new Exception("参数为空");
    }
    File zipFile = new File(zipFilePath);
    // 如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径
    if (includeZipFileName) {
      String fileName = zipFile.getName();
      if (StringUtils.isNotEmpty(fileName)) {
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
      }
      unzipFilePath = unzipFilePath + File.separator + fileName;
    }
    // 创建解压缩文件保存的路径
    File unzipFileDir = new File(unzipFilePath);
    if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
      unzipFileDir.mkdirs();
    }

    // 开始解压
    ZipEntry entry = null;
    String entryFilePath = null, entryDirPath = null;
    File entryFile = null, entryDir = null;
    int index = 0, count = 0, bufferSize = 1024;
    byte[] buffer = new byte[bufferSize];
    BufferedInputStream bis = null;
    BufferedOutputStream bos = null;
    ZipFile zip = new ZipFile(zipFile);
    Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
    // 循环对压缩包里的每一个文件进行解压
    while (entries.hasMoreElements()) {
      entry = entries.nextElement();
      // 构建压缩包中一个文件解压后保存的文件全路径
      entryFilePath = unzipFilePath + File.separator + entry.getName();
      // 构建解压后保存的文件夹路径
      index = entryFilePath.lastIndexOf(File.separator);
      if (index != -1) {
        entryDirPath = entryFilePath.substring(0, index);
      } else {
        entryDirPath = "";
      }
      entryDir = new File(entryDirPath);
      // 如果文件夹路径不存在，则创建文件夹
      if (!entryDir.exists() || !entryDir.isDirectory()) {
        entryDir.mkdirs();
      }

      // 创建解压文件
      entryFile = new File(entryFilePath);
      if (entryFile.exists()) {
        // 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
        SecurityManager securityManager = new SecurityManager();
        securityManager.checkDelete(entryFilePath);
        // 删除已存在的目标文件
        entryFile.delete();
      }

      // 写入文件
      bos = new BufferedOutputStream(new FileOutputStream(entryFile));
      bis = new BufferedInputStream(zip.getInputStream(entry));
      while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
        bos.write(buffer, 0, count);
      }
      bos.flush();
      bos.close();
    }
  }

  public static String readText(String resourceName) {
    if (resourceName.startsWith("=")) {
      return resourceName.substring(1);
    }
    StringWriter sw = new StringWriter();
    Reader isr = null;
    try {
      isr = new InputStreamReader(getInputStream(resourceName), UTF_8);
      char[] chars = new char[BUFFER_SIZE];
      int len;
      while ((len = isr.read(chars)) > 0) {
        sw.write(chars, 0, len);
      }
    } catch (IOException e) {

    } finally {
      if (isr != null) {
        try {
          isr.close();
        } catch (IOException e) {
          e.printStackTrace();
          logger.trace("Failed to close {}", isr, e);
        }
      }
    }
    return sw.toString();
  }

  private static byte[] readBytes(File file) {
    if (!file.exists())
      return null;
    long len = file.length();
    if (len > Runtime.getRuntime().totalMemory() / 10)
      throw new IllegalStateException("Attempted to read large file " + file + " was " + len + " bytes.");
    byte[] bytes = new byte[(int) len];
    DataInputStream dis = null;
    try {
      dis = new DataInputStream(new FileInputStream(file));
      dis.readFully(bytes);
    } catch (IOException e) {
      logger.warn("Unable to read {}", file, e);
      throw new IllegalStateException("Unable to read file " + file, e);
    } finally {
      if (dis != null) {
        try {
          dis.close();
        } catch (IOException e) {
          e.printStackTrace();
          logger.trace("Failed to close {}", dis, e);
        }
      }
    }
    return bytes;
  }

  public static InputStream getInputStream(String filename) throws FileNotFoundException {
    if (filename.isEmpty())
      throw new IllegalArgumentException("The file name cannot be empty.");
    if (filename.charAt(0) == '=')
      return new ByteArrayInputStream(encodeUTF8(filename.substring(1)));
    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
    InputStream is = contextClassLoader.getResourceAsStream(filename);
    if (is != null)
      return is;
    InputStream is2 = contextClassLoader.getResourceAsStream('/' + filename);
    if (is2 != null)
      return is2;
    return new FileInputStream(filename);
  }

  public static byte[] encodeUTF8(String text) {
    try {
      return text.getBytes(UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  public static String decodeUTF8(byte[] bytes) {
    try {
      return new String(bytes, UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError(e);
    }
  }

  public static boolean writeText(File file, String text) {
    return writeBytes(file, encodeUTF8(text));
  }

  public static boolean writeBytes(File file, byte[] bytes) {
    File parentDir = file.getParentFile();
    if (!parentDir.isDirectory() && !parentDir.mkdirs())
      throw new IllegalStateException("Unable to create directory " + parentDir);
    // only write to disk if it has changed.
    File bak = null;
    if (file.exists()) {
      byte[] bytes2 = readBytes(file);
      if (Arrays.equals(bytes, bytes2))
        return false;
      bak = new File(parentDir, file.getName() + ".bak");
      file.renameTo(bak);
    }

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file);
      fos.write(bytes);
    } catch (IOException e) {
      logger.warn("Unable to write {} as {}", file, decodeUTF8(bytes), e);
      file.delete();
      if (bak != null) {
        bak.renameTo(file);
      }
      throw new IllegalStateException("Unable to write " + file, e);
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
          logger.trace("Failed to close {}", fos, e);
        }
      }
    }
    return true;
  }

  /**
   * 产生yyyyMMddHHmmssSSS格式精确时间，mongodb查询时可以作为对比条件排序
   *
   * @return add user : wjzhang
   */
  /*
   * public static String getCurrentTime() { Calendar cal =
   * Calendar.getInstance(); return sdFormat.format(cal.getTime()).replace("-",
   * "").replace(":", "").replace(" ", ""); }
   */

  public static double round(double d, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b = new BigDecimal(Double.toString(d));
    BigDecimal one = new BigDecimal("1");
    return b.divide(one, scale, 4).doubleValue();
  }

  /**
   * 金额数值单位为分，将double取余后返回int
   *
   * @param d
   *          单位为分的数值
   * @return add user : wjzhang
   */
  public static int roundInt(double d) {
    return (int) round(d, 0);
  }

  public static String[] getLastPathArray(String sPath) {
    String[] a = { "", "" };
    int iPosition = sPath.lastIndexOf(".");
    if (iPosition == -1) {
      a[1] = sPath;
    } else {
      a[0] = sPath.substring(0, iPosition);
      a[1] = sPath.substring(iPosition + 1);
    }
    return a;
  }

  public static String[] getFirstPathArray(String sPath) {
    String[] a = { "", "" };
    int iPosition = sPath.indexOf(".");
    if (iPosition == -1) {
      a[0] = sPath;
    } else {
      a[0] = sPath.substring(0, iPosition);
      a[1] = sPath.substring(iPosition + 1);
    }
    return a;
  }

  /**
   * 根据系统线程获取调用方法的类名方法名路径
   *
   * @return {String}
   */
  public static String getObjectClassMethodPath() {
    StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
    String sClassName = getLastPathArray(ste.getClassName())[1];
    String sMethodName = ste.getMethodName();
    return sClassName + "." + sMethodName;
  }

  /**
   * <pre>
   * 省、直辖市代码表：
   *     11 : 北京  12 : 天津  13 : 河北       14 : 山西  15 : 内蒙古
   *     21 : 辽宁  22 : 吉林  23 : 黑龙江  31 : 上海  32 : 江苏
   *     33 : 浙江  34 : 安徽  35 : 福建       36 : 江西  37 : 山东
   *     41 : 河南  42 : 湖北  43 : 湖南       44 : 广东  45 : 广西      46 : 海南
   *     50 : 重庆  51 : 四川  52 : 贵州       53 : 云南  54 : 西藏
   *     61 : 陕西  62 : 甘肃  63 : 青海       64 : 宁夏  65 : 新疆
   *     71 : 台湾
   *     81 : 香港  82 : 澳门
   *     91 : 国外
   * </pre>
   */
  private static String cityCode[] = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37",
      "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71", "81", "82", "91" };

  /**
   * 每位加权因子
   */
  private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

  /**
   * 根据身份证获取省份
   *
   * @param sIdcard
   *          身份证
   * @return {String}
   */
  public static String getProvinceFromIdcard(String sIdcard) {
    String sProvince = "";
    if (ifIsValidIdcard(sIdcard)) {
      sProvince = sIdcard.substring(0, 2) + "0000";
    }
    return sProvince;
  }

  /**
   * 根据身份证获取城市
   *
   * @param sIdcard
   *          身份证
   * @return {String}
   */
  public static String getCityFromIdcard(String sIdcard) {
    String sCity = "";
    if (ifIsValidIdcard(sIdcard)) {
      sCity = sIdcard.substring(0, 4) + "00";
    }
    return sCity;
  }

  /**
   * 根据身份证获取区县
   *
   * @param sIdcard
   *          身份证
   * @return {String}
   */
  public static String getDistrictFromIdcard(String sIdcard) {
    String sDistrict = "";
    if (ifIsValidIdcard(sIdcard)) {
      sDistrict = sIdcard.substring(0, 6);
    }
    return sDistrict;
  }

  public static int getAgetByBirthday(String birthday) throws ParseException {
    // 格式化传入的时间
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date parse = format.parse(birthday);
    int age = 0;
    Calendar now = Calendar.getInstance();
    now.setTime(new Date()); // 当前时间
    Calendar birth = Calendar.getInstance();
    birth.setTime(parse); // 传入的时间
    // 如果传入的时间，在当前时间的后面，返回0岁
    if (birth.after(now)) {
      age = 0;
    } else {
      age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
      if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
        age += 1;
      }
    }
    return age;
  }

  /**
   * 验证所有的身份证的合法性
   *
   * @param idcard
   *          身份证
   * @return 合法返回true，否则返回false
   */
  public static boolean ifIsValidIdcard(String idcard) {
    if (idcard == null || "".equals(idcard)) {
      return false;
    }
    if (idcard.length() == 15) {
      return validate15IDCard(idcard);
    }
    return validate18Idcard(idcard);
  }

  /**
   * <p>
   * 判断18位身份证的合法性
   * </p>
   * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
   * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
   * <p>
   * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
   * </p>
   * <p>
   * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
   * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码； 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
   * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
   * </p>
   * <p>
   * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4 2
   * 1 6 3 7 9 10 5 8 4 2
   * </p>
   * <p>
   * 2.将这17位数字和系数相乘的结果相加。
   * </p>
   * <p>
   * 3.用加出来和除以11，看余数是多少
   * </p>
   * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
   * 2。
   * <p>
   * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
   * </p>
   *
   * @param idcard
   * @return
   */
  public static boolean validate18Idcard(String idcard) {
    if (idcard == null) {
      return false;
    }

    // 非18位为假
    if (idcard.length() != 18) {
      return false;
    }
    // 获取前17位
    String idcard17 = idcard.substring(0, 17);

    // 前17位全部为数字
    if (!isDigital(idcard17)) {
      return false;
    }

    String provinceid = idcard.substring(0, 2);
    // 校验省份
    if (!checkProvinceid(provinceid)) {
      return false;
    }

    // 校验出生日期
    String birthday = idcard.substring(6, 14);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

    try {
      Date birthDate = sdf.parse(birthday);
      String tmpDate = sdf.format(birthDate);
      if (!tmpDate.equals(birthday)) {// 出生年月日不正确
        return false;
      }

    } catch (ParseException e1) {

      return false;
    }

    // 获取第18位
    String idcard18Code = idcard.substring(17, 18);

    char c[] = idcard17.toCharArray();

    int bit[] = converCharToInt(c);

    int sum17 = 0;

    sum17 = getPowerSum(bit);

    // 将和值与11取模得到余数进行校验码判断
    String checkCode = getCheckCodeBySum(sum17);
    if (null == checkCode) {
      return false;
    }
    // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
    if (!idcard18Code.equalsIgnoreCase(checkCode)) {
      return false;
    }

    return true;
  }

  /**
   * 校验15位身份证
   *
   * <pre>
   * 只校验省份和出生年月日
   * </pre>
   *
   * @param idcard
   * @return
   */
  public static boolean validate15IDCard(String idcard) {
    if (idcard == null) {
      return false;
    }
    // 非15位为假
    if (idcard.length() != 15) {
      return false;
    }

    // 15全部为数字
    if (!isDigital(idcard)) {
      return false;
    }

    String provinceid = idcard.substring(0, 2);
    // 校验省份
    if (!checkProvinceid(provinceid)) {
      return false;
    }

    String birthday = idcard.substring(6, 12);

    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    try {
      Date birthDate = sdf.parse(birthday);
      String tmpDate = sdf.format(birthDate);
      if (!tmpDate.equals(birthday)) {// 身份证日期错误
        return false;
      }

    } catch (ParseException e1) {

      return false;
    }

    return true;
  }

  /**
   * 将15位的身份证转成18位身份证
   *
   * @param idcard
   * @return
   */
  public static String convertIdcarBy15bit(String idcard) {
    if (idcard == null) {
      return null;
    }

    // 非15位身份证
    if (idcard.length() != 15) {
      return null;
    }

    // 15全部为数字
    if (!isDigital(idcard)) {
      return null;
    }

    String provinceid = idcard.substring(0, 2);
    // 校验省份
    if (!checkProvinceid(provinceid)) {
      return null;
    }

    String birthday = idcard.substring(6, 12);

    SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");

    Date birthdate = null;
    try {
      birthdate = sdf.parse(birthday);
      String tmpDate = sdf.format(birthdate);
      if (!tmpDate.equals(birthday)) {// 身份证日期错误
        return null;
      }

    } catch (ParseException e1) {
      return null;
    }

    Calendar cday = Calendar.getInstance();
    cday.setTime(birthdate);
    String year = String.valueOf(cday.get(Calendar.YEAR));

    String idcard17 = idcard.substring(0, 6) + year + idcard.substring(8);

    char c[] = idcard17.toCharArray();
    String checkCode = "";

    // 将字符数组转为整型数组
    int bit[] = converCharToInt(c);

    int sum17 = 0;
    sum17 = getPowerSum(bit);

    // 获取和值与11取模得到余数进行校验码
    checkCode = getCheckCodeBySum(sum17);

    // 获取不到校验位
    if (null == checkCode) {
      return null;
    }
    // 将前17位与第18位校验码拼接
    idcard17 += checkCode;
    return idcard17;
  }

  /**
   * 校验省份
   *
   * @param provinceid
   * @return 合法返回TRUE，否则返回FALSE
   */
  private static boolean checkProvinceid(String provinceid) {
    for (String id : cityCode) {
      if (id.equals(provinceid)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 数字验证
   *
   * @param str
   * @return
   */
  private static boolean isDigital(String str) {
    return str.matches("^[0-9]*$");
  }

  /**
   * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
   *
   * @param bit
   * @return
   */
  private static int getPowerSum(int[] bit) {

    int sum = 0;

    if (power.length != bit.length) {
      return sum;
    }

    for (int i = 0; i < bit.length; i++) {
      for (int j = 0; j < power.length; j++) {
        if (i == j) {
          sum = sum + bit[i] * power[j];
        }
      }
    }
    return sum;
  }

  /**
   * 将和值与11取模得到余数进行校验码判断
   *
   * @param sum17
   * @return 校验位
   */
  private static String getCheckCodeBySum(int sum17) {
    String checkCode = null;
    switch (sum17 % 11) {
    case 10:
      checkCode = "2";
      break;
    case 9:
      checkCode = "3";
      break;
    case 8:
      checkCode = "4";
      break;
    case 7:
      checkCode = "5";
      break;
    case 6:
      checkCode = "6";
      break;
    case 5:
      checkCode = "7";
      break;
    case 4:
      checkCode = "8";
      break;
    case 3:
      checkCode = "9";
      break;
    case 2:
      checkCode = "x";
      break;
    case 1:
      checkCode = "0";
      break;
    case 0:
      checkCode = "1";
      break;
    }
    return checkCode;
  }

  /**
   * 将字符数组转为整型数组
   *
   * @param c
   * @return
   * @throws NumberFormatException
   */
  private static int[] converCharToInt(char[] c) throws NumberFormatException {
    int[] a = new int[c.length];
    int k = 0;
    for (char temp : c) {
      a[k++] = Integer.parseInt(String.valueOf(temp));
    }
    return a;
  }

  /**
   * 将 元 转为 分
   *
   * @param dYuan
   *          元
   * @return {int}
   */
  public static int convertYUAN2Penney(Double dYuan) {
    return (new Double(dYuan * 100)).intValue();
  }

  /**
   * 将 分 转为 元
   *
   * @param iPenney
   *          分
   * @return {double}
   */
  public static double convertPenney2YUAN(int iPenney) {
    return new Double(iPenney * 1.0 / 100);
  }

  public static String pathTogether(String separator, String... paths) {
    String sPath = "";
    for (int i = 0, l = paths.length; i < l; i++) {
      String path = paths[i];
      if (ifIsEmpty(path))
        continue;
      if (CommonUtil.ifIsEmpty(sPath)) {
        sPath = path;
      } else {
        sPath += separator + path;
      }
    }
    return sPath;
  }

  /**
   * 解析出url参数中的键值对 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
   *
   * @param URL
   *          url地址
   * @return url请求参数部分
   */
  public static Map<String, String> URLRequest(String URL) {
    Map<String, String> mapRequest = new HashMap<String, String>();

    String[] arrSplit = null;

    String strUrlParam = TruncateUrlPage(URL);
    if (strUrlParam == null) {
      return mapRequest;
    }
    // 每个键值为一组 www.2cto.com
    arrSplit = strUrlParam.split("[&]");
    for (String strSplit : arrSplit) {
      String[] arrSplitEqual = null;
      arrSplitEqual = strSplit.split("[=]");

      // 解析出键值
      if (arrSplitEqual.length > 1) {
        // 正确解析
        mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

      } else {
        if (arrSplitEqual[0] != "") {
          // 只有参数没有值，不加入
          mapRequest.put(arrSplitEqual[0], "");
        }
      }
    }
    return mapRequest;
  }

  /**
   * 去掉url中的路径，留下请求参数部分
   *
   * @param strURL
   *          url地址
   * @return url请求参数部分
   */
  private static String TruncateUrlPage(String strURL) {
    String strAllParam = null;
    String[] arrSplit = null;

    strURL = strURL.trim();

    arrSplit = strURL.split("[?]");
    if (strURL.length() > 1) {
      if (arrSplit.length > 1) {
        if (arrSplit[1] != null) {
          strAllParam = arrSplit[1];
        }
      }
    }

    return strAllParam;
  }

  public static String tableRecordsCallback(JsonNode jDatas, String sObject) throws Exception {
    return tableRecordsCallback(jDatas, sObject, sObject);
  }

  public static String tableRecordsCallback(JsonNode jDatas, String sObject, boolean bSearch) throws Exception {
    return tableRecordsCallback(jDatas, sObject, sObject, bSearch);
  }

  public static String tableRecordsCallback(JsonNode jDatas, String sObject, String sCurrent) throws Exception {
    return tableRecordsCallback(jDatas, sObject, sCurrent, true);
  }

  public static String tableRecordsCallback(JsonNode jDatas, String sObject, String sCurrent, boolean bSearch) throws Exception {
    String sCallback = "";
    if (CommonUtil.ifIsNull(bSearch))
      bSearch = true;
    if (CommonUtil.ifIsEmpty(sCurrent))
      sCurrent = sObject;
    int iTotalCount = JsonUtil.getJsonIntValue(jDatas, "c");
    int iLimit = JsonUtil.getJsonIntValue(jDatas, "p");
    int iSkip = JsonUtil.getJsonIntValue(jDatas, "s");
    int iCount = JsonUtil.getJsonIntValue(jDatas, "t");
    JsonNode jRecordIDs = JsonUtil.queryJson(jDatas, "o");
    JsonNode jEmbedRecordIDs = JsonUtil.queryJson(jDatas, "o1");
    JsonNode jRecords = JsonUtil.queryJson(jDatas, "r");
    sCallback += "_webapp.mergeJson(" + sObject + ", " + jRecords + ");";

    if (bSearch) {
      sCallback += "_webapp.ifIsEmpty(_webapp.getJsonValue(Current, '" + sCurrent
          + ".search')) && (_webapp.setJsonValue(Current, '" + sCurrent + ".search', []));";
      String sRecordIDs = jRecordIDs.toString();
      sRecordIDs = sRecordIDs.substring(1, sRecordIDs.length() - 1);
      sCallback += "Current." + sCurrent + ".search.push(" + sRecordIDs + ");";
    }
    /*
     * sCallback += "_webapp.ifIsEmpty(_webapp.getJsonValue(Current, '" +
     * sCurrent + ".search')) && (_webapp.setJsonValue(Current, '" + sCurrent +
     * ".search', {}));"; sCallback += "_webapp.mergeJson(Current."+sCurrent+
     * ".search, "+jEmbedRecordIDs+");";
     */
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.index', " + (iSkip + iCount) + ");";
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.skip', " + iSkip + ");";
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.page', " + (iSkip / iLimit) + ");";
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.size', " + iLimit + ");";
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.total', " + iTotalCount + ");";
    sCallback += "_webapp.setJsonValue(Current, '" + sCurrent + ".list.number', " + iCount + ");";
    return sCallback;
  }

  /**
   * 根据url获取文件base64
   * @param imageUrl
   * @return
   * @throws Exception
   */
  public static byte[] getURLImage(String imageUrl) throws Exception {
    //new一个URL对象
    URL url = new URL(imageUrl);
    //打开链接
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    //设置请求方式为"GET"
    conn.setRequestMethod("GET");
    //超时响应时间为5秒
    conn.setConnectTimeout(5 * 1000);
    //通过输入流获取图片数据
    InputStream inStream = conn.getInputStream();
    //得到图片的二进制数据，以二进制封装得到数据，具有通用性
    return readInputStream(inStream);
  }

  public static byte[] readInputStream(InputStream inStream) throws Exception{
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    //创建一个Buffer字符串
    byte[] buffer = new byte[1024];
    //每次读取的字符串长度，如果为-1，代表全部读取完毕
    int len = 0;
    //使用一个输入流从buffer里把数据读取出来
    while( (len=inStream.read(buffer)) != -1 ){
      //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
      outStream.write(buffer, 0, len);
    }
    //关闭输入流
    inStream.close();
    //把outStream里的数据写入内存
    return outStream.toByteArray();
  }

}
