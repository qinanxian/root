package com.vekai.crops.util.mongo3;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.model.PushOptions;
import com.mongodb.client.model.Updates;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import static com.mongodb.client.model.Updates.*;

public class Update {

  public static Logger logger = LoggerFactory.getLogger(Update.class);
  public static final int SLICE = 10000 //
      , SORT = 0 //
      , POSITION = -1 //
  ;
  public static final String ARRAYSETFLAG = "@" // 数组是set(不重复数组元素)类型标识
      , ARRAYFIELD = "field" //
      , DIRECTARRAYVALUE = "r" //
      , DIRECTARRAYPOSITION = "pos" //
      , DIRECTARRAYSORT = "sort" //
      , DIRECTARRAYSLICE = "slice" //
      , DELETETYPE = "type" //
      , DELETEFIRST = "first" // delete first element
      , DELETELAST = "last" // delete last element
      , DELETEDIRECT = "direct" // delete direct array
      , DELETEEMBED = "embed" // delete embed array
      , DELETEFILTER = "filter" // delete by filter
  ;

  private Bson updates;

  public static Update Builder() {
    return new Update();
  }

  public Bson builder() {
    return updates;
  }

  private void updateCombine(Bson... bsUpdates) {
    Bson[] bson = bsUpdates;
    List<Bson> list = Arrays.asList(bson);
    updateCombine(new ArrayList<Bson>(list));
  }

  private void updateCombine(List<Bson> lUpdates) {
    if (CommonUtil.ifIsNotEmpty(lUpdates)) {
      if (CommonUtil.ifIsNotNull(updates)) {
        lUpdates.add(updates);
      }
      updates = combine(lUpdates);
    }
  }

  /**
   * combine update set message
   * 
   * @param sUpdate
   *          set 更新信息
   * @return Update
   * @throws Exception
   *           jsonutil exception
   */
  public Update updateSet(String sUpdate) throws Exception {
    return updateSet(sUpdate, "");
  }

  /**
   * combine update set message with prefix field
   * 
   * @param sUpdate
   *          set 更新信息
   * @param sPrefix
   *          更新信息追加的前置字段信息
   * @return updateSet
   * @throws Exception
   *           jsonutil exception
   */
  public Update updateSet(String sUpdate, String sPrefix) throws Exception {
    String sBsonUpdate = "";
    // 针对Update Bson转换过来的已经带有$set:的直接使用
    if (sUpdate.indexOf("$set") >= 0) {
      sBsonUpdate = sUpdate;
    } else {
      sBsonUpdate = "{$set: " + JsonUtil.getJsonValueWithPath(JsonUtil.getJson(sUpdate), sPrefix) + "}";
    }
    Bson bsSet = BsonDocument.parse(sBsonUpdate);
    updateCombine(bsSet);
    return this;
  }

  public <T> Update updateSetValue(String sFieldName, T TValue) {
    Bson bsSet = set(sFieldName, TValue);
    updateCombine(bsSet);
    return this;
  }

