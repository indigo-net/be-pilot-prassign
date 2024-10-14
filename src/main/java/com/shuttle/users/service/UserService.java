package com.shuttle.users.service;

import java.util.List;
import java.util.Map;

import com.shuttle.users.model.dto.User;

public interface UserService {
	Map<String, List<User>> selectAll();
	void regist(User user);
	void deleteAll();
	void delete(String uuid);
	void update(String uuid, String status);
}
