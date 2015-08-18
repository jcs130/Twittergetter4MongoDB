package com.zhongli.TwitterGetter.service.twitter4j;

import java.util.ArrayList;

import twitter4j.FilterQuery;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.zhongli.TwitterGetter.dao.InfoGetterDAO;
import com.zhongli.TwitterGetter.dao.TwitterSaveDAO;
import com.zhongli.TwitterGetter.dao.impl.InfoGetterDAO_MySQL;
import com.zhongli.TwitterGetter.dao.impl.TwitterSaveDAO_mongoDB;
import com.zhongli.TwitterGetter.dao.impl.TwitterSaveDBHelper;
import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.service.ServiceThread;



/**
 * ��size���������һ�������߳̽��м��ӣ����������޸��⼸���������ֱ��ֹͣ
 * 
 * @author zhonglili
 *
 */
public class TwitterStreamThread extends ServiceThread {
	private ArrayList<EarthSqure> watchList;
	private InfoGetterDAO db_info;
	private TwitterSaveDAO db_save;
	private TwitterTools tt;
	private TwitterStream twitterStream;
	private LocatedTwitterListener listener;
	private TwitterSaveDBHelper tsDBHelper;

	public TwitterStreamThread(ArrayList<EarthSqure> watchList, TwitterTools tt) {
		this.watchList = watchList;
		this.tt = tt;
		tsDBHelper=new TwitterSaveDBHelper();
		init();
	}

	// @Override
	// public void run() {
	// super.run();
	// }
	public void startListening() {
		ArrayList<double[]> locs = new ArrayList<double[]>();
		// ���Squre
		for (int i = 0; i < watchList.size(); i++) {
			addLocationArea(locs, watchList.get(i).getSouth(), watchList.get(i)
					.getWest(), watchList.get(i).getNorth(), watchList.get(i)
					.getEast());
		}
		double[][] lo = locs.toArray(new double[locs.size()][2]);

		FilterQuery q = new FilterQuery();
		q.locations(lo);
		twitterStream.filter(q);
	}

	// ����W,S,E,N��˳�����
	private void addLocationArea(ArrayList<double[]> locs, double south,
			double west, double north, double east) {
		locs.add(new double[] { west, south });
		locs.add(new double[] { east, north });
	}

	// ���ؼ����б�Ĵ�С
	public int getWatchSize() {
		return watchList.size();
	}

	// ���߳����ƴ������ݿⲢ���޸�����״̬
	public void saveThreadname() {
		for (int i = 0; i < watchList.size(); i++) {
			watchList.get(i).setStreamState(1);
			watchList.get(i).setThreadName(this.gettName());
			db_info.changeSqureState(watchList.get(i).getSqureID(), 1,
					this.gettName());
		}

	}

	// ��������IDֹͣ����
	public void stopStreamSqure(int squreID) {
		for (int i = 0; i < watchList.size(); i++) {
			if (watchList.get(i).getSqureID() == squreID) {
				// �ı����ݿ���������״̬
				db_info.changeSqureState(squreID, 0, "none");
				watchList.remove(i);
				reStart();
				return;
			}
		}
	}

	// �������б����仯��������������
	private void reStart() {
		// ֹͣ��ǰ��
		twitterStream.cleanUp();
		twitterStream.shutdown();
		init();
		// ��������
		startListening();
	}

	private void init() {
		this.db_info = new InfoGetterDAO_MySQL();
		this.db_save=new TwitterSaveDAO_mongoDB(tsDBHelper.getCollection());
		ConfigurationBuilder cb = tt.getConfigurationBuilder();
		this.twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
		this.listener = new LocatedTwitterListener(db_save);
		this.twitterStream.addListener(listener);
	}

	@Override
	public void stopMe() {
		// ֹͣ��ǰ��
		twitterStream.clearListeners();
		twitterStream.cleanUp();
		twitterStream.shutdown();
	}

}
