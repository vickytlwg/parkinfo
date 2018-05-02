package cn.parkinfo.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.parkinfo.dao.MonthuserDAO;
import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Monthuser;
import cn.parkinfo.service.MonthUserService;
@Transactional
@Service
public class MonthUserServiceImpl implements MonthUserService {

	@Autowired
	private MonthuserDAO monthUserDao;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return monthUserDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.insert(record);
	}

	@Override
	public int insertSelective(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.insertSelective(record);
	}

	@Override
	public Monthuser selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return monthUserDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.updateByPrimaryKey(record);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return monthUserDao.getCount();
	}

	@Override
	public List<Monthuser> getByStartAndCount(int start, int count) {
		// TODO Auto-generated method stub
		return monthUserDao.getByStartAndCount(start, count);
	}

	@Override
	public int updateByPrimaryKeySelective(Monthuser record) {
		// TODO Auto-generated method stub
		return monthUserDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public List<Monthuser> getByPlateNumber(String platenumber) {
		// TODO Auto-generated method stub
		return monthUserDao.getByPlateNumber(platenumber);
	}

	@Override
	public int getCountByParkId(int parkId) {
		// TODO Auto-generated method stub
		return monthUserDao.getCountByParkId(parkId);
	}

	@Override
	public List<Monthuser> getByParkIdAndCount(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return monthUserDao.getByParkIdAndCount(parkId, start, count);
	}

	@Override
	public List<Monthuser> getByStartAndCountOrder(int start, int count, int type) {
		// TODO Auto-generated method stub
		return monthUserDao.getByStartAndCountAndOrder(start, count, type);
	}

	@Override
	public List<Monthuser> getByParkIdAndCountOrder(int parkId, int start, int count, int type) {
		// TODO Auto-generated method stub
		return monthUserDao.getByParkIdAndCountOrder(parkId, start, count, type);
	}

	@Override
	public List<Monthuser> getByUsernameAndPark(String username, int parkId) {
		// TODO Auto-generated method stub
		return monthUserDao.getByUsernameAndPark(username, parkId);
	}

	@Override
	public List<Monthuser> getByCarnumberAndPark(String carnumber, int parkId) {
		// TODO Auto-generated method stub
		return monthUserDao.getByCarnumberAndPark(carnumber, parkId);
	}
	
	@Override
	public List<Monthuser> getByCarnumberAndPark2(String platenumber, int parkId) {
		// TODO Auto-generated method stub
		return monthUserDao.getByCarnumberAndPark2(platenumber, parkId);
	}

	@Override
	public List<Monthuser> getByPark(int parkId) throws ParseException {
		// TODO Auto-generated method stub
		return monthUserDao.getByParkIdAndCount(parkId,0,1000);
	}

	@Override
	public List<Monthuser> getByParkAndDayRange(int parkId, Date starttime, Date endtime) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date dstartDate=sFormat.parse(starttime+" 00:00:00");
		Date dendDate=sFormat.parse(endtime+" 23:59:59");
		return monthUserDao.getByRange(parkId, starttime, endtime);
	}

}
