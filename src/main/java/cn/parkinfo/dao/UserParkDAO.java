package cn.parkinfo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.parkinfo.model.ParkDetail;
import cn.parkinfo.model.UserPark;

@Repository
public interface UserParkDAO {

	public List<Integer> getOwnParkId(int userId);
	
	public List<String> getOwnParkName(int userId);
	
	public int insertUserParkMap(UserPark userParkMap);
	
	public int deleteUserParkMap(int userId);
}
