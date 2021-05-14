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
package cn.fisok.web.holder;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 23:11
 * @desc :
 */
public abstract class WebHolder {

    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<HttpServletResponse>();
    private static final ThreadLocal<HttpSession> sessionHolder = new ThreadLocal<HttpSession>();
    private static ServletContext SERVLET_CONTEXT = null;

    public static void setRequest(HttpServletRequest request){
        requestHolder.set(request);
    }
    public static HttpServletRequest getRequest() {
        return requestHolder.get();
    }


    public static ValueObject getRequestParameter(String name){
        return WebHolder.getRequestParameters().getValue(name);
    }

    public static String getRequestParameterForString(String name,String defaultValue){
        return getRequestParameter(name).strValue(defaultValue);
    }
    public static Integer getRequestParameterForInt(String name,Integer defaultValue){
        return getRequestParameter(name).intValue(defaultValue);
    }
    public static Double getRequestParameterForInt(String name,Double defaultValue){
        return getRequestParameter(name).doubleValue(defaultValue);
    }
    public static Date getRequestParameterForDate(String name){
        return getRequestParameter(name).dateValue();
    }
    public static Boolean getRequestParameterForBoolean(String name,Boolean defaultValue){
        return getRequestParameter(name).boolValue(defaultValue);
    }

    public static void setResponse(HttpServletResponse response){
        responseHolder.set(response);
    }
    public static HttpServletResponse getResponse() {
        return responseHolder.get();
    }
    public static void setServletContext(ServletContext servletContext){
        SERVLET_CONTEXT = servletContext;
    }
    public static ServletContext getServletContext(){
        return SERVLET_CONTEXT;
    }
    public static void setSession(HttpSession session){
        sessionHolder.set(session);
    }
    public static HttpSession getSession(){
        return sessionHolder.get();
    }

    public static void clear(){
        requestHolder.remove();
        responseHolder.remove();
        sessionHolder.remove();
    }


//    public static WebApplicationContext getWebApplicationContext(){
//        return ContextLoader.getCurrentWebApplicationContext();
//    }


    /**
     * 请求参数拉平（数据有多个值的变一个）
     * @param map
     * @param <T>
     * @return
     */
    public static <T> MapObject flatParameters(Map<String,T> map){
        MapObject retMap = new MapObject();
        Iterator<Map.Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, T> entry = iterator.next();
            String name = entry.getKey();
            T value = entry.getValue();
            Object[] valueArray;
            if(value!=null&&value.getClass().isArray()){
                valueArray = (Object[])value;
                if(valueArray.length>1){
                    retMap.put(name,new ValueObject(valueArray));
                }else if(valueArray.length==1){
                    retMap.put(name,new ValueObject(valueArray[0]));
                }else{
                    retMap.put(name,new ValueObject(null));
                }
            }else{
                retMap.put(name,new ValueObject(value));
            }
        }
        return retMap;
    }

    public static MapObject getRequestParameters(HttpServletRequest request){
        Map<String, String[]> parameterMap = request.getParameterMap();
        return flatParameters(parameterMap);
    }

    public static MapObject getRequestParameters(){
        return getRequestParameters(getRequest());
    }



}
