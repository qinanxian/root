package com.vekai.crops.util.mongo3;

import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.util.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.mongodb.client.model.Filters.*;

public class Filter {

  public static Logger logger = LoggerFactory.getLogger(Filter.class);

  private static Bson filters;

  public static Filter Builder() {
    return new Filter();
  }

  public Bson builder() {
    return filters;
  }

  public static String eqIDFilter(String sID) {
    return eqFilter("_id", sID);
  }
  
  public static String eqFilter(String sField, String sValue) {
    if (CommonUtil.ifIsEmpty(sValue))
      return null;
    return "{'" + sField + "':'" + sValue + "'}";
  }

  public static String inIDFilter(String sID) {
    return inFilter("_id", sID);
  }

  public static String inFilter(String sField, String sValue) {
    if (CommonUtil.ifIsEmpty(sValue))
      return null;
    String[] aIds = StringUtils.split(sValue, ",");
    String sFilter = "";
    if (1 == aIds.length) {
      sFilter = eqFilter(sField, sValue);
    } else {
      for (String id : aIds) {
        if (CommonUtil.ifIsNotEmpty(id))
          sFilter += ",'" + id + "'";
      }
      if (CommonUtil.ifIsNotEmpty(sFilter))
        sFilter = sFilter.substring(1);
      sFilter = "{'" + sField + "':{'$in':[" + sFilter + "]}}";
    }
    return sFilter;
  }

  public Bson eqID(String sID) {
    return eq("_id", sID);
  }

  public Bson inID(String sID) {
    String[] aIDs = StringUtils.split(sID, ",");
    return in("_id", aIDs);
  }

  public Filter parseFilter(String sFilter) throws Exception {
    if (CommonUtil.ifIsEmpty(sFilter)) {
      throw new Exception("查询条件为空！");
    }
    filters = BsonDocument.parse(sFilter);
    return this;
  }
  
  public <T> Filter and(T... filter) {
    
    return this;
  }

  /**
   * at least one member of array matches the give filter
   * 
   * 数组值中至少一个元素满足至少一个的指定匹配条件
   * 
   * <p>
   * <B style="color:red">支持单组匹配条件和多组匹配条件</B>
   * </p>
   * 
   * <p>
   * <B>e.g.:</B> <br>
   * &ensp;{<br>
   * &emsp;_id: ObjectId("5234cc89687ea597eabee675"),<br>
   * &emsp;code: "xyz",<br>
   * &emsp;tags: [ "school", "book", "bag", "headphone", "appliance" ],<br>
   * &emsp;results: [ 82, 85, 88 ],<br>
   * &emsp;qty: [<br>
   * &emsp;&ensp;{ size: "S", num: 10, color: "blue" },<br>
   * &emsp;&ensp;{ size: "M", num: 45, color: "blue" },<br>
   * &emsp;&ensp;{ size: "L", num: 100, color: "green" }<br>
   * &emsp;]<br>
   * &ensp;}
   * </p>
   * <ul>
   * <li><B>shell:</B> { results: { $elemMatch: { $gte: 80, $lt: 85 } } }</li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"results"</li>
   * <li>sValues:"{ $gte: 80, $lt: 85 }"</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * <ul>
   * <li><B>shell:</B> { qty: { $elemMatch: { size: "M", num: {$gte: 30} } } }
   * </li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"qty"</li>
   * <li>sValues:{ size: "M", num: {$gte: 30} }</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * <ul>
   * <li><B>shell:</B> {qty: { $all: [{ "$elemMatch" : { size: "M", num: { $gt:
   * 50} } }, { "$elemMatch" : { num : 100, color: "green" } } ] } }</li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"qty"</li>
   * <li>sValues:[{ size: "M", num: { $gt: 50} }, { num : 100, color: "green" }]
   * </li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param sField
   *          数组字段
   * @param aFilters
   *          匹配条件
   * @return {Bson}
   */
  public Bson arrayOfElementsMatchesByFilter(String sField, String[] aFilters) {
    if (CommonUtil.ifIsEmpty(aFilters)) {
      return null;
    } else if (1 == aFilters.length) {
      return arrayOfElementsMatchesByFilter(sField, aFilters[0]);
    } else {
      List<String> list = new ArrayList<String>();
      for (String filter : aFilters) {
        list.add("{'$elemMatch' :" + filter + "}");
      }
      return arrayOfElementLikeMatches(sField, list);
    }
  }

