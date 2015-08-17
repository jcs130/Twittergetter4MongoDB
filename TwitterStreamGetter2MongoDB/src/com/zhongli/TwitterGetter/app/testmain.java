package com.zhongli.TwitterGetter.app;

import java.util.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.*;

/**
 * 程序入口
 * 
 * @author John
 *
 */
public class testmain {
	public static void main(String[] args) {
		testmain tm = new testmain();
		tm.test();
	}

	/**
	 * test
	 */
	private void test() {
		// 获取链接
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// 获取数据库
		MongoDatabase database = mongoClient.getDatabase("mydb");
		// 进入某个文档集
		MongoCollection<Document> collection = database.getCollection("test");
		// 创建新文档
		Document doc = new Document("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new Document("x", 203).append("y", 102));
		// 将文档插入文档集合
		collection.insertOne(doc);

		// 创建一个包含多个文档的列表
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}
		// 向文档中插入列表
		collection.insertMany(documents);

		// 显示集合中的文档
		System.out.println(collection.count());

		// 查询集合中的第一个文档
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());
		
		//获取集合中的全部文档
		// MongoCursor<Document> cursor = collection.find().iterator();
		// try {
		// while (cursor.hasNext()) {
		// System.out.println(cursor.next().toJson());
		// }
		// } finally {
		// cursor.close();
		// }
		
		

	}

}
