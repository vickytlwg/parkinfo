package cn.parkinfo.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.parkinfo.dao.AuthorityDAO;
import cn.parkinfo.dao.UserParkDAO;
import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.UserPark;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.UserParkService;

@Transactional
@Service
public class AuthorityServiceImpl implements AuthorityService{

	@Autowired
	private AuthorityDAO authDao;
	
	@Autowired
	private UserParkDAO userParkDao;
	
	@Override
	public boolean checkUserAccess(String username, String password) {
		AuthUser user = authDao.getUser(username);
		if(user == null)
			return false;
		return password.equals(user.getPassword());
	}

	
	@Override
	public AuthUser getUserByUsername(String username) {
		
		return authDao.getUser(username);
	}

	@Override
	public List<AuthUser> getUsers() {
		return authDao.getUsers();
	}

	
	@Override
	public String getUserPasswd(String username) {
		return authDao.getUserPasswd(username);
	}

	@Override
	public AuthUser getUser(String username) {
		return authDao.getUser(username);
	}

	
	@Override
	public int insertUser(AuthUser user, List<Integer> parkIds) {
		authDao.insertUser(user);
		AuthUser insertedUser = authDao.getUser(user.getUsername());
		
		if(insertedUser == null)
			return 0;
		
		for(Integer parkId : parkIds){
			UserPark userParkMap = new UserPark();
			userParkMap.setUserId(insertedUser.getId());
			userParkMap.setParkId(parkId);
			
			userParkDao.insertUserParkMap(userParkMap);
		}
		return 1;
			
	}
	
	

	@Override
	public int deleteUser(int id) {
		userParkDao.deleteUserParkMap(id);
		return authDao.deleteUser(id);
	}

	@Override
	public int updateUser(AuthUser user,  List<Integer> parkIds) {
		userParkDao.deleteUserParkMap(user.getId());
		for(Integer id : parkIds){
			UserPark userParkMap = new UserPark();
			userParkMap.setUserId(user.getId());
			userParkMap.setParkId(id);
			
			userParkDao.insertUserParkMap(userParkMap);
		}
		return authDao.updateUser(user);
	}



	@Override
	public int getUserCount() {
		
		return authDao.getUserCount();
	}



	@Override
	public List<String> getOwnUserParkName(int userId) {
	
		return userParkDao.getOwnParkName(userId);
	}

	
}
