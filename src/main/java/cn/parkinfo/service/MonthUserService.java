package cn.parkinfo.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import cn.parkinfo.model.Monthuser;
import cn.parkinfo.model.PosChargeData;

public interface  MonthUserService {

	    int deleteByPrimaryKey(Integer id);

	    int insert(Monthuser record);

	    int insertSelective(Monthuser record);

	    Monthuser selectByPrimaryKey(Integer id);
	    
	    int updateByPrimaryKey(Monthuser record);
	    
	    int updateByPrimaryKeySelective(Monthuser record);
	    
	    int getCount();
	    
	    int getCountByParkId(int parkId);
	    
	    List<Monthuser> getByStartAndCount(int start,int count);
	    
	    List<Monthuser> getByStartAndCountOrder(int start,int count,int type);
	    
	    List<Monthuser> getByUsernameAndPark(String username,int parkId);
	    
	    List<Monthuser> getByCarnumberAndPark(String carnumber,int parkId);
	    
	    List<Monthuser> getByCarnumberAndPark2(String platenumber,int parkId);
	    
	    List<Monthuser> getByPlateNumber(String platenumber);
	    
	    List<Monthuser> getByPark(int parkId) throws ParseException;
	    
	    List<Monthuser> getByParkAndDayRange(int parkId, Date starttime,Date endtime) throws ParseException;
	    
	    List<Monthuser> getByParkIdAndCount(int parkId,int start,int count);
	    
	    List<Monthuser> getByParkIdAndCountOrder(int parkId,int start,int count,int type);
	    	    
}
