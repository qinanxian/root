/**
 * Mongodb 数据操作入口
 * 
 * @since mongodb-driver-3.4.2.jar
 * @version 2.1.2 (2017-11-12)
 * @history
 *    V2.0.0: 提供mongodb.3.4.2版本的数据操作入口，迁移ObjectUtil中的方法，大部分方法进行了兼容性处理
 *      1、对比老版本，增加和优化了【组合操作】相关方法处理；
 *      2、根据mongodb3.0以后的新特性，将查询条件(Filter)、更新内容(Update)、查询结果过滤(Projection)、
 *          排序(Sort)等均在mongodb api的基础上进行了再次类封装处理，减少在业务逻辑中写mongodb语法；
 *      3、根据mongodb3.0以后的新特性，增加了insert、update、delete的针对单个collection的批量操作处理方法
 *      
 *    V2.1.0: 增加对直接或内嵌数组的增、删、改、查操作方法；
 *    V2.1.1: 优化处理 
 *      1、增加replaceOneByID，兼容员ObjectUtil中的save方法
 *      2、使用查询条件查询，当全表查询或查询条件为空的容错处理
 *      3、针对JsonNode[] 数组格式返回优化
 *    V2.1.2: 优化处理
 *      优化“查询满足匹配条件的数组字段的某一个元素”;
 *        ①：findOneArrayElementOnEmbedIDByID/findOneArrayElementOnEmbedIDByFilter
 *        ②：findArrayWithOneElementByID/findArrayWithOneElementByFilter
 *        ①支持数组字段是最外层数组或内嵌在对象中的数组，建议使用
 *        ②仅支持数组字段是最外层数据，不支持数组内嵌在对象中的情况；
 *      统一更新更新数据内容的逻辑处理采用saveByFilter,具体实现在ActionObject#setupdateArrayOfEmbedOnOneElementByID/Filter
 *      
 *    
 * @author Alias 
 */

/**
 * TODO 针对delete、drop、update等有全表操作风险的增加控制
 */

package com.vekai.crops.util.mongo3;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Mongodb 数据操作入口
 * 
 * @since mongodb-driver-3.4.2.jar
 * @author Alias
 */
public class DBEntry {

  public static final String VERSION = "2.1.2 (2017-11-12)";
  
  public static final String DBEntry_increaseByFilter = "递增信息内容为空！";
  public static final String DBEntry_deleteFieldsByFilter = "删除字段项为空！";
  public static final String DBEntry_delete = "请指定删除记录_id!";
  public static final String DBEntry_deleteByFilter = "删除条件为空!";
  

  public static HashMap<String, ConcurrentHashMap<String, String>> concurrent = new HashMap<String, ConcurrentHashMap<String, String>>();

  public static ConcurrentHashMap<String, String> getConcurrentMap(String sObject) throws Exception {
    if (!concurrent.containsKey(sObject)) {
      concurrent.put(sObject, new ConcurrentHashMap<String, String>());
    }
    return concurrent.get(sObject);
  }

  public static void open(String sObject, String sID) throws Exception {
    while (getConcurrentMap(sObject).containsKey(sID)) {
      Thread.sleep(10);
    }
    getConcurrentMap(sObject).put(sID, "");
  }

  public static void close(String sObject, String sID) throws Exception {
    getConcurrentMap(sObject).remove(sID);
  }

  /**********************************************************************************************************
   * 组合操作 BEGIN
   **********************************************************************************************************/
  /**
   * find one record before update and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, ActionObject oAction)
      throws Exception {
    return findOneAndUpdateByID(sCollection, sID, sUpdate, "", oAction);
  }

  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, ActionObject oAction,
                                              String sDatabase) throws Exception {
    return findOneAndUpdateByID(sCollection, sID, sUpdate, "", oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              ActionObject oAction) throws Exception {
    return findOneAndUpdateByID(sCollection, sID, sUpdate, sProjection, true, oAction);
  }

  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndUpdateByID(sCollection, sID, sUpdate, sProjection, true, oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return {Document}
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              Boolean bUpsert, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, "", bUpsert, oAction);
  }

  public static JsonNode findOneAndUpdateByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, "", bUpsert, oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, "", oAction);
  }

  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, ActionObject oAction,
                                          String sDatabase) throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, "", oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          ActionObject oAction) throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, "", true, oAction);
  }

  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, "", true, oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, ActionObject oAction) throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, sSort, true, oAction);
  }

  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndUpdate(sCollection, sFilter, sUpdate, sProjection, sSort, true, oAction, sDatabase);
  }

  /**
   * find one record before update and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, Boolean bUpsert, ActionObject oAction) throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, "", "", sProjection, sSort, bUpsert, true, oAction);
  }

  public static JsonNode findOneAndUpdate(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, "", "", sProjection, sSort, bUpsert, true, oAction,
        sDatabase);
  }

  /**
   * updates or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, ActionObject oAction)
      throws Exception {
    return updateOneAndFindByID(sCollection, sID, sUpdate, "", oAction);
  }

  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, ActionObject oAction,
                                              String sDatabase) throws Exception {
    return updateOneAndFindByID(sCollection, sID, sUpdate, "", oAction, sDatabase);
  }

  /**
   * updates or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              ActionObject oAction) throws Exception {
    return updateOneAndFindByID(sCollection, sID, sUpdate, sProjection, true, oAction);
  }

  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              ActionObject oAction, String sDatabase) throws Exception {
    return updateOneAndFindByID(sCollection, sID, sUpdate, sProjection, true, oAction, sDatabase);
  }

  /**
   * updates or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              Boolean bUpsert, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return updateOneAndFind(sCollection, sFilter, sUpdate, sProjection, "", bUpsert, oAction);
  }

  public static JsonNode updateOneAndFindByID(String sCollection, String sID, String sUpdate, String sProjection,
                                              Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return updateOneAndFind(sCollection, sFilter, sUpdate, sProjection, "", bUpsert, oAction, sDatabase);
  }

  /**
   * updates or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return updateOneAndFind(sCollection, sFilter, sUpdate, "", oAction);
  }

  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, ActionObject oAction,
                                          String sDatabase) throws Exception {
    return updateOneAndFind(sCollection, sFilter, sUpdate, "", oAction, sDatabase);
  }

  /**
   * updates or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          ActionObject oAction) throws Exception {
    return updateOneAndFind(sCollection, sFilter, sUpdate, sProjection, "", true, oAction);
  }

  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          ActionObject oAction, String sDatabase) throws Exception {
    return updateOneAndFind(sCollection, sFilter, sUpdate, sProjection, "", true, oAction, sDatabase);
  }

  /**
   * updates or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, Boolean bUpsert, ActionObject oAction) throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, "", "", sProjection, sSort, bUpsert, false, oAction);
  }

  public static JsonNode updateOneAndFind(String sCollection, String sFilter, String sUpdate, String sProjection,
                                          String sSort, Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, "", "", sProjection, sSort, bUpsert, false, oAction,
        sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, ActionObject oAction)
      throws Exception {
    return findOneAndIncreaseByID(sCollection, sID, sIncrease, "", oAction);
  }

  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, ActionObject oAction,
                                                String sDatabase) throws Exception {
    return findOneAndIncreaseByID(sCollection, sID, sIncrease, "", oAction, sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                ActionObject oAction) throws Exception {
    return findOneAndIncreaseByID(sCollection, sID, sIncrease, sProjection, true, oAction);
  }

  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndIncreaseByID(sCollection, sID, sIncrease, sProjection, true, oAction, sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                Boolean bUpsert, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneAndIncrease(sCollection, sFilter, sIncrease, sProjection, "", bUpsert, oAction);
  }

  public static JsonNode findOneAndIncreaseByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneAndIncrease(sCollection, sFilter, sIncrease, sProjection, "", bUpsert, oAction, sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, ActionObject oAction)
      throws Exception {
    return findOneAndIncrease(sCollection, sFilter, sIncrease, "", oAction);
  }

  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, ActionObject oAction,
                                            String sDatabase) throws Exception {
    return findOneAndIncrease(sCollection, sFilter, sIncrease, "", oAction, sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            ActionObject oAction) throws Exception {
    return findOneAndIncrease(sCollection, sFilter, sIncrease, sProjection, "", true, oAction);
  }

  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndIncrease(sCollection, sFilter, sIncrease, sProjection, "", true, oAction, sDatabase);
  }

  /**
   * find one record before increase and then updates or insert by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            String sSort, Boolean bUpsert, ActionObject oAction) throws Exception {
    return findOneAndModify(sCollection, sFilter, "", sIncrease, "", sProjection, sSort, bUpsert, true, oAction);
  }

  public static JsonNode findOneAndIncrease(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            String sSort, Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndModify(sCollection, sFilter, "", sIncrease, "", sProjection, sSort, bUpsert, true, oAction,
        sDatabase);
  }

  /**
   * increase or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, ActionObject oAction)
      throws Exception {
    return increaseOneAndFindByID(sCollection, sID, sIncrease, "", oAction);
  }

  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, ActionObject oAction,
                                                String sDatabase) throws Exception {
    return increaseOneAndFindByID(sCollection, sID, sIncrease, "", oAction, sDatabase);
  }

  /**
   * increase or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                ActionObject oAction) throws Exception {
    return increaseOneAndFindByID(sCollection, sID, sIncrease, sProjection, true, oAction);
  }

  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                ActionObject oAction, String sDatabase) throws Exception {
    return increaseOneAndFindByID(sCollection, sID, sIncrease, sProjection, true, oAction, sDatabase);
  }

  /**
   * increase or insert one record and then find it by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                Boolean bUpsert, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return increaseOneAndFind(sCollection, sFilter, sIncrease, sProjection, "", bUpsert, oAction);
  }

  public static JsonNode increaseOneAndFindByID(String sCollection, String sID, String sIncrease, String sProjection,
                                                Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return increaseOneAndFind(sCollection, sFilter, sIncrease, sProjection, "", bUpsert, oAction, sDatabase);
  }

  /**
   * increase or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   更新内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, ActionObject oAction)
      throws Exception {
    return increaseOneAndFind(sCollection, sFilter, sIncrease, "", oAction);
  }

  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, ActionObject oAction,
                                            String sDatabase) throws Exception {
    return increaseOneAndFind(sCollection, sFilter, sIncrease, "", oAction, sDatabase);
  }

  /**
   * increase or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            ActionObject oAction) throws Exception {
    return increaseOneAndFind(sCollection, sFilter, sIncrease, sProjection, "", true, oAction);
  }

  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            ActionObject oAction, String sDatabase) throws Exception {
    return increaseOneAndFind(sCollection, sFilter, sIncrease, sProjection, "", true, oAction, sDatabase);
  }

  /**
   * increase or insert one record and then find it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sIncrease   更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            String sSort, Boolean bUpsert, ActionObject oAction) throws Exception {
    return findOneAndModify(sCollection, sFilter, "", sIncrease, "", sProjection, sSort, bUpsert, false, oAction);
  }

  public static JsonNode increaseOneAndFind(String sCollection, String sFilter, String sIncrease, String sProjection,
                                            String sSort, Boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    return findOneAndModify(sCollection, sFilter, "", sIncrease, "", sProjection, sSort, bUpsert, false, oAction,
        sDatabase);
  }

  /*
   * public static Document findOneAndUnset() { return null; }
   * 
   * public static Document unsetOneAndFind() { return null; }
   */