  /**
   * combine increase message
   * 
   * @param sUpdate
   *          increase内容信息
   * @return Update
   * @throws Exception
   *           jsonutil exception
   */
  public Update updateInc(String sUpdate) throws Exception {
    JsonNode jIncrease = JsonUtil.getJson(sUpdate);
    jIncrease = JsonUtil.getJson(JsonUtil.getJsonValueWithPath(jIncrease));
    List<Bson> list = new ArrayList<Bson>();
    Iterator<Entry<String, JsonNode>> it = jIncrease.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      String sField = entry.getKey();
      JsonNode jValue = entry.getValue();
      Number number = null;
      if (jValue.isNumber()) {
        number = jValue.numberValue();
      }
      list.add(Updates.inc(sField, number));
    }
    Bson bsSet = Updates.combine(list);
    updateCombine(bsSet);
    return this;
  }

  /**
   * combine unset message
   * 
   * @param sFieldName
   *          unset字段项
   * @return Update
   */
  public Update updateUnset(String sFieldName) {
    String[] aFieldNames = StringUtils.split(sFieldName, ",");
    List<Bson> list = new ArrayList<Bson>();
    for (String field : aFieldNames) {
      list.add(Updates.unset(field));
    }
    Bson bsSet = Updates.combine(list);
    updateCombine(bsSet);
    return this;
  }

  /**
   * set addtional elements to direct set array
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param TValue
   *          添加数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setSetArrayDirectAdd(JsonNode jAddDatas, String sField, T... TValue) throws Exception {
    JsonNode jData = JsonUtil.getJson("{}");
    JsonUtil.setValue(jData, ARRAYFIELD, ARRAYSETFLAG + sField);
    JsonUtil.addValue(jData, DIRECTARRAYVALUE, TValue);
    JsonUtil.addValue(jAddDatas, "", jData);
  }

  /**
   * set addtional elements to direct no-set array
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param TValue
   *          添加的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setNosetArrayDirectAdd(JsonNode jAddDatas, String sField, T... TValue) throws Exception {
    setNosetArrayDirectAdd(jAddDatas, sField, 0, TValue);
  }

  /**
   * set addtional elements to direct no-set array
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param iPosition
   *          增加字段位子
   * @param TValue
   *          添加的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setNosetArrayDirectAdd(JsonNode jAddDatas, String sField, int iPosition, T... TValue) throws Exception {
    setNosetArrayDirectAdd(jAddDatas, sField, iPosition, SORT, TValue);
  }

  /**
   * set addtional elements to direct no-set array
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param iPosition
   *          添加字段位置
   * @param iSort
   *          字段排序
   * @param TValue
   *          添加的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setNosetArrayDirectAdd(JsonNode jAddDatas, String sField, int iPosition, int iSort, T... TValue) throws Exception {
    setNosetArrayDirectAdd(jAddDatas, sField, iPosition, iSort, SLICE, TValue);
  }

  /**
   * set addtional elements to direct no-set array
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param iPosition
   *          添加字段位置
   * @param iSort
   *          字段排序
   * @param iSlice
   *          切片保留数组元素个数
   * @param TValue
   *          添加的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setNosetArrayDirectAdd(JsonNode jAddDatas, String sField, int iPosition, int iSort, int iSlice, T... TValue) throws Exception {
    JsonNode jData = JsonUtil.getJson("{}");
    JsonUtil.setValue(jData, ARRAYFIELD, sField);
    JsonUtil.setValue(jData, DIRECTARRAYPOSITION, iPosition);
    JsonUtil.setValue(jData, DIRECTARRAYSORT, iSort);
    JsonUtil.setValue(jData, DIRECTARRAYSLICE, iSlice);
    JsonUtil.addValue(jData, DIRECTARRAYVALUE, TValue);
    JsonUtil.addValue(jAddDatas, "", jData);
    // return "{'" + sField + "':{'" + DIRECTARRAYVALUE + "':[" + TValue + "]},
    // '" + DIRECTARRAYPOSITION + "':" + iPosition
    // + ", '" + DIRECTARRAYSORT + "':" + iSort + ", '" + DIRECTARRAYSLICE +
    // "':" + iSlice + "}";
  }

  /**
   * set addtional elements to embed set array
   * 
   * @param sField
   *          内嵌数组元素字段
   * @param TValue
   *          添加的数组元素内容
   * @return String
   * @throws Exception
   */
  public static void setSetArrayEmbedAdd(JsonNode jAddDatas, String sField, JsonNode... TValue) throws Exception {
    JsonNode jData = JsonUtil.getJson("{}");
    JsonUtil.setValue(jData, ARRAYFIELD, ARRAYSETFLAG + sField);
    JsonUtil.addValue(jData, DIRECTARRAYVALUE, TValue);
    JsonUtil.addValue(jAddDatas, "", jData);
  }

  /**
   * set addtional elements to embed no-set array
   * 
   * @param sField
   *          内嵌数组元素字段
   * @param TValue
   *          添加的数组元素内容
   * @return String
   * @throws Exception
   */
  public static void setNosetArrayEmbedAdd(JsonNode jAddDatas, String sField, JsonNode... TValue) throws Exception {
    setNosetArrayEmbedAdd(jAddDatas, sField, "", "", TValue);
  }

  /**
   * set addtional elements to embed no-set array
   * 
   * @param sField
   *          内嵌数组元素字段
   * @param sPosition
   *          添加元素位置
   * @param TValue
   *          添加的数组元素内容
   * @return String
   * @throws Exception
   */
  public static void setNosetArrayEmbedAdd(JsonNode jAddDatas, String sField, String sPosition, JsonNode... TValue) throws Exception {
    setNosetArrayEmbedAdd(jAddDatas, sField, sPosition, "", TValue);
  }

  /**
   * set addtional elements to embed no-set array
   * 
   * @param sField
   *          直接数组字段
   * @param sPosition
   *          添加字段位置
   * @param sSort
   *          字段排序
   * @param TValue
   *          添加的数组元素
   * @return String
   * @throws Exception
   */
  public static void setNosetArrayEmbedAdd(JsonNode jAddDatas, String sField, String sPosition, String sSort, JsonNode... TValue) throws Exception {
    setNosetArrayEmbedAdd(jAddDatas, sField, sPosition, sSort, "", TValue);
  }

  /**
   * set addtional elements to embed no-set array
   * 
   * @param sField
   *          直接数组字段
   * @param sPosition
   *          添加字段位置
   * @param sSort
   *          字段排序
   * @param sSlice
   *          切片保留数组元素个数
   * @param TValue
   *          添加的数组元素
   * @return String
   */
  public static void setNosetArrayEmbedAdd(JsonNode jAddDatas, String sField, String sPosition, String sSort, String sSlice, JsonNode... TValue)
      throws Exception {
    JsonNode jData = JsonUtil.getJson("{}");
    JsonUtil.setValue(jData, ARRAYFIELD, sField);
    if (CommonUtil.ifIsNotEmpty(sPosition))
      JsonUtil.setValue(jData, DIRECTARRAYPOSITION, sPosition);
    if (CommonUtil.ifIsNotEmpty(sSort))
      JsonUtil.setValue(jData, DIRECTARRAYSORT, sSort);
    if (CommonUtil.ifIsNotEmpty(sSlice))
      JsonUtil.setValue(jData, DIRECTARRAYSLICE, sSlice);
    JsonUtil.addValue(jData, DIRECTARRAYVALUE, TValue);
    JsonUtil.addValue(jAddDatas, "", jData);
  }

  /**
   * 根据规则添加元素到直接数组中
   * 
   * @param sArrayUpdate
   *          添加数组元素内容
   *          <p>
   *          NoSet: {'fieldName':{'r':['fieldValue'...], 'pos':N, 'sort':N,
   *          'slice':N}}
   *          </p>
   *          <p>
   *          Set: {'@fieldName':{'r':['fieldValue'...]}}
   *          </p>
   * @return {Update}
   * @throws Exception
   *           if has some exception
   */
  public Update add2DirectArray(String sArrayUpdate) throws Exception {
    return add2Array(sArrayUpdate, false);
  }

  /**
   * 根据规则添加元素到内嵌文档数组中
   * 
   * @param sArrayUpdate
   *          添加数组元素内容
   *          <p>
   *          NoSet: {'fieldName':{'r':[{key:value,...},...], 'pos':N, 'sort':N,
   *          'slice':N}}
   *          </p>
   *          <p>
   *          Set: {'@fieldName':{'r':[{key:value,...},...]}}
   *          </p>
   * @return {Update}
   * @throws Exception
   *           if has some exception
   */
  public Update add2EmbedArray(String sArrayUpdate) throws Exception {
    return add2Array(sArrayUpdate, true);
  }

  private Update add2Array(String sArrayUpdate, boolean bEmbed) throws Exception {
    if (CommonUtil.ifIsEmpty(sArrayUpdate))
      return this;
    JsonNode jArrayUpdates = JsonUtil.getJson(sArrayUpdate);
    Iterator<JsonNode> it = jArrayUpdates.elements();
    while (it.hasNext()) {
      JsonNode jUpdate = it.next();
      JsonNode jUpdateValue = JsonUtil.queryJson(jUpdate, DIRECTARRAYVALUE);
      if (CommonUtil.ifIsEmpty(jUpdateValue))
        continue;
      List<Object> list = new ArrayList<Object>();
      int iLength = ARRAYSETFLAG.length();
      Bson bsUpdate = null;
      Iterator<JsonNode> itValue = jUpdateValue.elements();
      while (itValue.hasNext()) {
        JsonNode value = itValue.next();
        if (bEmbed) {
          if (CommonUtil.ifIsNull(value))
            continue;
          if (JsonUtil.isArrayNode(value)) {
            Iterator<JsonNode> it2 = value.elements();
            while (it2.hasNext()) {
              JsonNode jThisValue = it2.next();
              list.add(BsonDocument.parse(jThisValue.toString()));
            }
          } else {
            String sValue = value.toString();
            list.add(BsonDocument.parse(sValue));
          }
        } else {
          list.add(JsonUtil.toObject(value));
        }
      }
      String sArrayField = JsonUtil.getJsonStringValue(jUpdate, ARRAYFIELD);
      if (sArrayField.startsWith(ARRAYSETFLAG)) {
        sArrayField = sArrayField.substring(iLength);
        bsUpdate = add2SetOfArray(sArrayField, list);
      } else {
        bsUpdate = add2NosetOfArray(sArrayField, list, jUpdate.toString());
      }
      updateCombine(bsUpdate);
    }
    return this;
  }

  private <T> Bson add2NosetOfArray(String sField, List<T> lValues, String sOptions) throws Exception {
    Bson bsSet = null;
    if (CommonUtil.ifIsEmpty(sOptions)) {
      bsSet = pushEach(sField, lValues);
    } else {
      PushOptions options = new PushOptions();
      JsonNode jOptions = JsonUtil.getJson(sOptions);
      if (jOptions.has(DIRECTARRAYPOSITION)) {
        try {
          int iPosition = JsonUtil.getJsonIntValue(jOptions, DIRECTARRAYPOSITION);
          options.position(iPosition);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }
      if (jOptions.has(DIRECTARRAYSORT)) {
        try {
          int iSort = JsonUtil.getJsonIntValue(jOptions, DIRECTARRAYSORT);
          if (SORT != iSort) {
            options.sort(JsonUtil.getJsonIntValue(jOptions, DIRECTARRAYSORT));
          }
        } catch (Exception e) {
          String sSort = JsonUtil.getJsonStringValue(jOptions, DIRECTARRAYSORT);
          options.sortDocument(BsonDocument.parse(sSort));
        }
      }
      if (jOptions.has(DIRECTARRAYSLICE)) {
        try {
          int iSlice = JsonUtil.getJsonIntValue(jOptions, DIRECTARRAYSLICE);
          if (iSlice <= SLICE)
            options.slice(iSlice);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      bsSet = pushEach(sField, lValues, options);
    }
    // updateCombine(bsSet);
    return bsSet;
  }

  private <T> Bson add2SetOfArray(String sField, List<T> lValues) {
    Bson bsSet = addEachToSet(sField, lValues);
    // updateCombine(bsSet);
    return bsSet;
  }

  /**
   * set array first element delete
   * 
   * @param sField
   *          数组字段
   * @return String
   * @throws Exception
   */
  public static void setArrayFirstDelete(JsonNode jDeleteFilters, String sField) throws Exception {
    if (CommonUtil.ifIsEmpty(sField))
      return;
    // String sDeleteFilter = "{'" + sField + "':{'" + DELETETYPE + "':'" +
    // DELETEFIRST + "', '" + DIRECTARRAYVALUE
    // + "':[]}}";
    String sDeleteFilter = "{'" + DELETETYPE + "':'" + DELETEFIRST + "', '" + DIRECTARRAYVALUE + "':[]" + ", '" + ARRAYFIELD + "':'" + sField + "'}";
    JsonUtil.addValue(jDeleteFilters, "", JsonUtil.getJson(sDeleteFilter));
  }

  /**
   * set array last element delete
   * 
   * @param sField
   *          数组字段
   * @return String
   * @throws Exception
   */
  public static void setArrayLastDelete(JsonNode jDeleteFilters, String sField) throws Exception {
    if (CommonUtil.ifIsEmpty(sField))
      return;
    // String sDeleteFilter = "{'" + sField + "':{'" + DELETETYPE + "':'" +
    // DELETELAST + "', '" + DIRECTARRAYVALUE
    // + "':[]}}";
    String sDeleteFilter = "{'" + DELETETYPE + "':'" + DELETELAST + "', '" + DIRECTARRAYVALUE + "':[]" + ", '" + ARRAYFIELD + "':'" + sField + "'}";
    JsonUtil.addValue(jDeleteFilters, "", JsonUtil.getJson(sDeleteFilter));
  }

  /**
   * set direct array elements delete witch elements match given values
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param TValue
   *          预删除的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setArrayDirectDelete(JsonNode jDeleteFilters, String sField, T... TValue) throws Exception {
    if (CommonUtil.ifIsEmpty(sField) || CommonUtil.ifIsEmpty(TValue))
      return;
    // String sDeleteFilter = "{'" + sField + "':{'" + DELETETYPE + "':'" +
    // DELETEDIRECT + "', '" + DIRECTARRAYVALUE
    // + "':[" + TValue + "]}}";
    JsonNode jDeleteFilter = JsonUtil.getJson("{}");
    JsonUtil.setValue(jDeleteFilter, DELETETYPE, DELETEDIRECT);
    JsonUtil.setValue(jDeleteFilter, ARRAYFIELD, sField);
    JsonUtil.addValue(jDeleteFilter, DIRECTARRAYVALUE, TValue);
    JsonUtil.addValue(jDeleteFilters, "", jDeleteFilter);
  }

  /**
   * set embed array elements delete witch elements math given values
   * 
   * @param <T>
   *          T
   * @param sField
   *          直接数组字段
   * @param sValue
   *          预删除的数组元素
   * @return String
   * @throws Exception
   */
  public static <T> void setArrayEmbedDelete(JsonNode jDeleteFilters, String sField, String sValue) throws Exception {
    if (CommonUtil.ifIsEmpty(sField) || CommonUtil.ifIsEmpty(sValue))
      return;
    // String sDeleteFilter = "{'" + sField + "':{'" + DELETETYPE + "':'" +
    // DELETEEMBED + "', '" + DIRECTARRAYVALUE + "':["
    // + sValue + "]}}";
    // String sDeleteFilter = "{'" + DELETETYPE + "':'" + DELETEEMBED + "', '" +
    // DIRECTARRAYVALUE + "':[" + sValue + "]"
    // + ", '" + ARRAYFIELD + "':'" + sField + "'}";
    JsonNode jDeleteFilter = JsonUtil.getJson("{}");
    JsonUtil.setValue(jDeleteFilter, DELETETYPE, DELETEEMBED);
    JsonUtil.setValue(jDeleteFilter, ARRAYFIELD, sField);
    JsonUtil.addValue(jDeleteFilter, DIRECTARRAYVALUE, JsonUtil.getJson(sValue));
    JsonUtil.addValue(jDeleteFilters, "", jDeleteFilter);
  }

  public static <T> void setArrayIDDelete(JsonNode jDeleteFilters, String sField, String sID) throws Exception {
    setArrayFilterDelete(jDeleteFilters, sField, "{'_id':'" + sID + "'}");
  }

  /**
   * set array elements delete with fiter
   * 
   * @param <T>
   *          T
   * @param sField
   *          数组元素
   * @param sFilter
   *          匹配条件
   * @return String
   * @throws Exception
   */
  public static <T> void setArrayFilterDelete(JsonNode jDeleteFilters, String sField, String sFilter) throws Exception {
    if (CommonUtil.ifIsEmpty(sField) || CommonUtil.ifIsEmpty(sFilter))
      return;
    // String sDeleteFilter = "{'" + DELETETYPE + "':'" + DELETEFILTER + "', '"
    // + DIRECTARRAYVALUE + "':" + sFilter + ", '"
    // + ARRAYFIELD + "':'" + sField + "'}";
    JsonNode jDeleteFilter = JsonUtil.getJson("{}");
    JsonUtil.setValue(jDeleteFilter, DELETETYPE, DELETEFILTER);
    JsonUtil.setValue(jDeleteFilter, ARRAYFIELD, sField);
    JsonUtil.setValue(jDeleteFilter, DIRECTARRAYVALUE, JsonUtil.getJson(sFilter));
    JsonUtil.addValue(jDeleteFilters, "", jDeleteFilter);
  }

  /**
   * 根据规则设置数组元素删除
   * 
   * @param sDeleteFilter
   *          删除条件信息
   *          <p>
   *          deleteFirst: {'fieldName':{'type':'first', 'r':[]}}
   *          </p>
   *          <p>
   *          deleteLast: {'fieldName':{'type':'last', 'r':[]}}
   *          </p>
   *          <p>
   *          deleteDirect: {'fieldName':{'type':'direct', 'r':['value1',...]}}
   *          </p>
   *          <p>
   *          deleteEmbed: {'fieldName':{'type':'embed', 'r':[{'key1':'value2',
   *          'key2':'value2'}]}}
   *          </p>
   *          <p>
   *          deleteByFilter: {'fieldName':{'type':'filter', 'r':sFilter}}
   *          </p>
   * @return {Update}
   * @throws Exception
   *           if has some exception
   */
  public Update deleteOfArray(String sDeleteFilter) throws Exception {
    if (CommonUtil.ifIsEmpty(sDeleteFilter))
      return this;
    JsonNode jDeleteFilter = JsonUtil.getJson(sDeleteFilter);
    Iterator<JsonNode> it = jDeleteFilter.elements();
    while (it.hasNext()) {
      JsonNode jFilter = it.next();
      Bson bsDelete = null;
      String sField = JsonUtil.getJsonStringValue(jFilter, ARRAYFIELD);
      String sDeleteType = JsonUtil.getJsonStringValue(jFilter, DELETETYPE);
      if (DELETEFIRST.equals(sDeleteType)) {
        bsDelete = popFirst(sField);
      } else if (DELETELAST.equals(sDeleteType)) {
        bsDelete = popLast(sField);
      } else if (DELETEFILTER.equals(sDeleteType)) {
        JsonNode jDeleteValue = JsonUtil.queryJson(jFilter, DIRECTARRAYVALUE);
        bsDelete = pullByFilter(Filter.Builder().parseFilter("{'" + sField + "':" + jDeleteValue.toString() + "}").builder());
      } else {
        List<Object> list = new ArrayList<Object>();
        JsonNode jDeleteValue = JsonUtil.queryJson(jFilter, DIRECTARRAYVALUE);
        Iterator<JsonNode> itValue = jDeleteValue.elements();
        while (itValue.hasNext()) {
          JsonNode value = itValue.next();
          if (DELETEDIRECT.equals(sDeleteType)) {
            list.add(JsonUtil.toObject(value));
          } else if (DELETEEMBED.equals(sDeleteType)) {
            String sValue = value.toString();
            list.add(BsonDocument.parse(sValue));
          }
        }
        bsDelete = pullAll(sField, list);
      }
      updateCombine(bsDelete);
    }
    return this;
  }

}
