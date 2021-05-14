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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.UUID;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:24
 * @desc :
 */
public abstract class NumberKit {
    /**
     * 除法计算时，的精度
     */
    public static final int DIVIDE_SCALE = 128;

    /**
     * 小数保留精度，四舍五入
     *
     * @param value 小数值
     * @param scale 小数精度
     * @return double
     */
    public static double round(Number value, int scale) {
        return BigDecimal.valueOf(value.doubleValue()).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 小数保留精度，向上取整
     *
     * @param value value
     * @param scale scale
     * @return number
     */
    public static Number ceil(Number value, int scale) {
        return BigDecimal.valueOf(value.doubleValue()).setScale(scale, RoundingMode.UP);
    }

    /**
     * 方法返回从1970年1月1日0时0分0秒（这与UNIX系统有关）到现在的一个long型的毫秒数，取模之后即可得到所需范围内的随机数
     * @param min
     * @param max
     * @return
     */
    public static Double random(double min,double max){
        long randomNum = System.currentTimeMillis();
        return (randomNum%(max-min)+min);
    }

    public static Integer randomInt(int min,int max){
        Double value = random((double)min,(double)max);
        return value.intValue();
    }

    /**
     * 数保留精度，向下取整
     *
     * @param value value
     * @param scale scale
     * @return double
     */
    public static double floor(Number value, int scale) {
        return BigDecimal.valueOf(value.doubleValue()).setScale(scale, RoundingMode.DOWN).doubleValue();
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        String uuidValue = uuid.toString();
        uuidValue = uuidValue.toUpperCase();
        return uuidValue;
    }

    public static Number divide(Number v1, Number v2) {
        return BigDecimal.valueOf(v1.doubleValue()).divide(BigDecimal.valueOf(v2.doubleValue()), DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 任意整数转为36进制的字符串
     *
     * @param i i
     * @return String
     */
    public static String convert36Radix(int i) {
        return Integer.toString(i, 36).toUpperCase();
    }

    /**
     * 任意整数转为36进制的字符串
     *
     * @param i i
     * @return string
     */
    public static String convert36Radix(long i) {
        return Long.toString(i, 36).toUpperCase();
    }

    /**
     * 当前时间的纳秒值，以36进制表示
     *
     * @return string
     */
    public static String nanoTime36() {
        return convert36Radix(System.nanoTime());
    }

    public static Long nanoTime() {
        return System.nanoTime();
    }

    /**
     * 时间新纳秒值+随机数
     *
     * @param randomCount 随机数个数
     * @return string
     */
    public static String nanoTimeRandom36(int randomCount) {
        StringBuffer sv = new StringBuffer(convert36Radix(System.nanoTime()));
        Random random = new Random();
        String rv = convert36Radix(random.nextInt(randomCount * 36));
        return sv.append(rv).toString();
    }

    public static double rmbUppercase2Double(String chineseNumber) {
        double result = 0;
        double temp = 1;//存放一个单位的数字如：十万
        int count = 0;//判断是否有chArr
        char[] cnArr = new char[]{'壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
        char[] chArr = new char[]{'拾', '佰', '仟', '万', '亿', '角', '分', '厘'};
        for (int i = 0; i < chineseNumber.length(); i++) {
            boolean b = true;//判断是否是chArr
            char c = chineseNumber.charAt(i);
            for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
                if (c == cnArr[j]) {
                    if (0 != count) {//添加下一个单位之前，先把上一个单位值添加到结果中
                        result += temp;
                        temp = 1;
                        count = 0;
                    }
                    // 下标+1，就是对应的值
                    temp = j + 1;
                    b = false;
                    break;
                }
            }
            if (b) {//单位{'十','百','千','万','亿'}
                for (int j = 0; j < chArr.length; j++) {
                    if (c == chArr[j]) {
                        switch (j) {
                            case 0:
                                temp *= 10;
                                break;
                            case 1:
                                temp *= 100;
                                break;
                            case 2:
                                temp *= 1000;
                                break;
                            case 3:
                                temp *= 10000;
                                break;
                            case 4:
                                temp *= 100000000;
                                break;
                            case 5:
                                temp *= 0.1;
                                break;
                            case 6:
                                temp *= 0.01;
                                break;
                            case 7:
                                temp *= 0.001;
                                break;
                            default:
                                break;
                        }
                        count++;
                    }
                }
            }
            if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
                result += temp;
            }
        }
        return result;
    }

    /**
     * 处理的最大数字达千万亿位 精确到分
     *
     * @param num num
     * @return string
     */
    public static String double2RMBUppercase(double num) {
        String fraction[] = { "角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = { { "元", "万", "亿"}, { "", "拾", "佰", "仟"}};

        String head = num < 0 ? "负" : "";
        num = Math.abs(num);

        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int) (Math.floor(num * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int) Math.floor(num);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && num > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
}