  /**
   * at least one member of array matches the give filter
   * 
   * 数组值中至少一个元素满足所有指定的匹配条件
   * 
   * <p>
   * <B style="color:red">仅支持单组匹配条件</B>
   * </p>
   * 
   * <p>
   * <B>e.g.:</B> <br>
   * &ensp;{<br>
   * &emsp;_id: ObjectId("5234cc89687ea597eabee675"),<br>
   * &emsp;code: "xyz",<br>
   * &emsp;tags: [ "school", "book", "bag", "headphone", "appliance" ],<br>
   * &emsp;results: [ 82, 85, 88 ],<br>
   * &emsp;qty: [<br>
   * &emsp;&ensp;{ size: "S", num: 10, color: "blue" },<br>
   * &emsp;&ensp;{ size: "M", num: 45, color: "blue" },<br>
   * &emsp;&ensp;{ size: "L", num: 100, color: "green" }<br>
   * &emsp;]<br>
   * &ensp;}
   * </p>
   * <ul>
   * <li><B>shell:</B> { results: { $elemMatch: { $gte: 80, $lt: 85 } } }</li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"results"</li>
   * <li>sValues:"{ $gte: 80, $lt: 85 }"</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * <ul>
   * <li><B>shell:</B> { qty: { $elemMatch: { size: "M", num: {$gte: 30} } } }
   * </li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"qty"</li>
   * <li>sValues:{ size: "M", num: {$gte: 30} }</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param sField
   *          数组字段
   * @param sFilter
   *          匹配条件
   * @return {Bson}
   */
  public Bson arrayOfElementsMatchesByFilter(String sField, String sFilter) {
    Bson bsFilter = BsonDocument.parse(sFilter);
    return arrayOfElementsMatchesByFilter(sField, bsFilter);
  }

  /**
   * at least one member of array matches the give filter
   * 
   * 数组值中至少一个元素满足所有指定的匹配条件
   * 
   * <p>
   * <B style="color:red">仅支持单组匹配条件</B>
   * </p>
   * 
   * <p>
   * <B>e.g.:</B> <br>
   * &ensp;{<br>
   * &emsp;_id: ObjectId("5234cc89687ea597eabee675"),<br>
   * &emsp;code: "xyz",<br>
   * &emsp;tags: [ "school", "book", "bag", "headphone", "appliance" ],<br>
   * &emsp;results: [ 82, 85, 88 ],<br>
   * &emsp;qty: [<br>
   * &emsp;&ensp;{ size: "S", num: 10, color: "blue" },<br>
   * &emsp;&ensp;{ size: "M", num: 45, color: "blue" },<br>
   * &emsp;&ensp;{ size: "L", num: 100, color: "green" }<br>
   * &emsp;]<br>
   * &ensp;}
   * </p>
   * <ul>
   * <li><B>shell:</B> { results: { $elemMatch: { $gte: 80, $lt: 85 } } }</li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"results"</li>
   * <li>sValues:"{ $gte: 80, $lt: 85 }"</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * <ul>
   * <li><B>shell:</B> { qty: { $elemMatch: { size: "M", num: {$gte: 30} } } }
   * </li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"qty"</li>
   * <li>sValues:{ size: "M", num: {$gte: 30} }</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param sField
   *          数组字段
   * @param bsFilter
   *          匹配条件
   * @return {Bson}
   */
  public Bson arrayOfElementsMatchesByFilter(String sField, Bson bsFilter) {
    return elemMatch(sField, bsFilter);
  }

  /**
   * contrains all the specified value of array
   * 
   * 数组值中满足所有指定的匹配条件
   * 
   * <p>
   * <B style="color:red">模糊匹配，不考虑多出的元素、不考虑元素顺序</B>
   * </p>
   * 
   * <p>
   * <B>e.g.:</B> <br>
   * &ensp;{<br>
   * &emsp;_id: ObjectId("5234cc89687ea597eabee675"),<br>
   * &emsp;code: "xyz",<br>
   * &emsp;tags: [ "school", "book", "bag", "headphone", "appliance" ],<br>
   * &emsp;results: [ 82, 85, 88 ],<br>
   * &emsp;qty: [<br>
   * &emsp;&ensp;{ size: "S", num: 10, color: "blue" },<br>
   * &emsp;&ensp;{ size: "M", num: 45, color: "blue" },<br>
   * &emsp;&ensp;{ size: "L", num: 100, color: "green" }<br>
   * &emsp;]<br>
   * &ensp;}
   * </p>
   * <ul>
   * <li><B>shell:</B> { tags: { $all: [ "appliance", "school", "book" ] } }
   * </li>
   * <li><B>code:</B>
   * <ul>
   * <li>sField:"tags"</li>
   * <li>sValues:"appliance,school,book"</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param sField
   *          数组字段
   * @param sValues
   *          匹配值，可多个用逗号(,)分隔
   * @return {Bson}
   */
  public Bson arrayOfElementLikeMatchesByValue(String sField, String sValues) {
    if (CommonUtil.ifIsEmpty(sValues)) {
      return null;
    } else {
      List<String> list = Arrays.asList(StringUtils.split(sValues, ","));
      return arrayOfElementLikeMatches(sField, list);
    }
  }

  public Bson arrayOfElementLikeMatchesByValue(String sField, JsonNode jaValues) {
    if (CommonUtil.ifIsEmpty(jaValues)) {
      return null;
    } else {
      List<String> list = new ArrayList<String>();
      Iterator<JsonNode> it = jaValues.elements();
      while (it.hasNext()) {
        JsonNode jValue = it.next();
        if (CommonUtil.ifIsEmpty(jValue))
          continue;
        list.add(jValue.asText());
      }
      return arrayOfElementLikeMatches(sField, list);
    }
  }

  public Bson arrayOfElementLikeMatches(String sField, List<String> list) {
    return all(sField, list);
  }

  public Bson arrayOfSizeMatches(String sField, int iSize) {
    return size(sField, iSize);
  }

}
