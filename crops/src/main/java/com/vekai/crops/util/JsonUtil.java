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
    return mapper.readTree(file); // mapper???ObjectMapper??????
  }

  /**
   * ??????XML?????????????????????????????????json??????
   *
   * @param xml
   *          xml???????????????
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
   * json???????????????????????????????????????????????????????????????
   * <br>1????????????null??????????????????null???
   * <br>2????????????null?????????????????????
   * @param nSrc
   * @param nTag
   * @return
   */
  public static boolean isNotSame(JsonNode nSrc, JsonNode nTag) {
    return !isSame(nSrc, nTag);
  }

  /**
   * json?????????????????????????????????????????????????????????
   * <br>1?????????null???
   * <br>2????????????null??????????????????
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
   * ??????jsonnode???????????????????????????????????????String??????????????????
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
   * ???JsonNodeArray????????????????????????????????????Json?????????
   * @param jsonNodeArray ????????????JsonNode??????
   * @param toNodeName    ?????????????????????
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
   * ???iIndex??????????????????????????????json??????
   * @param aSrc    ?????????json
   * @param iIndex  ?????????????????????????????????0??????
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

  // ??????Json?????????????????????????????????????????????????????????
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

  // ?????????????????????????????????????????????
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
        // ??????????????????,
        String[] aSearch = StringUtils.split(i, ",");
        // ???????????????
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
          // ??????????????????
          for (JsonNode node1 : aNode1) {
            boolean bMatch = true;
            // System.out.println(node1.get(k));
            // ????????????
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
   * ????????????????????????????????????????????? a.b[0].c.d[1].f??????????????????????????????json??????????????????????????????json????????????????????????????????????????????????
   *
   * @param mainNode
   *          ???json??????
   * @param sPath
   *          ????????????
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
   * ????????????????????????????????????????????? a.b[0].c.d[1].f??????????????????????????????json??????????????????????????????json????????????????????????????????????????????????
   *
   * @param mainNode
   *          ???json??????
   * @param sPath
   *          ????????????
   * @param bForce
   *          ?????????????????????????????????
   * @param bArray
   *          ????????????????????????
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

  // ??????????????????
  /*
   * public static JsonNode getTableRecord(String sTable, String sID) throws
   * Exception { DBObject result = DBUtil.getCollection(sTable).findOne(sID); if
   * (result != null) { return getJson(result); } else { return null; } }
   */

  /*
   *
   * //??????????????? public static WriteResult saveTableRecord(String sTable, String
   * sValue) throws Exception { return
   * DBUtil.getCollection(sTable).save(getDBObject(sValue)); }
   *
   * //??????????????? public static WriteResult saveTableRecord(String sTable, JsonNode
   * sValue) throws Exception { return
   * DBUtil.getCollection(sTable).save(getDBObject(sValue)); }
   */
  // ??????????????????
  /*
   * public static Map<String, JsonNode> getCollectionMap(String sTable) throws
   * Exception { Map<String, JsonNode> map = new HashMap<String, JsonNode>();
   * DBCursor result = DBUtil.getCollection(sTable).find(); for (DBObject
   * dbObject : result) { map.put(dbObject.get("_id").toString(),
   * getJson(dbObject)); } return map; }
   */

  // ??????Map???Json??????
  public static JsonNode getMapJson(Map<String, JsonNode> map, String sID) throws Exception {
    return map.get(sID);
  }

  // ????????????json????????????????????????????????????
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

  //????????????????????????
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

  //????????????????????????
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
   * ????????????????????????key???
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
   * ????????????????????????keyObject
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
   * ??????????????????????????????
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

  // ????????????key???????????????
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

  // ??????????????????key????????????????????????
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
   * ???jsonnode???sNodePath?????????????????????????????????????????????????????????????????????
   *
   * @param jsonnode
   *          ????????????json??????
   * @param sNodePath
   *          ????????????
   * @param sFieldPath
   *          ????????????
   * @param bAsce
   *          ????????????
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
   * ??????????????????topcount?????????
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
   * ??????????????????lastcount?????????
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
   * ??????jsonNode????????????
   *
   * @param jsonnode
   * @return
   */
  public static String getJsonStringName(JsonNode jsonnode) {
    return getJsonStringName(jsonnode, "");
  }

  /**
   * ??????jsonNode????????????
   *
   * @param jsonnode
   * @param sType
   *          ?????????????????????
   *          <ul>
   *          <li>1: ???''???????????????????????????????????????</li>
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
   * ???jsonnode??????????????????????????????
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
   * ???json???????????????xml?????????
   *
   * @param jNode
   * @return {String}
   * @throws JsonProcessingException
   */
  public static String getXmlFromJson(JsonNode jNode) throws JsonProcessingException {
    String sXML = "";
    if (CommonUtil.ifIsNotEmpty(jNode)) {
      sXML = xmlMapper.writeValueAsString(jNode);
      // ?????????<ObjectNode xmlns=""> XX </ObjectNode>??????
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
   * json????????????
   * <ul>
   * <li>??????????????????????????????????????????copyJsonFields(JsonNode jFromJson, String sFields)</li>
   * </ul>
   *
   * @param jFromJson
   *          ???Json
   * @param jToJson
   *          ??????Json
   * @param sFields
   *          ????????????????????????(,)??????
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
   * json????????????
   * <ul>
   * <li>??????????????????????????????????????????copyJsonFields(String sFromJson, String sFields)</li>
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
   * Json????????????
   * <ul>
   * <li>????????????(???key???)json????????????</li>
   * </ul>
   *
   * @param jFromJson
   *          ???Json
   * @param sFields
   *          ????????????????????????(,)??????
   * @return {JsonNode}
   * @throws Exception
   */
  public static JsonNode copyJsonFields(JsonNode jFromJson, String sFields) throws Exception {
    return copyJsonFields(jFromJson.toString(), sFields);
  }

  /**
   * Json????????????
   * <ul>
   * <li>????????????(???key???)json????????????</li>
   * </ul>
   *
   * @param sFromJson
   *          ???Json
   * @param sFields
   *          ????????????????????????(,)??????
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
   * json????????????
   *
   * @param jFromJson
   *          ???Json
   * @param jToJson
   *          ??????Json
   * @param sFromFields
   *          ????????????????????????(,)??????
   * @param sToFields
   *          ????????????????????????(,)??????
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
   * json????????????
   *
   * @param sFromJson
   *          ???Json
   * @param jToJson
   *          ??????Json
   * @param sFromFields
   *          ????????????????????????(,)??????
   * @param sToFields
   *          ????????????????????????(,)??????
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
    // ??????????????????
    if (CommonUtil.ifIsNotEmpty(sFieldPath) && CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldPath + "." + sFieldName;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldPath;
    } else if (CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldName;
    } else {
      throw new Exception("[JsonUtil.setValue] ??????????????????: ??????????????????????????????");
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
      throw new Exception("[JsonUtil.setValue] ??????????????????: ?????????????????????????????? addValue???");
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
   *          ????????????
   *          <p>
   *          ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
   *          </p>
   * @throws Exception
   */
  private static <T> void addValue(JsonNode jNode, String sFieldPath, String sFieldName, T... TValue) throws Exception {

    String sPath = "";
    // ??????????????????
    if (CommonUtil.ifIsNotEmpty(sFieldPath) && CommonUtil.ifIsNotEmpty(sFieldName)) {
      sPath = sFieldPath + "." + sFieldName;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldPath;
    } else if (CommonUtil.ifIsNotEmpty(sFieldPath)) {
      sPath = sFieldName;
    } else {
      // queryJsonArrayForce(jNode, "") = jNode
      // throw new Exception("[JsonUtil.setValue] ??????????????????: ??????????????????????????????");
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
      throw new Exception("[JsonUtil.setValue] ??????????????????: ????????????????????????????????????????????????????????????");
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
   * ??????json???key???????????????(??????????????????)???????????????
   *
   * @param node
   * @param prefix
   *          ????????????
   * @return
   */
  public static String getJsonKeyList(JsonNode node, String prefix) throws Exception {
    return getJsonKeyList(node, "", prefix);
  }

  /**
   * ??????json?????????????????????key???????????????(??????????????????)???????????????
   *
   * @param node
   * @param path
   *          key??????
   * @param prefix
   *          ????????????
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

  // ??????json??????????????????????????????????????????????????????
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

  //?????????????????????????????????
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
   * @Description ??????json???????????????????????????
   * @Author yangfei
   * @date 2019/7/8 15:50
   * @param jNode ???????????????json
   * @param sPath json??????
   * @throws
   * @return boolean
   */
  public static boolean has(JsonNode jNode, String sPath) throws Exception {
    //??????json??????,??????????????????
    if(jNode == null){
      return false;
    }
    //??????????????????,???????????????
    if (CommonUtil.ifIsEmpty(sPath)){
      return true;
    }
    //????????????json
    JsonNode fNode = JsonUtil.getJson(jNode.toString());
    //?????????????????????
    String[] sNodes = sPath.split("\\.");
    int len = sNodes.length;
    for (int i = 0; i < len; i++) {
      //??????????????????????????????,??????????????????false
      if(CommonUtil.ifIsEmpty(sNodes[i])){
        logger.error("????????????[sPath="+sPath+"]");
        throw new Exception("????????????");
      }
      if(!fNode.has(sNodes[i])){
        return false;
      }
      //????????????????????????,????????????????????????,??????????????????json
      fNode = fNode.get(sNodes[i]);
    }
    return true;
  }
}
