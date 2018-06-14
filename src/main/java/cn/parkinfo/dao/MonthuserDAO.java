package cn.parkinfo.dao;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.parkinfo.model.Monthuser;
import cn.parkinfo.model.PosChargeData;
@Repository
public interface MonthuserDAO {
	
    int deleteByPrimaryKey(Integer id);

    int insert(Monthuser record);

    int insertSelective(Monthuser record);

    Monthuser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Monthuser record);

    int updateByPrimaryKey(Monthuser record);
    
    int getCount();
    
    int getCountByParkId(@Param("parkId")int parkId);
    
    List<Monthuser> getByStartAndCount(@Param("start")int start,@Param("count")int count);
    
    List<Monthuser> getByUsernameAndPark(@Param("username")String username,@Param("parkId")int parkId);
    
    List<Monthuser> getByCarnumberAndPark(@Param("carnumber")String carnumber,@Param("parkId")int parkId);
    
    List<Monthuser> getByCarnumberAndPark2(@Param("platenumber")String platenumber,@Param("parkId")int parkId);
    
    List<Monthuser> getByStartAndCountAndOrder(@Param("start")int start,@Param("count")int count,@Param("type")int type);
    
    List<Monthuser> getByPlateNumber(@Param("platenumber")String platenumber);
    
    List<Monthuser> getByPlateNumber22(@Param("platenumber")String platenumber,@Param("parkId")int parkId);
    
    List<Monthuser> getByParkIdAndCount(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
    
    List<Monthuser> getByParkIdAndCountOrder(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count,@Param("type")int type);

    List<Monthuser> getByRange(@Param("parkId")int parkId,@Param("starttime")Date starttime,@Param("endtime")Date endtime);
}