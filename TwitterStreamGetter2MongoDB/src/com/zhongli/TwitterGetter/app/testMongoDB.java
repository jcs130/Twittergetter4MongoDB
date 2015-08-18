package com.zhongli.TwitterGetter.app;

import java.util.*;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.util.JSON;

import static com.mongodb.client.model.Filters.*;

/**
 * �������
 * 
 * @author John
 *
 */
public class testMongoDB {
	public static void main(String[] args) {
		testMongoDB tm = new testMongoDB();
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

		/********************** ���ݲ��� ****************************/
		// // �������ĵ�
//		 Document doc = new Document("name", "MongoDB")
		// .append("type", "database").append("count", 1)
		// .append("info", new Document("x", 203).append("y", 102));
		// // ���ĵ������ĵ�����
		// collection.insertOne(doc);
		//
		// // ����һ����������ĵ����б�
		// List<Document> documents = new ArrayList<Document>();
		// for (int i = 0; i < 100; i++) {
		// documents.add(new Document("i", i));
		// }
		// // ���ĵ��в����б�
		// collection.insertMany(documents);

		/***************** ���ݶ�ȡ ****************************************/
		// // ��ʾ�����е��ĵ�������
		// System.out.println(collection.count());
		//
		// // ��ѯ�����еĵ�һ���ĵ�
		// Document myDoc = collection.find().first();
		// System.out.println(myDoc.toJson());
		//
		// //��ȡ�����е�ȫ���ĵ�
		// MongoCursor<Document> cursor = collection.find().iterator();
		// try {
		// while (cursor.hasNext()) {
		// System.out.println(cursor.next().toJson());
		// }
		// } finally {
		// cursor.close();
		// }

		// //��ȡȫ���ĵ�����һ�ַ���
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }

		// // ����������ȡĳ���ĵ� eq:==
		// Document myDoc = collection.find(eq("i", 71)).first();
		// System.out.println(myDoc.toJson());

		// ͨ����ѯ���һ���Ի�ȡ�������
		// Block<Document> printBlock = new Block<Document>() {
		// @Override
		// public void apply(final Document document) {
		// System.out.println(document.toJson());
		// }
		// };
		// ������д���50��
		// collection.find(gt("i", 50)).forEach(printBlock);
		// ����50 С�� 100
		// collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);

		// ������ĵ���������,-1Ϊ�ݼ���1Ϊ����
		// �ٷ��ĵ�����������http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/#sorting-documents
		// Document myDoc = collection.find(exists("i"))
		// .sort(new BasicDBObject("i", -1)).first();
		// System.out.println(myDoc.toJson());

		// ѡ�����������е�Ԫ�أ�0Ϊ����ʾ��1Ϊ��ʾ
		// �ٷ��ĵ��е������ֲ����ã�http://mongodb.github.io/mongo-java-driver/3.0/driver/getting-started/quick-tour/#projecting-fields
		// BasicDBObject exclude = new BasicDBObject();
		// exclude.append("_id", 0);
		// // exclude.append("count", 0);
		// exclude.append("name", 1);
		// exclude.append("info", 1);
		// Document myDoc = collection.find().projection(exclude).first();
		// System.out.println(myDoc.toJson());

		/************************* �޸����ݿ������� *************************************/

		// �޸�ʱ�Ĳ�����
		// $inc ��ָ����Ԫ�ؼ�
		// $mul ��
		// $rename �޸�Ԫ������
		// $setOnInsert �����ǰû�����Ԫ�����������Ԫ�أ��������κθ���
		// $set �޸��ƶ�Ԫ�ص�ֵ
		// $unset �Ƴ��ض���Ԫ��
		// $min ���ԭʼ���ݸ������޸ģ������޸�Ϊָ����ֵ
		// $max ��$min�෴
		// $currentDate �޸�ΪĿǰ��ʱ��

		// //�޸ĵ�һ����������������
		// $set Ϊ�޸�
		// collection.updateOne(eq("i", 10), new Document("$set", new
		// Document("i", 110)));
		// // ��ȡȫ���ĵ�,���Կ�����ǰ10�ĵط������110
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }

		// �����޸����ݲ��ҷ����޸ĵĽ����������С��100�Ľ������100
		// UpdateResult updateResult = collection.updateMany(lt("i", 100),
		// new Document("$inc", new Document("i", 100)));
		// // ��ʾ�����仯������
		// System.out.println(updateResult.getModifiedCount());
		// // ��ȡȫ���ĵ�,���Կ������˸ղ��޸ĵ�110������ȫΪ��100
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }

		/************************** ɾ������ *****************************/
		// ɾ����һ����������������
		// collection.deleteOne(eq("i", 110));
		// // ��ȡȫ���ĵ�,���Կ���û��110�������
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }

		// ɾ�����з������������ݣ����ҷ��ؽ��
		// DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
		// // ���ɾ��������
		// System.out.println(deleteResult.getDeletedCount());
		// // ��ȡȫ���ĵ�,����i>=100�����ݶ�û��
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }
		/*************************** ����飬һ��ִ�ж������ ********************************/
		// ��������Ⱥ�˳��ִ��
		// collection.bulkWrite(Arrays.asList(new InsertOneModel<>(new Document(
		// "_id", 4)), new InsertOneModel<>(new Document("_id", 5)),
		// new InsertOneModel<>(new Document("_id", 6)),
		// new UpdateOneModel<>(new Document("_id", 1), new Document(
		// "$set", new Document("x", 2))), new DeleteOneModel<>(
		// new Document("_id", 2)),
		// new ReplaceOneModel<>(new Document("_id", 3), new Document(
		// "_id", 3).append("x", 4))));
		// // ��ȡȫ���ĵ�
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }

		// ����������Ⱥ�˳��ִ��
		// collection.bulkWrite(Arrays.asList(new InsertOneModel<>(new Document(
		// "_id", 4)), new InsertOneModel<>(new Document("_id", 5)),
		// new InsertOneModel<>(new Document("_id", 6)),
		// new UpdateOneModel<>(new Document("_id", 1), new Document(
		// "$set", new Document("x", 2))), new DeleteOneModel<>(
		// new Document("_id", 2)),
		// new ReplaceOneModel<>(new Document("_id", 3), new Document(
		// "_id", 3).append("x", 4))), new BulkWriteOptions()
		// .ordered(false));
		// ��ȡȫ���ĵ�
		// for (Document cur : collection.find()) {
		// System.out.println(cur.toJson());
		// }
		
		
		// �ر����ݿ�����
		mongoClient.close();

	}

}