  /**
   * find one record before or after updates
   * <p>
   * 支持查询修改前或修改后的记录信息
   * </p>
   * <p>
   * 支持update/upsert、increase or unset一并处理
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     过滤器
   * @param sUpdate     更新内容
   * @param sIncrease   递增内容
   * @param sUnset      删除内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param bBefore     是否 查询更新前的数据
   * @param oAction     ActionObject
   * @since 2.0
   * @return {Document}
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndModify(String sCollection, String sFilter, String sUpdate, String sIncrease,
                                          String sUnset, String sProjection, String sSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction)
      throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, sIncrease, sUnset, sProjection, sSort, bUpsert, bBefore,
        oAction, "");
  }

  public static JsonNode findOneAndModify(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return findOneAndModify(sCollection, sFilter, sUpdate, "", "", "", "", true, false, oAction, "");
  }

  public static JsonNode findOneAndModify(String sCollection, String sFilter, String sUpdate, String sIncrease,
                                          String sUnset, String sProjection, String sSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction,
                                          String sDatabase) throws Exception {
    Bson bsFilter = BsonDocument.parse(sFilter);
    Update update = Update.Builder();
    if (CommonUtil.ifIsNotEmpty(sUpdate)) {
      update.updateSet(sUpdate);
    }
    if (CommonUtil.ifIsNotEmpty(sIncrease)) {
      update.updateInc(sIncrease);
    }
    if (CommonUtil.ifIsNotEmpty(sUnset)) {
      update.updateUnset(sUnset);
    }
    Bson bsUpdate = update.builder();
    Bson bsProjection = Projection.Builder().inOrExclude(sProjection).builder();
    Bson bsSort = Sort.Builder().ascOrDescending(sSort).builder();
    return findOneAndModify(sCollection, bsFilter, bsUpdate, bsProjection, bsSort, bUpsert, bBefore, oAction,
        sDatabase);
  }

  /**
   * find one record before update and then updates
   * 
   * @param sCollection  集合名称
   * @param bsFilter     过滤器
   * @param bsUpdate     更新内容
   * @param bsProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param bsSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert      是否 upsert
   * @param bBefore      是否 查询更新前的数据
   * @param oAction      ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static JsonNode findOneAndModify(String sCollection, Bson bsFilter, Bson bsUpdate, Bson bsProjection,
                                          Bson bsSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction) throws Exception {
    return findOneAndModify(sCollection, bsFilter, bsUpdate, bsProjection, bsSort, bUpsert, bBefore, oAction, "");
  }

  public static JsonNode findOneAndModify(String sCollection, Bson bsFilter, Bson bsUpdate, Bson bsProjection,
                                          Bson bsSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction, String sDatabase) throws Exception {
    FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
    options.upsert(bUpsert);
    if (CommonUtil.ifIsNotNull(bsProjection)) {
      options.projection(bsProjection);
    }
    if (CommonUtil.ifIsNotEmpty(bsSort)) {
      options.sort(bsSort);
    }
    if (bBefore) {
      options.returnDocument(ReturnDocument.BEFORE);
    } else {
      options.returnDocument(ReturnDocument.AFTER);
    }

    Document document = MongoDB.findOneAndUpdate(sDatabase, sCollection, bsFilter, bsUpdate, options);
    if (CommonUtil.ifIsNull(document)) {
      return null;
    }
    return JsonUtil.getJson(document.toJson());
  }

  /**
   * find one record before replace and then replace it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sUpdate     更新内容
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param bUpsert     是否 upsert
   * @param bBefore     是否 查询更新前的数据
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static Document findOneAndReplace(String sCollection, String sFilter, String sUpdate, String sProjection,
                                           String sSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction) throws Exception {
    return findOneAndReplace(sCollection, sFilter, sUpdate, sProjection, sSort, bUpsert, bBefore, oAction, "");
  }

  public static Document findOneAndReplace(String sCollection, String sFilter, String sUpdate, String sProjection,
                                           String sSort, Boolean bUpsert, Boolean bBefore, ActionObject oAction, String sDatabase) throws Exception {
    FindOneAndReplaceOptions options = new FindOneAndReplaceOptions();
    options.upsert(bUpsert);
    if (CommonUtil.ifIsNotEmpty(sProjection)) {
      Bson bsProjection = Projection.Builder().inOrExclude(sProjection).builder();
      options.projection(bsProjection);
    }
    if (CommonUtil.ifIsNotEmpty(sSort)) {
      Bson bsSort = Sort.Builder().ascOrDescending(sSort).builder();
      options.sort(bsSort);
    }
    if (bBefore) {
      options.returnDocument(ReturnDocument.BEFORE);
    } else {
      options.returnDocument(ReturnDocument.AFTER);
    }

    Bson bsFilter = BsonDocument.parse(sFilter);
    Document docUpdate = Document.parse(sUpdate);
    Document document = MongoDB.findOneAndReplace(sDatabase, sCollection, bsFilter, docUpdate, options);
    return document;
  }

  /**
   * find one record before delete and then delete it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static Document findOneAndDelete(String sCollection, String sFilter, ActionObject oAction) throws Exception {
    return findOneAndDelete(sCollection, sFilter, "", "", oAction);
  }

  public static Document findOneAndDelete(String sCollection, String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    return findOneAndDelete(sCollection, sFilter, "", "", oAction, sDatabase);
  }

  /**
   * find one record before delete and then delete it
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询/更新条件
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @since 2.0
   * @return Document
   * @throws Exception if has some exception
   */
  public static Document findOneAndDelete(String sCollection, String sFilter, String sProjection, String sSort,
                                          ActionObject oAction) throws Exception {
    return findOneAndDelete(sCollection, sFilter, sProjection, sSort, oAction, "");
  }

  public static Document findOneAndDelete(String sCollection, String sFilter, String sProjection, String sSort,
                                          ActionObject oAction, String sDatabase) throws Exception {
    FindOneAndDeleteOptions options = new FindOneAndDeleteOptions();
    if (CommonUtil.ifIsNotEmpty(sProjection)) {
      Bson bsProjection = Projection.Builder().inOrExclude(sProjection).builder();
      options.projection(bsProjection);
    }
    if (CommonUtil.ifIsNotEmpty(sSort)) {
      Bson bsSort = Sort.Builder().ascOrDescending(sSort).builder();
      options.sort(bsSort);
    }

    Bson bsFilter = BsonDocument.parse(sFilter);
    Document document = MongoDB.findOneAndDelete(sDatabase, sCollection, bsFilter, options);
    return document;
  }

  /**********************************************************************************************************
   * 组合操作 END
   **********************************************************************************************************/

  /**********************************************************************************************************
   * 新增操作 BEGIN
   **********************************************************************************************************/
  /**
   * insert one record by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sInsert     新增记录内容
   * @param oAction     ActionObject
   * @throws Exception if has some exception
   */
  public static void insertOne(String sCollection, String sID, String sInsert, ActionObject oAction) throws Exception {
    insertOne(sCollection, sID, sInsert, oAction, "");
  }

  public static void insertOne(String sCollection, String sID, String sInsert, ActionObject oAction, String sDatabase)
      throws Exception {
    if (CommonUtil.ifIsEmpty(sID)) {
      sID = CommonUtil.getUUID();
    }
    JsonNode jInsert = JsonUtil.getJson(sInsert);
    JsonUtil.setJsonStringValue(jInsert, "_id", sID);
    sInsert = jInsert.toString();
    insertOne(sCollection, sInsert, oAction, sDatabase);
  }

  /**
   * inserts one record
   * <p>
   * 如果没有设定id，则默认使用SERIALNO_INFO.getSerialNo
   * </p>
   * <p style="color:red">
   * 若需要新增完成后同时返回新增内容信息，建议使用 updateOneAndFind
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sInsert     插入信息内容
   * @param oAction     ActionObject
   * @throws Exception if has some exception
   */
  public static void insertOne(String sCollection, String sInsert, ActionObject oAction) throws Exception {
    insertOne(sCollection, sInsert, oAction, "");
  }

  public static void insertOne(String sCollection, String sInsert, ActionObject oAction, String sDatabase)
      throws Exception {
    String sID = JsonUtil.getJsonStringValue(sInsert, "_id");
    if (CommonUtil.ifIsEmpty(sID)) {
      sID = CommonUtil.getUUID();
      JsonNode jInsert = JsonUtil.getJson(sInsert);
      JsonUtil.setJsonStringValue(jInsert, "_id", sID);
      sInsert = jInsert.toString();
    }
    InsertOneOptions options = new InsertOneOptions();
    MongoDB.insertOne(sDatabase, sCollection, sInsert, options);
  }

  /**
   * insert many record
   * 
   * @param sCollection 集合名称
   * @param sInsert     插入信息内容
   * @param oAction     ActionObject
   * @throws Exception if has some exception
   */
  public static void insertMany(String sCollection, String sInsert, ActionObject oAction) throws Exception {
    insertMany(sCollection, sInsert, oAction, "");
  }

  public static void insertMany(String sCollection, String sInsert, ActionObject oAction, String sDatabase)
      throws Exception {
    InsertManyOptions options = new InsertManyOptions();
    MongoDB.insertMany(sDatabase, sCollection, sInsert, options);
  }

  /**********************************************************************************************************
   * 新增操作 END
   **********************************************************************************************************/

  /**********************************************************************************************************
   * 查询操作 BEGIN
   **********************************************************************************************************/
  /**
   * find record count by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     过滤条件
   * @param oAction     ActionObject
   * @return {long}
   * @throws Exception if has some exception
   */
  public static long findCount(String sCollection, String sFilter, ActionObject oAction) throws Exception {
    return findCount(sCollection, sFilter, oAction, "");
  }

  public static long findCount(String sCollection, String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    CountOptions options = new CountOptions();
    return MongoDB.count(sDatabase, sCollection, sFilter, options);
  }

  /**
   * find one String record by id
   * <p>
   * 根据_id查询单笔记录
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   */
  public static String findOneStringByID(String sCollection, String sID, ActionObject oAction) {
    return findOneStringByID(sCollection, sID, "", oAction);
  }

  public static String findOneStringByID(String sCollection, String sID, ActionObject oAction, String sDatabase) {
    return findOneStringByID(sCollection, sID, "", oAction, sDatabase);
  }

  /**
   * find one String record by id
   * <p>
   * 根据_id查询单笔记录,可设定投射字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   */
  public static String findOneStringByID(String sCollection, String sID, String sProjection, ActionObject oAction) {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneStringByFilter(sCollection, sFilter, sProjection, oAction);
  }

  public static String findOneStringByID(String sCollection, String sID, String sProjection, ActionObject oAction,
                                         String sDatabase) {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneStringByFilter(sCollection, sFilter, sProjection, oAction, sDatabase);
  }

  /**
   * find one String record by filter
   * <p>
   * 根据_id查询单笔记录
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   */
  public static String findOneStringByFilter(String sCollection, String sFilter, ActionObject oAction) {
    return findOneStringByFilter(sCollection, sFilter, "", oAction);
  }

  public static String findOneStringByFilter(String sCollection, String sFilter, ActionObject oAction,
                                             String sDatabase) {
    return findOneStringByFilter(sCollection, sFilter, "", oAction, sDatabase);
  }

  /**
   * find one String record by filter
   * <p>
   * 根据_id查询单笔记录,可设定投射字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   */
  public static String findOneStringByFilter(String sCollection, String sFilter, String sProjection,
                                             ActionObject oAction) {
    return findOneStringByFilter(sCollection, sFilter, sProjection, "", oAction);
  }

  public static String findOneStringByFilter(String sCollection, String sFilter, String sProjection,
                                             ActionObject oAction, String sDatabase) {
    return findOneStringByFilter(sCollection, sFilter, sProjection, "", oAction, sDatabase);
  }

