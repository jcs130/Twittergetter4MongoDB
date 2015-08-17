package com.zhongli.TwitterGetter.app;

import java.util.*;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.*;

/**
 * �������
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
		// ��ȡ����
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		// ��ȡ���ݿ�
		MongoDatabase database = mongoClient.getDatabase("mydb");
		// ����ĳ���ĵ���
		MongoCollection<Document> collection = database.getCollection("test");
		// �������ĵ�
		Document doc = new Document("name", "MongoDB")
				.append("type", "database").append("count", 1)
				.append("info", new Document("x", 203).append("y", 102));
		// ���ĵ������ĵ�����
		collection.insertOne(doc);

		// ����һ����������ĵ����б�
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}
		// ���ĵ��в����б�
		collection.insertMany(documents);

		// ��ʾ�����е��ĵ�
		System.out.println(collection.count());

		// ��ѯ�����еĵ�һ���ĵ�
		Document myDoc = collection.find().first();
		System.out.println(myDoc.toJson());
		
		//��ȡ�����е�ȫ���ĵ�
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
