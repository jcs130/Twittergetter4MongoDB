package com.zhongli.TwitterGetter.service;

import java.util.ArrayList;

import com.zhongli.TwitterGetter.service.twitter4j.TwitterStreamThread;

/**
 * �洢�������е��̵߳ľ�̬��
 * 
 * @author zhonglili
 *
 */
public class ThreadsPool {
	private static ArrayList<ServiceThread> serverThreads = new ArrayList<ServiceThread>();
	// jianshiStream������̵߳�����
	private static int twitterStreamThreadNum = 0;

	public static void addTwitterStreamThread(TwitterStreamThread tst) {
		// 1.��������,���������г��ֿն�
		String threadName = makeTwitterStreamThreadName(tst.getClass()
				.getSimpleName());
		tst.settName(threadName);
		// 2.��ӵ��߳��б�
		addThread(tst);
		// 3.�޸�����
		twitterStreamThreadNum++;
		// 4.���߳����ƴ������ݿ�
		tst.saveThreadname();
		// 5.���������̣߳�
		// tst.start();
		tst.startListening();

	}

	private static String makeTwitterStreamThreadName(String className) {
		String name = "" + className;
		for (int i = 0; i < twitterStreamThreadNum; i++) {
			if (!hasSameName(className + i)) {
				// ������ֿն���ʹ�������������
				name += i;
				return name;
			}
		}
		name += twitterStreamThreadNum;
		return name;
	}

	public static int getTwitterStreamThreadsNum() {
		return twitterStreamThreadNum;
	}

	/**
	 * ����һ���߳�
	 * 
	 * @param tName
	 * @param t
	 */
	public static boolean addThread(ServiceThread t) {
		// �������Ψһ�����ӳɹ�
		if (!hasSameName(t.gettName())) {
			t.start();
			serverThreads.add(t);
			System.out.println(t.gettName() + " ��ʼ����..");
			return true;
		} else {
			// �Զ�������
		}
		return false;
	}

	/**
	 * ����Ƿ�����ͬ������
	 * 
	 * @param name
	 * @return
	 */
	private static boolean hasSameName(String name) {
		for (int i = 0; i < serverThreads.size(); i++) {
			if (serverThreads.get(i).gettName().toLowerCase()
					.equals(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * ֹͣ��ɾ��һ���߳�
	 * 
	 * @param tName
	 * @return
	 */
	public static boolean stopThread(String tName) {
		for (int i = 0; i < serverThreads.size(); i++) {
			if (serverThreads.get(i).gettName().toLowerCase()
					.equals(tName.toLowerCase())) {
				// if (serverThreads.get(i).isAlive()) {
				serverThreads.get(i).stopMe();
				// }
				serverThreads.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * ֹͣ��ɾ��һ���߳�
	 * 
	 * @param tName
	 * @return
	 */
	public static boolean removeThread(String tName) {
		for (int i = 0; i < serverThreads.size(); i++) {
			if (serverThreads.get(i).gettName().toLowerCase()
					.equals(tName.toLowerCase())) {
				// if (serverThreads.get(i).isAlive()) {
				serverThreads.get(i).stopMe();
				// }
				serverThreads.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * ͨ���߳����ֵõ�һ���߳�
	 * 
	 * @param tName
	 * @return
	 */
	public static ServiceThread getThread(String tName) {
		for (int i = 0; i < serverThreads.size(); i++) {
			if (serverThreads.get(i).gettName().toLowerCase()
					.equals(tName.toLowerCase())) {
				return serverThreads.get(i);
			}
		}
		return null;
	}

	/**
	 * �õ��������е��̵߳������б�
	 * 
	 * @return
	 */
	public static ArrayList<String> getRunningThreadsList() {
		ArrayList<String> result = new ArrayList<String>();
		for (int i = 0; i < serverThreads.size(); i++) {
			result.add(serverThreads.get(i).gettName());
		}
		return result;
	}

}
