package com.vekai.crops.util.mongo3;

import com.vekai.crops.util.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;

public class BsonUtil {

  public static String toString(Bson bson) {
    if (bson == null)
      return null;
    BsonDocument document = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
    String sDoc = document.toJson();
    return sDoc;
  }

  public static JsonNode toJson(Bson bson) throws Exception {
    if (bson == null)
      return null;
    BsonDocument document = bson.toBsonDocument(BsonDocument.class, MongoClient.getDefaultCodecRegistry());
    JsonNode jDoc = JsonUtil.getJson(document.toJson());
    return jDoc;
  }

  public static Bson toBson(JsonNode jBson) {
    return toBson(jBson.toString());
  }

  public static Bson toBson(String sBson) {
    Bson bson = BsonDocument.parse(sBson);
    return bson;
  }

}
