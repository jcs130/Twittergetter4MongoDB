package com.zhongli.TwitterGetter.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.zhongli.TwitterGetter.dao.InfoGetterDAO;
import com.zhongli.TwitterGetter.model.EarthSqure;
import com.zhongli.TwitterGetter.model.LocArea;
import com.zhongli.TwitterGetter.model.LocPoint;
import com.zhongli.TwitterGetter.model.RegInfo;

public class InfoGetterDAO_MySQL implements InfoGetterDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public InfoGetterDAO_MySQL() {
		dataSource = new InfoDBHelper();
	}

	@Override
	public List<EarthSqure> getSqureInfo(int type) {
		// ���ݿ��ѯ���
		String sqlString = "SELECT * FROM earthsqure where streamstate ="
				+ type + ";";
		ArrayList<EarthSqure> result = new ArrayList<EarthSqure>();
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			EarthSqure squre = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				squre = new EarthSqure(rs.getDouble("south"),
						rs.getDouble("north"), rs.getDouble("west"),
						rs.getDouble("east"), rs.getInt("row"),
						rs.getInt("col"), rs.getDouble("degreepersqure"));
				squre.setSqureID(rs.getInt("squreid"));
				squre.setStreamState(rs.getInt("streamstate"));
				squre.setUseTimes(rs.getInt("usetimes"));
				squre.setThreadName(rs.getString("threadname"));
				result.add(squre);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	@Override
	public List<RegInfo> getRegInfo(int type) {
		// ����type��ȡ���������Ϣ
		String sqlString = "SELECT * FROM regnames where streamstate =" + type
				+ ";";
		ArrayList<RegInfo> result = new ArrayList<RegInfo>();
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			RegInfo reg = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reg = new RegInfo(rs.getString("regname"));
				reg.setRegID(rs.getInt("regid"));
				getAreasByReg(reg);
				// System.out.println(reg);
				result.add(reg);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		// System.out.println(result.size());
		return result;
	}

	// ��ȡ��С�����ϵ����Ӵ������µ�С����
	private void getAreasByReg(RegInfo reg) {
		// ����б�Ϊ��������б�
		if (reg.getAreas().size() != 0) {
			reg.getAreas().clear();
		} else {

			// �Ȳ�ѯregandarea��õ������������С������
			ArrayList<Integer> areaIDs = new ArrayList<Integer>();
			String sqlString = "SELECT * FROM regandarea where regid="
					+ reg.getRegID() + ";";
			// ��ѯ���ݿ⣬��ȡ���
			Connection conn = null;
			try {
				conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					areaIDs.add(rs.getInt("areaid"));
				}
				// ���ݱ����Ӿ���Ķ���
				for (int i = 0; i < areaIDs.size(); i++) {
					sqlString = "SELECT * FROM interestareas where areaid="
							+ areaIDs.get(i) + ";";
					ps = conn.prepareStatement(sqlString);
					rs = ps.executeQuery();
					while (rs.next()) {
						LocArea loc = new LocArea(areaIDs.get(i),
								rs.getDouble("north"), rs.getDouble("west"),
								rs.getDouble("south"), rs.getDouble("east"));
						loc.setCenterAndRange(
								new LocPoint(rs.getDouble("center_lat"), rs
										.getDouble("center_lon")), rs
										.getInt("range"));
						reg.getAreas().add(loc);
					}
				}

			} catch (SQLException e) {
				throw new RuntimeException(e);

			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}

		}
	}

	@Override
	public void saveEarthSqure(double south, double north, double west,
			double east, int row, int col, double degreepersqure) {
		// ���ж����ݿ��д治������ͬ�����飬����������½�һ������
		if (!haveSqure(row, col)) {

			Connection conn = null;
			String sqlString = "INSERT INTO earthsqure (south, north, west, east, row, col,degreepersqure) VALUES ("
					+ south
					+ ", "
					+ north
					+ ", "
					+ west
					+ ", "
					+ east
					+ ", "
					+ row + ", " + col + ", " + degreepersqure + ");";
			try {
				conn = dataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sqlString);
				ps.executeUpdate();
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
					}
				}
			}

		} else {
			// ������������������ʲôҲ����
		}
	}

	/**
	 * ��ѯ���ݿ����Ƿ���ָ��������
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean haveSqure(int row, int col) {
		EarthSqure es = getSqureInfo(row, col);
		if (es == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void saveEarthSqure(EarthSqure es) {
		saveEarthSqure(es.getSouth(), es.getNorth(), es.getWest(),
				es.getEast(), es.getRow(), es.getCol(),
				es.getDegreePerSqure_lon());

	}

	@Override
	public EarthSqure getSqureInfo(int row, int col) {
		// ����type��ȡ���������Ϣ
		String sqlString = "SELECT * FROM earthsqure where row =" + row
				+ "&& col =" + col + ";";
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		EarthSqure squre = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				squre = new EarthSqure(rs.getDouble("south"),
						rs.getDouble("north"), rs.getDouble("west"),
						rs.getDouble("east"), rs.getInt("row"),
						rs.getInt("col"), rs.getDouble("degreepersqure"));
				squre.setSqureID(rs.getInt("squreid"));
				squre.setStreamState(rs.getInt("streamstate"));
				squre.setUseTimes(rs.getInt("usetimes"));
				squre.setThreadName(rs.getString("threadname"));
			}
		} catch (SQLException e) {
			// throw new RuntimeException(e);
			e.printStackTrace();

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return squre;
	}

	@Override
	public List<LocArea> getAreaByLoc(double north, double south, double west,
			double east) {
		// ���ݿ��ѯ���
		String sqlString = "SELECT * FROM interestareas where center_lat<="
				+ north + "&&center_lat>=" + south + "&&center_lon>=" + west
				+ "&&center_lon<=" + east + ";";
		ArrayList<LocArea> result = new ArrayList<LocArea>();
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				LocArea loc = new LocArea(rs.getInt("areaid"),
						rs.getDouble("north"), rs.getDouble("west"),
						rs.getDouble("south"), rs.getDouble("east"));
				loc.setCenterAndRange(new LocPoint(rs.getDouble("center_lat"),
						rs.getDouble("center_lon")), rs.getInt("range"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	// @Override
	// public List<EarthSqure> getSquresByLoc(double north, double south,
	// double west, double east) {
	// // ��ȡ�ĸ���������ڵ����飬����֮������鶼���
	// return null;
	// }

	@Override
	public List<EarthSqure> getStreamSqures(RegInfo reg) {
		ArrayList<LocArea> areas = reg.getAreas();
		ArrayList<EarthSqure> result = new ArrayList<EarthSqure>();
		// System.out.println();
		// System.out.println(reg.getRegName());
		// ѭ���õ�Stream����
		for (int i = 0; i < areas.size(); i++) {
			// System.out.println("area:" + (i + 1));
			// ÿ�������ĸ�����ֱ����
			// NW
			EarthSqure e1 = new EarthSqure(areas.get(i).getNorth(), areas
					.get(i).getWest());
			addToArray(result, e1);
			// NE
			EarthSqure e2 = new EarthSqure(areas.get(i).getNorth(), areas
					.get(i).getEast());
			addToArray(result, e2);
			// SW
			EarthSqure e3 = new EarthSqure(areas.get(i).getSouth(), areas
					.get(i).getWest());
			addToArray(result, e3);
			// SE
			EarthSqure e4 = new EarthSqure(areas.get(i).getSouth(), areas
					.get(i).getEast());
			addToArray(result, e4);
			// System.out.println("fill:");
			// ����ĸ��������ڵ�����֮����ڿ�϶�����ӿ�϶���������
			// ���Ȳ�ȫ�������߽������
			for (int j = e1.getCol(); j < e2.getCol(); j++) {
				EarthSqure e = new EarthSqure(e1.getRow(), j);
				addToArray(result, e);
			}
			for (int j = e3.getCol(); j < e4.getCol(); j++) {
				EarthSqure e = new EarthSqure(e3.getRow(), j);
				addToArray(result, e);
			}

			// ��ʼ��ȫ�м����
			for (double j = e3.getNorth(); j < e1.getSouth(); j += 0.15) {
				// ��һ�д���������ȫ
				EarthSqure ew = new EarthSqure(j, areas.get(i).getWest());
				EarthSqure ee = new EarthSqure(j, areas.get(i).getEast());
				addToArray(result, ew);
				addToArray(result, ee);
				for (int k = ew.getCol(); k < ee.getCol(); k++) {
					EarthSqure e = new EarthSqure(ew.getRow(), k);
					addToArray(result, e);
				}
			}

		}
		return result;
	}

	private boolean addToArray(ArrayList<EarthSqure> list, EarthSqure e) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getRow() == e.getRow()
					&& list.get(i).getCol() == e.getCol()) {
				return false;
			}
		}
		// System.out.println(e);
		list.add(e);
		return true;

	}

	@Override
	public void changeRegState(int id, int type) {
		String sqlString = "UPDATE regnames SET streamstate=? WHERE regID=?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setInt(1, type);
			ps.setInt(2, id);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void changeSqureState(int id, int type, String threadname) {
		String sqlString = "UPDATE earthsqure SET streamstate=?, threadname=? WHERE squreid=?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setInt(1, type);
			ps.setString(2, threadname);
			ps.setInt(3, id);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void squreAddUseTime(int row, int col) {
		String sqlString = "UPDATE earthsqure SET usetimes=usetimes+1 WHERE row=? && col=?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setInt(1, row);
			ps.setInt(2, col);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public void squreDelUseTime(int row, int col) {
		String sqlString = "UPDATE earthsqure SET usetimes=usetimes-1 WHERE row=? && col=?";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setInt(1, row);
			ps.setInt(2, col);
			ps.executeUpdate();

			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	@Override
	public List<EarthSqure> getReadySqure() {
		// ���ݿ��ѯ���
		String sqlString = "SELECT * FROM earthsqure where streamstate =0&&usetimes>0;";
		ArrayList<EarthSqure> result = new ArrayList<EarthSqure>();
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			EarthSqure squre = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				squre = new EarthSqure(rs.getDouble("south"),
						rs.getDouble("north"), rs.getDouble("west"),
						rs.getDouble("east"), rs.getInt("row"),
						rs.getInt("col"), rs.getDouble("degreepersqure"));
				squre.setSqureID(rs.getInt("squreid"));
				squre.setStreamState(rs.getInt("streamstate"));
				squre.setUseTimes(rs.getInt("usetimes"));
				squre.setThreadName(rs.getString("threadname"));
				result.add(squre);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	@Override
	public List<EarthSqure> getShouldStopSqure() {
		// ���ݿ��ѯ���
		String sqlString = "SELECT * FROM earthsqure where streamstate =1&&usetimes<=0;";
		ArrayList<EarthSqure> result = new ArrayList<EarthSqure>();
		// ��ѯ���ݿ⣬��ȡ���
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			EarthSqure squre = null;
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				squre = new EarthSqure(rs.getDouble("south"),
						rs.getDouble("north"), rs.getDouble("west"),
						rs.getDouble("east"), rs.getInt("row"),
						rs.getInt("col"), rs.getDouble("degreepersqure"));
				squre.setSqureID(rs.getInt("squreid"));
				squre.setStreamState(rs.getInt("streamstate"));
				squre.setUseTimes(rs.getInt("usetimes"));
				squre.setThreadName(rs.getString("threadname"));
				result.add(squre);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	@Override
	public void reSetStates() {
		String sqlString = "UPDATE earthsqure SET streamstate=0, usetimes=0, threadname=? WHERE squreID>0;";
		String sqlString2 = "UPDATE regnames SET streamstate=0 WHERE regid>0;";
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sqlString);
			ps.setString(1, "none");
			ps.executeUpdate();
			ps.close();
			ps = conn.prepareStatement(sqlString2);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}
}
