package cn.parkinfo.service;

import java.util.List;

import cn.parkinfo.model.Street;

public interface StreetService {
	 int deleteByPrimaryKey(Integer id);

	    int insert(Street record);

	    int insertSelective(Street record);

	    Street selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(Street record);

	    int updateByPrimaryKey(Street record);
	    
	    int getCount();
	    
	    List<Street> getByStartAndCount(int start,int count);
	    
	    List<Street> getByArea(int areaId);
}
