package cn.parkinfo.service;

import java.util.List;

import cn.parkinfo.model.User;
import cn.parkinfo.model.UserDetail;

public interface UserService {

	List<User> getUsers();
	
	public List<UserDetail> getUserDetail(int low, int count);
	
	public int getUserCount();
	
	String insertUser(User userItem);
	
	public int updateUser(User user);
	
	String getUserPassword(String userName);
	
	public boolean userExistByUserName(String userName);
	
	public boolean userExistByNumber(String number);
	
	public int getUserIdByNumber(String number);
	
	public User getUserByUsername(String username);
	
	public int changeUserPassword(String username, String password);
}
