package com.vekai.crops.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vekai.crops.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

public class JsonUtil {
  public static Logger logger = (Logger) LoggerFactory.getLogger(JsonUtil.class);
  public static ObjectMapper mapper = new ObjectMapper().configure(Feature.ALLOW_SINGLE_QUOTES, true)
          .configure(Feature.ALLOW_COMMENTS, true).configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
          .configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
  public static ObjectMapper xmlMapper = new XmlMapper();

  public static ObjectNode getObjectNode() throws Exception {
    return mapper.createObjectNode();
  }

  public static ArrayNode getArrayNode() throws Exception {
    return mapper.createArrayNode();
  }

  public static ObjectNode getObjectNode(File sJson) throws Exception {
    return (ObjectNode) getJson(sJson);
  }

  public static ObjectNode getObjectNode(String sJson) throws Exception {
    return (ObjectNode) getJson(sJson);
  }

  public static JsonNode getJson() {
    try {
      return mapper.readTree("{}");
    } catch (Exception e) {
      return null;
    }
  }

  public static JsonNode getJsonByJava(Object javaObject) {
    try {
      return mapper.valueToTree(javaObject);
    } catch (Exception e) {
      return null;
    }
  }

  public static JsonNode getJsonFromJava(Object object) {
    try {
      String s = mapper.writeValueAsString(object);
      return mapper.readTree(s);
    } catch (Exception e) {
      return null;
    }
  }

  public static JsonNode getRecordJson(String sID) throws Exception {
    return mapper.readTree("{'_id':'" + sID + "'}");
  }

  public static JsonNode getJson(JsonNode json) throws Exception {
    if (CommonUtil.ifIsEmpty(json)) {
      if (CommonUtil.ifIsNotNull(json) && "[]".equals(json.toString())) {
        return getJson("[]");
      } else {
        return getJson();
      }
    } else {
      return getJson(json.toString());
    }
  }

  public static JsonNode getJson(String sJson) throws Exception {
    if (CommonUtil.ifIsNull(sJson)) {
      return null;
    }
    return mapper.readTree(sJson);
  }

  public static JsonNode getJson(File file) throws Exception {
    return mapper.readTree(file); // mapper：ObjectMapper对象
  }

  /**
   * 根据XML格式字符串获取相应格式json对象
   *
   * @param xml
   *          xml格式字符串
   *          <ul>
   *          <li>&lt;xml&gt;&lt;a&gt;1&lt;/a&gt;&lt;b&gt;2&lt;/b&gt;&lt;/xml&gt
   *          ;"</li>
   *          </ul>
   * @return {String}
   * @throws Exception
   */
  public static JsonNode getJsonFromXML(String xml) throws Exception {
    return getJsonFromXML(xml, false);
  }

  public static JsonNode getJsonFromXML(String xml, boolean bKeepString) throws Exception {
    JSONObject xmlJsonObj = XML.toJSONObject(xml, bKeepString);
    return JsonUtil.getJson(xmlJsonObj.toString());
  }

  public static JsonNode getJsonFromXML2(String xml) throws Exception {
    JSONObject jObject = XML.toJSONObject(xml);
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    Object json = mapper.readValue(jObject.toString(), Object.class);
    String output = mapper.writeValueAsString(json);

    return getJson(output);
  }

  public static <T>T getJavaFromJson(String sJson, Class<T> objClass) throws Exception {
    if (CommonUtil.ifIsEmpty(sJson)) {
      return null;
    } else {
      return mapper.readValue(sJson, objClass);
    }
  }

  public static <T>T getJavaFromJson(JsonNode jsonNode, Class<T> objClass) throws Exception {
    if (CommonUtil.ifIsEmpty(jsonNode)) {
      return null;
    } else {
      return mapper.readValue(jsonNode.toString(), objClass);
    }
  }

  public static <T> List<T> getListFromJson(String sJson, Class<T> objClass) throws Exception {
    if (CommonUtil.ifIsEmpty(sJson)) {
      return null;
    } else {
      try {
        ArrayNode aList = (ArrayNode)JsonUtil.getJson(sJson);
        List<T> list = new ArrayList();
        for (JsonNode nItem : aList) {
          list.add(JsonUtil.getJavaFromJson(nItem, objClass));
        }
        return list;
      } catch (Exception e) {
        return null;
      }
    }
  }

  public static <T> List<T> getListFromJson(JsonNode jsonNode, Class<T> objClass) throws Exception {
    if (CommonUtil.ifIsEmpty(jsonNode)) {
      return null;
    } else {
      return getListFromJson(jsonNode.toString(), objClass);
    }
  }

  /**
   * json对象内容比较是否不一致，不一致的情况如下：
   * <br>1、一个为null，另一个不为null；
   * <br>2、都不为null，且内容不一致
   * @param nSrc
   * @param nTag
   * @return
   */
  public static boolean isNotSame(JsonNode nSrc, JsonNode nTag) {
    return !isSame(nSrc, nTag);
  }

  /**
   * json对象内容比较是否一致，一致的情况如下：
   * <br>1、都为null；
   * <br>2、都不为null，且内容一致
   * @param nSrc
   * @param nTag
   * @return
   */
  public static boolean isSame(JsonNode nSrc, JsonNode nTag) {
    boolean bSame = false;
    if (nSrc == null && nTag == null) {
      return true;
    } else if (nSrc != null && nTag != null && nSrc.toString().equals(nTag.toString())) {
      return true;
    }

    return bSame;
  }

