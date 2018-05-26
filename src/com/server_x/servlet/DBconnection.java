package com.server_x.servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

public class DBconnection {

	private static String url = "jdbc:mysql://127.0.0.1:3306/permission_application";
	// 202.191.58.39
	private static String driverName = "com.mysql.jdbc.Driver";
	private static String username = "root";
	private static String password = "";
	private static Connection con;
	private static String urlstring;

	public static Connection getConnection() {
		try {
			Class.forName(driverName);
			try {
				con = DriverManager.getConnection(url, username, password);
			} catch (SQLException ex) {
				System.out.println("Failed to create the database connection.");
			}
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found.");
		}
		return con;
	}

	public static ResultSet getUser(PreparedStatement Stmt, Connection conn) {
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = (Statement) conn.createStatement();
			String query = "SELECT * FROM user";
			rs = (ResultSet) stmt.executeQuery(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static void createTable(PreparedStatement Stmt, Connection conn, String imei) {
		try {
			Stmt = conn.prepareStatement("CREATE TABLE transaction_" + imei
					+ "(packet VARCHAR(64) NOT NULL PRIMARY KEY, permission VARCHAR(64),timeScan VARCHAR(64))");
			Stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void createTableUser(PreparedStatement Stmt, Connection conn) {
		try {
			Stmt = conn.prepareStatement(
					"CREATE TABLE user(ten VARCHAR(64) NOT NULL, lop VARCHAR(64) NOT NULL, malop VARCHAR(64) NOT NULL, masinhvien VARCHAR(64) NOT NULL,mahocphan VARCHAR(64) NOT NULL, imei VARCHAR(64) NOT NULL)");
			Stmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean checkTable(PreparedStatement Stmt, Connection conn, String imei) {
		Boolean exist = false;
		try {
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = (ResultSet) dbm.getTables(null, null, "transaction_" + imei, null);
			if (!rs.next()) {
				exist = false;
			} else {
				exist = true;
				System.out.println("already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}

	public static Boolean checkTableUser(PreparedStatement Stmt, Connection conn) {
		Boolean exist = false;
		try {
			DatabaseMetaData dbm = (DatabaseMetaData) conn.getMetaData();
			ResultSet rs = (ResultSet) dbm.getTables(null, null, "user", null);
			if (!rs.next()) {
				exist = false;
			} else {
				exist = true;
				System.out.println(" table user already exists");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return exist;
	}

	public static void insertTable(PreparedStatement Stmt, Connection conn, String list, String imei) {
		Statement st = null;
		try {
			String[] list1 = list.split(":");
			if (list1[1].length() > 0) {
				if (DBconnection.checkRecord(conn, st, list1, imei) == false) {
					String query = " insert into transaction_" + imei + " (packet, permission, timeScan)"
							+ " values (?, ?, ?)";
					Stmt = conn.prepareStatement(query);
					Stmt.setString(1, list1[0]);
					Stmt.setString(2, list1[1]);
					Stmt.setString(3, list1[2]);
					Stmt.execute();
				}
			} else {
				if (DBconnection.checkRecord(conn, st, list1, imei) == false) {
					String query = " insert into transaction_" + imei + " (packet, permission, timeScan)"
							+ " values (?, ?, ?)";
					Stmt = conn.prepareStatement(query);
					Stmt.setString(1, "" + list1[0]);
					Stmt.setString(2, " ");
					Stmt.setString(3, list1[2]);
					Stmt.execute();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void insertTableUser(PreparedStatement Stmt, Connection conn, User user) {
		Statement st = null;
		try {
			if (DBconnection.checkUser(conn, st, user) == false) {
				String query = " insert into user(ten , lop , malop, masinhvien, mahocphan, imei) values (?, ?, ?, ?, ?, ?)";
				Stmt = conn.prepareStatement(query);
				Stmt.setString(1, user.getTen());
				Stmt.setString(2, user.getLop());
				Stmt.setString(3, user.getMalop());
				Stmt.setString(4, user.getMssv());
				Stmt.setString(5, user.getMahocphan());
				Stmt.setString(6, user.getImei());
				Stmt.execute();
			} else {
				String query = "UPDATE user SET ten = " + user.getTen() + ", lop = " + user.getLop() + ", masinhvien = "
						+ user.getMssv() + ",malop = " + user.getMalop() + ", mahocphan = " + user.getMahocphan()
						+ " WHERE imei = " + user.getImei();
				Stmt.executeUpdate(query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateUser(PreparedStatement Stmt, Connection conn, User user) {

		try {
			Stmt = conn.prepareStatement(
					"UPDATE user SET ten = ?, malop = ?, lop = ?, mahocphan = ?, masinhvien = ?  WHERE imei = ?");
			Stmt.setString(1, user.getTen());
			Stmt.setString(2, user.getMalop());
			Stmt.setString(3, user.getLop());
			Stmt.setString(4, user.getMahocphan());
			Stmt.setString(5, user.getMssv());
			Stmt.setString(6, user.getImei());
			Stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Boolean checkRecord(Connection conn, Statement st, String[] list, String imei) {
		ResultSet rs = null;
		int count = 0;
		Boolean check = false;
		try {
			st = (Statement) conn.createStatement();
			rs = (ResultSet) st.executeQuery(" select * from transaction_" + imei + " where packet='" + list[0] + "'");
			while (rs.next()) {
				count++;
			}
			if (count > 0) {
				check = true;
			} else {
				check = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

	public static Boolean checkUser(Connection conn, Statement st, User user) {
		ResultSet rs = null;
		int count = 0;
		Boolean check = false;
		try {
			st = (Statement) conn.createStatement();
			rs = (ResultSet) st.executeQuery(" select * from user where imei='" + user.getImei() + "'");
			while (rs.next()) {
				count++;
			}
			if (count > 0) {
				check = true;
			} else {
				check = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check;
	}

}
