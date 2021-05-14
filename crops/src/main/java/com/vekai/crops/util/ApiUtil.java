package com.vekai.crops.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class ApiUtil {
  public static Logger logger = LoggerFactory.getLogger(ApiUtil.class);

  @Value("${mrapi.gateway.url}")
  String Url;

  private String CephHeaderFid = "CEPH-FID";

  /**
   * 调用接口网关
   *
   * @param gatewayPath
   * @param param
   * @return
   */
  public String callGateway(String gatewayPath, String param) {
    String sReturn = "";

    try {
      JsonNode nHeader = JsonUtil.getJson();
      JsonUtil.setJsonStringValue(nHeader, "Content-Type", "application/json; charset=utf-8");
      sReturn = callGateway(gatewayPath, param, nHeader);
    } catch (Exception e) {
      logger.error("调用网关异常！", e);
    }

    return sReturn;
  }

  /**
   * 调用接口网关
   *
   * @param gatewayPath
   * @param param
   * @param nHeader
   * @return
   */
  public String callGateway(String gatewayPath, String param, JsonNode nHeader) {
    String sReturn = "";

    try {
      sReturn = HttpUtil.doPost(Url + gatewayPath, param, nHeader);
    } catch (Exception e) {
      logger.error("调用网关异常！", e);
    }

    return sReturn;
  }

  /**
   * 通过fid上传ceph文件数据流
   * @param gatewayPath
   * @param inputStream
   * @param fid
   * @return
   * @throws Exception
   */
  public String putCephObject(String gatewayPath, InputStream inputStream, String fid) throws Exception {
    JsonNode nHeader = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHeader, CephHeaderFid, fid);
    return putGatewayStream(gatewayPath, inputStream, nHeader);
  }

  /**
   * 调用接口网关，上传数据流
   *
   * @param gatewayPath
   * @param inputStream
   * @param nHeader
   * @return
   */
  public String putGatewayStream(String gatewayPath, InputStream inputStream, JsonNode nHeader) throws Exception {
    String sReturn = "";

    try {
      sReturn = HttpUtil.doPut(Url + gatewayPath, inputStream, nHeader);
    } catch (Exception e) {
      logger.error("调用网关异常！", e);
      throw e;
    }

    return sReturn;
  }

  /**
   * 通过fid下载ceph文件数据流
   * @param gatewayPath
   * @param fid
   * @return
   * @throws Exception
   */
  public InputStream getCephObject(String gatewayPath, String fid) throws Exception {
    JsonNode nHeader = JsonUtil.getJson();
    JsonUtil.setJsonStringValue(nHeader, CephHeaderFid, fid);
    return getGatewayStream(gatewayPath, nHeader);
  }

  /**
   * 调用接口网关，获取数据流
   *
   * @param gatewayPath
   * @param nHeader
   * @return
   */
  public InputStream getGatewayStream(String gatewayPath, JsonNode nHeader) throws Exception {
    InputStream inputStream = null;

    try {
      inputStream = HttpUtil.doGetStream(Url + gatewayPath, nHeader);
    } catch (Exception e) {
      logger.error("调用网关异常！", e);
      throw e;
    }

    return inputStream;
  }
}
