package com.shuttle.users.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shuttle.users.model.dao.UserDao;
import com.shuttle.users.model.dao.UserDaoImpl;
import com.shuttle.users.model.dto.User;

public class UserServiceImpl implements UserService {

	private static UserService instance = new UserServiceImpl();
	private UserDao dao = UserDaoImpl.getInstance();
	
	private UserServiceImpl() {}
	
	public static UserService getInstance() {
		return instance;
	}
	
	@Override
	public Map<String, List<User>> selectAll() {
		try {
			List<User> userlist = dao.selectAll();
			for(int i=0,end=userlist.size();i<end;i++) {
				User user = userlist.get(i);
				switch(user.getStatus()) {
				case "휴식":
					user.setStatus("REST");
					break;
				case "준비":
					user.setStatus("READY");
					break;
				case "게임":
					user.setStatus("GAME");
					break;
				}
			}
			Map<String, List<User>> data = new HashMap<>();
			data.put("list", userlist);
			return data;
		}catch (SQLException e) {
			throw new RuntimeException("selectAll SQL Exception 발생!");
		}
	}

	@Override
	public void regist(User user) {
		try {
			dao.insert(user);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("regist SQL Exception 발생!");
		}
	}

	@Override
	public void deleteAll() {
		try {
			dao.deleteAll();
		} catch (SQLException e) {
			throw new RuntimeException("deleteAll SQL Exception 발생!");
		}
	}

	@Override
	public void delete(String uuid) {
		try {
			dao.delete(uuid);
		} catch (SQLException e) {
			throw new RuntimeException("delete SQL Exception 발생!");
		}
	}

	@Override
	public void update(String uuid, String status) {
		try {
			dao.update(uuid, status);
		} catch (SQLException e) {
			throw new RuntimeException("update SQL Exception 발생!");
		}
	}

}
