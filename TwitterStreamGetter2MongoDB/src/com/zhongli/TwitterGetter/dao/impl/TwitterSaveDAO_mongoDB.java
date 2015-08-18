package com.zhongli.TwitterGetter.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.zhongli.TwitterGetter.dao.TwitterSaveDAO;

public class TwitterSaveDAO_mongoDB implements TwitterSaveDAO {
	MongoCollection<Document> collection;

	public TwitterSaveDAO_mongoDB(MongoCollection<Document> collection) {
		super();
		this.collection = collection;
	}

	@Override
	public void insert(String msg) {
		Document doc = Document.parse(msg);
		collection.insertOne(doc);
	}

	@Override
	public void insert(List<String> msgs) {
		List<Document> docs = new ArrayList<Document>();
		for (int i = 0; i < msgs.size(); i++) {
			docs.add(Document.parse(msgs.get(i)));
		}

	}

}
