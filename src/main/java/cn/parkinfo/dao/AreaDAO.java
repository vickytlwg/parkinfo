package cn.parkinfo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.parkinfo.model.Area;

public interface AreaDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Area record);

    int insertSelective(Area record);

    Area selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Area record);

    int updateByPrimaryKey(Area record);
    
    int getCount();
    
    List<Area> getByStartAndCount(@Param("start")int start,@Param("count")int count);
    
    List<Area> getByZoneCenterId(@Param("zoneid")int zoneid);
}