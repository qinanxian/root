package com.vekai.base.mongo;

import cn.fisok.raw.kit.BsonKit;
import cn.fisok.raw.kit.DateKit;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.vekai.base.TestData;
import com.vekai.base.entity.Person;
import org.bson.Document;
import org.junit.Test;

import java.util.List;

public class MongoTest {

    @Test
    public void connectTest() {
        try {

            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // Now connect to your databases
            MongoDatabase mgdb = mongoClient.getDatabase("rober");

            System.out.println("Connect to database successfully!");
            System.out.println("MongoDatabase inof is : " + mgdb.getName());
            // If MongoDB in secure mode, authentication is required.
            // boolean auth = db.authenticate(myUserName, myPassword);
            // System.out.println("Authentication: "+auth);

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    public void connStrTest() {
//        String strUri = "mongodb://user1:pwd1@host1/?authSource=db1";
        String strUri = "mongodb://127.0.0.1:27017";
        MongoClientURI uri = new MongoClientURI(strUri);
        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase mgdb = mongoClient.getDatabase("rober");

        System.out.println("MongoDatabase inof is : " + mgdb.getName());
    }

    @Test
    public void crudTest() {
        String strUri = "mongodb://127.0.0.1:27017";
        MongoClientURI moUri = new MongoClientURI(strUri);
        MongoClient moClient = new MongoClient(moUri);
        MongoDatabase modb = moClient.getDatabase("rober");

        String collectName = "testPerson";

        MongoCollection<Document> collect = modb.getCollection(collectName);
        if (collect != null) collect.drop();

        try {
            modb.createCollection(collectName);
            System.out.println("集合创建成功");
        } catch (MongoCommandException e) {
            //"code" : 48, "codeName" : "NamespaceExists"
            if (48 == e.getErrorCode()) {
                System.out.println("集合已经存在");
            }
        }

        collect = modb.getCollection(collectName);


        Document document = new Document("code", "JDA001").
                append("name", "syang").
                append("gender", "1").
                append("height", 172).
                append("familyYearIncome", 40).
                append("birth", DateKit.parse("1987-01-30")).
                append("createTime", DateKit.now());
        collect.insertOne(document);

        List<Person> personList = TestData.personList;
        List<Document> docList = BsonKit.bean2DocumentList(personList);
        collect.insertMany(docList);

    }
}
