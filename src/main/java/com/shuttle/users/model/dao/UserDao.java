package com.shuttle.users.model.dao;

import java.sql.SQLException;
import java.util.List;

import com.shuttle.users.model.dto.User;

public interface UserDao {
	List<User> selectAll() throws SQLException;
	void insert(User user) throws SQLException;
	void deleteAll() throws SQLException;
	void delete(String uuid) throws SQLException;
	void update(String uuid, String status) throws SQLException;
}