  public static boolean isJsonNode(JsonNode jNode) {
    if (CommonUtil.ifIsNull(jNode))
      return false;
    String sClassName = jNode.getClass().getName();
    if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(sClassName))
      return true;
    return false;
  }

  public static boolean isArrayNode(JsonNode jNode) {
    if (CommonUtil.ifIsNull(jNode))
      return false;
    String sClassName = jNode.getClass().getName();
    if ("com.fasterxml.jackson.databind.node.ArrayNode".equals(sClassName))
      return true;
    return false;
  }

  public static String getJsonStringValue(JsonNode mainNode, String sPath) throws Exception {
    return getJsonStringValue(mainNode, sPath, "");
  }

  public static String getJsonStringValue(String mainNode, String sPath) throws Exception {
    return getJsonStringValue(mainNode, sPath, "");
  }

  public static String getJsonStringValue(JsonNode mainNode, String path, String defaultValue) throws Exception {
    if (CommonUtil.ifIsNull(mainNode)) {
      return defaultValue;
    }
    JsonNode jNode = queryJson(mainNode, path);
    if (CommonUtil.ifIsNotNull(jNode)) {
      return jNode.asText();
    } else {
      return defaultValue;
    }
  }

  /**
   * 获取jsonnode中的指定值，若不存在或不是String则采用默认值
   *
   * @param mainNode
   * @param sPath
   * @param sDefaultValue
   * @return
   * @throws Exception
   */
  public static String getJsonStringValue(String mainNode, String sPath, String sDefaultValue) throws Exception {
    return getJsonStringValue(JsonUtil.getJson(mainNode), sPath, sDefaultValue);
  }

  public static Boolean getJsonBooleanValue(String mainNode, String sPath) throws Exception {
    return getJsonBooleanValue(getJson(mainNode), sPath, false);
  }

  public static Boolean getJsonBooleanValue(JsonNode mainNode, String sPath) throws Exception {
    return getJsonBooleanValue(mainNode, sPath, false);
  }

  public static Boolean getJsonBooleanValue(String mainNode, String sPath, boolean bDefault) throws Exception {
    return getJsonBooleanValue(getJson(mainNode), sPath, bDefault);
  }

  public static Boolean getJsonBooleanValue(JsonNode mainNode, String sPath, boolean bDefault) throws Exception {
    if (CommonUtil.ifIsEmpty(mainNode))
      return bDefault;
    JsonNode jNode = queryJson(mainNode, sPath);
    if (CommonUtil.ifIsNotEmpty(jNode)) {
      return jNode.asBoolean();
    } else {
      return bDefault;
    }
  }

  public static String getJsonNodeStringValue(JsonNode mainNode, String sPath) throws Exception {
    JsonNode node = mainNode.get(sPath);
    // System.out.println("node="+node);
    String sReturn = "";
    if (node != null) {
      sReturn = node.asText();
    }
    return sReturn;
  }

  public static double getJsonNodeDoubleValue(JsonNode mainNode, String sPath) throws Exception {
    JsonNode node = mainNode.get(sPath);
    // System.out.println("node="+node);
    double sReturn = 0;
    if (node != null) {
      sReturn = node.asDouble();
    }

    return sReturn;
  }

  public static int getJsonNodeIntValue(JsonNode mainNode, String sPath) throws Exception {
    JsonNode node = queryJson(mainNode, sPath);
    // System.out.println("node="+node);
    int sReturn = 0;
    if (node != null) {
      sReturn = node.asInt();
    }

    return sReturn;
  }

  public static void setJsonStringValue(String mainNode, String sPath, String sName, String sValue) throws Exception {
    setJsonStringValue(getJson(mainNode), sPath, sName, sValue);
  }

  public static void setJsonStringValue(JsonNode mainNode, String sPath, String sName, String sValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    ((ObjectNode) node).put(sName, sValue);
  }

  public static void setJsonStringValue(JsonNode mainNode, String sPath, String sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      setJsonStringValue(mainNode, "", sPath, sValue);
    } else {
      setJsonStringValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
    // String[] aPath = CommonUtil.getLastPathArray(sPath);
    // JsonNode node = queryJsonForce(mainNode, aPath[0]);
    // ((ObjectNode) node).put(aPath[1], sValue);
  }

  public static void setJsonValue(JsonNode mainNode, String sPath, String sValue) throws Exception {
    setJsonValue(mainNode, sPath, getJson(sValue));
  }

  public static void setJsonValue(JsonNode mainNode, String sPath, String sName, String sValue) throws Exception {
    setJsonValue(mainNode, sPath, sName, getJson(sValue));
  }

  public static void setJsonValue(JsonNode mainNode, String sPath, JsonNode sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      setJsonValue(mainNode, "", sPath, sValue);
    } else {
      setJsonValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
  }

  public static void setJsonValue(JsonNode mainNode, String sPath, String sName, JsonNode sValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    ((ObjectNode) node).set(sName, sValue);
  }

  public static BigDecimal getJsonBigDecimalValue(JsonNode mainNode, String sPath) throws Exception {
    return new BigDecimal(getJsonDoubleValue(mainNode, sPath, 0));
  }

  public static double getJsonDoubleValue(JsonNode mainNode, String sPath) throws Exception {
    return getJsonDoubleValue(mainNode, sPath, 0);
  }

  public static double getJsonDoubleValue(JsonNode mainNode, String sPath, double dDefault) throws Exception {
    JsonNode node = queryJson(mainNode, sPath);
    double sReturn = 0;
    if (node != null) {
      if (node.isTextual()) {
        sReturn = Double.parseDouble(node.asText().replaceAll(",", ""));
      } else {
        sReturn = node.asDouble();
      }
    } else {
      return dDefault;
    }

    return sReturn;
  }

  public static double getJsonDoubleValue(String mainNode, String sPath) throws Exception {
    return getJsonDoubleValue(getJson(mainNode), sPath);
  }

  public static int getJsonIntValue(String mainNode, String sPath) throws Exception {
    return getJsonIntValue(mainNode, sPath, 0);
  }

  public static int getJsonIntValue(JsonNode mainNode, String sPath) throws Exception {
    return getJsonIntValue(mainNode.toString(), sPath, 0);
  }

  public static int getJsonIntValue(JsonNode mainNode, String sPath, int iDefault) throws Exception {
    JsonNode node = queryJson(mainNode, sPath);
    // if (node != null) {
    if (CommonUtil.ifIsNotEmpty(node)) {
      if (node.isTextual()) {
        return Integer.parseInt(node.asText().replaceAll(",", ""));
      } else {
        return node.asInt();
      }
    } else {
      return iDefault;
    }
  }

  public static int getJsonIntValue(String mainNode, String sPath, int iDefault) throws Exception {
    return getJsonIntValue(JsonUtil.getJson(mainNode), sPath, iDefault);
  }

  public static long getJsonLongValue(JsonNode mainNode, String sPath) throws Exception {
    JsonNode node = queryJson(mainNode, sPath);
    long sReturn = 0;
    if (node != null) {
      if (node.isTextual()) {
        sReturn = Long.parseLong(node.asText().replaceAll(",", ""));
      } else {
        sReturn = node.asLong();
      }
    }
    return sReturn;
  }

  /**
   * @author zjxu
   * @dateTime 20190402
   * 将JsonNodeArray转换为带指定接节点名称的Json字符串
   * @param jsonNodeArray 目标转换JsonNode数组
   * @param toNodeName    指定的节点名称
   * @return
   * @throws Exception
   */
  public static String getJsonArrayString(JsonNode[] jsonNodeArray, String toNodeName) throws Exception {
    StringBuffer sb = new StringBuffer();
    if(jsonNodeArray!=null&&toNodeName!=null&&!"".equals(toNodeName)&&jsonNodeArray.length>0){
      for(int i=0;i<jsonNodeArray.length;i++){
        String jsonStr = mapper.writeValueAsString(jsonNodeArray[i]);
        if(i==0){
          sb.append("{'"+toNodeName+"':["+jsonStr+",");
        }else if(i>0&&i<jsonNodeArray.length-1){
          sb.append(jsonStr+",");
        }else{
          sb.append(","+jsonStr+"]}");
        }
      }
    }else{
      sb.append("{'"+toNodeName+"':'[]'}");
    }
    return sb.toString();
  }
  public static void setJsonIntValue(String mainNode, String sPath, int sValue) throws Exception {
    setJsonIntValue(JsonUtil.getJson(mainNode), sPath, sValue);
  }

  public static void setJsonIntValue(JsonNode mainNode, String sPath, int sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      setJsonIntValue(mainNode, "", sPath, sValue);
    } else {
      setJsonIntValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
  }

  public static void setJsonDoubleValue(String mainNode, String sPath, double sValue) throws Exception {
    setJsonDoubleValue(JsonUtil.getJson(mainNode), sPath, sValue);
  }

  public static void setJsonDoubleValue(JsonNode mainNode, String sPath, double sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      setJsonDoubleValue(mainNode, "", sPath, sValue);
    } else {
      setJsonDoubleValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
  }

  public static void setJsonIntValue(String mainNode, String sPath, String sName, int sValue) throws Exception {
    setJsonIntValue(getJson(mainNode), sPath, sName, sValue);
  }

  public static void setJsonIntValue(JsonNode mainNode, String sPath, String sName, int sValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    ((ObjectNode) node).put(sName, sValue);
  }

  public static void setJsonLongValue(JsonNode mainNode, String sPath, String sName, long lValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    ((ObjectNode) node).put(sName, lValue);
  }

  public static void setJsonDoubleValue(String mainNode, String sPath, String sName, double sValue) throws Exception {
    setJsonDoubleValue(getJson(mainNode), sPath, sName, sValue);
  }

  public static void setJsonDoubleValue(JsonNode mainNode, String sPath, String sName, double sValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    ((ObjectNode) node).put(sName, sValue);
  }

  public static void setJsonNodeDoubleValue(JsonNode mainNode, String sName, double sValue) throws Exception {
    ((ObjectNode) mainNode).put(sName, sValue);
  }

  public static void setJsonNodeStringValue(JsonNode mainNode, String sName, String sValue) throws Exception {
    ((ObjectNode) mainNode).put(sName, sValue);
  }

  public static void setJsonNodeIntValue(JsonNode mainNode, String sName, int sValue) throws Exception {
    ((ObjectNode) mainNode).put(sName, sValue);
  }

  public static JsonNode matchJson(JsonNode mainNode, String sMatch) throws Exception {
    JsonNode nNode = JsonUtil.getJson("{}");
    String[] a = StringUtils.split(sMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n != null) {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    return nNode;
  }

  public static JsonNode matchJson(JsonNode mainNode, String sMatch, String nMatch) throws Exception {
    JsonNode nNode = JsonUtil.getJson("{}");
    String[] a = StringUtils.split(sMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n != null) {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    a = StringUtils.split(nMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        throw new Exception(s + " is null");
      } else if (!NumberUtils.isNumber(n.asText())) {
        throw new Exception(s + " is not number");
      } else {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    return nNode;
  }

  public static JsonNode matchJson(JsonNode mainNode, String sMatch, String iMatch, String nMatch) throws Exception {
    JsonNode nNode = JsonUtil.getJson("{}");
    String[] a = StringUtils.split(sMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n != null) {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    a = StringUtils.split(iMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        throw new Exception(s + " is null");
      } else if (!n.isInt()) {
        throw new Exception(s + " is not double");
      } else {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    a = StringUtils.split(nMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        throw new Exception(s + " is null");
      } else if (!n.isDouble()) {
        throw new Exception(s + " is not double");
      } else {
        ((ObjectNode) nNode).put(s, n);
      }
    }
    return nNode;
  }

  public static String checkJson(JsonNode mainNode, String sMatch, String iMatch, String nMatch) throws Exception {
    String sError = "";
    String[] a = StringUtils.split(sMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (CommonUtil.isNull(n)) {
        sError = s + " is null";
      }
    }
    a = StringUtils.split(iMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        sError = s + " is null";
      } else if (!n.isInt()) {
        sError = s + " is not int";
      }
    }
    a = StringUtils.split(nMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        sError = s + " is null";
      } else if (!n.isDouble()) {
        sError = s + " is not double";
      }
    }
    return sError;
  }

  public static String checkJson(JsonNode mainNode, String sMatch, String nMatch) throws Exception {
    String sError = "";
    String[] a = StringUtils.split(sMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (CommonUtil.isNull(n)) {
        sError = s + " is null";
      }
    }
    a = StringUtils.split(nMatch, ",");
    for (String s : a) {
      JsonNode n = JsonUtil.queryJson(mainNode, s);
      if (n == null) {
        sError = s + " is null";
      } else if (!NumberUtils.isNumber(n.asText())) {
        sError = s + " is not number";
      }
    }
    return sError;
  }

  public static JsonNode mergeJson(String mainNode, String updateNode) throws Exception {
    if (!CommonUtil.isNull(updateNode)) {
      return mergeJson(getJson(mainNode), getJson(updateNode));
    } else {
      return getJson(mainNode);
    }
  }

  public static JsonNode mergeJson(JsonNode mainNode, String updateNode) throws Exception {
    if (!CommonUtil.isNull(updateNode)) {
      return mergeJson(mainNode, getJson(updateNode));
    } else {
      return mainNode;
    }
  }

  public static JsonNode mergeJson(String mainNode, JsonNode updateNode) throws Exception {
    if (updateNode != null) {
      return mergeJson(getJson(mainNode), updateNode);
    } else {
      return getJson(mainNode);
    }
  }

  public static JsonNode mergeJson(JsonNode mainNode, JsonNode updateNode) {
    return mergeJson(mainNode, updateNode, true);
  }

  public static JsonNode mergeJson(JsonNode mainNode, JsonNode updateNode, boolean bAdd) {
    if (updateNode != null) {
      Iterator<String> fieldNames = updateNode.fieldNames();
      while (fieldNames.hasNext()) {
        String fieldName = fieldNames.next();
        JsonNode node = updateNode.get(fieldName);
        JsonNode jsonNode = mainNode.get(fieldName);
        if (jsonNode != null) {
          if (jsonNode.isObject()) {
            mergeJson(jsonNode, node);
          } else if (jsonNode.isArray()) {
            ArrayNode aNode1 = (ArrayNode) jsonNode;
            ArrayNode aNode2 = (ArrayNode) node;
            for (int i = 0; i < aNode2.size(); i++) {
              JsonNode s2 = aNode2.get(i).get("_id");
              boolean bFound = false;
              for (int j = 0; j < aNode1.size(); j++) {
                JsonNode s1 = aNode1.get(j).get("_id");
                // System.out.println("s1="+s1+" s2="+s2);
                if (CommonUtil.ifIsNotEmpty(s1) && CommonUtil.ifIsNotEmpty(s2) && ("" + s1).equals(s2 + "")) {
                  mergeJson(aNode1.get(j), aNode2.get(i));
                  // aNode1.set(j, aNode2.get(i));
                  bFound = true;
                  break;
                }
              }
              if (!bFound) {
                // System.out.println(bFound);
                aNode1.add(aNode2.get(i));
              }
            }
          } else {
            if (mainNode instanceof ObjectNode) {
              ((ObjectNode) mainNode).set(fieldName, node);
            }
          }
        } else {
          if (bAdd)
            ((ObjectNode) mainNode).set(fieldName, node);
        }
      }
    }
    return mainNode;
  }

  public static boolean copyAllJson(String mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return copyAllJson(getJson(mainNode), sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean copyAllJson(JsonNode mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return copyAllJson(mainNode, sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean copyAllJson(String mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    return copyAllJson(getJson(mainNode), sourcePath, updateNode, targetPath);
  }

  public static boolean copyAllJson(JsonNode mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    ArrayNode aNode1 = (ArrayNode) queryJson(mainNode, sourcePath);

    if (aNode1 != null) {
      ArrayNode aNode2 = (ArrayNode) queryJsonArrayForce(updateNode, targetPath);

      for (int i = 0; i < aNode1.size(); i++) {
        aNode2.add(aNode1.get(i));
      }
      return true;
    } else {
      return false;
    }
  }

  public static boolean appendJson(String mainNode, String sourcePath, String updateNode) throws Exception {
    return appendJson(getJson(mainNode), sourcePath, getJson(updateNode));
  }

  public static boolean appendJson(JsonNode mainNode, String sourcePath, String updateNode) throws Exception {
    return appendJson(mainNode, sourcePath, getJson(updateNode));
  }

  public static boolean appendJson(String mainNode, String sourcePath, JsonNode updateNode) throws Exception {
    return appendJson(getJson(mainNode), sourcePath, updateNode);
  }

  public static boolean appendJson(JsonNode mainNode, String sourcePath, JsonNode updateNode) throws Exception {
    ArrayNode aNode1 = (ArrayNode) queryJsonArrayForce(mainNode, sourcePath);

    if (aNode1 != null) {
      aNode1.add(updateNode);
      return true;
    } else {
      return false;
    }
  }

  public static boolean moveAllJsonArray(String mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return moveAllJsonArray(getJson(mainNode), sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean moveAllJsonArray(JsonNode mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return moveAllJsonArray(mainNode, sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean moveAllJsonArray(String mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    return moveAllJsonArray(getJson(mainNode), sourcePath, updateNode, targetPath);
  }

  public static boolean moveAllJsonArray(JsonNode mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    ArrayNode aNode1 = (ArrayNode) queryJson(mainNode, sourcePath);

    if (aNode1 != null) {
      ArrayNode aNode2 = (ArrayNode) queryJsonArrayForce(updateNode, targetPath);

      for (int i = 0; i < aNode1.size(); i++) {
        aNode2.add(aNode1.get(i));
      }
      aNode1.removeAll();
      return true;
    } else {
      return false;
    }
  }

  public static boolean moveJsonArrayById(String mainNode, String sourcePath, String sCondition, String updateNode,
                                          String targetPath) throws Exception {
    return moveJsonArrayById(getJson(mainNode), sourcePath, sCondition, getJson(updateNode), targetPath);
  }

  public static boolean moveJsonArrayById(JsonNode mainNode, String sourcePath, String sCondition, String updateNode,
                                          String targetPath) throws Exception {
    return moveJsonArrayById(mainNode, sourcePath, sCondition, getJson(updateNode), targetPath);
  }

  public static boolean moveJsonArrayById(String mainNode, String sourcePath, String sCondition, JsonNode updateNode,
                                          String targetPath) throws Exception {
    return moveJsonArrayById(getJson(mainNode), sourcePath, sCondition, updateNode, targetPath);
  }

  public static boolean moveJsonArrayById(JsonNode mainNode, String sourcePath, String sCondition, JsonNode updateNode,
                                          String targetPath) throws Exception {
    ArrayNode aNode1 = (ArrayNode) queryJson(mainNode, sourcePath);

    if (aNode1 != null) {
      ArrayNode aNode2 = (ArrayNode) queryJsonArrayForce(updateNode, targetPath);
      String s = "," + sCondition + ",";

      for (int i = 0; i < aNode1.size(); i++) {
        if (s.indexOf("," + aNode1.get(i).get("i").asText() + ",") > -1) {
          aNode2.add(aNode1.get(i));
          aNode1.remove(i);
          i--;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  public static boolean deleteJsonArray(String mainNode, String updateNode) throws Exception {
    return deleteJsonArray(getJson(mainNode), getJson(updateNode));
  }

  public static boolean deleteJsonArray(JsonNode mainNode, String updateNode) throws Exception {
    return deleteJsonArray(mainNode, getJson(updateNode));
  }

  public static boolean deleteJsonArray(JsonNode mainNode, JsonNode updateNode) {
    return deleteJsonArray(mainNode, updateNode, "i");
  }

  public static boolean deleteJsonArray(JsonNode mainNode, JsonNode updateNode, String sSeparator) {
    boolean bDelete = false;
    Iterator<String> fieldNames = updateNode.fieldNames();
    while (fieldNames.hasNext()) {
      String fieldName = fieldNames.next();
      JsonNode node = updateNode.get(fieldName);
      JsonNode jsonNode = mainNode.get(fieldName);
      // System.out.println("node"+node);
      if (jsonNode != null) {
        if (jsonNode.isObject()) {
          deleteJsonArray(jsonNode, node);
        } else if (jsonNode.isArray()) {
          ArrayNode aNode1 = (ArrayNode) jsonNode;
          ArrayNode aNode2 = (ArrayNode) node;
          for (int i = 0; i < aNode2.size(); i++) {
            String s2 = "" + aNode2.get(i).get(sSeparator);
            for (int j = 0; j < aNode1.size(); j++) {
              String s1 = "" + aNode1.get(j).get(sSeparator);
              // System.out.println("s1=" + s1 + " s2=" + s2);
              if (s1.equals(s2)) {
                aNode1.remove(j);
                bDelete = true;
                // aNode1.set(j, null);
                break;
              }
            }
          }
        }
      }
    }

    return bDelete;
  }

  /**
   * 从iIndex开始，删除数组后面的json数据
   * @param aSrc    源数组json
   * @param iIndex  删除数据的起始位置，以0开始
   */
  public static void removeJsonArrayFromIndex(ArrayNode aSrc, int iIndex) {
    if(CommonUtil.ifIsNotEmpty(aSrc)) {
      int iCount = aSrc.size();
      if (iCount > iIndex) {
        for (int i = iCount - 1; i >= iIndex; i--) {
          aSrc.remove(i);
        }
      }
    }
  }

  // 查询Json节点属性字段，返回指定位置和大小的数据
  public static LinkedHashMap<String, JsonNode> queryJsonField(JsonNode node, String sPath, int iPosition, int iSize)
          throws Exception {
    Iterator<Entry<String, JsonNode>> it = queryJson(node, sPath).fields();
    int iIndex = 0;
    int iCount = 0;
    LinkedHashMap<String, JsonNode> list = new LinkedHashMap<String, JsonNode>();
    while (it.hasNext()) {
      iIndex++;
      if (iCount >= iSize) {
        break;
      }
      Entry<String, JsonNode> entry = it.next();
      if (iIndex < iPosition) {
        continue;
      }
      list.put(entry.getKey(), entry.getValue());
      iCount++;
    }

    return list;
  }

  // 强制查询，如果不存在则生成节点
  public static JsonNode queryJsonForce(String mainNode, String updateNode) throws Exception {
    return queryJson(getJson(mainNode), updateNode, true, false);
  }

  public static JsonNode queryJsonForce(JsonNode mainNode, String updateNode) throws Exception {
    return queryJson(mainNode, updateNode, true, false);
  }

  public static JsonNode queryJsonArrayForce(String mainNode, String updateNode) throws Exception {
    return queryJson(getJson(mainNode), updateNode, true, true);
  }

  public static JsonNode queryJsonArrayForce(JsonNode mainNode, String updateNode) throws Exception {
    return queryJson(mainNode, updateNode, true, true);
  }

  public static JsonNode queryJson(String mainNode, String updateNode) throws Exception {
    return queryJson(getJson(mainNode), updateNode, false, false);
  }

  public static JsonNode queryJsonArrayNode(ArrayNode nodes, String field, String value) throws Exception {
    for (JsonNode nField : nodes) {
      if (value.equals(JsonUtil.getJsonStringValue(nField, field))) {
        return nField;
      }
    }
    return null;
  }

  public static JsonNode queryJsonArrayNode(JsonNode[] nodes, String field, String value) throws Exception {
    for (JsonNode nField : nodes) {
      if (value.equals(JsonUtil.getJsonStringValue(nField, field))) {
        return nField;
      }
    }
    return null;
  }

  public static JsonNode queryJsonByField(JsonNode mainNode, String updateNode, String sField, String sValue)
          throws Exception {
    JsonNode nodes = JsonUtil.queryJson(mainNode, updateNode);
    if (nodes != null) {
      Iterator<Entry<String, JsonNode>> it = nodes.fields();
      while (it.hasNext()) {
        Entry<String, JsonNode> entry = it.next();
        JsonNode nField = entry.getValue();
        if (sValue.equals(JsonUtil.getJsonStringValue(nField, sField))) {
          return nField;
        }
      }
    }
    return null;
  }

  public static JsonNode queryJsonByField(ArrayNode mainNode, String sField, String sValue) throws Exception {
    JsonNode node = null;
    for (JsonNode node1 : mainNode) {
      if (sValue.equals(JsonUtil.getJsonStringValue(node1, sField))) {
        return node1;
      }
    }
    return node;
  }

  public static JsonNode queryJson(JsonNode mainNode, String updateNode) throws Exception {
    return queryJson(mainNode, updateNode, false, false);
  }

  public static JsonNode queryJson(JsonNode mainNode, String sPath, boolean bForce, boolean bArray) throws Exception {
    JsonNode node = mainNode;
    String[] a = StringUtils.splitPreserveAllTokens(sPath, ".");

    for (String s : a) {
      String i = CommonUtil.getBetweenString(s, "[", "]");
      if ("".equals(i)) {
        JsonNode node1 = node.get(s);
        // System.out.println(node1);
        if (node1 == null) {
          if (bForce) {
            JsonNode node2 = null;
            if (!bArray) {
              node2 = getJson("{}");
            } else {
              node2 = getJson("[]");
            }
            ((ObjectNode) node).set(s, node2);
            node = node2;
          } else {
            node = null;
          }
        } else {
          node = node1;
        }
      } else {
        String n = CommonUtil.getBeforeString(s, "[");
        // System.out.println("s="+s+" n="+n);
        ArrayNode aNode1 = (ArrayNode) node.get(n);
        // 循环判断条件,
        String[] aSearch = StringUtils.split(i, ",");
        // 符合的条件
        String sCondition = "";
        for (String sSearch : aSearch) {
          String k = CommonUtil.getBeforeString(sSearch, "=");
          String v = CommonUtil.getAfterString(sSearch, "=");
          sCondition += ",'" + k + "':'" + v + "'";
        }
        if (aNode1 != null) {
          node = null;
          // System.out.println("k="+k+" v="+v);
          boolean bFound = false;
          // 针对节点循环
          for (JsonNode node1 : aNode1) {
            boolean bMatch = true;
            // System.out.println(node1.get(k));
            // 匹配条件
            for (String sSearch : aSearch) {
              String k = CommonUtil.getBeforeString(sSearch, "=");
              String v = CommonUtil.getAfterString(sSearch, "=");
              // System.out.println(sSearch);
              if (!v.equals(getJsonNodeStringValue(node1, k))) {
                bMatch = false;
                break;
              }
            }
            if (bMatch) {
              node = node1;
              bFound = true;
              break;
            }
          }

          if (bForce) {
            if (!bFound) {
              node = getJson("{" + sCondition.substring(1) + "}");
              aNode1.add(node);
            }
          }
        } else {
          if (bForce) {
            JsonNode node2 = getJson("['" + sCondition.substring(1) + "']");

            node = node2;
          } else {
            node = null;
          }
        }
      }
      if (node == null) {
        break;
      }
    }
    return node;
  }

  /**
   * 支持父节点是数组的查找，例如： a.b[0].c.d[1].f，如果数组下标对应的json不存在，则新增数组的json对象（可能位置与下标数字不一致）
   *
   * @param mainNode
   *          主json对象
   * @param sPath
   *          查找路径
   * @return
   * @throws Exception
   */
  public static JsonNode queryJsonNew(JsonNode mainNode, String sPath) throws Exception {
    return queryJsonNew(mainNode, sPath, false);
  }

  public static JsonNode queryJsonNew(JsonNode mainNode, String sPath, boolean bForce) throws Exception {
    return queryJsonNew(mainNode, sPath, bForce, false);
  }

  /**
   * 支持父节点是数组的查找，例如： a.b[0].c.d[1].f，如果数组下标对应的json不存在，则新增数组的json对象（可能位置与下标数字不一致）
   *
   * @param mainNode
   *          主json对象
   * @param sPath
   *          查找路径
   * @param bForce
   *          不存在节点时，是否新增
   * @param bArray
   *          查找节点是否数组
   * @return
   * @throws Exception
   */
  public static JsonNode queryJsonNew(JsonNode mainNode, String sPath, boolean bForce, boolean bArray)
          throws Exception {
    JsonNode node = mainNode;
    String[] a = StringUtils.split(sPath, ".");

    int iMax = a.length;
    for (int iCount = 0; iCount < iMax; iCount++) {
      String s = a[iCount];
      String i = CommonUtil.getBetweenString(s, "[", "]");
      if ("".equals(i)) {
        // JsonNode node1 = node.get(s);
        JsonNode node1;
        if (node instanceof ArrayNode) {
          node1 = node.get(Integer.valueOf(s));
        } else {
          node1 = node.get(s);
        }
        // System.out.println(node1);
        if (node1 == null || node1.isNull()) {
          if (bForce) {
            JsonNode node2 = null;
            if (iCount == (iMax - 1)) {
              if (!bArray) {
                node2 = getJson("{}");
              } else {
                node2 = getJson("[]");
              }
            } else {
              node2 = getJson("{}");
            }
            ((ObjectNode) node).set(s, node2);
            node = node2;
          } else {
            node = null;
          }
        } else {
          node = node1;
        }
      } else {
        String n = CommonUtil.getBeforeString(s, "[");
        JsonNode node1 = node.get(n);
        if (node1 != null) {
          ArrayNode aNode1 = (ArrayNode) node1;
          JsonNode node2 = aNode1.get(Integer.valueOf(i));
          if (node2 == null) {
            if (bForce) {
              node2 = getJson("{}");
              aNode1.add(node2);
              node = node2;
            } else {
              node = null;
            }
          } else {
            node = node2;
          }
        } else {
          if (bForce) {
            ArrayNode node2 = (ArrayNode) getJson("[]");
            ((ObjectNode) node).set(n, node2);
            JsonNode node3 = getJson("{}");
            node2.add(node3);
            node = node3;
          } else {
            node = null;
          }
        }
      }
      if (node == null) {
        break;
      }
    }
    return node;
  }

  public static void setJsonValueNew(JsonNode mainNode, String sPath, String sName, JsonNode nValue) throws Exception {
    JsonNode node = queryJsonNew(mainNode, sPath, true);
    ((ObjectNode) node).set(sName, nValue);
  }

  public static void setJsonStringValueNew(JsonNode mainNode, String sPath, String sValue) throws Exception {
    String[] aPath = CommonUtil.getLastPathArray(sPath);
    JsonNode node = queryJsonNew(mainNode, aPath[0], true);
    ((ObjectNode) node).put(aPath[1], sValue);
  }

  public static void setJsonDoubleValueNew(JsonNode mainNode, String sPath, double sValue) throws Exception {
    String[] aPath = CommonUtil.getLastPathArray(sPath);
    JsonNode node = queryJsonNew(mainNode, aPath[0], true);
    ((ObjectNode) node).put(aPath[1], sValue);
  }

  public static void setJsonIntValueNew(JsonNode mainNode, String sPath, int sValue) throws Exception {
    String[] aPath = CommonUtil.getLastPathArray(sPath);
    JsonNode node = queryJsonNew(mainNode, aPath[0], true);
    ((ObjectNode) node).put(aPath[1], sValue);
  }

  public static String getJsonStringValueNew(JsonNode mainNode, String sPath) throws Exception {
    return getJsonStringValueNew(mainNode, sPath, "");
  }

  public static String getJsonStringValueNew(JsonNode mainNode, String sPath, String defaultValue) throws Exception {
    if (CommonUtil.ifIsNull(mainNode)) {
      return defaultValue;
    }
    JsonNode jNode = queryJsonNew(mainNode, sPath);
    if (CommonUtil.ifIsNotNull(jNode)) {
      return jNode.asText();
    } else {
      return defaultValue;
    }
  }

  public static Double getJsonDoubleValueNew(JsonNode mainNode, String sPath) throws Exception {
    return getJsonDoubleValueNew(mainNode, sPath, 0.0);
  }

  public static Double getJsonDoubleValueNew(JsonNode mainNode, String sPath, Double defaultValue) throws Exception {
    JsonNode node = queryJsonNew(mainNode, sPath);
    Double dReturn;
    try {
      if (node != null) {
        if (node.isTextual()) {
          dReturn = Double.parseDouble(node.asText().replaceAll(",", ""));
        } else {
          dReturn = node.asDouble();
        }
      } else {
        return defaultValue;
      }
    } catch (Exception e) {
      return defaultValue;
    }

    return dReturn;
  }

  public static Integer getJsonIntValueNew(JsonNode mainNode, String sPath) throws Exception {
    return getJsonIntValueNew(mainNode, sPath, 0);
  }

  public static Integer getJsonIntValueNew(JsonNode mainNode, String sPath, Integer defaultValue) throws Exception {
    JsonNode node = queryJsonNew(mainNode, sPath);
    try {
      if (CommonUtil.ifIsNotEmpty(node)) {
        if (node.isTextual()) {
          return Integer.parseInt(node.asText().replaceAll(",", ""));
        } else {
          return node.asInt();
        }
      } else {
        return defaultValue;
      }
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static String getJsonString(JsonNode object, String sName, String sValue) throws Exception {
    String[] a = StringUtils.split(sName, ",");
    StringBuffer sb = new StringBuffer();
    for (String s : a) {
      JsonNode node = queryJson(object, s);
      if (node != null) {
        sb.append(",\"" + s + "\":" + node.toString());
      } else {
        sb.append(",\"" + s + "\":\"\"");
      }
    }
    if ("".equals(sName)) {
      if ("".equals(sValue)) {
        return object.toString();
      } else {
        return "{" + sValue + "," + object.toString().substring(1);
      }
    } else {
      if ("".equals(sValue)) {
        return "{" + sb.substring(1) + "}";
      } else {
        return "{" + sValue + sb + "}";
      }
    }
  }

  public static String getJsonString(JsonNode object, String sName) throws Exception {
    return getJsonString(object, sName, "");
  }

  public static String getJsonArrayString(JsonNode object, String sName, String sValue) throws Exception {
    String[] a = StringUtils.split(sName, ",");
    StringBuffer sb = new StringBuffer();
    for (String s : a) {
      JsonNode node = queryJson(object, s);
      if (node != null) {
        sb.append("," + node.toString());
      } else {
        sb.append(",\"\"");
      }
    }
    if ("".equals(sName)) {
      if ("".equals(sValue)) {
        return "[]";
      } else {
        return "[" + sValue + "]";
      }
    } else {
      if ("".equals(sValue)) {
        return "[" + sb.substring(1) + "]";
      } else {
        return "[" + sValue + sb + "]";
      }
    }
  }

  public static String getJsonArrayString(JsonNode object, String sName) throws Exception {
    return getJsonArrayString(object, sName, "");
  }

  // 读取表到缓存
  /*
   * public static JsonNode getTableRecord(String sTable, String sID) throws
   * Exception { DBObject result = DBUtil.getCollection(sTable).findOne(sID); if
   * (result != null) { return getJson(result); } else { return null; } }
   */

  /*
   *
   * //保存表数据 public static WriteResult saveTableRecord(String sTable, String
   * sValue) throws Exception { return
   * DBUtil.getCollection(sTable).save(getDBObject(sValue)); }
   *
   * //保存表数据 public static WriteResult saveTableRecord(String sTable, JsonNode
   * sValue) throws Exception { return
   * DBUtil.getCollection(sTable).save(getDBObject(sValue)); }
   */
  // 读取表到缓存
  /*
   * public static Map<String, JsonNode> getCollectionMap(String sTable) throws
   * Exception { Map<String, JsonNode> map = new HashMap<String, JsonNode>();
   * DBCursor result = DBUtil.getCollection(sTable).find(); for (DBObject
   * dbObject : result) { map.put(dbObject.get("_id").toString(),
   * getJson(dbObject)); } return map; }
   */

  // 获取Map的Json对象
  public static JsonNode getMapJson(Map<String, JsonNode> map, String sID) throws Exception {
    return map.get(sID);
  }

  // 判断是否json数组是否有传入元素的编号
  public static JsonNode getArrayJson(ArrayNode node, String sID) throws Exception {
    return getArrayJson(node, sID, "_id");
  }

  public static JsonNode getArrayJson(ArrayNode node, String sID, String sField) throws Exception {
    for (Iterator<JsonNode> iter = node.elements(); iter.hasNext();) {
      JsonNode circleNode = iter.next();
      if (sID.equals(circleNode.get(sField).asText())) {
        return circleNode;
      }
    }
    return null;
  }

  public static void sortJsonNode(JsonNode node, String sPath) throws Exception {
    JsonNode j = queryJson(node, sPath);
    if (j != null) {
      Iterator<Entry<String, JsonNode>> it = j.fields();
      SortedSet<String> keys = new TreeSet<String>();
      while (it.hasNext()) {
        Entry<String, JsonNode> entry = it.next();
        // list.add(entry.getKey());
        keys.add(entry.getKey());
      }

      JsonNode n = getJson("{}");
      ObjectNode no = (ObjectNode) n;
      Iterator<String> it1 = keys.iterator();
      while (it1.hasNext()) {
        String s = it1.next();
        no.set(s, j.get(s));
      }
      setJsonValue(node, "", sPath, n);
    }
  }

  public static boolean moveJsonNode(String mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return moveJsonNode(getJson(mainNode), sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean moveJsonNode(JsonNode mainNode, String sourcePath, String updateNode, String targetPath)
          throws Exception {
    return moveJsonNode(mainNode, sourcePath, getJson(updateNode), targetPath);
  }

  public static boolean moveJsonNode(String mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    return moveJsonNode(getJson(mainNode), sourcePath, updateNode, targetPath);
  }

  public static boolean moveJsonNode(JsonNode mainNode, String sourcePath, JsonNode updateNode, String targetPath)
          throws Exception {
    JsonNode aNode1 = queryJson(mainNode, sourcePath);
    int i = StringUtils.lastIndexOf(targetPath, ".");
    String s1 = "";
    String s2 = targetPath;
    if (i > -1) {
      s1 = targetPath.substring(0, i);
      s2 = targetPath.substring(i + 1);
    }
    setJsonValue(updateNode, s1, s2, aNode1);
    deleteJsonNode(mainNode, sourcePath);
    return true;
  }

  public static boolean deleteJsonNode(String mainNode, String sourcePath) throws Exception {
    return deleteJsonNode(getJson(mainNode), sourcePath);
  }

  public static boolean deleteJsonNode(JsonNode mainNode, String sourcePath) throws Exception {
    int i = StringUtils.lastIndexOf(sourcePath, ".");
    String s1 = "";
    String s2 = sourcePath;
    if (i > -1) {
      s1 = sourcePath.substring(0, i);
      s2 = sourcePath.substring(i + 1);
      ObjectNode n = ((ObjectNode) queryJson(mainNode, s1));
      if (n != null) {
        n.remove(s2);
      }
    } else {
      ((ObjectNode) mainNode).remove(sourcePath);
    }
    return true;
  }

  public static int substractJsonNodeIntValue(JsonNode mainNode, String sPath, String sNode) throws Exception {
    return addJsonNodeIntValue(mainNode, sPath, sNode, -1);
  }

  public static int addJsonNodeIntValue(JsonNode mainNode, String sPath, String sNode) throws Exception {
    return addJsonNodeIntValue(mainNode, sPath, sNode, 1);
  }

  public static int substractJsonNodeIntValue(JsonNode mainNode, String sPath) throws Exception {
    return addJsonNodeIntValue(mainNode, sPath, -1);
  }

  public static int addJsonNodeIntValue(JsonNode mainNode, String sPath) throws Exception {
    return addJsonNodeIntValue(mainNode, sPath, 1);
  }

  //增加节点的整数值
  public static int addJsonNodeIntValue(JsonNode mainNode, String sPath, String sNode, int iValue) throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    int i = getJsonNodeIntValue(node, sNode);
    i += iValue;
    setJsonNodeIntValue(node, sNode, i);
    return i;
  }

  public static int addJsonNodeIntValue(JsonNode mainNode, String sPath, int sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      return addJsonNodeIntValue(mainNode, "", sPath, sValue);
    } else {
      return addJsonNodeIntValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
  }

  //增加节点的浮点值
  public static double addJsonNodeDoubleValue(JsonNode mainNode, String sPath, String sNode, double iValue)
          throws Exception {
    JsonNode node = queryJsonForce(mainNode, sPath);
    double i = getJsonNodeDoubleValue(node, sNode);
    i += iValue;
    setJsonNodeDoubleValue(node, sNode, i);
    return i;
  }

  public static double addJsonNodeDoubleValue(JsonNode mainNode, String sPath, double sValue) throws Exception {
    int i = sPath.lastIndexOf(".");
    if (i == -1) {
      return addJsonNodeDoubleValue(mainNode, "", sPath, sValue);
    } else {
      return addJsonNodeDoubleValue(mainNode, sPath.substring(0, i), sPath.substring(i + 1), sValue);
    }
  }

  /**
   * 获取对象的第一个key值
   *
   * @param node
   * @return
   * @throws Exception
   */
  public static String queryObjectFirstKey(JsonNode node) throws Exception {
    Iterator<Entry<String, JsonNode>> it = node.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      String key = entry.getKey();
      // System.out.println(key);
      return key;
    }
    return "";
  }

  /**
   * 获取对象的第一个keyObject
   *
   * @param node
   * @return
   * @throws Exception
   */
  public static JsonNode queryObjectFirstKObject(JsonNode node) throws Exception {
    Iterator<Entry<String, JsonNode>> it = node.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      String key = entry.getKey();
      JsonNode keyvalue = entry.getValue();
      // System.out.println(keyvalue);
      return getJson("{'k':'" + key + "','v':" + keyvalue + "}");
    }
    return null;
  }

  /**
   * 查询对象指定规则记录
   *
   * @param node
   * @return
   * @throws Exception
   */
  public static String queryObjectRule(JsonNode node) throws Exception {
    JsonNode aNode = queryJson(node, "r");
    String r = getJsonValueWithPath(aNode);
    // for (int i = 0; i < aNode.size(); i++) {
    // r = "," + aNode.get(i).asText();
    // }
    // if (!isNull(r))
    // r = r.substring(1);
    return r;
  }

  public static String getJsonValueWithPath(JsonNode node) throws Exception {
    return getJsonValueWithPath(node, "");
  }

  // 返回基于key路径的对象
  public static String getJsonValueWithPath(JsonNode node, String sPrefix) throws Exception {
    List<String> list = new ArrayList<String>();
    genJsonQueryString(node, "", list, sPrefix);
    StringBuffer sb = new StringBuffer();
    for (String s : list) {
      sb.append("," + s);
    }
    if (!sb.toString().equals("")) {
      return "{" + sb.substring(1) + "}";
    } else {
      return "{}";
    }
  }

  public static void genJsonQueryString(JsonNode node, String sPath, List<String> list) throws Exception {
    genJsonQueryString(node, sPath, list, "");
  }

  // 返回基于对象key值的查询语句列表
  public static void genJsonQueryString(JsonNode node, String sPath, List<String> list, String sPrefix)
          throws Exception {
    // if(CommonUtil.ifIsNotEmpty(sPrefix)) sPrefix = sPrefix+".";
    if (node.isContainerNode()) {
      Iterator<Entry<String, JsonNode>> it = node.fields();
      if (!it.hasNext()) {
        list.add("'" + sPrefix + sPath.substring(1) + "':" + node);
      } else {
        while (it.hasNext()) {
          Entry<String, JsonNode> entry = it.next();
          genJsonQueryString(entry.getValue(), sPath + "." + entry.getKey(), list, sPrefix);
        }
      }
    } else {
      list.add("'" + sPrefix + sPath.substring(1) + "':" + node);
    }
  }

  /**
   * 对jsonnode中sNodePath对应的数组节点重新排序，根据数组中的某字段排序
   *
   * @param jsonnode
   *          待排序的json对象
   * @param sNodePath
   *          节点路径
   * @param sFieldPath
   *          字段路径
   * @param bAsce
   *          是否升序
   * @throws Exception
   */
  public static void sortArrayJsonByValueDesc(JsonNode jsonnode, String sNodePath, String sFieldPath, boolean bAsce)
          throws Exception {
    ArrayNodeValueComparatorDesc com = new ArrayNodeValueComparatorDesc();
    ArrayNode node;
    if (CommonUtil.ifIsEmpty(sNodePath)) {
      node = (ArrayNode) jsonnode;
    } else {
      node = (ArrayNode) queryJson(jsonnode, sNodePath);
    }

    Iterator<JsonNode> it = node.iterator();
    List<JsonNode> list = new ArrayList();

    while (it.hasNext()) {
      list.add(it.next());
    }
    com.Field = sFieldPath;
    com.ASCE = bAsce;
    Collections.sort(list, com);
    node.removeAll();
    for (JsonNode i : list) {
      node.add(i);
    }
    list = null;
  }

  /**
   * 获取数组的前topcount条数据
   * @param node
   * @param path
   * @param topcount
   * @return
   * @throws Exception
   */
  public static ArrayNode getArrayTopCount(JsonNode node, String path, int topcount) throws Exception {
    ArrayNode aTop = (ArrayNode)getJson("[]");

    ArrayNode aCur;
    if (CommonUtil.ifIsNotEmpty(path)) {
      aCur = (ArrayNode)queryJson(node, path);
    } else {
      aCur = (ArrayNode)node;
    }

    for (int i=0;i<aCur.size();i++) {
      if (i == topcount) {
        break;
      }
      aTop.add(aCur.get(i));
    }

    return aTop;
  }

  /**
   * 获取数组的后lastcount条数据
   * @param node
   * @param path
   * @param lastcount
   * @return
   * @throws Exception
   */
  public static ArrayNode getArrayLastCount(JsonNode node, String path, int lastcount) throws Exception {
    ArrayNode aLast = (ArrayNode)getJson("[]");

    ArrayNode aCur;
    if (CommonUtil.ifIsNotEmpty(path)) {
      aCur = (ArrayNode)queryJson(node, path);
    } else {
      aCur = (ArrayNode)node;
    }
    int iSize = aCur.size();
    for (int i = iSize; i > 0; i--) {
      if (i == (iSize - lastcount)) {
        break;
      }
      aLast.add(aCur.get(i - 1));
    }

    return aLast;
  }

  /**
   * 获取jsonNode的字段名
   *
   * @param jsonnode
   * @return
   */
  public static String getJsonStringName(JsonNode jsonnode) {
    return getJsonStringName(jsonnode, "");
  }

  /**
   * 获取jsonNode的字段名
   *
   * @param jsonnode
   * @param sType
   *          字段名拼接方式
   *          <ul>
   *          <li>1: 以''包裹字段名作为查询条件格式</li>
   *          </ul>
   * @return
   */
  public static String getJsonStringName(JsonNode jsonnode, String sType) {
    if (jsonnode == null)
      return "";
    String sFieldNames = "";
    if (jsonnode.isArray()) {
      Iterator<JsonNode> it = jsonnode.elements();
      while (it.hasNext()) {
        JsonNode jNode = it.next();
        if (CommonUtil.ifIsEmpty(jNode))
          continue;
        String sName = jNode.asText();
        if ("1".equals(sType))
          sName = "'" + sName + "'";
        sFieldNames += "," + sName;
      }
    } else {
      Iterator<String> it = jsonnode.fieldNames();
      while (it.hasNext()) {
        String sName = it.next();
        if ("1".equals(sType))
          sName = "'" + sName + "'";
        sFieldNames += "," + sName;
      }
    }
    if (CommonUtil.ifIsNotEmpty(sFieldNames))
      sFieldNames = sFieldNames.substring(1);
    return sFieldNames;
  }

  /**
   * 将jsonnode格式的数据格式化输出
   *
   * @param input
   * @return {String}
   */
  public static String getJsonFormatPrint(String input) {
    String sPrint = "";
    try {
      Object json = mapper.readValue(input, Object.class);
      sPrint = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sPrint;
  }

  public static String getJsonFormatPrint(JsonNode json) {
    String sPrint = "";
    try {
      sPrint = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sPrint;
  }

  public static JsonNode getJsonArrayByIndex(JsonNode jNode, int... aIndexs) throws Exception {
    if (CommonUtil.ifIsEmpty(jNode)) {
      logger.warn("[ERROR] result of getJsonArrayByIndex JsonNode is Empty! ", jNode);
      throw new Exception("[ERROR] result of getJsonArrayByIndex JsonNode is Empty");
    }
    if (CommonUtil.ifIsNotEmpty(jNode) && !jNode.isArray()) {
      logger.warn("[ERROR] result of getJsonArrayByIndex('{}') is not ArrayNode! ", jNode);
      throw new Exception("[ERROR] result of getJsonArrayByIndex('" + jNode + "') is not ArrayNode! ");
    }
    if (CommonUtil.ifIsEmpty(aIndexs)) {
      logger.warn("[ERROR] result of getJsonArrayByIndex('{}') is Empty Array! ", aIndexs);
      throw new Exception("[ERROR] result of getJsonArrayByIndex('{" + aIndexs + "}') is Empty Array! ");
    }
    JsonNode jReturn = JsonUtil.getJson("[]");
    for (int index : aIndexs) {
      ((ArrayNode) jReturn).add(((ArrayNode) jNode).get(index));
    }
    return jReturn;
  }

  public static JsonNode getJsonArrayByKeyValue(JsonNode jNode, String sKeyValues) throws Exception {
    JsonNode jKeyValues = null;
    if (CommonUtil.ifIsEmpty(sKeyValues)) {
      jKeyValues = JsonUtil.getJson("{}");
    } else {
      jKeyValues = JsonUtil.getJson(sKeyValues);
    }
    return getJsonArrayByKeyValue(jNode, jKeyValues);
  }

  public static JsonNode getJsonArrayByKeyValue(JsonNode jNode, JsonNode jKeyValues) throws Exception {
    if (CommonUtil.ifIsEmpty(jNode)) {
      logger.warn("[ERROR] result of getJsonArrayByIndex JsonNode is Empty! ", jNode);
      throw new Exception("[ERROR] result of getJsonArrayByIndex JsonNode is Empty");
    }
    if (CommonUtil.ifIsNotEmpty(jNode) && !jNode.isArray()) {
      logger.warn("[ERROR] result of getJsonArrayByIndex('{}') is not ArrayNode! ", jNode);
      throw new Exception("[ERROR] result of getJsonArrayByIndex('" + jNode + "') is not ArrayNode! ");
    }
    if (CommonUtil.ifIsEmpty(jKeyValues)) {
      return jNode;
    }
    JsonNode jReturn = JsonUtil.getJson("[]");
    Iterator<JsonNode> it = jNode.elements();
    while (it.hasNext()) {
      JsonNode node = it.next();
      Iterator<Entry<String, JsonNode>> it_kv = jKeyValues.fields();
      Boolean bMatch = true;
      while (it_kv.hasNext()) {
        Entry<String, JsonNode> entry = it_kv.next();
        String sKey = entry.getKey();
        JsonNode jValue = entry.getValue();
        if (CommonUtil.isNull(jValue))
          continue;
        String sValue = jValue.asText();
        if (!CommonUtil.ifIsIndexOf(sValue, JsonUtil.getJsonStringValue(node, sKey))) {
          bMatch = false;
          break;
        }
      }
      if (bMatch) {
        ((ArrayNode) jReturn).add(node);
      } else {
        continue;
      }
    }
    return jReturn;
  }

  public static void setJsonArrayValue(JsonNode jNode, String sPath, JsonNode arrayJson) throws Exception {
    if (CommonUtil.isNull(arrayJson)) {
      return;
    } else {
      JsonNode jPathValue = queryJson(jNode, sPath);
      if (CommonUtil.isNotNullJson(jPathValue) && !jPathValue.isArray()) {
        logger.warn("[WARN] result of queryJson('{}', '{}') is not Array! ", jNode, sPath);
        return;
      }
      if (CommonUtil.isNullJson(jPathValue)) {
        jPathValue = getJson("[]");
        String[] aPath = CommonUtil.getLastPathArray(sPath);
        JsonUtil.setJsonValue(jNode, aPath[0], aPath[1], jPathValue);
      }
      ArrayNode aPathValue = (ArrayNode) jPathValue;

      if (arrayJson.isArray()) {
        Iterator<JsonNode> it = arrayJson.elements();
        while (it.hasNext()) {
          JsonNode node = it.next();
          aPathValue.add(node);
        }
      } else if (arrayJson.isObject()) {
        aPathValue.add(arrayJson);
      }
    }
  }

  public static void setJsonArrayValue(JsonNode jNode, String sPath, String sArrayValue) throws Exception {
    JsonNode jPathValue = queryJson(jNode, sPath);
    if (CommonUtil.isNotNullJson(jPathValue) && !jPathValue.isArray()) {
      logger.warn("[WARN] result of queryJson('{}', '{}') is not Array! ", jNode, sPath);
      return;
    }
    if (CommonUtil.isNullJson(jPathValue)) {
      jPathValue = getJson("[]");
      String[] aPath = CommonUtil.getLastPathArray(sPath);
      JsonUtil.setJsonValue(jNode, aPath[0], aPath[1], jPathValue);
    }
    ArrayNode aPathValue = (ArrayNode) jPathValue;
    String[] aValue = StringUtils.split(sArrayValue, ",");
    for (String node : aValue) {
      aPathValue.add(node);
    }
  }

  /**
   * 将json对象转换成xml字符串
   *
   * @param jNode
   * @return {String}
   * @throws JsonProcessingException
   */
  public static String getXmlFromJson(JsonNode jNode) throws JsonProcessingException {
    String sXML = "";
    if (CommonUtil.ifIsNotEmpty(jNode)) {
      sXML = xmlMapper.writeValueAsString(jNode);
      // 截取掉<ObjectNode xmlns=""> XX </ObjectNode>部分
      if (sXML.indexOf("ObjectNode xmlns") >= 0) {
        sXML = sXML.substring(21, sXML.length() - 13);
      }
      /*
       * int iIndex = sXML.indexOf("ObjectNode xmlns"); if (iIndex > 12 &&
       * iIndex < 21) { sXML = sXML.substring(21, sXML.length() - 13); } else {
       * sXML = sXML.substring(12, sXML.length() - 13); }
       */
    }
    return sXML;
  }

  /**
   * json信息拷贝
   * <ul>
   * <li>过期，优化调用遍历，建议采用copyJsonFields(JsonNode jFromJson, String sFields)</li>
   * </ul>
   *
   * @param jFromJson
   *          源Json
   * @param jToJson
   *          目标Json
   * @param sFields
   *          拷贝信息项，逗号(,)分割
   * @throws Exception
   */
  @Deprecated
  public static void copyJsonFields(JsonNode jFromJson, JsonNode jToJson, String sFields) throws Exception {
    String[] aField = StringUtils.split(sFields, ",");
    for (String field : aField) {
      JsonNode jNode = JsonUtil.queryJson(jFromJson, field);
      JsonUtil.setJsonValue(jToJson, "", field, jNode);
    }
  }

  /**
   * json信息拷贝
   * <ul>
   * <li>过期，优化调用遍历，建议采用copyJsonFields(String sFromJson, String sFields)</li>
   * </ul>
   *
   * @throws Exception
   */
  @Deprecated
  public static void copyJsonFields(String sFromJson, JsonNode jToJson, String sFields) throws Exception {
    String[] aField = StringUtils.split(sFields, ",");
    for (String field : aField) {
      JsonNode jNode = JsonUtil.queryJson(sFromJson, field);
      JsonUtil.setJsonValue(jToJson, "", field, jNode);
    }
  }

  /**
   * Json信息拷贝
   * <ul>
   * <li>针对同源(同key值)json信息拷贝</li>
   * </ul>
   *
   * @param jFromJson
   *          源Json
   * @param sFields
   *          拷贝信息项，逗号(,)分割
   * @return {JsonNode}
   * @throws Exception
   */
  public static JsonNode copyJsonFields(JsonNode jFromJson, String sFields) throws Exception {
    return copyJsonFields(jFromJson.toString(), sFields);
  }

  /**
   * Json信息拷贝
   * <ul>
   * <li>针对同源(同key值)json信息拷贝</li>
   * </ul>
   *
   * @param sFromJson
   *          源Json
   * @param sFields
   *          拷贝信息项，逗号(,)分割
   * @return {JsonNode}
   * @throws Exception
   */
  public static JsonNode copyJsonFields(String sFromJson, String sFields) throws Exception {
    String[] aField = StringUtils.split(sFields, ",");
    JsonNode jToJson = JsonUtil.getJson("{}");
    for (String field : aField) {
      JsonNode jNode = JsonUtil.queryJson(sFromJson, field);
      JsonUtil.setJsonValue(jToJson, "", field, jNode);
    }
    return jToJson;
  }

  /**
   * json信息拷贝
   *
   * @param jFromJson
   *          源Json
   * @param jToJson
   *          目标Json
   * @param sFromFields
   *          拷贝信息项，逗号(,)分割
   * @param sToFields
   *          复制信息项，逗号(,)分割
   * @throws Exception
   */
  public static void copyJsonFields(JsonNode jFromJson, JsonNode jToJson, String sFromFields, String sToFields)
          throws Exception {
    String[] aFromFields = StringUtils.split(sFromFields, ",");
    String[] aToFields = StringUtils.split(sToFields, ",");
    int iLength = aFromFields.length;
    for (int i = 0; i < iLength; i++) {
      String sFromField = aFromFields[i];
      String sToField = aToFields[i];
      JsonNode jNode = JsonUtil.queryJson(jFromJson, sFromField);
      JsonUtil.setJsonValue(jToJson, "", sToField, jNode);
    }
  }

  /**
   * json信息拷贝
   *
   * @param sFromJson
   *          源Json
   * @param jToJson
   *          目标Json
   * @param sFromFields
   *          拷贝信息项，逗号(,)分割
   * @param sToFields
   *          复制信息项，逗号(,)分割
   * @throws Exception
   */
  public static void copyJsonFields(String sFromJson, JsonNode jToJson, String sFromFields, String sToFields)
          throws Exception {
    String[] aFromFields = StringUtils.split(sFromFields, ",");
    String[] aToFields = StringUtils.split(sToFields, ",");
    int iLength = aFromFields.length;
    for (int i = 0; i < iLength; i++) {
      String sFromField = aFromFields[i];
      String sToField = aToFields[i];
      JsonNode jNode = JsonUtil.queryJson(sFromJson, sFromField);
      JsonUtil.setJsonValue(jToJson, "", sToField, jNode);
    }
  }

  public static Object toObject(JsonNode jInput) {
    if (CommonUtil.ifIsNull(jInput))
      return null;
    if (CommonUtil.ifIsEmpty(jInput))
      return "";
    if (jInput.isBoolean()) {
      return jInput.asBoolean();
    } else if (jInput.isDouble()) {
      return jInput.asDouble();
    } else if (jInput.isFloat()) {
      return jInput.asDouble();
    } else if (jInput.isInt()) {
      return jInput.asInt();
    } else if (jInput.isLong()) {
      return jInput.asLong();
    } else if (jInput.isTextual()) {
      return jInput.asText();
    } else {
      return jInput.toString();
    }
  }

  public static <T> void setValueOfJson(JsonNode jNode, String sPath, String sValue) throws Exception {
    setValueOfJson(jNode, sPath, "", sValue);
  }

  public static <T> void setValueOfJson(JsonNode jNode, String sFieldPath, String sFieldName, String sValue)
          throws Exception {
    JsonNode jValue = JsonUtil.getJson(sValue);
    setValue(jNode, sFieldPath, sFieldName, jValue);
  }

  public static <T> void setValue(JsonNode jNode, String sPath, T TValue) throws Exception {
    setValue(jNode, sPath, "", TValue);
  }

  public static <T> void setValue(JsonNode jNode, String sFieldPath, String sFieldName, T TValue) throws Exception {
    String sPath = "";
    // 方法参数校验
    if (CommonUtil.ifIsNotEmpty(sFieldPath) && CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldPath + "." + sFieldName;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldPath;
    } else if (CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldName;
    } else {
      throw new Exception("[JsonUtil.setValue] 方法参数异常: 【设置的字段名为空】");
    }
    String[] aPath = CommonUtil.getLastPathArray(sPath);
    JsonNode node = null;
    if (CommonUtil.ifIsEmpty(aPath[0])) {
      node = jNode;
    } else {
      node = queryJsonForce(jNode, aPath[0]);
    }
    if (CommonUtil.ifIsNull(TValue)) {
      ((ObjectNode) node).set(aPath[1], (JsonNode) TValue);
    } else if (TValue instanceof BigDecimal) {
      ((ObjectNode) node).put(aPath[1], (BigDecimal) TValue);
    } else if (TValue instanceof Boolean) {
      ((ObjectNode) node).put(aPath[1], (Boolean) TValue);
    } else if (TValue instanceof byte[]) {
      ((ObjectNode) node).put(aPath[1], (byte[]) TValue);
    } else if (TValue instanceof Double) {
      ((ObjectNode) node).put(aPath[1], (Double) TValue);
    } else if (TValue instanceof Float) {
      ((ObjectNode) node).put(aPath[1], (Float) TValue);
    } else if (TValue instanceof Integer) {
      ((ObjectNode) node).put(aPath[1], (Integer) TValue);
    } else if (TValue instanceof Long) {
      ((ObjectNode) node).put(aPath[1], (Long) TValue);
    } else if (TValue instanceof Short) {
      ((ObjectNode) node).put(aPath[1], (Short) TValue);
    } else if (TValue instanceof Byte) {
      ((ObjectNode) node).put(aPath[1], (Byte) TValue);
    } else if (TValue instanceof String) {
      ((ObjectNode) node).put(aPath[1], (String) TValue);
    } else if (TValue instanceof JsonNode) {
      ((ObjectNode) node).set(aPath[1], (JsonNode) TValue);
    } else if (TValue.getClass().isArray()) {
      JsonNode jaValue = JsonUtil.getJson((String) TValue);
      ((ObjectNode) node).set(aPath[1], jaValue);
      throw new Exception("[JsonUtil.setValue] 方法执行异常: 【设置数组内容请使用 addValue】");
    } else {
      ((ObjectNode) node).put(aPath[1], TValue.toString());
    }
  }

  // public static <T> void addValue(JsonNode jNode, T... TValue) throws
  // Exception {
  // addValue(jNode, "", TValue);
  // }

  public static <T> void addValue(JsonNode jNode, String sPath, T... TValue) throws Exception {
    addValue(jNode, sPath, "", TValue);
  }

  /**
   *
   * @param jNode
   * @param sFieldPath
   * @param sFieldName
   * @param TValue
   *          增加内容
   *          <p>
   *          传入是数组时必须是对象类型，基本类型会被转当作单个基本类型数组，再嵌套一层数组
   *          </p>
   * @throws Exception
   */
  private static <T> void addValue(JsonNode jNode, String sFieldPath, String sFieldName, T... TValue) throws Exception {

    String sPath = "";
    // 方法参数校验
    if (CommonUtil.ifIsNotEmpty(sFieldPath) && CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldPath + "." + sFieldName;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldPath;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldName;
    } else {
      // queryJsonArrayForce(jNode, "") = jNode
      // throw new Exception("[JsonUtil.setValue] 方法参数异常: 【设置的字段名为空】");
    }
    String[] aPath = CommonUtil.getLastPathArray(sPath);
    JsonNode jArrayNode = null;
    if (CommonUtil.ifIsNotEmpty(aPath[0])) {
      JsonNode node = queryJsonForce(jNode, aPath[0]);
      jArrayNode = queryJsonArrayForce(node, aPath[1]);
    } else {
      jArrayNode = queryJsonArrayForce(jNode, aPath[1]);
    }
    if (!jArrayNode.isArray()) {
      throw new Exception("[JsonUtil.setValue] 方法执行异常: 【不能将现有非数组格式节点转成数组格式】");
    }
    if (CommonUtil.ifIsNull(TValue))
      return;
    ArrayNode aNode = (ArrayNode) jArrayNode;

    for (T value : TValue) {
      if (value instanceof BigDecimal) {
        aNode.add((BigDecimal) value);
      } else if (value instanceof Boolean) {
        aNode.add((Boolean) value);
      } else if (value instanceof byte[]) {
        aNode.add((byte[]) value);
      } else if (value instanceof Double) {
        aNode.add((Double) value);
      } else if (value instanceof Float) {
        aNode.add((Float) value);
      } else if (value instanceof Integer) {
        aNode.add((Integer) value);
      } else if (value instanceof Long) {
        aNode.add((Long) value);
      } else if (value instanceof Short) {
        aNode.add((Short) value);
      } else if (value instanceof String) {
        aNode.add((String) value);
      } else if (value instanceof JsonNode) {
        aNode.add((JsonNode) value);
      } else if (value.getClass().isArray()) {
        aNode.add((JsonNode) value);
      } else {
        aNode.add(value.toString());
      }
    }
  }

  /**
   * 返回json的key列表字符串(加上前缀字符)，逗号分隔
   *
   * @param node
   * @param prefix
   *          前缀字符
   * @return
   */
  public static String getJsonKeyList(JsonNode node, String prefix) throws Exception {
    return getJsonKeyList(node, "", prefix);
  }

  /**
   * 返回json对象对应路径的key列表字符串(加上前缀字符)，逗号分隔
   *
   * @param node
   * @param path
   *          key路径
   * @param prefix
   *          前缀字符
   * @return
   * @throws Exception
   */
  public static String getJsonKeyList(JsonNode node, String path, String prefix) throws Exception {
    String sKeyList = "";
    if (CommonUtil.ifIsNotEmpty(node)) {
      JsonNode temp = JsonUtil.queryJson(node, path);
      if (CommonUtil.ifIsNotEmpty(temp)) {
        Iterator<String> itFieldNames = temp.fieldNames();
        if (CommonUtil.ifIsEmpty(prefix)) {
          prefix = "";
        }
        while (itFieldNames.hasNext()) {
          if ("".equals(sKeyList)) {
            sKeyList = prefix + itFieldNames.next();
          } else {
            sKeyList += "," + prefix + itFieldNames.next();
          }
        }
      }
    }
    return sKeyList;
  }

  // 遍历json节点，根据里面字段的属性值来查找节点
  public static JsonNode queryNodeByFieldValue(JsonNode node, String field, String value) throws Exception {
    JsonNode nResult = null;
    Iterator<Entry<String, JsonNode>> it = node.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      JsonNode nField = entry.getValue();
      if (nField.get(field) != null && value.equals(nField.get(field).asText())) {
        nResult = nField;
      }
    }
    return nResult;
  }

  //根据传入路径，获取节点
  public static JsonNode getJsonNodeByPath(JsonNode aNode, String sPath) throws Exception {
    String[] aPath = StringUtils.split(sPath, ".");
    JsonNode node = aNode;
    if (node != null) {
      for (String s : aPath) {
        String[] a = StringUtils.split(s, "[]");
        int i = 0;
        for (String s1 : a) {
          if (i == 0) {
            node = node.get(s1);
          } else {
            node = ((ArrayNode) node).get(Integer.parseInt(s1));
          }
          i++;
        }
      }
    }
    return node;
  }

  public static ArrayNode getArrayNodeByPath(JsonNode aNode, String sPath) throws Exception {
    return (ArrayNode) getJsonNodeByPath(aNode, sPath);
  }

  public static ObjectNode getObjectNodeByPath(JsonNode aNode, String sPath) throws Exception {
    return (ObjectNode) getJsonNodeByPath(aNode, sPath);
  }

  public static String getStringByPath(JsonNode aNode, String sPath) throws Exception {
    JsonNode node = getJsonNodeByPath(aNode, sPath);
    if (node != null) {
      return node.asText();
    } else {
      return "";
    }
  }

  public static int getIntByPath(JsonNode aNode, String sPath) throws Exception {
    JsonNode node = getJsonNodeByPath(aNode, sPath);
    if (node != null) {
      return node.asInt();
    } else {
      return 0;
    }
  }

  public static double getDoubleByPath(JsonNode aNode, String sPath) throws Exception {
    JsonNode node = getJsonNodeByPath(aNode, sPath);
    if (node != null) {
      return node.asDouble();
    } else {
      return 0;
    }
  }

  public static int getSizeByPath(JsonNode aNode, String sPath) throws Exception {
    JsonNode node = getJsonNodeByPath(aNode, sPath);
    if (node != null) {
      return node.size();
    } else {
      return 0;
    }
  }
  /**
   * @Description 判断json内是否存在指定路径
   * @Author yangfei
   * @date 2019/7/8 15:50
   * @param jNode 需要判断的json
   * @param sPath json路径
   * @throws
   * @return boolean
   */
  public static boolean has(JsonNode jNode, String sPath) throws Exception {
    //如果json为空,则无需做判断
    if(jNode == null){
      return false;
    }
    //如果路径为空,则必然存在
    if (CommonUtil.ifIsEmpty(sPath)){
      return true;
    }
    //拷贝一份json
    JsonNode fNode = JsonUtil.getJson(jNode.toString());
    //获取一整条路径
    String[] sNodes = sPath.split("\\.");
    int len = sNodes.length;
    for (int i = 0; i < len; i++) {
      //判断当前路径是否存在,不存在则返回false
      if(CommonUtil.ifIsEmpty(sNodes[i])){
        logger.error("路径异常[sPath="+sPath+"]");
        throw new Exception("路径异常");
      }
      if(!fNode.has(sNodes[i])){
        return false;
      }
      //路径存在的前提下,获取当前路径的值,作为下一个子json
      fNode = fNode.get(sNodes[i]);
    }
    return true;
  }
}
