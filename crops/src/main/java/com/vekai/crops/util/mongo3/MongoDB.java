package com.vekai.crops.util.mongo3;

import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSDownloadOptions;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.Map.Entry;

public class MongoDB {
  public static Logger logger = LoggerFactory.getLogger(MongoDB.class);

  public static HashMap<String, MongoClient> clients = null;
  public static HashMap<String, MongoDatabase> databases = null;
  private static HashMap<String, GridFSBucket> buckets = null;

  public static String DB_NAME = "db" //
          , DB_ADDRESS = "address" //
          , DB_IP = "dbip" //
          , DB_PORT = "dbport" //
          , DB_USER = "dbuser" //
          , DB_PASSWORD = "dbpwd" //
          , DB_AUTH = "authdb" //
          ;

  public static final String OPTION_VALIDATION = "validation" // bypassDocumentValidation
          , OPTION_ORDER = "order" // ordered
          , OPTION_LIMIT = "limit" // limit
          , OPTION_SKIP = "skip" // skip
          ;

  public static final String DEFAULT_DATABASE = "default";

  public static void init(String mongoConfig) throws Exception {
    /*ClassPathResource resource = new ClassPathResource("mongodb.json");
    // 获取文件
    File file = resource.getFile();
    JsonNode jMongoConfig = JsonUtil.getJson(file);*/
    // 获取文件
    InputStream is = MongoDB.class.getClassLoader().getResourceAsStream(mongoConfig);
    JsonNode jMongoConfig = JsonUtil.mapper.readTree(is);
    // System.out.println("jMongoConfig=" + jMongoConfig.toString());
    Iterator<Entry<String, JsonNode>> it = jMongoConfig.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      String sKey = entry.getKey();
      JsonNode jValue = entry.getValue();
      JsonNode jAddress = JsonUtil.queryJson(jValue, DB_ADDRESS);
      ArrayList<ServerAddress> addressList = new ArrayList<ServerAddress>();
      Iterator<JsonNode> addressNodes = jAddress.elements();
      while (addressNodes.hasNext()) {
        JsonNode addressNode = addressNodes.next();
        String dbip = addressNode.get(DB_IP).asText();
        int dbport = addressNode.get(DB_PORT).asInt();
        addressList.add(new ServerAddress(dbip, dbport));
      }
      String sDBUser = JsonUtil.getJsonStringValue(jValue, DB_USER);
      String sDBPassword = JsonUtil.getJsonStringValue(jValue, DB_PASSWORD);
      String sDBAuth = JsonUtil.getJsonStringValue(jValue, DB_AUTH);
      String sDBName = JsonUtil.getJsonStringValue(jValue, DB_NAME);
      logger.info("mongo init : [" + sDBName + "] -> " + addressList);
      MongoClient client;
      if (CommonUtil.ifIsEmpty(sDBUser)) {
        client = new MongoClient(addressList);
      } else {
        MongoCredential credential = MongoCredential.createCredential(sDBUser, sDBAuth, sDBPassword.toCharArray());

        Builder builder = new MongoClientOptions.Builder();
        builder.connectionsPerHost(100) // 最大连接数量(默认100)
                .threadsAllowedToBlockForConnectionMultiplier(10) // 每个连接允许的排队队列数量(默认5)
                .maxWaitTime(2 * 60 * 1000) // 被堵塞线程从连接池中获取连接的最长等待时间(默认2分钟)
                .connectTimeout(5 * 1000) // “建立”socket连接的超时时间(默认10秒)
                .socketTimeout(0) // socket连接“使用”超时时间(默认0，无限制<infinite>)
        // .socketKeepAlive(true) //
        // .socketTimeout(5 * 1000) //
        ;
        MongoClientOptions options = builder.build();
        client = new MongoClient(addressList, Arrays.asList(credential), options);
      }
      MongoDatabase database = client.getDatabase(sDBName);
      GridFSBucket gridFSBucket = GridFSBuckets.create(database);

      if (CommonUtil.ifIsNull(clients))
        clients = new HashMap<String, MongoClient>();
      if (CommonUtil.ifIsNull(databases))
        databases = new HashMap<String, MongoDatabase>();
      if (CommonUtil.ifIsNull(buckets))
        buckets = new HashMap<String, GridFSBucket>();
      clients.put(sKey, client);
      databases.put(sKey, database);
      buckets.put(sKey, gridFSBucket);
    }
    logger.info("load mongoDB success");
  }

  // 获取默认数据库
  public static MongoDatabase getDatabase() {
    return getDatabase(DEFAULT_DATABASE);
  }

  // 获取指定名称的数据库
  public static MongoDatabase getDatabase(String sDatabaseName) {
    return getDatabase(DEFAULT_DATABASE, sDatabaseName);
  }

  // 获取指定实例名称和数据库名称的数据库
  public static MongoDatabase getDatabase(String sClientKey, String sDatabaseName) {
    if (CommonUtil.ifIsEmpty(sDatabaseName))
      sDatabaseName = DEFAULT_DATABASE;
    MongoDatabase database = databases.get(sDatabaseName);

//    DefaultClusterFactory dcf = new DefaultClusterFactory();
//    dcf.create(settings, serverSettings, connectionPoolSettings, streamFactory, heartbeatStreamFactory, credentialList, clusterListener, connectionPoolListener, connectionListener);
//
//    SocketSettings ss = SocketSettings.builder().build();
//    SslSettings ssl = SslSettings.builder().build();
//    SocketStreamFactory ssf = new SocketStreamFactory(ss, ssl);
//    Stream stream = ssf.create(new ServerAddress("101.132.195.210", 38128));
//
//    MongoClient client1 = clients.get(DEFAULT_DATABASE);
//    DB db = client1.getDB("instal");
//    CommandResult stats = db.command("serverStatus");

    if (database == null) {
      if (CommonUtil.ifIsEmpty(sClientKey))
        sClientKey = DEFAULT_DATABASE;
      MongoClient client = clients.get(sClientKey);
      database = client.getDatabase(sDatabaseName);
    }
    return database;
  }

  // 获取默认数据库的集合对象
  public static MongoCollection<Document> getCollection(String sCollection) {
    return getCollection(DEFAULT_DATABASE, sCollection);
  }

  // 获取默认数据库实例、指定数据库的集合对象
  public static MongoCollection<Document> getCollection(String sDatabaseName, String sCollection) {
    return getCollection(DEFAULT_DATABASE, sDatabaseName, sCollection);
  }

  // 获取指定数据库实例、指定数据库的集合对象
  public static MongoCollection<Document> getCollection(String sClientName, String sDatabaseName, String sCollection) {
    MongoDatabase database = getDatabase(sClientName, sDatabaseName);
    return database.getCollection(sCollection);
  }

  // 获取默认数据库实例、默认数据库的GridFS对象
  public static GridFSBucket getGridFSBucket(String sBucket) {
    return getGridFSBucket(DEFAULT_DATABASE, sBucket);
  }

  // 获取默认数据库实例、指定数据库的GridFS对象
  public static GridFSBucket getGridFSBucket(String sDatabaseName, String sBucket) {
    return getGridFSBucket(DEFAULT_DATABASE, sDatabaseName, sBucket);
  }

  // 获取指定数据库实例、指定数据库的GridFS对象
  public static GridFSBucket getGridFSBucket(String sClientName, String sDatabaseName, String sBucket) {
    if (CommonUtil.ifIsEmpty(sDatabaseName))
      sDatabaseName = DEFAULT_DATABASE;
    if (CommonUtil.ifIsEmpty(sBucket)) {
      GridFSBucket bucket = buckets.get(sDatabaseName);
      return bucket;
    } else {
      MongoDatabase database = getDatabase(sClientName, sDatabaseName);
      return GridFSBuckets.create(database, sBucket);
    }
  }

  /**
   * 新增文档 - 单笔
   * <p>
   * 支持设置新增参数
   * </p>
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param sInsert     String类型的Json/Document格式的记录信息 <var style="color:red">必输</var>
   * @param options     新增操作的相关参数信息
   * @since 1.0
   */
  public static void insertOne(String sCollection, String sInsert, InsertOneOptions options) {
    insertOne(DEFAULT_DATABASE, sCollection, sInsert, options);
  }

  public static void insertOne(String sDatabase, String sCollection, String sInsert, InsertOneOptions options) {
    Document doc = Document.parse(sInsert);
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      collection.insertOne(doc);
    } else {
      collection.insertOne(doc, options);
    }
  }

  /**
   * 新增文档 - 批量
   * <p>
   * 支持设置新增参数
   * </p>
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param sInsert     String类型的Json/Document格式的记录信息 {'id':{...}, 'id':{...}}
   *                    <var style="color:red">必输</var>
   * @param options     新增操作的相关参数信息
   * @throws Exception jsonutil exception
   * @since 1.0
   */
  public static void insertMany(String sCollection, String sInsert, InsertManyOptions options) throws Exception {
    insertMany(DEFAULT_DATABASE, sCollection, sInsert, options);
  }

  public static void insertMany(String sDatabase, String sCollection, String sInsert, InsertManyOptions options) throws Exception {
    List<? extends Document> list = getListDocument(sInsert);
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      collection.insertMany(list);
    } else {
      collection.insertMany(list, options);
    }
  }

  /**
   * 查询文档数
   * <p>
   * 支持设置查询条件
   * </p>
   * <p>
   * 支持设置记录数参数
   * </p>
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param sFilter     查询条件 <var style="color:red">必输</var>
   * @param options     查询记录数的相关参数信息
   * @return {long}
   * @since 1.0
   */
  public static long count(String sCollection, String sFilter, CountOptions options) {
    return count(DEFAULT_DATABASE, sCollection, sFilter, options);
  }

  public static long count(String sDatabase, String sCollection, String sFilter, CountOptions options) {
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsEmpty(sFilter)) {
      return collection.count();
    } else {
      Bson bsFilter = BsonDocument.parse(sFilter);
      if (CommonUtil.ifIsNull(options)) {
        return collection.count(bsFilter);
      } else {
        return collection.count(bsFilter, options);
      }
    }
  }

  public static MongoCursor<String> distinct(String sCollection, String sFilter, String sField) {
    return distinct(DEFAULT_DATABASE, sCollection, sFilter, sField);
  }

  public static MongoCursor<String> distinct(String sDatabase, String sCollection, String sFilter, String sField) {
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    DistinctIterable<String> iterable = null;
    if (CommonUtil.ifIsEmpty(sFilter)) {
      iterable = collection.distinct(sField, String.class);
    } else {
      Bson bsFilter = BsonDocument.parse(sFilter);
      iterable = collection.distinct(sField, bsFilter, String.class);
    }
    return iterable.iterator();
  }

  /**
   * 查询文档
   * <p>
   * 通过FindOperation设置查询参数
   * </p>
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    查询条件 <var style="color:red">必输</var>
   * @param options     FindOperation 查询参数
   * @return {FindIterable}
   * @since 1.0
   */
  public static FindIterable<Document> find(String sCollection, Bson bsFilter, FindOptions options) {
    return find(DEFAULT_DATABASE, sCollection, bsFilter, options);
  }

  public static FindIterable<Document> find(String sDatabase, String sCollection, Bson bsFilter, FindOptions options) {
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    FindIterable<Document> iterator = null;
    iterator = collection.find(CommonUtil.ifIsNull(bsFilter) ? new BsonDocument() : bsFilter);
    if (CommonUtil.ifIsNotNull(options)) {
      if (CommonUtil.ifIsNotNull(options.getProjection()))
        iterator.projection(options.getProjection());
      if (options.getSkip() > 0)
        iterator.skip(options.getSkip());
      if (options.getLimit() > 0)
        iterator.limit(options.getLimit());
      if (CommonUtil.ifIsNotNull(options.getSort()))
        iterator.sort(options.getSort());
    }
    return iterator;
  }

  /**
   * 更新文档 - 单笔
   *
   * @param sCollection 集合名称
   * @param bsFilter    更新条件
   * @param bsUpdate    更新内容
   * @param options     更新的参数信息
   * @return {UpdateResult}
   * @since 1.0
   */
  public static UpdateResult updateOne(String sCollection, Bson bsFilter, Bson bsUpdate, UpdateOptions options) {
    return updateOne(DEFAULT_DATABASE, sCollection, bsFilter, bsUpdate, options);
  }

  public static UpdateResult updateOne(String sDatabase, String sCollection, Bson bsFilter, Bson bsUpdate, UpdateOptions options) {
    UpdateResult result = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      try {
        result = collection.updateOne(bsFilter, bsUpdate);
      } catch (Exception e) {
        result = collection.updateOne(bsFilter, bsUpdate);
      }
    } else {
      try {
        result = collection.updateOne(bsFilter, bsUpdate, options);
      } catch (Exception e) {
        result = collection.updateOne(bsFilter, bsUpdate, options);
      }
    }
    return result;
  }

  /**
   * 更新文档 - 批量
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    更新条件 <var style="color:red">必输</var>
   * @param bsUpdate    更新内容 <var style="color:red">必输</var>
   * @param options     更新的参数信息
   * @return {UpdateResult}
   * @since 1.0
   */
  public static UpdateResult updateMany(String sCollection, Bson bsFilter, Bson bsUpdate, UpdateOptions options) {
    return updateMany(DEFAULT_DATABASE, sCollection, bsFilter, bsUpdate, options);
  }

  public static UpdateResult updateMany(String sDatabase, String sCollection, Bson bsFilter, Bson bsUpdate, UpdateOptions options) {
    UpdateResult result = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      result = collection.updateMany(bsFilter, bsUpdate);
    } else {
      result = collection.updateMany(bsFilter, bsUpdate, options);
    }
    return result;
  }

  /**
   * 替换文档 - 单笔
   * <p>
   * 根据传入参数进行整个文档的替换
   * <p>
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    命中原文档条件 <var style="color:red">必输</var>
   * @param bsReplace   更新后的文档 <var style="color:red">必输</var>
   * @param options     UpdateOptions 更新参数
   * @return {UpdateResult}
   * @since 1.0
   */
  public static UpdateResult replaceOne(String sCollection, Bson bsFilter, String bsReplace, UpdateOptions options) {
    return replaceOne(DEFAULT_DATABASE, sCollection, bsFilter, bsReplace, options);
  }

  public static UpdateResult replaceOne(String sDatabase, String sCollection, Bson bsFilter, String bsReplace, UpdateOptions options) {
    UpdateResult result = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      result = collection.replaceOne(bsFilter, Document.parse(bsReplace));
    } else {
      result = collection.replaceOne(bsFilter, Document.parse(bsReplace), options);
    }
    return result;
  }

  /**
   * 删除文档 - 单笔
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    删除条件 <var style="color:red">必输</var>
   * @param options     DeleteOptions 删除的参数信息
   * @return {DeleteResult}
   * @since 1.0
   */
  public static DeleteResult deleteOne(String sCollection, Bson bsFilter, DeleteOptions options) {
    return deleteOne(DEFAULT_DATABASE, sCollection, bsFilter, options);
  }

  public static DeleteResult deleteOne(String sDatabase, String sCollection, Bson bsFilter, DeleteOptions options) {
    DeleteResult result = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      result = collection.deleteOne(bsFilter);
    } else {
      result = collection.deleteOne(bsFilter, options);
    }
    return result;
  }

  /**
   * 删除文档 - 批量
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    删除条件 <var style="color:red">必输</var>
   * @param options     DeleteOptions 删除的参数信息
   * @return {DeleteResult}
   * @since 1.0
   */
  public static DeleteResult deleteMany(String sCollection, Bson bsFilter, DeleteOptions options) {
    return deleteMany(DEFAULT_DATABASE, sCollection, bsFilter, options);
  }

  public static DeleteResult deleteMany(String sDatabase, String sCollection, Bson bsFilter, DeleteOptions options) {
    DeleteResult result = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      result = collection.deleteMany(bsFilter);
    } else {
      result = collection.deleteMany(bsFilter, options);
    }
    return result;
  }

  public static MongoIterable<String> getDatabaseNames(String sClientKey) {
    if (CommonUtil.ifIsEmpty(sClientKey))
      sClientKey = DEFAULT_DATABASE;
    MongoClient client = clients.get(sClientKey);
    MongoIterable<String> mit = client.listDatabaseNames();
    return mit;
  }

  @Deprecated
  public static void dropDatabase(String sDatabase) {
    if (CommonUtil.ifIsNotEmpty(sDatabase)) {
      String[] aDatabase = StringUtils.split(sDatabase, ",");
      for (String database : aDatabase) {
        if (CommonUtil.ifIsEmpty(database))
          continue;
        MongoDatabase Database = getDatabase(database);
        if (Database != null)
          Database.drop();
      }
    }
  }

  /**
   * drop - 指定表
   *
   * @param sDropCollection
   */
  @Deprecated
  public static void dropCollection(String... sDropCollection) {
    if (CommonUtil.ifIsNotEmpty(sDropCollection)) {
      for (String sCollection : sDropCollection) {
        getCollection(sCollection).drop();
      }
    }
  }

  /**
   * drop - 除指定表
   *
   * @param lWithoutCollection
   */
  @Deprecated
  public static void dropCollectionWithout(List<String> lWithoutCollection) {
    dropWithout(DEFAULT_DATABASE, lWithoutCollection);
  }

  public static void dropWithout(String sDatabase, List<String> lWithoutCollection) {
    if (CommonUtil.ifIsEmpty(sDatabase))
      sDatabase = DEFAULT_DATABASE;
    MongoDatabase database = getDatabase(sDatabase);
    MongoIterable<String> iterable = database.listCollectionNames();
    Iterator<String> it = iterable.iterator();
    while (it.hasNext()) {
      String sCollection = it.next();
      if (lWithoutCollection.contains(sCollection))
        continue;
      getCollection(sCollection).drop();
    }
  }

  public static MongoIterable<String> listCollections() {
    return listCollections(DEFAULT_DATABASE);
  }

  public static MongoIterable<String> listCollections(String sDatabase) {
    if (CommonUtil.ifIsEmpty(sDatabase))
      sDatabase = DEFAULT_DATABASE;
    MongoDatabase database = getDatabase(sDatabase);
    return database.listCollectionNames();
  }

  public static boolean hasCollection(String sCollection) {
    return hasCollection(sCollection, DEFAULT_DATABASE);
  }

  public static boolean hasCollection(String sCollection, String sDatabase) {
    if (CommonUtil.ifIsEmpty(sCollection))
      return false;
    if (CommonUtil.ifIsEmpty(sDatabase))
      sDatabase = DEFAULT_DATABASE;
    MongoIterable<String> iterable = listCollections(sDatabase);
    Iterator<String> it = iterable.iterator();
    while (it.hasNext()) {
      String sCollectionName = it.next();
      if (sCollection.equals(sCollectionName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 查找并更新文档 - 单笔
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    查找条件 <var style="color:red">必输</var>
   * @param bsUpdate    更新内容 <var style="color:red">必输</var>
   * @param options     FindOneAndUpdateOptions 参数信息
   * @return {Document}
   * @since 1.0
   */
  public static Document findOneAndUpdate(String sCollection, Bson bsFilter, Bson bsUpdate, FindOneAndUpdateOptions options) {
    return findOneAndUpdate(DEFAULT_DATABASE, sCollection, bsFilter, bsUpdate, options);
  }

  public static Document findOneAndUpdate(String sDatabase, String sCollection, Bson bsFilter, Bson bsUpdate, FindOneAndUpdateOptions options) {
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    Document document = null;
    if (CommonUtil.ifIsNull(options)) {
      document = collection.findOneAndUpdate(bsFilter, bsUpdate);
    } else {
      document = collection.findOneAndUpdate(bsFilter, bsUpdate, options);
    }
    return document;
  }

  /**
   * 查找并替换文档 - 单笔
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    查找条件 <var style="color:red">必输</var>
   * @param docReplace  替换内容 <var style="color:red">必输</var>
   * @param options     FindOneAndReplaceOptions 参数信息
   * @return {Document}
   * @since 1.0
   */
  public static Document findOneAndReplace(String sCollection, Bson bsFilter, Document docReplace, FindOneAndReplaceOptions options) {
    return findOneAndReplace(DEFAULT_DATABASE, sCollection, bsFilter, docReplace, options);
  }

  public static Document findOneAndReplace(String sDatabase, String sCollection, Bson bsFilter, Document docReplace, FindOneAndReplaceOptions options) {
    Document document = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      document = collection.findOneAndReplace(bsFilter, docReplace);
    } else {
      document = collection.findOneAndReplace(bsFilter, docReplace, options);
    }
    return document;
  }

  /**
   * 查找并删除文档 - 单笔
   *
   * @param sCollection 集合名称 <var style="color:red">必输</var>
   * @param bsFilter    查找条件 <var style="color:red">必输</var>
   * @param options     FindOneAndDeleteOptions 参数信息
   * @return {Document}
   * @since 1.0
   */
  public static Document findOneAndDelete(String sCollection, Bson bsFilter, FindOneAndDeleteOptions options) {
    return findOneAndDelete(DEFAULT_DATABASE, sCollection, bsFilter, options);
  }

  public static Document findOneAndDelete(String sDatabase, String sCollection, Bson bsFilter, FindOneAndDeleteOptions options) {
    Document document = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    if (CommonUtil.ifIsNull(options)) {
      document = collection.findOneAndDelete(bsFilter);
    } else {
      document = collection.findOneAndDelete(bsFilter, options);
    }
    return document;
  }

  /**
   * 采用aggregate进行分组统计
   *
   * @param sCollection 集合名称
   * @param aggregate   aggregate集合数据
   * @return {AggregateIterable}
   */
  public static AggregateIterable<Document> aggregate(String sCollection, List<Bson> aggregate) {
    return aggregate(DEFAULT_DATABASE, sCollection, aggregate);
  }

  public static AggregateIterable<Document> aggregate(String sDatabase, String sCollection, List<Bson> aggregate) {
    AggregateIterable<Document> iterator = null;
    MongoCollection<Document> collection = getCollection(sDatabase, sCollection);
    iterator = collection.aggregate(aggregate);
    return iterator;
  }

  private static List<? extends Document> getListDocument(String sInput) throws Exception {
    List<Document> documents = new ArrayList<Document>();
    JsonNode jInput = JsonUtil.getJson(sInput);
    Iterator<Entry<String, JsonNode>> it = jInput.fields();
    while (it.hasNext()) {
      Entry<String, JsonNode> entry = it.next();
      JsonNode jValue = entry.getValue();
      if (CommonUtil.ifIsEmpty(jValue))
        continue;
      documents.add(Document.parse(jValue.toString()));
    }
    return documents;
  }

  /**
   * 上传文件到gfs
   *
   * @param sBucket   gridFS bucket名称
   * @param sID       gridFS id
   * @param sFileName 文件名称
   * @param insSource 文件流
   * @param options   GridFSUploadOptions 参数信息
   * @since 1.0
   */
  public static void uploadGridFS(String sBucket, String sID, String sFileName, InputStream insSource, GridFSUploadOptions options) {
    uploadGridFS(DEFAULT_DATABASE, sBucket, sID, sFileName, insSource, options);
  }

  public static void uploadGridFS(String sDatabase, String sBucket, String sID, String sFileName, InputStream insSource, GridFSUploadOptions options) {
    GridFSBucket gridFSBucket = getGridFSBucket(sDatabase, sBucket);
    if (CommonUtil.ifIsNull(options)) {
      gridFSBucket.uploadFromStream(new BsonString(sID), sFileName, insSource);
    } else {
      gridFSBucket.uploadFromStream(new BsonString(sID), sFileName, insSource, options);
    }
  }

  /**
   * 查询gfs文件
   * <p>
   * 通过FindOperation设置查询参数
   * </p>
   *
   * @param sBucket  gridFS bucket名称
   * @param bsFilter 查询条件 <var style="color:red">必输</var>
   * @param options  FindOperation 查询参数
   * @return {GridFSFindIterable}
   * @since 1.0
   */
  public static GridFSFindIterable findGridFS(String sBucket, Bson bsFilter, FindOptions options) {
    return findGridFS(DEFAULT_DATABASE, sBucket, bsFilter, options);
  }

  public static GridFSFindIterable findGridFS(String sDatabase, String sBucket, Bson bsFilter, FindOptions options) {
    GridFSBucket gridFSBucket = getGridFSBucket(sDatabase, sBucket);
    GridFSFindIterable iterator = gridFSBucket.find(bsFilter);
    if (CommonUtil.ifIsNotNull(options)) {
      if (options.getSkip() > 0)
        iterator.skip(options.getSkip());
      if (options.getLimit() > 0)
        iterator.limit(options.getLimit());
      if (CommonUtil.ifIsNotNull(options.getSort()))
        iterator.sort(options.getSort());
    }
    return iterator;
  }

  /**
   * 下载gfs文件
   *
   * @param sBucket         gridFS bucket名称
   * @param sID             gridFS id
   * @param sFileName       文件名称
   * @param outsDestination 文件输出流
   * @param options         GridFSDownloadOptions 参数信息
   * @since 1.0
   */
  public static void downloadGridFS(String sBucket, String sID, String sFileName, OutputStream outsDestination, GridFSDownloadOptions options) {
    downloadGridFS(DEFAULT_DATABASE, sBucket, sID, sFileName, outsDestination, options);
  }

  public static void downloadGridFS(String sDatabase, String sBucket, String sID, String sFileName, OutputStream outsDestination, GridFSDownloadOptions options) {
    GridFSBucket gridFSBucket = getGridFSBucket(sDatabase, sBucket);
    if (CommonUtil.ifIsNotEmpty(sID)) {
      gridFSBucket.downloadToStream(new BsonString(sID), outsDestination);
    } else {
      if (CommonUtil.ifIsNull(options)) {
        gridFSBucket.downloadToStream(sFileName, outsDestination);
      } else {
        gridFSBucket.downloadToStream(sFileName, outsDestination, options);
      }
    }
  }

  /**
   * 重命名文件名称
   *
   * @param sBucket   gridFS bucket名称
   * @param sID       gridFS id
   * @param sFileName 重命名文件名称
   * @since 1.0
   */
  public static void renameGridFS(String sBucket, String sID, String sFileName) {
    renameGridFS(DEFAULT_DATABASE, sBucket, sID, sFileName);
  }

  public static void renameGridFS(String sDatabase, String sBucket, String sID, String sFileName) {
    GridFSBucket gridFSBucket = getGridFSBucket(sDatabase, sBucket);
    gridFSBucket.rename(new BsonString(sID), sFileName);
  }

  /**
   * 删除文件
   *
   * @param sBucket gridFS bucket名称
   * @param sID     gridFS id
   * @since 1.0
   */
  public static void deleteGridFS(String sBucket, String sID) {
    deleteGridFS(DEFAULT_DATABASE, sBucket, sID);
  }

  public static void deleteGridFS(String sDatabase, String sBucket, String sID) {
    GridFSBucket gridFSBucket = getGridFSBucket(sDatabase, sBucket);
    gridFSBucket.delete(new BsonString(sID));
  }

}
