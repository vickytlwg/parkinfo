package cn.parkinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.parkinfo.model.AuthUser;

@Repository
public interface AuthorityDAO {
	
	public List<AuthUser> getUsers();
	
	public int getUserCount();
	
	public String getUserPasswd(String username);
	
	public AuthUser getUser(String username);
	
	public int insertUser(AuthUser user);
	
	public int deleteUser(int id);
	
	public int updateUser(AuthUser user);
	
	
}
