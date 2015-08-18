package com.zhongli.TwitterGetter.dao;

import java.util.List;

import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.model.LocArea;
import com.zhongli.TwitterGetter.model.RegInfo;


/**
 * ���ݿ�����ӿ���
 * 
 * @author John
 *
 */
public interface InfoGetterDAO {
	
	/**
	 * �������ݿ��״̬
	 */
	public void reSetStates();
	
	/**
	 * �����ݿ��л�ȡ�����������Ϣ
	 * 
	 * @param type
	 *            0:�����δ������ 1:���ڼ����� 2:���رյ�
	 * @return
	 */
	public List<RegInfo> getRegInfo(int type);

	/**
	 * �ı������״̬
	 * 
	 * @param id
	 * @param type
	 * @param usetimes
	 */
	public void changeRegState(int id, int type);

	// /**
	// * ��ȡָ��������������Stream�����Լ������״̬
	// *
	// * @param north
	// * @param south
	// * @param west
	// * @param east
	// * @return
	// */
	// public List<EarthSqure> getSquresByLoc(double north, double south,
	// double west, double east);

	/**
	 * ��ȡָ�������ڵ������û��Զ�������
	 * 
	 * @param north
	 * @param south
	 * @param west
	 * @param east
	 * @return
	 */
	public List<LocArea> getAreaByLoc(double north, double south, double west,
			double east);

	/**
	 * �����ݿ��д洢Stream����
	 * 
	 * @param south
	 * @param north
	 * @param west
	 * @param east
	 * @param row
	 * @param col
	 * @param degreepersqure
	 */
	public void saveEarthSqure(double south, double north, double west,
			double east, int row, int col, double degreepersqure);

	/**
	 * �����ݿ��д洢Stream����
	 * 
	 * @param es
	 */
	public void saveEarthSqure(EarthSqure es);

	/**
	 * �������в�ѯ����
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public EarthSqure getSqureInfo(int row, int col);

	/**
	 * �����ݿ��л�ȡ�����������Ϣ
	 * 
	 * @param type
	 *            0:�����δ������ 1:���ڼ����� 2:���رյ�
	 * @return
	 */
	public List<EarthSqure> getSqureInfo(int type);

	/**
	 * �����ݿ��л�ȡʹ�ô���>0����û�б����ӵ�Stream����
	 * 
	 * @return
	 */
	public List<EarthSqure> getReadySqure();

	/**
	 * �����ݿ��л�ȡʹ�ô���Ϊ0����û��ֹͣ���ӵĿ�
	 * 
	 * @return
	 */
	public List<EarthSqure> getShouldStopSqure();

	/**
	 * �ı�ָ�������״̬����
	 * 
	 * @param id
	 * @param type
	 * @param threadname
	 */
	public void changeSqureState(int id, int type, String threadname);

	/**
	 * ���������ʹ�ô��� +1
	 */
	public void squreAddUseTime(int row, int col);

	/**
	 * ���������ʹ�ô��� -1
	 */
	public void squreDelUseTime(int row, int col);

	/**
	 * �����������󣬱���С����Ȼ��С�����ĸ������ڵ�Stream�����ǳ�����
	 * 
	 * @param reg
	 * @return
	 */
	public List<EarthSqure> getStreamSqures(RegInfo reg);

}
