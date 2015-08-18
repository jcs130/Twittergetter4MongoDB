package com.zhongli.TwitterGetter.service;

import java.util.ArrayList;

import com.zhongli.TwitterGetter.dao.InfoGetterDAO;
import com.zhongli.TwitterGetter.dao.impl.InfoGetterDAO_MySQL;
import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.service.twitter4j.TwitterStreamThread;
import com.zhongli.TwitterGetter.service.twitter4j.TwitterTools;


/**
 * ����Stream�����̵߳Ĺ����̣߳���ѯ�������ȡ����ʹ�ô�������0������״̬Ϊ0��ֹͣ�������飬�½���Ӧ��Stream�����߳�
 * 
 * @author zhonglili
 *
 */
public class StreamsManagerThread extends ServiceThread {
	private int time;
	private boolean isRunning = false;
	private TwitterTools tt;

	public StreamsManagerThread(int time) {
		this.settName(this.getClass().getSimpleName());
		this.time = time;
		this.tt = new TwitterTools();
	}

	@Override
	public void run() {
		super.run();
		isRunning = true;
		InfoGetterDAO db = new InfoGetterDAO_MySQL();
		ArrayList<EarthSqure> ess;
		
		//��Ҫ�޸��سǲ��޷�ֹ�����������������ݶ�ʧ����
		while (isRunning) {
			try {
				// �����ݿ��л�ȡʹ�ô���>0����û�б����ӵ�Stream����
				ess = (ArrayList<EarthSqure>) db.getReadySqure();
				// �½������̣߳�ÿ�������̼߳���5������
				buildStreamthreads(20, ess);
				sleep(2000);
			} catch (InterruptedException e) {
				isRunning = false;
				System.out.println("�߳�:" + this.gettName() + "����....");
				// e.printStackTrace();
			}
		}

	}

	// ���������ϲ�Ϊ���߳�
	private void buildStreamthreads(int size, ArrayList<EarthSqure> ess) {
		ArrayList<EarthSqure> smallList = new ArrayList<EarthSqure>();
		for (int i = 0; i < ess.size(); i++) {
			// ������̵߳ļ�������С��size������������������߳�
			if (!insert2Thread(ess.get(i))) {
				if (smallList.size() < size) {
					smallList.add(ess.get(i));
				} else {
					TwitterStreamThread tst = new TwitterStreamThread(
							smallList, tt);
					ThreadsPool.addTwitterStreamThread(tst);
					try {
						sleep(time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					smallList = new ArrayList<EarthSqure>();
				}

			}
		}
		if (smallList.size() > 0) {
			TwitterStreamThread tst = new TwitterStreamThread(smallList, tt);
			ThreadsPool.addTwitterStreamThread(tst);
		}

	}

	private boolean insert2Thread(EarthSqure earthSqure) {
		for (int j = 0; j < ThreadsPool.getTwitterStreamThreadsNum(); j++) {

		}
		return false;
	}

	/**
	 * ��ֹ�̵߳ķ���
	 */
	@Override
	public void stopMe() {
		isRunning = false;
		if (this.isAlive()) {
			super.interrupt();
		}
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
