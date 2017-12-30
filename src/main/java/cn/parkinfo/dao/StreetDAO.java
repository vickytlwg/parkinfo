package cn.parkinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.parkinfo.model.Street;

public interface StreetDAO {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Street record);

    int insertSelective(Street record);

    Street selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Street record);

    int updateByPrimaryKey(Street record);
    
    int getCount();
    
    List<Street> getByStartAndCount(@Param("start")int start,@Param("count")int count);
    
    List<Street> getByArea(@Param("areaId")int areaId);
    
}