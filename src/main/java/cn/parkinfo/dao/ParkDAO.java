package cn.parkinfo.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.parkinfo.model.Park;
import cn.parkinfo.model.ParkDetail;
//import cn.parkinfo.model.ParkNews;

@Repository
public interface ParkDAO {

	public List<Park> getParks();
	
	public Park getParkById(@Param("id")int id);
	
	public int nameToId(String name);
	
	public List<Park> getParkByName(String name);
	
	public List<ParkDetail> getParkDetail(@Param("low")int low, @Param("count")int count);
	
	public List<ParkDetail> getOutsideParkDetail(@Param("low")int low, @Param("count")int count);
	
	public List<Park> getParkDetailByKeywords(@Param("param1")String keywords);
	
	public List<Park> getOutsideParkByStreetId(@Param("streetId")int streetId);
	
	public Park getLastPark();
	
	public int getParkCount();
	
	public int getOutsideParkCount();
	
	public int insertPark(Park park);
	
	public int insertParkList(List<Park> parks);
	
	public int updatePark(Park park);
	
	public int updateLeftPortCount(@Param("id")int parkId, @Param("portLeftCount") int leftPortCount);
	
	public int deletePark(int id);
	
	//查询停车场总金额
	public List<Park> getParkByMoney(Map<String, Object> map);
	
	
}
