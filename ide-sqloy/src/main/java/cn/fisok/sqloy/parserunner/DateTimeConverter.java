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
package cn.fisok.sqloy.parserunner;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/01/04
 * @desc : 日期时间转换器，把毫秒转为和实际相符合的：小时+分+秒+毫秒
 */
class DateTimeConverter {
    /**
     * 把毫秒转换成小时+分+秒的形式
     *
     * @param timeMillis timeMillis
     * @return String
     */
    public static String longSecond2HMS(long timeMillis) {
        StringBuffer hms = new StringBuffer();//"{0}时{1}分{2}秒{3}毫秒";
        long totalSec = (long) timeMillis / 1000;
        long totalMin = (long) totalSec / 60;
        long totalHour = (long) totalMin / 60;

        long millis = timeMillis % 1000;
        long sec = totalSec % 60;
        long min = totalMin % 60;
        long hour = totalHour;

        if (hour > 0) hms.append(hour).append("小时");
        if (hour > 0 || min > 0) hms.append(min).append("分");
        if (hour > 0 || min > 0 || sec > 0) hms.append(sec).append("秒");
        hms.append(millis).append("毫秒");

        return hms.toString();
    }
}
