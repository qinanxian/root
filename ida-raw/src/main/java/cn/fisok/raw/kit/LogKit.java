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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:04
 * @desc :
 */
public abstract class LogKit {
    private static final Object[] EMPTY_ARRAY = new Object[] {};
    private static final String FQCN = LogKit.class.getName();


    public static LocationAwareLogger getLocationAwareLogger(final int depth) {
        @SuppressWarnings("deprecation")
        String className = sun.reflect.Reflection.getCallerClass(depth).getName();
        return (LocationAwareLogger) LoggerFactory.getLogger(className);
    }

    /**
     * 静态的获取日志器
     *
     * @return Logger
     */
    public static Logger getLogger() {
        return getLocationAwareLogger(3);
    }

    public static String getName() {
        return getLocationAwareLogger(3).getName();
    }

    public static boolean isTraceEnabled() {
        return getLocationAwareLogger(3).isTraceEnabled();
    }

    public static void trace(String msg) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.TRACE_INT, msg, EMPTY_ARRAY, null);
    }


    public static void trace(String format, Object... arguments) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.TRACE_INT, StringKit.format(format,arguments), arguments, null);
    }

    public static void trace(String msg, Throwable t) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.TRACE_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isTraceEnabled(Marker marker) {
        return getLocationAwareLogger(3).isTraceEnabled(marker);
    }

    public static void trace(Marker marker, String msg) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.TRACE_INT, msg, EMPTY_ARRAY, null);
    }

    public static void trace(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.TRACE_INT, StringKit.format(format,argArray), argArray, null);
    }

    public static void trace(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.TRACE_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isInfoEnabled() {
        return getLocationAwareLogger(3).isInfoEnabled();
    }

    public static void info(String format, Object... arguments) {
        getLocationAwareLogger(3).log(null, FQCN, LocationAwareLogger.INFO_INT,
                StringKit.format(format,arguments), arguments, null);
    }

    public static void info(String msg, Throwable t) {
        getLocationAwareLogger(3).log(null, FQCN, LocationAwareLogger.INFO_INT,
                msg, EMPTY_ARRAY, t);
    }

    public static boolean isInfoEnabled(Marker marker) {
        return getLocationAwareLogger(3).isInfoEnabled(marker);
    }

    public static void info(Marker marker, String msg) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.INFO_INT, msg, EMPTY_ARRAY, null);
    }

    public static void info(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.INFO_INT, StringKit.format(format,argArray), argArray, null);
    }

    public static void info(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.INFO_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isDebugEnabled() {
        return getLocationAwareLogger(3).isDebugEnabled();
    }

    public static void debug(String msg) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.DEBUG_INT, msg, EMPTY_ARRAY, null);
    }

    public static void debug(String format, Object... arguments) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.DEBUG_INT, StringKit.format(format,arguments), arguments, null);
    }

    public static void debug(String msg, Throwable t) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.DEBUG_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isDebugEnabled(Marker marker) {
        return getLocationAwareLogger(3).isDebugEnabled(marker);
    }

    public static void debug(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.DEBUG_INT, StringKit.format(format,argArray), argArray, null);
    }

    public static void debug(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.DEBUG_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isWarnEnabled() {
        return getLocationAwareLogger(3).isWarnEnabled();
    }


    public static void warn(String format, Object... arguments) {
        getLocationAwareLogger(3).log(null, FQCN, LocationAwareLogger.WARN_INT,
                StringKit.format(format,arguments), arguments, null);
    }

    public static void warn(String msg, Throwable t) {
        getLocationAwareLogger(3).log(null, FQCN, LocationAwareLogger.WARN_INT,
                msg, EMPTY_ARRAY, t);
    }

    public static boolean isWarnEnabled(Marker marker) {
        return getLocationAwareLogger(3).isWarnEnabled(marker);
    }


    public static void warn(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.WARN_INT, StringKit.format(format,argArray), argArray, null);
    }

    public static void warn(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.WARN_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isErrorEnabled() {
        return getLocationAwareLogger(3).isErrorEnabled();
    }


    public static void error(String format, Object... arguments) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.ERROR_INT, StringKit.format(format,arguments), arguments, null);
    }

    public static void error(String msg, Throwable t) {
        getLocationAwareLogger(3).log(null, FQCN,
                LocationAwareLogger.ERROR_INT, msg, EMPTY_ARRAY, t);
    }

    public static boolean isErrorEnabled(Marker marker) {
        return getLocationAwareLogger(3).isErrorEnabled(marker);
    }

    public static void error(Marker marker, String format, Object... argArray) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.ERROR_INT, StringKit.format(format,argArray), argArray, null);
    }

    public static void error(Marker marker, String msg, Throwable t) {
        getLocationAwareLogger(3).log(marker, FQCN,
                LocationAwareLogger.ERROR_INT, msg, EMPTY_ARRAY, t);
    }
}
