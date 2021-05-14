package com.vekai.crops.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;

public class HttpUtil {

  private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

  private static final int SUCCESS_CODE = 200;

  public static String doGet(String url) throws Exception {
    JsonNode nHead = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHead, "Content-Type", "application/json; charset=utf-8");
    return doGet(url, nHead);
  }

  public static String doGet(String url, JsonNode nHead) throws Exception {
    String sReturn = "";

    CloseableHttpClient client = null;
    CloseableHttpResponse response = null;
    try {
      /**
       *  创建一个httpclient对象
       */
      client = HttpClients.createDefault();
      /**
       * 创建一个post对象
       */
      HttpGet httpget = new HttpGet(url);
      /**
       * 设置请求的报文头部
       */
      if (CommonUtil.ifIsNotEmpty(nHead)) {
        Iterator<Entry<String, JsonNode>> itHead = nHead.fields();
        while(itHead.hasNext()) {
          Entry<String, JsonNode> field = itHead.next();
          String sKey = field.getKey();
          String sValue = field.getValue().asText();
          httpget.setHeader(new BasicHeader(sKey, sValue));
        }
      }
      /**
       * 执行post请求
       */
      response = client.execute(httpget);
      /**
       * 获取响应码
       */
      int statusCode = response.getStatusLine().getStatusCode();
      if (SUCCESS_CODE == statusCode) {
        /**
         * 通过EntityUitls获取返回内容
         */
        sReturn = EntityUtils.toString(response.getEntity(), "UTF-8");
      } else {
        throw new Exception("接口[" + url + "]响应异常[" + statusCode + "]！");
      }
    } catch (Exception e) {
      logger.error("接口[" + url + "]调用异常！", e);
      throw e;
    } finally {
      if (response != null)
        response.close();
      if (client != null)
        client.close();
    }

    return sReturn;
  }

  /**
   * get获取数据流
   * @param url
   * @return
   * @throws Exception
   */
  public static InputStream doGetStream(String url) throws Exception {
    JsonNode nHead = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHead, "Content-Type", "application/json; charset=utf-8");
    return doGetStream(url, nHead);
  }

  /**
   * get获取数据流
   * @param url
   * @param nHead
   * @return
   * @throws Exception
   */
  public static InputStream doGetStream(String url, JsonNode nHead) throws Exception {
    InputStream is = null;

    CloseableHttpClient client = null;
    CloseableHttpResponse response = null;
    try {
      /**
       *  创建一个httpclient对象
       */
      client = HttpClients.createDefault();
      /**
       * 创建一个post对象
       */
      HttpGet httpget = new HttpGet(url);
      /**
       * 设置请求的报文头部
       */
      if (CommonUtil.ifIsNotEmpty(nHead)) {
        Iterator<Entry<String, JsonNode>> itHead = nHead.fields();
        while(itHead.hasNext()) {
          Entry<String, JsonNode> field = itHead.next();
          String sKey = field.getKey();
          String sValue = field.getValue().asText();
          httpget.setHeader(new BasicHeader(sKey, sValue));
        }
      }
      /**
       * 执行post请求
       */
      response = client.execute(httpget);
      /**
       * 获取响应码
       */
      int statusCode = response.getStatusLine().getStatusCode();
      if (SUCCESS_CODE == statusCode) {
        /**
         * 通过EntityUitls获取返回内容
         */
        InputStream inputStream = response.getEntity().getContent();
        ByteArrayOutputStream baos = null;
        try {
          baos = new ByteArrayOutputStream();
          byte[] buffer = new byte[4096];
          int len;
          while ((len = inputStream.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
          }
          baos.flush();
          is = new ByteArrayInputStream(baos.toByteArray());
        } catch (Exception e) {
          if (baos != null) {
            baos.close();
          }
        }
      } else {
        throw new Exception("接口[" + url + "]响应异常[" + statusCode + "]！");
      }
    } catch (Exception e) {
      logger.error("接口[" + url + "]调用异常！", e);
      throw e;
    } finally {
      if (response != null)
        response.close();
      if (client != null)
        client.close();
    }

    return is;
  }

  public static String doPost(String url, String sParam) throws Exception {
    JsonNode nHead = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHead, "Content-Type", "application/json; charset=utf-8");
    return doPost(url, sParam, nHead);
  }

  public static String doPost(String url, String sParam, JsonNode nHead) throws Exception {
    String sReturn = "";

    CloseableHttpClient client = null;
    CloseableHttpResponse response = null;
    try {
      /**
       *  创建一个httpclient对象
       */
      client = HttpClients.createDefault();
      /**
       * 创建一个post对象
       */
      HttpPost post = new HttpPost(url);
      /**
       * 包装成一个Entity对象
       */
      StringEntity se = new StringEntity(sParam, "utf-8");
      /**
       * 设置请求的内容
       */
      post.setEntity(se);
      /**
       * 设置请求的报文头部
       */
      if (CommonUtil.ifIsNotEmpty(nHead)) {
        Iterator<Entry<String, JsonNode>> itHead = nHead.fields();
        while(itHead.hasNext()) {
          Entry<String, JsonNode> field = itHead.next();
          String sKey = field.getKey();
          String sValue = field.getValue().asText();
          post.setHeader(new BasicHeader(sKey, sValue));
        }
      }
      /**
       * 执行post请求
       */
      response = client.execute(post);
      /**
       * 获取响应码
       */
      int statusCode = response.getStatusLine().getStatusCode();
      if (SUCCESS_CODE == statusCode) {
        /**
         * 通过EntityUitls获取返回内容
         */
        sReturn = EntityUtils.toString(response.getEntity(), "UTF-8");
      } else {
        throw new Exception("接口[" + url + "]响应异常[" + statusCode + "]！");
      }
    } catch (Exception e) {
      logger.error("接口[" + url + "]调用异常！", e);
      throw e;
    } finally {
      if (response != null)
        response.close();
      if (client != null)
        client.close();
    }

    return sReturn;
  }

  /**
   * put上传数据流
   * @param url
   * @param inputStream
   * @return
   * @throws Exception
   */
  public static String doPut(String url, InputStream inputStream) throws Exception {
    JsonNode nHead = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHead, "Content-Type", "application/octet-stream");
    return doPut(url, inputStream, nHead);
  }

  /**
   * put上传数据流
   * @param url
   * @param inputStream
   * @param nHead
   * @return
   * @throws Exception
   */
  public static String doPut(String url, InputStream inputStream, JsonNode nHead) throws Exception {
    String sReturn = "";

    CloseableHttpClient client = null;
    CloseableHttpResponse response = null;
    try {
      /**
       *  创建一个httpclient对象
       */
      client = HttpClients.createDefault();
      /**
       * 创建一个post对象
       */
      HttpPut httpPut = new HttpPut(url);
      /**
       * 包装成一个Entity对象
       */
      EntityBuilder builder = EntityBuilder.create().setContentEncoding("utf-8");
      builder.setStream(inputStream);
      HttpEntity entity = builder.build();
      /**
       * 设置请求的内容
       */
      httpPut.setEntity(entity);
      /**
       * 设置请求的报文头部
       */
      if (CommonUtil.ifIsNotEmpty(nHead)) {
        Iterator<Entry<String, JsonNode>> itHead = nHead.fields();
        while(itHead.hasNext()) {
          Entry<String, JsonNode> field = itHead.next();
          String sKey = field.getKey();
          String sValue = field.getValue().asText();
          httpPut.setHeader(new BasicHeader(sKey, sValue));
        }
      }
      /**
       * 执行post请求
       */
      response = client.execute(httpPut);
      /**
       * 获取响应码
       */
      int statusCode = response.getStatusLine().getStatusCode();
      if (SUCCESS_CODE == statusCode) {
        /**
         * 通过EntityUitls获取返回内容
         */
        sReturn = EntityUtils.toString(response.getEntity(), "UTF-8");
      } else {
        throw new Exception("接口[" + url + "]响应异常[" + statusCode + "]！");
      }
    } catch (Exception e) {
      logger.error("接口[" + url + "]调用异常！", e);
      throw e;
    } finally {
      if (response != null)
        response.close();
      if (client != null)
        client.close();
    }

    return sReturn;
  }

  /**
   * 获取请求IP地址
   * @param request
   * @return
   */
  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
