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
    // ????????????
    File file = resource.getFile();
    JsonNode jMongoConfig = JsonUtil.getJson(file);*/
    // ????????????
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
        builder.connectionsPerHost(100) // ??????????????????(??????100)
                .threadsAllowedToBlockForConnectionMultiplier(10) // ???????????????????????????????????????(??????5)
                .maxWaitTime(2 * 60 * 1000) // ???????????????????????????????????????????????????????????????(??????2??????)
                .connectTimeout(5 * 1000) // ????????????socket?????????????????????(??????10???)
                .socketTimeout(0) // socket??????????????????????????????(??????0????????????<infinite>)
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

  // ?????????????????????
  public static MongoDatabase getDatabase() {
    return getDatabase(DEFAULT_DATABASE);
  }

  // ??????????????????????????????
  public static MongoDatabase getDatabase(String sDatabaseName) {
    return getDatabase(DEFAULT_DATABASE, sDatabaseName);
  }

  // ??????????????????????????????????????????????????????
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

  // ????????????????????????????????????
  public static MongoCollection<Document> getCollection(String sCollection) {
    return getCollection(DEFAULT_DATABASE, sCollection);
  }

  // ????????????????????????????????????????????????????????????
  public static MongoCollection<Document> getCollection(String sDatabaseName, String sCollection) {
    return getCollection(DEFAULT_DATABASE, sDatabaseName, sCollection);
  }

  // ????????????????????????????????????????????????????????????
  public static MongoCollection<Document> getCollection(String sClientName, String sDatabaseName, String sCollection) {
    MongoDatabase database = getDatabase(sClientName, sDatabaseName);
    return database.getCollection(sCollection);
  }

  // ????????????????????????????????????????????????GridFS??????
  public static GridFSBucket getGridFSBucket(String sBucket) {
    return getGridFSBucket(DEFAULT_DATABASE, sBucket);
  }

  // ????????????????????????????????????????????????GridFS??????
  public static GridFSBucket getGridFSBucket(String sDatabaseName, String sBucket) {
    return getGridFSBucket(DEFAULT_DATABASE, sDatabaseName, sBucket);
  }

  // ????????????????????????????????????????????????GridFS??????
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
   * ???????????? - ??????
   * <p>
   * ????????????????????????
   * </p>
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param sInsert     String?????????Json/Document????????????????????? <var style="color:red">??????</var>
   * @param options     ?????????????????????????????????
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
   * ???????????? - ??????
   * <p>
   * ????????????????????????
   * </p>
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param sInsert     String?????????Json/Document????????????????????? {'id':{...}, 'id':{...}}
   *                    <var style="color:red">??????</var>
   * @param options     ?????????????????????????????????
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
   * ???????????????
   * <p>
   * ????????????????????????
   * </p>
   * <p>
   * ???????????????????????????
   * </p>
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param sFilter     ???????????? <var style="color:red">??????</var>
   * @param options     ????????????????????????????????????
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
   * ????????????
   * <p>
   * ??????FindOperation??????????????????
   * </p>
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param options     FindOperation ????????????
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
   * ???????????? - ??????
   *
   * @param sCollection ????????????
   * @param bsFilter    ????????????
   * @param bsUpdate    ????????????
   * @param options     ?????????????????????
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
   * ???????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param bsUpdate    ???????????? <var style="color:red">??????</var>
   * @param options     ?????????????????????
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
   * ???????????? - ??????
   * <p>
   * ?????????????????????????????????????????????
   * <p>
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ????????????????????? <var style="color:red">??????</var>
   * @param bsReplace   ?????????????????? <var style="color:red">??????</var>
   * @param options     UpdateOptions ????????????
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
   * ???????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param options     DeleteOptions ?????????????????????
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
   * ???????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param options     DeleteOptions ?????????????????????
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
   * drop - ?????????
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
   * drop - ????????????
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
   * ????????????????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param bsUpdate    ???????????? <var style="color:red">??????</var>
   * @param options     FindOneAndUpdateOptions ????????????
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
   * ????????????????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param docReplace  ???????????? <var style="color:red">??????</var>
   * @param options     FindOneAndReplaceOptions ????????????
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
   * ????????????????????? - ??????
   *
   * @param sCollection ???????????? <var style="color:red">??????</var>
   * @param bsFilter    ???????????? <var style="color:red">??????</var>
   * @param options     FindOneAndDeleteOptions ????????????
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
   * ??????aggregate??????????????????
   *
   * @param sCollection ????????????
   * @param aggregate   aggregate????????????
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
   * ???????????????gfs
   *
   * @param sBucket   gridFS bucket??????
   * @param sID       gridFS id
   * @param sFileName ????????????
   * @param insSource ?????????
   * @param options   GridFSUploadOptions ????????????
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
   * ??????gfs??????
   * <p>
   * ??????FindOperation??????????????????
   * </p>
   *
   * @param sBucket  gridFS bucket??????
   * @param bsFilter ???????????? <var style="color:red">??????</var>
   * @param options  FindOperation ????????????
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
   * ??????gfs??????
   *
   * @param sBucket         gridFS bucket??????
   * @param sID             gridFS id
   * @param sFileName       ????????????
   * @param outsDestination ???????????????
   * @param options         GridFSDownloadOptions ????????????
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
   * ?????????????????????
   *
   * @param sBucket   gridFS bucket??????
   * @param sID       gridFS id
   * @param sFileName ?????????????????????
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
   * ????????????
   *
   * @param sBucket gridFS bucket??????
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
