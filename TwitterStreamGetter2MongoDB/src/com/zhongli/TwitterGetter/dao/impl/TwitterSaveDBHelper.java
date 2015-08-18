package com.zhongli.TwitterGetter.dao.impl;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TwitterSaveDBHelper {
	MongoCollection<Document> collection;
	MongoClient mongoClient;
	MongoDatabase database;

	public TwitterSaveDBHelper(String ip, int port, String dbName,
			String collectionName) {
		// 获取链接
		mongoClient = new MongoClient(ip, port);
		// 获取数据库
		database = mongoClient.getDatabase(dbName);
		// 进入某个文档集
		collection = database.getCollection(collectionName);
	}

	public TwitterSaveDBHelper() {
		// 获取链接
		mongoClient = new MongoClient("localhost", 27017);
		// 获取数据库
		database = mongoClient.getDatabase("happycityproject");
		// 进入某个文档集
		collection = database.getCollection("rawTwitters");
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}
}
