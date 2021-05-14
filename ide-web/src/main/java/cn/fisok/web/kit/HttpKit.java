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
package cn.fisok.web.kit;

import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.RawException;
import cn.fisok.web.holder.WebHolder;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2019/12/28 22:54
 * @desc :
 */
public abstract class HttpKit {
    /**
     * 把请求数据中的map转换处理下,单个字串的直接以字串给返回出来
     *
     * @param request request
     * @return map
     */
    public static Map<String,Object> getRequestParameterMap(HttpServletRequest request){
        Map<String,Object> retMap = new LinkedHashMap<String,Object>();

        Map<String, String[]> parameterMap = request.getParameterMap();
        Iterator<String> iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()){
            String name = iterator.next();
            String[] value = request.getParameterValues(name);
            if(value!=null&&value.length==1){
                retMap.put(name,value[0]);
            }else{
                retMap.put(name,value);
            }
        }

        return retMap;
    }

    public static void renderStream(HttpServletResponse response, InputStream inputStream, String contentType, Map<String, String> headers) {
        response.reset();
        response.setContentType(contentType);
        //修改HTTP协议头
        if(headers!=null&&headers.size()>0){
            Set<Map.Entry<String,String>> entries = headers.entrySet();
            for(Map.Entry<String,String> entry:entries){
                response.addHeader(entry.getKey(),entry.getValue());
            }
        }
        //渲染输出
        ServletOutputStream outputStream = null;

        try {
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[10240];
            int len = 0;
            while((len=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,len);
            }
            response.flushBuffer();
            outputStream.flush();

        } catch (IOException e) {
            throw new RawException(e);
        } finally {
            IOKit.close(outputStream);
        }
    }

    public static void renderStream(OutputStream outputStream, InputStream inputStream) {
        //渲染输出
        try {
            byte[] buffer = new byte[10240];
            int len = 0;
            while((len=inputStream.read(buffer))>0){
                outputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            throw new RawException(e);
        } finally {
            IOKit.close(outputStream);
        }
    }

    public static void sendRedirect(HttpServletResponse response,String location){
        response.addHeader(HttpHeaders.LOCATION,location);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
    }

    public static boolean isAjaxRequest(HttpServletRequest request) {
        return  "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static String retrieveRealView(String staticResourceProxyUrl, String url) {
        if (StringKit.isEmpty(staticResourceProxyUrl))
            return url;
        else {
            return "redirect:" + staticResourceProxyUrl + url;
        }
    }

    public static String iso8859(String str,HttpServletRequest request){
        String charset = request.getCharacterEncoding();
        if(charset==null)charset = Charset.defaultCharset().toString();
        try {
            return new String(str.getBytes(charset),"iso8859-1");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getClientAddr(){
        HttpServletRequest request = WebHolder.getRequest();
        if(request==null)return "";
        String ip = request.getHeader("x-forwarded-for");
//        System.out.println("x-forwarded-for ip: " + ip);
        if (StringKit.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
//            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
//            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
//            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
//            System.out.println("X-Real-IP ip: " + ip);
        }
        if (StringKit.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
//            System.out.println("getRemoteAddr ip: " + ip);
        }
//        System.out.println("获取客户端ip: " + ip);
        return ip;
    }

    public static String getUserAgent(){
        HttpServletRequest request = WebHolder.getRequest();
        if(request==null)return "";
        return request.getHeader("user-agent");
    }

    public static String getRequestURI(){
        HttpServletRequest request = WebHolder.getRequest();
        if(request==null)return "";
        return request.getRequestURI();
    }


    public static String getContextPath(){
        HttpServletRequest request = WebHolder.getRequest();
        if(request==null)return "";
        return request.getServletContext().getContextPath();
    }

    public static String getRequestQueryString(){
        HttpServletRequest request = WebHolder.getRequest();
        if(request==null)return "";
        return request.getQueryString();
    }
}
