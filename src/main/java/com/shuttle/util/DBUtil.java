package com.shuttle.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	static final String URL = "jdbc:sqlite:C:/Dev/workspace/prassign/resources/database.db";
	static final String DRIVER = "org.sqlite.JDBC";
	private static DBUtil instance = new DBUtil();
	
	private DBUtil() {
		try {
			Class.forName(DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static DBUtil getInstance() {
		return instance;
	}
	
	public Connection getConnection() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL);
			System.out.println("SQLite 연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("SQLite 연결 실패");
		}
		return con;
	}
	
	public void close(AutoCloseable ... acs) {
		for(AutoCloseable c: acs) {
			if(c!=null) {
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void createTable() {
		String sql = "CREATE TABLE IF NOT EXISTS users (\n"
				+ " uuid CHAR(36) PRIMARY KEY, \n"
				+ " userName VARCHAR(50) NOT NULL,\n"
				+ " arriveTimeStamp VARCHAR(50) NOT NULL,\n"
				+ " status VARCHAR(50) NOT NULL,\n"
				+ " fcmToken VARCHAR(255) NOT NULL);";
		try(Connection con = instance.getConnection();
			Statement stmt = con.createStatement()){
			stmt.execute(sql);
			System.out.println("테이블 생성 성공!");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("테이블 생성 실패");
		}
		
	}
	
	public static void main(String[] args) {
		DBUtil util = DBUtil.getInstance();
		util.createTable();
	}
	
}
