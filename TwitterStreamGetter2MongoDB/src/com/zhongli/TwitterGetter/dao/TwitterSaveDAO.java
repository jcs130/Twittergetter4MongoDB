package com.zhongli.TwitterGetter.dao;

import java.util.List;

import org.bson.Document;


/**
 * Twitter message data access object
 * 
 * @author John
 *
 */
public interface TwitterSaveDAO {
	
	/**
	 * Insert one data into the database 
	 * @param msg
	 */
	public void insert(String msg);
	
	/**
	 * Insert many data into databases
	 * @param msgs
	 */
	public void insert(List<String> msgs);
	
	
	
	
}
