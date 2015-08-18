package com.zhongli.TwitterGetter.service;

import java.util.ArrayList;

import com.zhongli.TwitterGetter.dao.InfoGetterDAO;
import com.zhongli.TwitterGetter.dao.impl.InfoGetterDAO_MySQL;
import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.model.RegInfo;


/**
 * ɨ������������������߳�
 * 
 * @author zhonglili
 *
 */
public class ScanRegsThread extends ServiceThread {
	private int time;
	private boolean isRunning = false;
	private InfoGetterDAO db;

	public ScanRegsThread(int time) {
		this.time = time;
		this.settName(this.getClass().getSimpleName());
		this.db = new InfoGetterDAO_MySQL();
	}

	@Override
	public void run() {
		super.run();
		isRunning = true;

		ArrayList<RegInfo> regs;
		// ѭ��ɨ��
		while (isRunning) {
			// 1.ɨ��reg���ݿ⣬��ȡ����״̬Ϊ0�����򲢽����е�reg�����С�������
			regs = (ArrayList<RegInfo>) db.getRegInfo(0);
			// 2.����С��������������ݿ������Stream����
			for (int i = 0; i < regs.size(); i++) {
				ArrayList<EarthSqure> ess = (ArrayList<EarthSqure>) db
						.getStreamSqures(regs.get(i));
				for (int j = 0; j < ess.size(); j++) {
					// ��ȷ�����������С���ٴ������ݿ�
					db.saveEarthSqure(ess.get(j));
				}
				// �޸Ĵ�����״̬Ϊ������״̬��3��
				db.changeRegState(regs.get(i).getRegID(), 3);
			}
			try {
				sleep(time);
			} catch (InterruptedException e) {
				isRunning = false;
				System.out.println("�߳�:" + this.gettName() + "����....");
				// e.printStackTrace();
			}
		}
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