  /**
   * find one String record by filter
   * <p>
   * 根据_id查询单笔记录,可设定投射字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   */
  public static String findOneStringByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                             ActionObject oAction) {
    Document doc = findOne(sCollection, sFilter, sProjection, sSort, oAction);
    return doc.toJson();
  }

  public static String findOneStringByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                             ActionObject oAction, String sDatabase) {
    Document doc = findOne(sCollection, sFilter, sProjection, sSort, oAction, sDatabase);
    return doc.toJson();
  }

  /**
   * find one json record by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param oAction     ActionObject
   * @return JsonNode
   * @throws Exception if has some exception
   */
  public static JsonNode findOneJsonByID(String sCollection, String sID, ActionObject oAction) throws Exception {
    return findOneJsonByID(sCollection, sID, "", oAction);
  }

  public static JsonNode findOneJsonByID(String sCollection, String sID, ActionObject oAction, String sDatabase)
      throws Exception {
    return findOneJsonByID(sCollection, sID, "", oAction, sDatabase);
  }

  /**
   * find one json record by id
   * <p>
   * 根据_id查询单笔记录,可设定投射字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findOneJsonByID(String sCollection, String sID, String sProjection, ActionObject oAction)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneJsonByFilter(sCollection, sFilter, sProjection, oAction);
  }

  public static JsonNode findOneJsonByID(String sCollection, String sID, String sProjection, ActionObject oAction,
                                         String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneJsonByFilter(sCollection, sFilter, sProjection, oAction, sDatabase);
  }

  public static JsonNode findOneJsonByID(String sCollection, String sID, Bson bsProjection, ActionObject oAction)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    Bson bsFilter = BsonDocument.parse(sFilter);
    Document doc = findOne(sCollection, bsFilter, bsProjection, null, oAction);
    return JsonUtil.getJson(doc.toJson());
  }

  public static JsonNode findOneJsonByID(String sCollection, String sID, Bson bsProjection, ActionObject oAction,
                                         String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    Bson bsFilter = BsonDocument.parse(sFilter);
    Document doc = findOne(sCollection, bsFilter, bsProjection, null, oAction, sDatabase);
    return JsonUtil.getJson(doc.toJson());
  }

  /**
   * find one json record by filter
   * <p>
   * 根据_id查询单笔记录
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, ActionObject oAction)
      throws Exception {
    return findOneJsonByFilter(sCollection, sFilter, "", oAction);
  }

  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    return findOneJsonByFilter(sCollection, sFilter, "", oAction, sDatabase);
  }

  /**
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return JsonNode
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, String sProjection,
                                             ActionObject oAction) throws Exception {
    return findOneJsonByFilter(sCollection, sFilter, sProjection, "", oAction);
  }

  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, String sProjection,
                                             ActionObject oAction, String sDatabase) throws Exception {
    return findOneJsonByFilter(sCollection, sFilter, sProjection, "", oAction, sDatabase);
  }

  /**
   * find one json record by filter
   * <p>
   * 根据_id查询单笔记录,可设定投射字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     过滤条件
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         e.g. {'_id':'', ...}
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                             ActionObject oAction) throws Exception {
    Document doc = findOne(sCollection, sFilter, sProjection, sSort, oAction);
    if (CommonUtil.ifIsNull(doc)) {
      return null;
    }
    return JsonUtil.getJson(doc.toJson());
  }

  public static JsonNode findOneJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                             ActionObject oAction, String sDatabase) throws Exception {
    Document doc = findOne(sCollection, sFilter, sProjection, sSort, oAction, sDatabase);
    if (CommonUtil.ifIsNull(doc)) {
      return null;
    }
    return JsonUtil.getJson(doc.toJson());
  }

  /**
   * find many json record by id
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id，id可以为多个，以逗号分隔
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByID(String sCollection, String sID, ActionObject oAction) throws Exception {
    return findJsonByID(sCollection, sID, "", oAction);
  }

  public static JsonNode findJsonByID(String sCollection, String sID, ActionObject oAction, String sDatabase)
      throws Exception {
    return findJsonByID(sCollection, sID, "", oAction, sDatabase);
  }

  /**
   * find many json record by id
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id，id可以为多个，以逗号分隔
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, ActionObject oAction)
      throws Exception {
    return findJsonByID(sCollection, sID, sProjection, "", oAction);
  }

  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, ActionObject oAction,
                                      String sDatabase) throws Exception {
    return findJsonByID(sCollection, sID, sProjection, "", oAction, sDatabase);
  }

  /**
   * find many json record by id
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id，id可以为多个，以逗号分隔
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, String sSort,
                                      ActionObject oAction) throws Exception {
    return findJsonByID(sCollection, sID, sProjection, sSort, 0, 0, oAction);
  }

  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, String sSort,
                                      ActionObject oAction, String sDatabase) throws Exception {
    return findJsonByID(sCollection, sID, sProjection, sSort, 0, 0, oAction, sDatabase);
  }

  /**
   * find many json record by id
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id，id可以为多个，以逗号分隔
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip       查询时跳过的记录数；
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, String sSort, int iLimit,
                                      int iSkip, ActionObject oAction) throws Exception {
    String sFilter = Filter.inIDFilter(sID);
    return findJsonByFilter(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction);
  }

  public static JsonNode findJsonByID(String sCollection, String sID, String sProjection, String sSort, int iLimit,
                                      int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.inIDFilter(sID);
    return findJsonByFilter(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, sDatabase);
  }

  /**
   * find many json record by filter
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByFilter(String sCollection, String sFilter, ActionObject oAction) throws Exception {
    return findJsonByFilter(sCollection, sFilter, "", oAction);
  }

  public static JsonNode findJsonByFilter(String sCollection, String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    return findJsonByFilter(sCollection, sFilter, "", oAction, sDatabase);
  }

  /**
   * find many json record by filter
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, ActionObject oAction)
      throws Exception {
    return findJsonByFilter(sCollection, sFilter, sProjection, "", oAction);
  }

  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, ActionObject oAction,
                                          String sDatabase) throws Exception {
    return findJsonByFilter(sCollection, sFilter, sProjection, "", oAction, sDatabase);
  }

  /**
   * find many json record by filter
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                          ActionObject oAction) throws Exception {
    return findJsonByFilter(sCollection, sFilter, sProjection, sSort, 0, 0, oAction);
  }

  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                          ActionObject oAction, String sDatabase) throws Exception {
    return findJsonByFilter(sCollection, sFilter, sProjection, sSort, 0, 0, oAction, sDatabase);
  }

  /**
   * find many json record by filter
   * <p>
   * 若投射字段项中显式的排除了_id，则返回ArrayNode，否则返回JsonNode
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip       查询时跳过的记录数；
   * @param oAction     ActionObject
   * @return {JsonNode}
   *         <p>
   *         {'$_id':{...}, '$_id':{...}}
   *         </p>
   *         或
   *         <p>
   *         [{...}, {...}]
   *         </p>
   * @throws Exception if has some exception
   */
  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                          int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findJsonByFilter(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static JsonNode findJsonByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                          int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    JsonNode jResult = null;
    if (CommonUtil.ifIsNotEmpty(sProjection) && CommonUtil.ifIsIndexOf(sProjection, "-" + "_id")) {
      jResult = JsonUtil.getJson("[]");
    } else {
      jResult = JsonUtil.getJson("{}");
    }
    Iterator<Document> it = findIt.iterator();
    while (it.hasNext()) {
      Document doc = it.next();
      if (doc.isEmpty())
        continue;
      if (jResult.isArray()) {
        JsonUtil.setJsonArrayValue(jResult, "", JsonUtil.getJson(doc.toJson()));
      } else {
        String _id = "";
        try {
          _id = doc.getString("_id");
        } catch (ClassCastException e) {
          _id = doc.getObjectId("_id").toString();
        }
        JsonUtil.setJsonValue(jResult, "", _id, doc.toJson());
      }
    }
    return jResult;
  }

  /**
   * find many records by filter and return array
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param oAction     ActionObject
   * @return JsonNode[]
   * @throws Exception if has some exception
   */
  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, ActionObject oAction)
      throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, "", oAction);
  }

  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, ActionObject oAction,
                                                 String sDatabase) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, "", oAction, sDatabase);
  }

  /**
   * find many records by filter and return array
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return JsonNode[]
   * @throws Exception if has some exception
   */
  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection,
                                                 ActionObject oAction) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, sProjection, "", oAction);
  }

  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection,
                                                 ActionObject oAction, String sDatabase) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, sProjection, "", oAction, sDatabase);
  }

  /**
   * find many records by filter and return array
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式
   * @param oAction     ActionObject
   * @return JsonNode[]
   * @throws Exception if has some exception
   */
  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                 ActionObject oAction) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, sProjection, sSort, 0, 0, oAction);
  }

  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                 ActionObject oAction, String sDatabase) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, sProjection, sSort, 0, 0, oAction, sDatabase);
  }

  /**
   * find many records by filter and return array
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式
   * @param iLimit      查询记录数
   * @param iSkip       查询过滤记录数
   * @param oAction     ActionObject
   * @return JsonNode[]
   * @throws Exception if has some exception
   */
  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                 int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findJsonArrayByFilter(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static JsonNode[] findJsonArrayByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                 int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    List<JsonNode> list = new ArrayList<JsonNode>();
    Iterator<Document> it = findIt.iterator();
    while (it.hasNext()) {
      Document doc = it.next();
      list.add(JsonUtil.getJson(doc.toJson()));
    }
    JsonNode[] aJson = new JsonNode[list.size()];
    return list.toArray(aJson);
  }

  public static ArrayNode findArrayNodeByFilter(String sCollection, String sFilter, String sProjection,
                                                ActionObject oAction) throws Exception {
    return findArrayNodeByFilter(sCollection, sFilter, sProjection, oAction, "");
  }

  public static ArrayNode findArrayNodeByFilter(String sCollection, String sFilter, String sProjection,
                                                ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, "{}", 0, 0, oAction, sDatabase);
    ArrayNode list = JsonUtil.getArrayNode();
    Iterator<Document> it = findIt.iterator();
    while (it.hasNext()) {
      Document doc = it.next();
      list.add(JsonUtil.getJson(doc.toJson()));
    }
    return list;
  }

  public static String findArrayStringByFilter(String sCollection, String sFilter, String sProjection,
                                               ActionObject oAction) throws Exception {
    return findArrayStringByFilter(sCollection, sFilter, sProjection, oAction, "");
  }

  public static String findArrayStringByFilter(String sCollection, String sFilter, String sProjection,
                                               ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, "{}", 0, 0, oAction, sDatabase);
    StringBuffer sb = new StringBuffer();
    Iterator<Document> it = findIt.iterator();
    int i = 0;
    sb.append("[");
    while (it.hasNext()) {
      Document doc = it.next();
      if (i != 0) {
        sb.append(",");
      }
      sb.append(JsonUtil.getJson(doc.toJson()));
      i++;
    }
    sb.append("]");
    return sb.toString();
  }

  public static ArrayNode findArrayNodeByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findArrayNodeByFilter(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static ArrayNode findArrayNodeByFilter(String sCollection, String sFilter, String sProjection, String sSort,
                                                int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    ArrayNode list = JsonUtil.getArrayNode();
    Iterator<Document> it = findIt.iterator();
    while (it.hasNext()) {
      Document doc = it.next();
      list.add(JsonUtil.getJson(doc.toJson()));
    }
    return list;
  }

  /**
   * find one record of field value by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sField      要查询的字段
   * @param oAction     ActionObject
   * @return {Object}
   *         <p>
   *         eg.{"_id":"11","za":{"1":{"a":"123","b":"234"},"2":{"a":"123","b":
   *         "234"}}}
   *         <p>
   *         sField = "" :
   *         {"_id":"11","za":{"1":{"a":"123","b":"234"},"2":{"a":"123","b":
   *         "234"}}}
   *         <p>
   *         sField = za : {"1":{"a":"123","b":"234"},"2":{"a":"123","b":"234"}}
   *         <p>
   *         sField = za.1 : {"a":"123","b":"234"}
   *         <p>
   *         sField = za.1.a : "123"
   * @throws Exception if has some exception
   */
  public static Object findOneOfFieldValueByID(String sCollection, String sID, String sFieldKey, String sField,
                                               ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneOfFieldValueByFilter(sCollection, sFilter, sFieldKey, sField, "", oAction);
  }

  public static Object findOneOfFieldValueByID(String sCollection, String sID, String sFieldKey, String sField,
                                               ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneOfFieldValueByFilter(sCollection, sFilter, sFieldKey, sField, "", oAction, sDatabase);
  }

  /**
   * find one record of field value by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sField      要查询的字段
   * @param sSort       排序方式
   * @param oAction     ActionObject
   * @return {Object}
   *         <p>
   *         eg.{"_id":"11","za":{"1":{"a":"123","b":"234"},"2":{"a":"123","b":
   *         "234"}}}
   *         <p>
   *         sField = "" :
   *         {"_id":"11","za":{"1":{"a":"123","b":"234"},"2":{"a":"123","b":
   *         "234"}}}
   *         <p>
   *         sField = za : {"1":{"a":"123","b":"234"},"2":{"a":"123","b":"234"}}
   *         <p>
   *         sField = za.1 : {"a":"123","b":"234"}
   *         <p>
   *         sField = za.1.a : "123"
   * @throws Exception if has some exception
   */
  public static Object findOneOfFieldValueByFilter(String sCollection, String sFilter, String sFieldKey, String sField,
                                                   String sSort, ActionObject oAction) throws Exception {
    return findOneOfFieldValueByFilter(sCollection, sFilter, sFieldKey, sField, sSort, oAction, "");
  }

  public static Object findOneOfFieldValueByFilter(String sCollection, String sFilter, String sFieldKey, String sField,
                                                   String sSort, ActionObject oAction, String sDatabase) throws Exception {
    String sProjection = "";
    if (CommonUtil.ifIsNotEmpty(sField)) {
      String[] aProjection = StringUtils.split(sField, ",");
      for (String projField : aProjection) {
        sProjection += "," + sFieldKey + "." + projField;
      }
      if (CommonUtil.ifIsNotEmpty(sProjection))
        sProjection = sProjection.substring(1);
    } else {
      sProjection = sFieldKey;
    }
    Document doc = findOne(sCollection, sFilter, sProjection, sSort, oAction, sDatabase);
    if (CommonUtil.ifIsNotNull(doc)) {
      JsonNode jFieldValue = JsonUtil.queryJson(JsonUtil.getJson(doc.toJson()), sFieldKey);
      return JsonUtil.toObject(jFieldValue);
    } else {
      return null;
    }
  }

  public static JsonNode findList(String sCollection, String sFilter, String sProjection, String sSort, int iLimit,
                                  int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    CountOptions options = new CountOptions();
    Long lRecordCount = MongoDB.count(sDatabase, sCollection, sFilter, options);
    Iterator<Document> it = findIt.iterator();
    int iIndex = 0;
    JsonNode jRecords = JsonUtil.getJson("[]");
    JsonNode jEmbedRecords = JsonUtil.getJson("{}");
    String sRecords = "";
    JsonNode jDatas = JsonUtil.getJson();
    while (it.hasNext()) {
      Document doc = it.next();
      String sID = "";
      try {
        sID = doc.getString("_id");
      } catch (ClassCastException e) {
        sID = doc.getObjectId("_id").toString();
      }
      sRecords += "," + sID;
      JsonUtil.addValue(jRecords, "", sID);
      JsonUtil.setValue(jEmbedRecords, sID + "._id", sID);
      JsonUtil.setValue(jDatas, sID, JsonUtil.getJson(doc.toJson()));
      iIndex++;
    }
    if (CommonUtil.ifIsNotEmpty(sRecords))
      sRecords = sRecords.substring(1);
    JsonNode jList = JsonUtil.getJson();
    JsonUtil.setValue(jList, "c", lRecordCount);
    JsonUtil.setValue(jList, "p", iLimit);
    JsonUtil.setValue(jList, "s", iSkip);
    JsonUtil.setValue(jList, "t", iIndex);
    JsonUtil.setValue(jList, "o", jRecords);
    JsonUtil.setValue(jList, "o1", jEmbedRecords);
    JsonUtil.setValue(jList, "o2", sRecords);
    JsonUtil.setValue(jList, "r", jDatas);
    return jList;
  }

  public static JsonNode findArrayItemList(String sCollection, String sID, String sDataItem, String sExcludeProjection,
                                           int iSkip, int iLimit, ActionObject oAction, String sDatabase) throws Exception {
    // TODO 查询内嵌数组对象大小，暂定内存中计算大小，待优化
    Bson bsProjection = Projection.Builder().sliceOfArray(sDataItem, iSkip, iLimit).inOrExclude(sExcludeProjection)
        .builder();
    JsonNode jArrayField = DBEntry.findOneJsonByID(sCollection, sID, sDataItem, oAction);
    JsonNode jAllArrayObject = JsonUtil.queryJson(jArrayField, sDataItem);
    int iAllCount = 0, iActSize = 0;
    JsonNode jArrayDatas = null;
    // JsonNode jDatas = JsonUtil.getJson();
    JsonNode jRecords = JsonUtil.getJson("[]");
    JsonNode jEmbedRecords = JsonUtil.getJson("{}");
    if (CommonUtil.ifIsNotEmpty(jAllArrayObject)) {
      iAllCount = jAllArrayObject.size();

      jArrayDatas = DBEntry.findOneJsonByID(sCollection, sID, bsProjection, oAction);
      JsonNode jItemDatas = JsonUtil.queryJson(jArrayDatas, sDataItem);
      iActSize = jItemDatas.size();
      if (CommonUtil.ifIsNotEmpty(jItemDatas)) {
        Iterator<JsonNode> it = jItemDatas.elements();
        while (it.hasNext()) {
          JsonNode jData = it.next();
          String sItemID = JsonUtil.getJsonStringValue(jData, "_id");
          JsonUtil.addValue(jRecords, "", sItemID);
          JsonUtil.setValue(jEmbedRecords, sItemID + "._id", sItemID);
          // JsonUtil.setValue(jDatas, sItemID, jData);
        }
      }
    }
    JsonNode jList = JsonUtil.getJson();
    JsonUtil.setValue(jList, "c", iAllCount);
    JsonUtil.setValue(jList, "p", iLimit);
    JsonUtil.setValue(jList, "s", iSkip);
    JsonUtil.setValue(jList, "t", iActSize);
    JsonUtil.setValue(jList, "o", jRecords);
    JsonUtil.setValue(jList, "o1", jEmbedRecords);
    JsonUtil.setValue(jList, "r", jArrayDatas);
    // JsonUtil.setValue(jList, "r1", jDatas);
    return jList;
  }

  /**
   * 根据查询条件查询集合数据记录，查询满足条件的所有记录,以展示列表格式返回数据
   * <p>
   * 支持查询指定字段项
   * </p>
   * <p>
   * 支持排序
   * </p>
   * <p>
   * 支持分页查询
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序方式 <B> 参考：{@link Sort} </B>
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip       查询跳过记录数
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         <B style="color:red">'c':'满足查询条件的所有记录数', 'p':'查询的记录数', 's':'跳过的记录数',
   *         't':'实际查询到的记录数', 'o':'查询到的数据的_id', 'r':{'$_id':{'a':'', 'b':''...},
   *         '$_id':{'a':'', 'b':''...}...} </B>
   *         </p>
   * @throws Exception if has some exception
   */
  public static String findTableRecordsByQuery(String sCollection, String sFilter, String sProjection, String sSort,
                                               int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findTableRecordsByQuery(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static String findTableRecordsByQuery(String sCollection, String sFilter, String sProjection, String sSort,
                                               int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    CountOptions options = new CountOptions();
    Long lRecordCount = MongoDB.count(sDatabase, sCollection, sFilter, options);
    StringBuffer sb = new StringBuffer();
    Iterator<Document> it = findIt.iterator();
    int iIndex = 0;
    JsonNode jRecords = JsonUtil.getJson("[]");
    JsonNode jEmbedRecords = JsonUtil.getJson("{}");
    while (it.hasNext()) {
      Document doc = it.next();
      if (0 != iIndex) {
        sb.append(",");
      }
      String sID = doc.getString("_id");
      JsonUtil.addValue(jRecords, "", sID);
      JsonUtil.setValue(jEmbedRecords, sID + "." + "_id", sID);
      sb.append("'" + sID + "':" + doc.toJson());
      iIndex++;
    }
    return "'c':" + lRecordCount + "" //
        + ",'p':" + iLimit + "" //
        + ",'s':" + iSkip + "" //
        + ",'t':" + iIndex + "" //
        + ",'o':" + jRecords + ""//
        + ",'o1':" + jEmbedRecords + "" //
        + ",'r':{" + sb.toString() + "}";
  }

  public static String findTableArrayRecordsByQuery(String sCollection, String sFilter, String sProjection,
                                                    String sSort, int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findTableArrayRecordsByQuery(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static String findTableArrayRecordsByQuery(String sCollection, String sFilter, String sProjection,
                                                    String sSort, int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindIterable<Document> findIt = findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction,
        sDatabase);
    CountOptions options = new CountOptions();
    Long lRecordCount = MongoDB.count(sDatabase, sCollection, sFilter, options);
    StringBuffer sb = new StringBuffer();
    Iterator<Document> it = findIt.iterator();
    int iIndex = 0;
    while (it.hasNext()) {
      Document doc = it.next();
      if (0 != iIndex) {
        sb.append(",");
      }
      sb.append(doc.toJson());
      iIndex++;
    }
    return "'c':" + lRecordCount + "" //
        + ",'p':" + iLimit + "" //
        + ",'s':" + iSkip + "" //
        + ",'t':" + iIndex + "" //
        + ",'r':[" + sb.toString() + "]";
  }

  /**
   * 根据查询条件查询集合子对象数据记录，查询满足条件的所有记录，以展示列表格式返回数据
   * <p>
   * 支持指定查询字段项
   * <p>
   * 不支持排序
   * <p>
   * 支持分页查询
   * 
   * @param sCollection  集合名称
   * @param sID          _id
   * @param sParentField 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param iLimit       查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip        查询跳过记录数
   * @param oAction      ActionObject
   * @return {String}
   *         <p>
   *         <B style="color:red">'c':'满足查询条件的所有记录数', 'p':'查询的记录数', 's':'跳过的记录数',
   *         't':'实际查询到的记录数', 'o':'查询到的数据的_id', 'r':{'$_id':{'a':'', 'b':''...},
   *         '$_id':{'a':'', 'b':''...}...} </B>
   *         </p>
   * @throws Exception if has some exception
   */
  public static String findSubTableRecordByID(String sCollection, String sID, String sParentField, int iSkip,
                                              int iLimit, ActionObject oAction) throws Exception {
    return findSubTableRecordByID(sCollection, sID, sParentField, iSkip, iLimit, oAction, "");
  }

  public static String findSubTableRecordByID(String sCollection, String sID, String sParentField, int iSkip,
                                              int iLimit, ActionObject oAction, String sDatabase) throws Exception {
    Object oSubTableValue = findOneOfFieldValueByID(sCollection, sID, sParentField, "", oAction, sDatabase);
    int iAllCount = 0, iActSize = 0, iIndex = 0;
    JsonNode jRecords = JsonUtil.getJson("[]");
    JsonNode jEmbedRecords = JsonUtil.getJson("{}");
    StringBuffer sb = new StringBuffer();
    if (CommonUtil.ifIsNotNull(oSubTableValue)) {
      if (CommonUtil.ifIsEmpty(oSubTableValue))
        oSubTableValue = "{}";
      JsonNode jObject = JsonUtil.getJson(oSubTableValue.toString());
      iAllCount = jObject.size();
      Iterator<Entry<String, JsonNode>> iter = jObject.fields();
      while (iter.hasNext()) {
        Entry<String, JsonNode> entry = iter.next();
        iIndex++;
        if (iIndex <= iSkip)
          continue;
        if (0 != iActSize) {
          sb.append(",");
        }
        iActSize++;
        String sKey = entry.getKey();
        JsonNode jValue = entry.getValue();
        sb.append("'" + sKey + "':" + jValue.toString());
        JsonUtil.addValue(jRecords, "", sKey);
        JsonUtil.setValue(jEmbedRecords, sKey + "." + "_id", sKey);
        if (iActSize >= iLimit)
          break;
      }
    }
    return "'c':" + iAllCount + "" //
        + ",'p':" + iLimit + "" //
        + ",'s':" + iSkip + "" //
        + ",'t':" + iActSize + "" //
        + ",'o':" + jRecords + ""//
        + ",'o1':" + jEmbedRecords + ""//
        + ",'r':{" + sb.toString() + "}";
  }

  /**
   * find one array or nested-array element on Embed element id by record id
   * <p>
   * 示例：({'notify_message_receive.h._id':'24'}, '_id':'1'},
   * {'notify_message_receive.h.$':1})
   * </p>
   * <p>
   * 支持查询数组字段项或内嵌在对象内的数组字段项
   * </p>
   * <p>
   * <B style="color:red">数组字段项-可用：{'_id':'1', 'a':[{},{}...]}</B>
   * </p>
   * <p>
   * <B style="color:red">内嵌在对象内的数组字段项-可用：{'_id':'2', 'a':{'b':[{}, {}...]}}</B>
   * </p>
   * <p>
   * 查询匹配每笔记录的数组字段项或内嵌在对象内的数组字段项的第一个数组元素
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sEmbedID    数据内嵌元素id值
   * @param sEmbedField 数组字段
   * @param oAction     ActionObject
   * @return {JsonNode}
   * @throws Exception if has some exception
   */
  public static JsonNode findOneArrayElementOnEmbedIDByID(String sCollection, String sID, String sEmbedID,
                                                          String sEmbedField, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneArrayElementOnEmbedIDByFilter(sCollection, sFilter, sEmbedID, sEmbedField, oAction);
  }

  public static JsonNode findOneArrayElementOnEmbedIDByID(String sCollection, String sID, String sEmbedID,
                                                          String sEmbedField, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findOneArrayElementOnEmbedIDByFilter(sCollection, sFilter, sEmbedID, sEmbedField, oAction, sDatabase);
  }

  /**
   * find one array or nested-array element on Embed element id by record filters
   * <p>
   * 示例：({'notify_message_receive.h._id':'24'}, '_id':'1'},
   * {'notify_message_receive.h.$':1})
   * </p>
   * <p>
   * 支持查询数组字段项或内嵌在对象内的数组字段项
   * </p>
   * <p>
   * <B style="color:red">数组字段项-可用：{'_id':'1', 'a':[{},{}...]}</B>
   * </p>
   * <p>
   * <B style="color:red">内嵌在对象内的数组字段项-可用：{'_id':'2', 'a':{'b':[{}, {}...]}}</B>
   * </p>
   * <p>
   * 查询匹配每笔记录的数组字段项或内嵌在对象内的数组字段项的第一个数组元素
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤条件
   * @param sEmbedID    数据内嵌元素id值
   * @param sEmbedField 数组字段
   * @param oAction     ActionObject
   * @return {JsonNode}
   * @throws Exception if has some exception
   */
  public static JsonNode findOneArrayElementOnEmbedIDByFilter(String sCollection, String sFilter, String sEmbedID,
                                                              String sEmbedField, ActionObject oAction) throws Exception {
    return findOneArrayElementOnEmbedIDByFilter(sCollection, sFilter, sEmbedID, sEmbedField, oAction, "");
  }

  public static JsonNode findOneArrayElementOnEmbedIDByFilter(String sCollection, String sFilter, String sEmbedID,
                                                              String sEmbedField, ActionObject oAction, String sDatabase) throws Exception {
    Bson bsFilter = Filters.and(Filter.Builder().parseFilter(sFilter).builder(),
        Filter.Builder().parseFilter("{'" + sEmbedField + "." + "_id" + "':'" + sEmbedID + "'}").builder());
    Bson bsProjection = BsonDocument.parse("{'" + sEmbedField + ".$" + "':1}");
    Document doc = findOne(sCollection, bsFilter, bsProjection, null, oAction, sDatabase);
    if (CommonUtil.ifIsNull(doc))
      return null;
    JsonNode jEmbed = JsonUtil.queryJson(doc.toJson(), sEmbedField);
    if (CommonUtil.ifIsEmpty(jEmbed))
      return null;
    ArrayNode aEmbed = (ArrayNode) jEmbed;
    return aEmbed.get(0);
  }

  /**
   * find one element of embed array with array-filter by record-id
   * 
   * <p>
   * 查询根据记录_id和数组过滤条件匹配的第一个数组元素
   * </p>
   * <p style="color:red">
   * 注：只支持查询最外层就是数组的字段项，内嵌在对象内的数组对象查询报错
   * <p>
   * <B style="color:red">可用：{'_id':'1', 'a':[{},{}...]}</B>
   * </p>
   * <p>
   * <B style="color:red">不可用：{'_id':'2', 'a':{'b':[{}, {}...]}}
   * 使用：findOneArrayElementOnEmbedIDByID</B>
   * </p>
   * </p>
   * 
   * <p>
   * 支持查询嵌入式数组文档
   * </p>
   * 
   * @param sCollection  集合名称
   * @param sID          _id
   * @param sProjection  数组字段
   * @param sArrayFilter 数组过滤条件
   * @param oAction      ActionObject
   * @return {JsonNode}
   * @throws Exception if has some exception
   */
  public static JsonNode findArrayWithOneElementByID(String sCollection, String sID, String sProjection,
                                                     String sArrayFilter, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findArrayWithOneElementByFilter(sCollection, sFilter, sProjection, sArrayFilter, oAction);
  }

  public static JsonNode findArrayWithOneElementByID(String sCollection, String sID, String sProjection,
                                                     String sArrayFilter, ActionObject oAction, String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return findArrayWithOneElementByFilter(sCollection, sFilter, sProjection, sArrayFilter, oAction, sDatabase);
  }

  /**
   * find one element of embed array with array-filter by filter
   * 
   * <p>
   * 查询根据记录_id和数组过滤条件匹配的第一个数组元素
   * </p>
   * <p>
   * 查询根据记录_id和数组过滤条件匹配的第一个数组元素
   * </p>
   * <p style="color:red">
   * 注：只支持查询最外层就是数组的字段项，内嵌在对象内的数组对象查询报错
   * <p>
   * <B style="color:red">可用：{'_id':'1', 'a':[{},{}...]}</B>
   * </p>
   * <p>
   * <B style="color:red">不可用：{'_id':'2', 'a':{'b':[{}, {}...]}}
   * 使用：findOneArrayElementOnEmbedIDByFilter</B>
   * </p>
   * </p>
   * 
   * <p>
   * 支持查询嵌入式数组文档
   * </p>
   * 
   * @param sCollection  集合名称
   * @param sFilter      查询过滤器
   * @param sProjection  数组字段
   * @param sArrayFilter 数组过滤条件
   * @param oAction      ActionObject
   * @return {JsonNode}
   * @throws Exception if has some exception
   */
  public static JsonNode findArrayWithOneElementByFilter(String sCollection, String sFilter, String sProjection,
                                                         String sArrayFilter, ActionObject oAction) throws Exception {
    return findArrayWithOneElementByFilter(sCollection, sFilter, sProjection, sArrayFilter, oAction, "");
  }

  public static JsonNode findArrayWithOneElementByFilter(String sCollection, String sFilter, String sProjection,
                                                         String sArrayFilter, ActionObject oAction, String sDatabase) throws Exception {
    Bson bsProjection = Projection.Builder().inOrExclude("-" + "_id").matchOneElementOfArray(sProjection, sArrayFilter)
        .builder();
    Bson bsFilter = Filter.Builder().parseFilter(sFilter).builder();
    Document doc = findOne(sCollection, bsFilter, bsProjection, null, oAction, sDatabase);
    if (CommonUtil.ifIsNull(doc)) {
      return null;
    }
    return JsonUtil.queryJson(doc.toJson(), sProjection);
  }

  /**
   * 根据查询条件查询集合数组的数据记录，查询满足条件的所有记录，以展示列表格式返回数据
   * 
   * <ul>
   * <li>支持分页查询</li>
   * <li>不支持条件查询</li>
   * <li>不支持排序</li>
   * </ul>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param iSkip       查询跳过记录数
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param oAction     ActionObject
   * @return {String}
   *         <p>
   *         <B style="color:red">'c':'满足查询条件的所有记录数', 'p':'查询的记录数', 's':'跳过的记录数',
   *         't':'实际查询到的记录数', 'o':'查询到的数据的_id', 'r':{'$_id':{'a':'', 'b':''...},
   *         '$_id':{'a':'', 'b':''...}...} </B>
   *         </p>
   * @throws Exception if has some exception
   */
  public static String findArrayRecordsByID(String sCollection, String sID, String sProjection, int iSkip, int iLimit,
                                            ActionObject oAction) throws Exception {
    return findArrayRecordsByID(sCollection, sID, sProjection, iSkip, iLimit, oAction, "");
  }

  public static String findArrayRecordsByID(String sCollection, String sID, String sProjection, int iSkip, int iLimit,
                                            ActionObject oAction, String sDatabase) throws Exception {
    int iAllCount = 0, iActSize = 0;
    String sRecordID = "", sRecordValue = "";
    Bson bsProjection = Projection.Builder().inOrExclude("-" + "_id" + "," + sProjection).builder();
    Bson bsFilter = Filter.Builder().eqID(sID);
    Document doc = findOne(sCollection, bsFilter, bsProjection, null, oAction, sDatabase);
    if (CommonUtil.ifIsEmpty(doc)) {

    } else {
      iAllCount = JsonUtil.queryJson(doc.toJson(), sProjection).size();
      bsProjection = Projection.Builder() //
          .sliceOfArray(sProjection, iSkip, iLimit)//
          // .inOrExclude("-" + "_id" + "," + sProjection)//
          .builder();

      doc = findOne(sCollection, bsFilter, bsProjection, null, oAction);

      if (CommonUtil.ifIsEmpty(doc)) {

      } else {
        JsonNode jRecordValue = JsonUtil.queryJson(doc.toJson(), sProjection);
        sRecordValue = jRecordValue.toString();
        iActSize = jRecordValue.size();
        Iterator<JsonNode> it = jRecordValue.elements();
        while (it.hasNext()) {
          JsonNode jValue = it.next();
          sRecordID += "," + JsonUtil.getJsonStringValue(jValue, "_id");
        }
        if (CommonUtil.ifIsNotEmpty(sRecordID))
          sRecordID = sRecordID.substring(1);
      }
    }
    return "'c':" + iAllCount + "" //
        + ",'p':" + iLimit + "" //
        + ",'s':" + iSkip + "" //
        + ",'t':" + iActSize + "," //
        + "'o':'" + sRecordID + "'"//
        + ",'r':" + sRecordValue + "";
  }

  public static Document findOne(String sCollection, String sFilter, String sProjection, String sSort,
                                 ActionObject oAction) {
    return findOne(sCollection, sFilter, sProjection, sSort, oAction, "");
  }

  public static Document findOne(String sCollection, String sFilter, String sProjection, String sSort,
                                 ActionObject oAction, String sDatabase) {
    Bson bsProjection = Projection.Builder().inOrExclude(sProjection).builder();
    Bson bsSort = Sort.Builder().ascOrDescending(sSort).builder();
    // 容错处理，全表查询(查询条件为空)
    if (CommonUtil.ifIsEmpty(sFilter))
      sFilter = "{}";
    Bson bsFilter = BsonDocument.parse(sFilter);
    return findOne(sCollection, bsFilter, bsProjection, bsSort, oAction, sDatabase);
  }

  public static Document findOne(String sCollection, Bson bsFilter, Bson bsProjection, Bson bsSort,
                                 ActionObject oAction) {
    FindIterable<Document> it = find(sCollection, bsFilter, bsProjection, bsSort, 0, 0, oAction);
    return it.first();
  }

  public static Document findOne(String sCollection, Bson bsFilter, Bson bsProjection, Bson bsSort,
                                 ActionObject oAction, String sDatabase) {
    FindIterable<Document> it = find(sCollection, bsFilter, bsProjection, bsSort, 0, 0, oAction, sDatabase);
    return it.first();
  }

  public static FindIterable<Document> findMany(String sCollection, String sFilter, String sProjection, String sSort,
                                                int iLimit, int iSkip, ActionObject oAction) {
    return findMany(sCollection, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static FindIterable<Document> findMany(String sCollection, String sFilter, String sProjection, String sSort,
                                                int iLimit, int iSkip, ActionObject oAction, String sDatabase) {
    Bson bsProjection = Projection.Builder().inOrExclude(sProjection).builder();
    Bson bsSort = Sort.Builder().ascOrDescending(sSort).builder();
    // 容错处理，全表查询(查询条件为空)
    if (CommonUtil.ifIsEmpty(sFilter))
      sFilter = "{}";
    Bson bsFilter = BsonDocument.parse(sFilter);
    FindIterable<Document> it = find(sCollection, bsFilter, bsProjection, bsSort, iLimit, iSkip, oAction, sDatabase);
    return it;
  }

  public static FindIterable<Document> find(String sCollection, Bson bsFilter, Bson bsProjection, Bson bsSort,
                                            int iLimit, int iSkip, ActionObject oAction) {
    return find(sCollection, bsFilter, bsProjection, bsSort, iLimit, iSkip, oAction, "");
  }

  public static FindIterable<Document> find(String sCollection, Bson bsFilter, Bson bsProjection, Bson bsSort,
                                            int iLimit, int iSkip, ActionObject oAction, String sDatabase) {
    FindOptions options = new FindOptions();
    if (CommonUtil.ifIsNotNull(bsProjection))
      options.projection(bsProjection);
    if (CommonUtil.ifIsNotNull(bsSort))
      options.sort(bsSort);
    if (iLimit > 0)
      options.limit(iLimit);
    if (iSkip > 0)
      options.skip(iSkip);
    FindIterable<Document> it = MongoDB.find(sDatabase, sCollection, bsFilter, options);
    return it;
  }

  /**********************************************************************************************************
   * FIND OPERATION END
   **********************************************************************************************************/

  /**********************************************************************************************************
   * UPDATE OPERATION BEGIN
   **********************************************************************************************************/
  /**
   * updates or inserts one collection it if record not exists
   * <p>
   * 有则更新，无则新增
   * </p>
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新或新增内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult upsertByID(String sCollection, String sID, String sUpdate, ActionObject oAction)
      throws Exception {
    return upsert(sCollection, Filter.Builder().eqID(sID), sUpdate, oAction);
  }

  public static UpdateResult upsertByID(String sCollection, String sID, String sUpdate, ActionObject oAction,
                                        String sDatabase) throws Exception {
    return upsert(sCollection, Filter.Builder().eqID(sID), sUpdate, oAction, sDatabase);
  }

  /**
   * updates collection or inserts it if record not exists
   * <p>
   * 有则更新，无则新增
   * </p>
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sUpdate     更新或新增内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult upsertByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return upsert(sCollection, BsonDocument.parse(sFilter), sUpdate, oAction);
  }

  public static UpdateResult upsertByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction,
                                            String sDatabase) throws Exception {
    return upsert(sCollection, BsonDocument.parse(sFilter), sUpdate, oAction, sDatabase);
  }

  /**
   * updates collection or inserts it if record not exists
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param bsFilter    查询过滤器
   * @param sUpdate     更新或新增内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult upsert(String sCollection, Bson bsFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    Bson bsUpdate = Update.Builder().updateSet(sUpdate).builder();
    return upsert(sCollection, bsFilter, bsUpdate, oAction);
  }

  public static UpdateResult upsert(String sCollection, Bson bsFilter, String sUpdate, ActionObject oAction,
                                    String sDatabase) throws Exception {
    Bson bsUpdate = Update.Builder().updateSet(sUpdate).builder();
    return upsert(sCollection, bsFilter, bsUpdate, oAction, sDatabase);
  }

  /**
   * updates collection or inserts it if record not exists
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param bsFilter    更新条件
   * @param bsUpdate    更新或新增内容
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   */
  public static UpdateResult upsert(String sCollection, Bson bsFilter, Bson bsUpdate, ActionObject oAction) {
    return updateOne(sCollection, bsFilter, bsUpdate, true, oAction);
  }

  public static UpdateResult upsert(String sCollection, Bson bsFilter, Bson bsUpdate, ActionObject oAction,
                                    String sDatabase) {
    return updateOne(sCollection, bsFilter, bsUpdate, true, oAction, sDatabase);
  }

  /**
   * increments fields value
   * <p>
   * 按查询条件查询递增
   * </p>
   * <p>
   * 支持单次操作多个字段项进行操作，字段项及其值存入JsonNode
   * </p>
   * <p>
   * 支持所有Number类型值进行操作
   * </p>
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult increaseByID(String sCollection, String sID, String sIncrease, ActionObject oAction)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return increaseByFilter(sCollection, sFilter, sIncrease, oAction);
  }

  public static UpdateResult increaseByID(String sCollection, String sID, String sIncrease, ActionObject oAction,
                                          String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return increaseByFilter(sCollection, sFilter, sIncrease, oAction, sDatabase);
  }

  /**
   * increments fields value
   * <p>
   * 按查询条件查询递增
   * </p>
   * <p>
   * 支持单次操作多个字段项进行操作，字段项及其值存入JsonNode
   * </p>
   * <p>
   * 支持所有Number类型值进行操作
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     Bson格式 查询条件
   * @param sIncrease   操作的字段项及其值 {'key':value, 'key1':value}
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult increaseByFilter(String sCollection, String sFilter, String sIncrease,
                                              ActionObject oAction) throws Exception {
    return increaseByFilter(sCollection, sFilter, sIncrease, oAction, "");
  }

  public static UpdateResult increaseByFilter(String sCollection, String sFilter, String sIncrease,
                                              ActionObject oAction, String sDatabase) throws Exception {
    if (CommonUtil.ifIsEmptyJson(sIncrease) && CommonUtil.ifIsNotNull(oAction)) {
      oAction.fail(DBEntry_increaseByFilter);
    }
    Bson bsIncrease = Update.Builder().updateInc(sIncrease).builder();
    Bson bsFilter = BsonDocument.parse(sFilter);
    UpdateResult result = updateMany(sCollection, bsFilter, bsIncrease, true, oAction, sDatabase);
    return result;
  }

  /**
   * update one record by id
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, ActionObject oAction)
      throws Exception {
    return saveOneByID(sCollection, sID, sUpdate, "", "", oAction);
  }

  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, ActionObject oAction,
                                         String sDatabase) throws Exception {
    return saveOneByID(sCollection, sID, sUpdate, "", "", oAction, sDatabase);
  }

  /**
   * update one record by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUpdate     更新内容
   * @param sInc        increase内容
   * @param sUnset      unset内容
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, String sInc, String sUnset,
                                         ActionObject oAction) throws Exception {
    return saveOneByID(sCollection, sID, sUpdate, sInc, sUnset, "", "", "", oAction);
  }

  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, String sInc, String sUnset,
                                         ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByID(sCollection, sID, sUpdate, sInc, sUnset, "", "", "", oAction, sDatabase);
  }

  /**
   * add elements to one record of direct array by id
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection    集合名称
   * @param sID            _id
   * @param sDirectAddData 直接数组增加元素内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveDirectArrayOneByID(String sCollection, String sID, String sDirectAddData,
                                                    ActionObject oAction) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", sDirectAddData, "", "", oAction);
  }

  public static UpdateResult saveDirectArrayOneByID(String sCollection, String sID, String sDirectAddData,
                                                    ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", sDirectAddData, "", "", oAction, sDatabase);
  }

  /**
   * add elements to one record of embed array of id
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection   集合名称
   * @param sID           _id
   * @param sEmbedAddData 内嵌数组增加元素内容
   * @param oAction       ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveEmbedArrayOneByID(String sCollection, String sID, String sEmbedAddData,
                                                   ActionObject oAction) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", "", sEmbedAddData, "", oAction);
  }

  public static UpdateResult saveEmbedArrayOneByID(String sCollection, String sID, String sEmbedAddData,
                                                   ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", "", sEmbedAddData, "", oAction, sDatabase);
  }

  /**
   * delete elements to one record of array by id
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sUnsetArray 删除数组元素内容
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveUnsetArrayOneByID(String sCollection, String sID, String sUnsetArray,
                                                   ActionObject oAction) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", "", "", sUnsetArray, oAction);
  }

  public static UpdateResult saveUnsetArrayOneByID(String sCollection, String sID, String sUnsetArray,
                                                   ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByID(sCollection, sID, "", "", "", "", "", sUnsetArray, oAction, sDatabase);
  }

  /**
   * update one record by id
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection    集合名称
   * @param sID            _id
   * @param sUpdate        更新内容
   * @param sInc           increase内容
   * @param sUnset         unset内容
   * @param sDirectAddData 直接数组增加元素内容
   * @param sEmbedAddData  内嵌数组增加元素内容
   * @param sUnsetArray    删除数组元素内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, String sInc, String sUnset,
                                         String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, sDirectAddData, sEmbedAddData, sUnsetArray, false,
        true, oAction);
  }

  public static UpdateResult saveOneByID(String sCollection, String sID, String sUpdate, String sInc, String sUnset,
                                         String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction, String sDatabase)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, sDirectAddData, sEmbedAddData, sUnsetArray, false,
        true, oAction, sDatabase);
  }

  /**
   * update one record by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     更新条件
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return saveOneByFilter(sCollection, sFilter, sUpdate, "", "", oAction);
  }

  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction,
                                             String sDatabase) throws Exception {
    return saveOneByFilter(sCollection, sFilter, sUpdate, "", "", oAction, sDatabase);
  }

  /**
   * update one record by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     过滤器
   * @param sUpdate     更新内容
   * @param sInc        increase内容
   * @param sUnset      unset内容
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                             String sUnset, ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, "", "", "", oAction);
  }

  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                             String sUnset, ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, "", "", "", oAction, sDatabase);
  }

  /**
   * add elements to one record of direct array by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection    集合名称
   * @param sFilter        查询过滤器
   * @param sDirectAddData 直接数组增加元素内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveDirectArrayOneByFilter(String sCollection, String sFilter, String sDirectAddData,
                                                        ActionObject oAction) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", sDirectAddData, "", "", oAction);
  }

  public static UpdateResult saveDirectArrayOneByFilter(String sCollection, String sFilter, String sDirectAddData,
                                                        ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", sDirectAddData, "", "", oAction, sDatabase);
  }

  /**
   * add elements to one record of embed array of filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection   集合名称
   * @param sFilter       查询过滤器
   * @param sEmbedAddData 内嵌数组增加元素内容
   * @param oAction       ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveEmbedArrayOneByFilter(String sCollection, String sFilter, String sEmbedAddData,
                                                       ActionObject oAction) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", "", sEmbedAddData, "", oAction);
  }

  public static UpdateResult saveEmbedArrayOneByFilter(String sCollection, String sFilter, String sEmbedAddData,
                                                       ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", "", sEmbedAddData, "", oAction, sDatabase);
  }

  /**
   * delete elements to one record of array by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sUnsetArray 删除数组元素内容
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveUnsetArrayOneByFilter(String sCollection, String sFilter, String sUnsetArray,
                                                       ActionObject oAction) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", "", "", sUnsetArray, oAction);
  }

  public static UpdateResult saveUnsetArrayOneByFilter(String sCollection, String sFilter, String sUnsetArray,
                                                       ActionObject oAction, String sDatabase) throws Exception {
    return saveOneByFilter(sCollection, sFilter, "", "", "", "", "", sUnsetArray, oAction, sDatabase);
  }

  /**
   * update one record by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection    集合名称
   * @param sFilter        查询过滤器
   * @param sUpdate        更新内容
   * @param sInc           increase内容
   * @param sUnset         unset内容
   * @param sDirectAddData 直接数组增加元素内容
   * @param sEmbedAddData  内嵌数组增加元素内容
   * @param sUnsetArray    删除数组元素内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                             String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction)
      throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, false, true, oAction);
  }

  public static UpdateResult saveOneByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                             String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction,
                                             String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, false, true, oAction, sDatabase);
  }

  /**
   * update one or more records by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * <p>
   * 支持批量
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     更新条件
   * @param sUpdate     更新内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction)
      throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, "", "", oAction);
  }

  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, ActionObject oAction,
                                          String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, "", "", oAction, sDatabase);
  }

  /**
   * update/upsert、increase or unset one or more records by filter
   * <p>
   * 支持更新或upsert
   * </p>
   * <p>
   * 支持批量
   * </p>
   * <p>
   * 支持update/upsert、increase or unset一并处理
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     更新条件
   * @param sUpdate     更新内容
   * @param sInc        递增或递减内容
   * @param sUnset      删除字段项内容
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, true, true, oAction);
  }

  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, true, true, oAction, sDatabase);
  }

  /**
   * add elements to records of direct array by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection    集合名称
   * @param sFilter        查询过滤器
   * @param sDirectAddData 直接数组增加元素内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveDirectArrayByFilter(String sCollection, String sFilter, String sDirectAddData,
                                                     ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", sDirectAddData, "", "", oAction);
  }

  public static UpdateResult saveDirectArrayByFilter(String sCollection, String sFilter, String sDirectAddData,
                                                     ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", sDirectAddData, "", "", oAction, sDatabase);
  }

  /**
   * add elements to record of embed array of filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection   集合名称
   * @param sFilter       查询过滤器
   * @param sEmbedAddData 内嵌数组增加元素内容
   * @param oAction       ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveEmbedArrayByFilter(String sCollection, String sFilter, String sEmbedAddData,
                                                    ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", "", sEmbedAddData, "", oAction);
  }

  public static UpdateResult saveEmbedArrayByFilter(String sCollection, String sFilter, String sEmbedAddData,
                                                    ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", "", sEmbedAddData, "", oAction, sDatabase);
  }

  /**
   * delete elements to record of array by filter
   * <p>
   * 有则更新，无则新增
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sUnsetArray 删除数组元素内容
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveUnsetArrayByFilter(String sCollection, String sFilter, String sUnsetArray,
                                                    ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", "", "", sUnsetArray, oAction);
  }

  public static UpdateResult saveUnsetArrayByFilter(String sCollection, String sFilter, String sUnsetArray,
                                                    ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, "", "", "", "", "", sUnsetArray, oAction, sDatabase);
  }

  /**
   * update/upsert、increase or unset one or more records by filter
   * <p>
   * 支持update/upsert、increase or unset一并处理
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     过滤器
   * @param sUpdate     更新内容
   * @param sInc        递增或递减内容
   * @param sUnset      删除字段项内容
   * @param bMulti      是否支持批量处理
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, boolean bMulti, boolean bUpsert, ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, "", "", "", bMulti, bUpsert, oAction);
  }

  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, boolean bMulti, boolean bUpsert, ActionObject oAction, String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, "", "", "", bMulti, bUpsert, oAction, sDatabase);
  }

  /**
   * update/upsert、increase or unset one or more records by filter
   * 
   * @param sCollection    集合名称
   * @param sFilter        查询过滤器
   * @param sUpdate        更新内容
   * @param sInc           increase内容
   * @param sUnset         unset内容
   * @param sDirectAddData 直接数组元素新增内容
   * @param sEmbedAddData  内嵌数组元素新增内容
   * @param sUnsetArray    数组元素删除内容
   * @param oAction        ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction)
      throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, sDirectAddData, sEmbedAddData, sUnsetArray, true,
        true, oAction);
  }

  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, ActionObject oAction,
                                          String sDatabase) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, sDirectAddData, sEmbedAddData, sUnsetArray, true,
        true, oAction, sDatabase);
  }

  /**
   * modify by filter
   * 
   * <p>
   * 根据条件修改集合数据
   * </p>
   * <ul>
   * <li style="color:red">支持指定是单笔或批量更新</li>
   * <li style="color:red">支持指定有则更新无则新增</li>
   * <li style="color:red">支持更新内容</li>
   * <li style="color:red">支持递增或递减内容</li>
   * <li style="color:red">支持删除字段项内容</li>
   * <li style="color:red">支持对直接数组新增内容</li>
   * <li style="color:red">支持对内嵌数组新增内容</li>
   * <li style="color:red">支持对数组删除内容</li>
   * </ul>
   * 
   * @param sCollection    集合名称
   * @param sFilter        过滤器
   * @param sUpdate        更新内容
   * @param sInc           递增或递减内容
   * @param sUnset         删除字段项内容
   * @param sDirectAddData 直接数组新增内容
   * @param sEmbedAddData  内嵌数组新增内容
   * @param sUnsetArray    数组删除内容
   * @param bMulti         是否支持批量处理
   * @param bUpsert        是否 upsert
   * @param oAction        ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, boolean bMulti, boolean bUpsert,
                                          ActionObject oAction) throws Exception {
    return saveByFilter(sCollection, sFilter, sUpdate, sInc, sUnset, sDirectAddData, sEmbedAddData, sUnsetArray, bMulti,
        bUpsert, oAction, "");
  }

  public static UpdateResult saveByFilter(String sCollection, String sFilter, String sUpdate, String sInc,
                                          String sUnset, String sDirectAddData, String sEmbedAddData, String sUnsetArray, boolean bMulti, boolean bUpsert,
                                          ActionObject oAction, String sDatabase) throws Exception {
    UpdateOptions options = new UpdateOptions();
    options.upsert(bUpsert);
    Update update = Update.Builder();
    if (CommonUtil.ifIsNotEmptyJson(sUpdate))
      update.updateSet(sUpdate);
    if (CommonUtil.ifIsNotEmptyJson(sInc))
      update.updateInc(sInc);
    if (CommonUtil.ifIsNotEmpty(sUnset))
      update.updateUnset(sUnset);
    if (CommonUtil.ifIsNotEmpty(sDirectAddData))
      update.add2DirectArray(sDirectAddData);
    if (CommonUtil.ifIsNotEmpty(sEmbedAddData))
      update.add2EmbedArray(sEmbedAddData);
    if (CommonUtil.ifIsNotEmpty(sUnsetArray))
      update.deleteOfArray(sUnsetArray);
    Bson bsUpdate = update.builder();
    UpdateResult result = null;
    Bson bsFilter = BsonDocument.parse(sFilter);
    if (bMulti) {
      result = MongoDB.updateMany(sDatabase, sCollection, bsFilter, bsUpdate, options);
    } else {
      result = MongoDB.updateOne(sDatabase, sCollection, bsFilter, bsUpdate, options);
    }
    return result;
  }

  /**
   * updates one record by filter
   * 
   * @param sCollection 集合名称
   * @param bsFilter    更新条件
   * @param bsUpdate    更新内容
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   */
  private static UpdateResult updateOne(String sCollection, Bson bsFilter, Bson bsUpdate, boolean bUpsert,
                                        ActionObject oAction) {
    return updateOne(sCollection, bsFilter, bsUpdate, bUpsert, oAction, "");
  }

  private static UpdateResult updateOne(String sCollection, Bson bsFilter, Bson bsUpdate, boolean bUpsert,
                                        ActionObject oAction, String sDatabase) {
    UpdateOptions options = new UpdateOptions();
    options.upsert(bUpsert);
    UpdateResult result = MongoDB.updateOne(sDatabase, sCollection, bsFilter, bsUpdate, options);
    return result;
  }

  /**
   * updates many records by filter
   * 
   * @param sCollection 集合名称
   * @param bsFilter    更新条件
   * @param bsUpdate    更新内容
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   */
  public static UpdateResult updateMany(String sCollection, Bson bsFilter, Bson bsUpdate, boolean bUpsert,
                                        ActionObject oAction) {
    return updateMany(sCollection, bsFilter, bsUpdate, bUpsert, oAction, "");
  }

  public static UpdateResult updateMany(String sCollection, Bson bsFilter, Bson bsUpdate, boolean bUpsert,
                                        ActionObject oAction, String sDatabase) {
    UpdateOptions options = new UpdateOptions();
    options.upsert(bUpsert);
    UpdateResult result = MongoDB.updateMany(sDatabase, sCollection, bsFilter, bsUpdate, options);
    return result;
  }

  /**
   * replace or upsert one record with replace data by id
   * 
   * <p>
   * 根据参数内容更新已存在的记录或插入新记录
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sReplace    更新内容
   *                    <ul>
   *                    <li>若参数内容中不包含_id则插入一条新记录，其_id为系统产生的唯一Id</li>
   *                    <li>若参数内容中包含_id则upsert处理
   *                    <ul>
   *                    <li>若_id对应的记录不存在，则按参数内容插入记录</li>
   *                    <li>若_id对应的记录存在，则替换</li>
   *                    </ul>
   *                    </li>
   *                    </ul>
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult replaceOneByID(String sCollection, String sReplace, ActionObject oAction)
      throws Exception {
    return replaceOneByID(sCollection, JsonUtil.getJson(sReplace), oAction);
  }

  public static UpdateResult replaceOneByID(String sCollection, String sReplace, ActionObject oAction, String sDatabase)
      throws Exception {
    return replaceOneByID(sCollection, JsonUtil.getJson(sReplace), oAction, sDatabase);
  }

  /**
   * replace or upsert one record with replace data by id
   * 
   * <p>
   * 根据参数内容更新已存在的记录或插入新记录
   * </p>
   * 
   * @param sCollection 集合名称
   * @param jReplace    更新内容
   *                    <ul>
   *                    <li>若参数内容中不包含_id则插入一条新记录，其_id为系统产生的唯一Id</li>
   *                    <li>若参数内容中包含_id则upsert处理
   *                    <ul>
   *                    <li>若_id对应的记录不存在，则按参数内容插入记录</li>
   *                    <li>若_id对应的记录存在，则替换</li>
   *                    </ul>
   *                    </li>
   *                    </ul>
   * @param oAction     ActionObject
   * @return UpdateResult
   * @throws Exception if has some exception
   */
  public static UpdateResult replaceOneByID(String sCollection, JsonNode jReplace, ActionObject oAction)
      throws Exception {
    String sID = JsonUtil.getJsonStringValue(jReplace, "_id");
    if (CommonUtil.ifIsEmpty(sID)) {
      sID = CommonUtil.getUUID();
    }
    return replaceOneByID(sCollection, sID, jReplace.toString(), oAction);
  }

  public static UpdateResult replaceOneByID(String sCollection, JsonNode jReplace, ActionObject oAction,
                                            String sDatabase) throws Exception {
    String sID = JsonUtil.getJsonStringValue(jReplace, "_id");
    if (CommonUtil.ifIsEmpty(sID)) {
      sID = CommonUtil.getUUID();
    }
    return replaceOneByID(sCollection, sID, jReplace.toString(), oAction, sDatabase);
  }

  /**
   * replace one record with replace data by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sReplace    替换内容
   * @param oAction     ActionObject
   * @return UpdateResult
   */
  public static UpdateResult replaceOneByID(String sCollection, String sID, String sReplace, ActionObject oAction) {
    String sFilter = Filter.eqIDFilter(sID);
    return replaceOneByFilter(sCollection, sFilter, sReplace, true, oAction);
  }

  public static UpdateResult replaceOneByID(String sCollection, String sID, String sReplace, ActionObject oAction,
                                            String sDatabase) {
    String sFilter = Filter.eqIDFilter(sID);
    return replaceOneByFilter(sCollection, sFilter, sReplace, true, oAction, sDatabase);
  }

  /**
   * replace records with replace data by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sReplace    替换内容
   * @param bUpsert     是否 upsert
   * @param oAction     ActionObject
   * @return UpdateResult
   */
  public static UpdateResult replaceOneByFilter(String sCollection, String sFilter, String sReplace, boolean bUpsert,
                                                ActionObject oAction) {
    return replaceOneByFilter(sCollection, sFilter, sReplace, bUpsert, oAction, "");
  }

  public static UpdateResult replaceOneByFilter(String sCollection, String sFilter, String sReplace, boolean bUpsert,
                                                ActionObject oAction, String sDatabase) {
    Bson bsFilter = BsonDocument.parse(sFilter);
    UpdateOptions options = new UpdateOptions();
    options.upsert(bUpsert);
    UpdateResult result = MongoDB.replaceOne(sDatabase, sCollection, bsFilter, sReplace, options);
    return result;
  }

  /**********************************************************************************************************
   * UPDATE OPERATION END
   **********************************************************************************************************/

  /**********************************************************************************************************
   * DELETE OPERATION BEGIN
   **********************************************************************************************************/
  /**
   * deletes fields
   * <p>
   * 按_id查询删除
   * </p>
   * <p>
   * 支持单次删除多个字段项，多个字段以逗号(,)分隔
   * </p>
   * <p>
   * 支持单次删除多层级的字段项
   * </p>
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sFieldName  指定删除的字段项
   * @param oAction     ActionObject
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult deleteFieldsByID(String sCollection, String sID, String sFieldName, ActionObject oAction)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return deleteFieldsByFilter(sCollection, sFilter, sFieldName, oAction);
  }

  public static UpdateResult deleteFieldsByID(String sCollection, String sID, String sFieldName, ActionObject oAction,
                                              String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return deleteFieldsByFilter(sCollection, sFilter, sFieldName, oAction, sDatabase);
  }

  /**
   * deletes fields
   * <p>
   * 按查询条件查询删除
   * </p>
   * <p>
   * 支持单次删除多个字段项，多个字段以逗号(,)分隔
   * </p>
   * <p>
   * 支持单次删除多层级的字段项
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询条件
   * @param sFieldName  指定删除的字段项
   * @param oAction     ActionObject
   * @since 2.0
   * @return {UpdateResult}
   * @throws Exception if has some exception
   */
  public static UpdateResult deleteFieldsByFilter(String sCollection, String sFilter, String sFieldName,
                                                  ActionObject oAction) throws Exception {
    if (CommonUtil.ifIsEmptyJson(sFieldName) && CommonUtil.ifIsNotNull(oAction)) {
      oAction.fail(DBEntry_deleteFieldsByFilter);
    }
    Bson bsUnset = Update.Builder().updateUnset(sFieldName).builder();
    Bson bsFilter = BsonDocument.parse(sFilter);
    UpdateResult result = updateMany(sCollection, bsFilter, bsUnset, false, oAction);
    return result;
  }

  public static UpdateResult deleteFieldsByFilter(String sCollection, String sFilter, String sFieldName,
                                                  ActionObject oAction, String sDatabase) throws Exception {
    if (CommonUtil.ifIsEmptyJson(sFieldName) && CommonUtil.ifIsNotNull(oAction)) {
      oAction.fail(DBEntry_deleteFieldsByFilter);
    }
    Bson bsUnset = Update.Builder().updateUnset(sFieldName).builder();
    Bson bsFilter = BsonDocument.parse(sFilter);
    UpdateResult result = updateMany(sCollection, bsFilter, bsUnset, false, oAction, sDatabase);
    return result;
  }

  /**
   * deletes record by id
   * <p>
   * 根据传入的id判断是deleteone 或 deletemany
   * </p>
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sID         id
   * @param oAction     ActionObject
   * @throws Exception if has some exception
   */
  public static void delete(String sCollection, String sID, ActionObject oAction) throws Exception {
    if (CommonUtil.ifIsEmpty(sID) && CommonUtil.ifIsNotNull(oAction))
      oAction.fail(DBEntry_delete);
    String[] aIDs = StringUtils.split(sID, ",");
    if (1 == aIDs.length) {
      deleteOneByID(sCollection, sID, oAction);
    } else {
      deleteManyByID(sCollection, sID, oAction);
    }
  }

  public static void delete(String sCollection, String sID, ActionObject oAction, String sDatabase) throws Exception {
    if (CommonUtil.ifIsEmpty(sID) && CommonUtil.ifIsNotNull(oAction))
      oAction.fail(DBEntry_delete);
    String[] aIDs = StringUtils.split(sID, ",");
    if (1 == aIDs.length) {
      deleteOneByID(sCollection, sID, oAction, sDatabase);
    } else {
      deleteManyByID(sCollection, sID, oAction, sDatabase);
    }
  }

  /**
   * delete one record by id
   * 
   * @param sCollection 集合名称
   * @param sID         id
   * @param oAction     ActionObject
   * @since 2.0
   */
  public static void deleteOneByID(String sCollection, String sID, ActionObject oAction) {
    deleteOne(sCollection, Filter.Builder().eqID(sID), oAction);
  }

  public static void deleteOneByID(String sCollection, String sID, ActionObject oAction, String sDatabase) {
    deleteOne(sCollection, Filter.Builder().eqID(sID), oAction, sDatabase);
  }

  /**
   * delete many records by id
   * 
   * @param sCollection 集合名称
   * @param sID         id
   * @param oAction     ActionObject
   * @since 2.0
   */
  public static void deleteManyByID(String sCollection, String sID, ActionObject oAction) {
    deleteMany(sCollection, Filter.Builder().inID(sID), oAction);
  }

  public static void deleteManyByID(String sCollection, String sID, ActionObject oAction, String sDatabase) {
    deleteMany(sCollection, Filter.Builder().inID(sID), oAction, sDatabase);
  }

  /**
   * deletes record by filter
   * <p style="color:red">
   * 兼容1.x版本
   * </p>
   * 
   * @param sCollection 集合名称
   * @param sFilter     删除条件
   * @param oAction     ActionObject
   * @throws Exception if has some exception
   */
  public static void deleteByFilter(String sCollection, String sFilter, ActionObject oAction) throws Exception {
    if (CommonUtil.ifIsEmpty(sFilter) && CommonUtil.ifIsNotNull(oAction))
      oAction.fail(DBEntry_deleteByFilter);
    Bson bsFilter = BsonDocument.parse(sFilter);
    deleteMany(sCollection, bsFilter, oAction);
  }

  public static void deleteByFilter(String sCollection, String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    if (CommonUtil.ifIsEmpty(sFilter) && CommonUtil.ifIsNotNull(oAction))
      oAction.fail(DBEntry_deleteByFilter);
    Bson bsFilter = BsonDocument.parse(sFilter);
    deleteMany(sCollection, bsFilter, oAction, sDatabase);
  }

  /**
   * delete one record by filter
   * 
   * @param sCollection 集合名称
   * @param bsFilter    删除条件
   * @param oAction     ActionObject
   * @since 2.0
   */
  public static void deleteOne(String sCollection, Bson bsFilter, ActionObject oAction) {
    deleteOne(sCollection, bsFilter, oAction, "");
  }

  public static void deleteOne(String sCollection, Bson bsFilter, ActionObject oAction, String sDatabase) {
    DeleteOptions options = new DeleteOptions();
    MongoDB.deleteOne(sDatabase, sCollection, bsFilter, options);
  }

  /**
   * delete many records by filter
   * 
   * @param sCollection 集合名称
   * @param bsFilter    删除条件
   * @param oAction     ActionObject
   * @since 2.0
   */
  public static void deleteMany(String sCollection, Bson bsFilter, ActionObject oAction) {
    deleteMany(sCollection, bsFilter, oAction, "");
  }

  public static void deleteMany(String sCollection, Bson bsFilter, ActionObject oAction, String sDatabase) {
    DeleteOptions options = new DeleteOptions();
    MongoDB.deleteMany(sDatabase, sCollection, bsFilter, options);
  }

  /**********************************************************************************************************
   * DELETE OPERATION END
   **********************************************************************************************************/
  /**
   * judge if record exists or not by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param oAction     ActionObject
   * @return boolean
   * @throws Exception if has some exception
   */
  public static boolean ifFindObjectExistByID(String sCollection, String sID, ActionObject oAction) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return ifFindObjectExistByFilter(sCollection, sFilter, oAction);
  }

  public static boolean ifFindObjectExistByID(String sCollection, String sID, ActionObject oAction, String sDatabase)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return ifFindObjectExistByFilter(sCollection, sFilter, oAction, sDatabase);
  }

  /**
   * judge if record exists or not by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param oAction     ActionObject
   * @return boolean
   * @throws Exception if has some exception
   */
  public static boolean ifFindObjectExistByFilter(String sCollection, String sFilter, ActionObject oAction)
      throws Exception {
    long lCount = findCount(sCollection, sFilter, oAction);
    return (lCount > 0);
  }

  public static boolean ifFindObjectExistByFilter(String sCollection, String sFilter, ActionObject oAction,
                                                  String sDatabase) throws Exception {
    long lCount = findCount(sCollection, sFilter, oAction, sDatabase);
    return (lCount > 0);
  }

  /**
   * judge if record field exists or not by id
   * 
   * @param sCollection 集合名称
   * @param sID         _id
   * @param sField      查询的字段项
   * @param oAction     ActionObject
   * @return boolean
   * @throws Exception if has some exception
   */
  public static boolean ifFindObjectHasKeyByID(String sCollection, String sID, String sField, ActionObject oAction)
      throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return ifFindObjectHasKeyByFilter(sCollection, sFilter, sField, oAction);
  }

  public static boolean ifFindObjectHasKeyByID(String sCollection, String sID, String sField, ActionObject oAction,
                                               String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    return ifFindObjectHasKeyByFilter(sCollection, sFilter, sField, oAction, sDatabase);
  }

  /**
   * judge if record field exists or not by filter
   * 
   * @param sCollection 集合名称
   * @param sFilter     查询过滤器
   * @param sField      查询字段项
   * @param oAction     ActionObject
   * @return boolean
   * @throws Exception if has some exception
   */
  public static boolean ifFindObjectHasKeyByFilter(String sCollection, String sFilter, String sField,
                                                   ActionObject oAction) throws Exception {
    sFilter = "{" + sFilter + ",'" + sField + "' : { $exists : true }}";
    long lCount = findCount(sCollection, sFilter, oAction);
    return (lCount > 0);
  }

  public static boolean ifFindObjectHasKeyByFilter(String sCollection, String sFilter, String sField,
                                                   ActionObject oAction, String sDatabase) throws Exception {
    sFilter = "{" + sFilter + ",'" + sField + "' : { $exists : true }}";
    long lCount = findCount(sCollection, sFilter, oAction, sDatabase);
    return (lCount > 0);
  }

  public static JsonNode aggregateJson(String sCollection, List<Bson> aggregation) throws Exception {
    return aggregateJson(sCollection, aggregation, false, "");
  }

  public static JsonNode aggregateJson(String sCollection, List<Bson> aggregation, boolean bArray) throws Exception {
    return aggregateJson(sCollection, aggregation, bArray, "");
  }
  
  public static JsonNode aggregateJson(String sCollection, List<Bson> aggregation, boolean bArray, String sDatabase) throws Exception {
    JsonNode jReturn = null;
    Iterator<Document> it = aggregation(sCollection, aggregation, sDatabase).iterator();
    if (bArray) {
      jReturn = JsonUtil.getJson("[]");
      while (it.hasNext()) {
        Document document = it.next();
        JsonNode jDocument = JsonUtil.getJson(document.toJson());
        ((ArrayNode)jReturn).add(jDocument);
      }
    } else {
      jReturn = JsonUtil.getJson("{}");
      while (it.hasNext()) {
        Document document = it.next();
        JsonNode jDocument = JsonUtil.getJson(document.toJson());
        String sID = JsonUtil.getJsonStringValue(jDocument, "_id");
        JsonUtil.setValue(jReturn, sID, jDocument);
      }
    }
    return jReturn;
  }
  
  public static int aggregateCount(String sCollection, List<Bson> aggregation) throws Exception {
    return aggregateCount(sCollection, aggregation, "");
  }
  
  public static int aggregateCount(String sCollection, List<Bson> aggregation, String sDatabase) throws Exception {
    int i = 0;
    String sCountBson = "{'$count':'count'}";
    Bson bsonCount = BsonDocument.parse(sCountBson);
    aggregation.add(bsonCount);
    Iterator<Document> it = aggregation(sCollection, aggregation, sDatabase).iterator();
    while (it.hasNext()) {
      Document document = it.next();
      JsonNode jDocument = JsonUtil.getJson(document.toJson());
      i = JsonUtil.getJsonIntValue(jDocument, "count");
    }
    return i;
  }

  public static AggregateIterable<Document> aggregation(String sCollection, List<Bson> stages) throws Exception {
    return aggregation(sCollection, stages, "");
  }

  private static AggregateIterable<Document> aggregation(String sCollection, List<Bson> stages, String sDatabase)
      throws Exception {
    return MongoDB.aggregate(sDatabase, sCollection, stages);
  }

  /**********************************************************************************************************
   * GridFS OPERATION BEGIN
   **********************************************************************************************************/
  /**
   * find one GridFS record by id
   * 
   * @param sID     _id
   * @param oAction ActionObject
   * @return GridFSFile
   * @throws Exception if has some exception
   */
  public static GridFSFile findOneGridFSByID(String sID, ActionObject oAction) throws Exception {
    return findOneGridFSByID("", sID, "", oAction);
  }

  public static GridFSFile findOneGridFSByID(String sID, ActionObject oAction, String sDatabase) throws Exception {
    return findOneGridFSByID("", sID, "", oAction, sDatabase);
  }

  /**
   * find one GridFS record by id
   * 
   * @param sBucket     存储bucket名
   * @param sID         _id
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param oAction     ActionObject
   * @return GridFSFile
   * @throws Exception if has some exception
   */
  public static GridFSFile findOneGridFSByID(String sBucket, String sID, String sProjection, ActionObject oAction)
      throws Exception {
    GridFSFile file = findOneGridFSByID(sBucket, sID, sProjection, oAction, "");
    return file;
  }

  public static GridFSFile findOneGridFSByID(String sBucket, String sID, String sProjection, ActionObject oAction,
                                             String sDatabase) throws Exception {
    String sFilter = Filter.eqIDFilter(sID);
    GridFSFindIterable iterator = findGridFS(sBucket, sFilter, sProjection, "", 0, 0, oAction, sDatabase);
    return iterator.first();
  }

  /**
   * find many GridFS records by filter
   * 
   * @param sFilter 查询过滤器
   * @param oAction AcitonObject
   * @return List
   * @throws Exception if has some exception
   */
  public static List<GridFSFile> findGridFSByFilter(String sFilter, ActionObject oAction) throws Exception {
    return findGridFSByFilter("", sFilter, "", "", 0, 0, oAction);
  }

  public static List<GridFSFile> findGridFSByFilter(String sFilter, ActionObject oAction, String sDatabase)
      throws Exception {
    return findGridFSByFilter("", sFilter, "", "", 0, 0, oAction, sDatabase);
  }

  /**
   * find many GridFS records by filter
   * 
   * @param sBucket     存储bucket名
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序字段项 <B> 参考：{@link Sort} </B>
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip       查询跳过记录数
   * @param oAction     ActionObject
   * @return List
   * @throws Exception if has some exception
   */
  public static List<GridFSFile> findGridFSByFilter(String sBucket, String sFilter, String sProjection, String sSort,
                                                    int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findGridFSByFilter(sBucket, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static List<GridFSFile> findGridFSByFilter(String sBucket, String sFilter, String sProjection, String sSort,
                                                    int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    List<GridFSFile> list = new ArrayList<GridFSFile>();
    GridFSFindIterable iterator = findGridFS(sBucket, sFilter, sProjection, sSort, iLimit, iSkip, oAction, sDatabase);
    Iterator<GridFSFile> it = iterator.iterator();
    while (it.hasNext()) {
      list.add(it.next());
    }
    return list;
  }

  /**
   * find many GridFS records by filter
   * 
   * @param sBucket     存储bucket名
   * @param sFilter     查询过滤器
   * @param sProjection 投射的字段项 <B> 参考：{@link Projection} </B>
   * @param sSort       排序字段项 <B> 参考：{@link Sort} </B>
   * @param iLimit      查询返回记录数；实际返回记录数=min(iLimit, 实际查询得到记录数)
   * @param iSkip       查询跳过记录数
   * @param oAction     ActionObject
   * @return GridFSFindIterable
   * @throws Exception if has some exception
   */
  public static GridFSFindIterable findGridFS(String sBucket, String sFilter, String sProjection, String sSort,
                                              int iLimit, int iSkip, ActionObject oAction) throws Exception {
    return findGridFS(sBucket, sFilter, sProjection, sSort, iLimit, iSkip, oAction, "");
  }

  public static GridFSFindIterable findGridFS(String sBucket, String sFilter, String sProjection, String sSort,
                                              int iLimit, int iSkip, ActionObject oAction, String sDatabase) throws Exception {
    FindOptions options = new FindOptions();
    if (CommonUtil.ifIsNotEmpty(sProjection))
      options.projection(Projection.Builder().inOrExclude(sProjection).builder());
    if (CommonUtil.ifIsNotEmpty(sSort))
      options.sort(Sort.Builder().ascOrDescending(sSort).builder());
    if (iLimit > 0)
      options.limit(iLimit);
    if (iSkip > 0)
      options.skip(iSkip);
    Bson bsFilter = BsonDocument.parse(sFilter);
    return MongoDB.findGridFS(sDatabase, sBucket, bsFilter, options);
  }

  /**
   * save GridFS
   * 
   * @param sID       _id
   * @param sFileName 文件名
   * @param bContent  文件内容
   * @param oAction   ActionObject
   * @throws Exception if has some exception
   */
  public static void saveGridFS(String sID, String sFileName, byte[] bContent, ActionObject oAction) throws Exception {
    saveGridFS("", sID, sFileName, bContent, oAction);
  }

  public static void saveGridFS(String sID, String sFileName, byte[] bContent, ActionObject oAction, String sDatabase)
      throws Exception {
    saveGridFS("", sID, sFileName, bContent, oAction, sDatabase);
  }

  /**
   * save GridFS
   * 
   * @param sBucket   存储bucket名称
   * @param sID       _id
   * @param sFileName 文件名
   * @param bContent  文件内容
   * @param oAction   ActionObject
   * @throws Exception if has some exception
   */
  public static void saveGridFS(String sBucket, String sID, String sFileName, byte[] bContent, ActionObject oAction)
      throws Exception {
    saveGridFS(sBucket, sID, sFileName, bContent, oAction, "");
  }

  public static void saveGridFS(String sBucket, String sID, String sFileName, byte[] bContent, ActionObject oAction,
                                String sDatabase) throws Exception {
    GridFSUploadOptions options = new GridFSUploadOptions();
    InputStream insSource = new ByteArrayInputStream(bContent);
    MongoDB.uploadGridFS(sDatabase, sBucket, sID, sFileName, insSource, options);
  }

  /**
   * delete gridfs
   * 
   * @param sBucket   存储bucket名称
   * @param sID       _id
   * @param oAction   ActionObject
   * @param sDatabase if has some exception
   */
  public static void deleteGridFS(String sBucket, String sID, ActionObject oAction, String sDatabase) {
    MongoDB.deleteGridFS(sDatabase, sBucket, sID);
  }

  /**
   * download GridFS file to stream by id
   * 
   * @param sID         _id
   * @param destination 输出流
   */
  public static void downloadGridFS2StreamByID(String sID, OutputStream destination) {
    downloadGridFS2StreamByID("", sID, destination);
  }

  public static void downloadGridFS2StreamByID(String sID, OutputStream destination, String sDatabase) {
    downloadGridFS2StreamByID("", sID, destination, sDatabase);
  }

  /**
   * download GridFS file to stream by id
   * 
   * @param sBucket     存储bucket名称
   * @param sID         _id
   * @param destination 输出流
   */
  public static void downloadGridFS2StreamByID(String sBucket, String sID, OutputStream destination) {
    downloadGridFS2StreamByID(sBucket, sID, destination, "");
  }

  public static void downloadGridFS2StreamByID(String sBucket, String sID, OutputStream destination, String sDatabase) {
    MongoDB.downloadGridFS(sDatabase, sBucket, sID, "", destination, null);
  }

  /**
   * download GridFS file to stream by filename
   * 
   * @param sFileName   文件名称
   * @param destination 输出流
   */
  public static void downloadGridFS2StreamByFileName(String sFileName, OutputStream destination) {
    downloadGridFS2StreamByFileName("", sFileName, destination);
  }

  public static void downloadGridFS2StreamByFileName(String sFileName, OutputStream destination, String sDatabase) {
    downloadGridFS2StreamByFileName("", sFileName, destination, sDatabase);
  }

  /**
   * download GridFS file to stream by filename
   * 
   * @param sBucket     存储bucket名称
   * @param sFileName   文件名称
   * @param destination 输出流
   */
  public static void downloadGridFS2StreamByFileName(String sBucket, String sFileName, OutputStream destination) {
    downloadGridFS2StreamByFileName(sBucket, sFileName, destination, "");
  }

  public static void downloadGridFS2StreamByFileName(String sBucket, String sFileName, OutputStream destination,
                                                     String sDatabase) {
    downloadGridFS2StreamByFileName(sBucket, sFileName, destination, null, sDatabase);
  }

  /**
   * download GridFS file to stream by filename
   * 
   * @param sBucket     存储bucket名称
   * @param sFileName   文件名称
   * @param destination 输出流
   * @param options     GridFS下载配置信息
   */
  public static void downloadGridFS2StreamByFileName(String sBucket, String sFileName, OutputStream destination,
                                                     GridFSDownloadOptions options) {
    downloadGridFS2StreamByFileName(sBucket, sFileName, destination, options, "");
  }

  public static void downloadGridFS2StreamByFileName(String sBucket, String sFileName, OutputStream destination,
                                                     GridFSDownloadOptions options, String sDatabase) {
    MongoDB.downloadGridFS(sDatabase, sBucket, "", sFileName, destination, options);
  }

  /**********************************************************************************************************
   * GridFS OPERATION END
   **********************************************************************************************************/
  public static MongoCursor<String> distinct(String sCollection, String sFilter, String sField, ActionObject oAction) {
    return distinct(sCollection, sFilter, sField, oAction, "");
  }

  public static MongoCursor<String> distinct(String sCollection, String sFilter, String sField, ActionObject oAction,
                                             String sDatabase) {
    return MongoDB.distinct(sDatabase, sCollection, sFilter, sField);
  }

  /**********************************************************************************************************
   * PRIVATE OPERATION BEGIN
   **********************************************************************************************************/

  /**********************************************************************************************************
   * PRIVATE OPERATION END
   **********************************************************************************************************/

}
