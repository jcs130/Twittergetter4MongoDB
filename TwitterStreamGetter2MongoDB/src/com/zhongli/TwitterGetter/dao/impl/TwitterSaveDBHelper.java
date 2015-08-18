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
		// ��ȡ����
		mongoClient = new MongoClient(ip, port);
		// ��ȡ���ݿ�
		database = mongoClient.getDatabase(dbName);
		// ����ĳ���ĵ���
		collection = database.getCollection(collectionName);
	}

	public TwitterSaveDBHelper() {
		// ��ȡ����
		mongoClient = new MongoClient("localhost", 27017);
		// ��ȡ���ݿ�
		database = mongoClient.getDatabase("happycityproject");
		// ����ĳ���ĵ���
		collection = database.getCollection("rawTwitters");
	}

	public MongoCollection<Document> getCollection() {
		return collection;
	}
}
