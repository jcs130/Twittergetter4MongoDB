package com.zhongli.TwitterGetter.service.twitter4j;

import twitter4j.RawStreamListener;

import com.zhongli.TwitterGetter.dao.TwitterSaveDAO;

/**
 * 消息监听器的实现类
 * 
 * @author zhonglili
 *
 */
public class LocatedTwitterListener implements RawStreamListener {

	private TwitterSaveDAO db;

	public LocatedTwitterListener(TwitterSaveDAO db) {
		this.db = db;
	}

	@Override
	public void onException(Exception arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(String rawString) {
		db.insert(rawString);
		System.out.println(rawString);
	}

}
