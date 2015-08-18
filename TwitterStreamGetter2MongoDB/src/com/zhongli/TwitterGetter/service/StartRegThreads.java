package com.zhongli.TwitterGetter.service;

import java.util.ArrayList;

import com.zhongli.TwitterGetter.dao.InfoGetterDAO;
import com.zhongli.TwitterGetter.dao.impl.InfoGetterDAO_MySQL;
import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.model.RegInfo;


/**
 * 1.ɨ��Reg���ݿ⣬��ȡ����״̬Ϊ3�����򲢽����е�reg�����С������� 2.����С����������ҵ�Stream���鲢���޸�Stream�����״̬
 * 
 * @author zhonglili
 *
 */
public class StartRegThreads extends ServiceThread {
	private int time;
	private boolean isRunning = false;
	private InfoGetterDAO db;

	public StartRegThreads(int time) {
		this.settName(this.getClass().getSimpleName());
		this.time = time;
		db = new InfoGetterDAO_MySQL();
	}

	@Override
	public void run() {
		super.run();
		isRunning = true;
		ArrayList<RegInfo> regs;
		// ѭ��ɨ��
		while (isRunning) {
			// 1.ɨ��Reg���ݿ⣬��ȡ����״̬Ϊ3�����򲢽����е�reg�����С�������
			regs = (ArrayList<RegInfo>) db.getRegInfo(3);
			// 2.����С����������ҵ�Stream���鲢���޸�Stream�����״̬
			for (int i = 0; i < regs.size(); i++) {
				ArrayList<EarthSqure> ess = (ArrayList<EarthSqure>) db
						.getStreamSqures(regs.get(i));
				// ѭ��������״̬����ʹ�ô���+1
				for (int j = 0; j < ess.size(); j++) {
					// ���������޸����������ݿ��е�״̬,����ʹ�ô���+1
					db.squreAddUseTime(ess.get(j).getRow(), ess.get(j).getCol());
				}
				// �޸Ĵ�����״̬Ϊ���ڼ���״̬��1��
				db.changeRegState(regs.get(i).getRegID(), 1);
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
