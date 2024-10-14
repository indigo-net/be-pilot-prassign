package com.shuttle.users.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shuttle.users.model.dto.User;
import com.shuttle.util.DBUtil;

public class UserDaoImpl implements UserDao {

	private DBUtil util = DBUtil.getInstance();
	private static UserDao instance = new UserDaoImpl();
	
	private UserDaoImpl() {}
	
	public static UserDao getInstance() {
		return instance;
	}
	
	@Override
	public List<User> selectAll() throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = util.getConnection();
			String sql = "select * from users";
			stmt = con.prepareStatement(sql);
			
			rs = stmt.executeQuery();
			List<User> users = new ArrayList<>(60);
			while(rs.next()) {
				User user = new User();
				user.setUuid(rs.getString("uuid"));
				user.setUserName(rs.getString("userName"));
				user.setArriveTimeStamp(rs.getString("arriveTimeStamp"));
				user.setStatus(rs.getString("status"));
				user.setFcmToken(sql);
				users.add(user);
			}
			return users;
		} finally {
			util.close(con, stmt);;
		}
	}

	@Override
	public void insert(User user) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = util.getConnection();
			String sql = "insert into users values(?,?,?,?,?)";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setString(idx++, user.getUuid());
			stmt.setString(idx++, user.getUserName());
			stmt.setString(idx++, user.getArriveTimeStamp());
			stmt.setString(idx++, user.getStatus());
			stmt.setString(idx++, user.getFcmToken());
			stmt.executeUpdate();
		} finally {
			util.close(con, stmt);
		}
	}

	@Override
	public void deleteAll() throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = util.getConnection();
			String sql = "delete from users";
			stmt = con.prepareStatement(sql);
			stmt.executeUpdate();
		} finally {
			util.close(con, stmt);
		}
	}

	@Override
	public void delete(String uuid) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = util.getConnection();
			String sql = "delete from users where uuid=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, uuid);
			stmt.executeUpdate();
		} finally {
			util.close(con, stmt);
		}
	}

	@Override
	public void update(String uuid, String status) throws SQLException {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = util.getConnection();
			String sql = "update users set status=? where uuid=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, status);
			stmt.setString(2, uuid);
			stmt.executeUpdate();
		} finally {
			util.close(con, stmt);
		}
	}

}
