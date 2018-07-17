package cn.parkinfo.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.parkinfo.model.Outsideparkinfo;
import cn.parkinfo.model.PosChargeData;

public interface PosChargeDataService {
	
	public PosChargeData getById(int id);		
	
	public List<PosChargeData> get();
	
	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public Map<String, Object> getByDayDateDiffNoOut(int parkId,String day);
	
	public Map<String, Object> calMoneyByParkAndRange(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getByDateDiffNoOut(int parkId,int days,int start,int count);
	
	public List<PosChargeData> getParkingData(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getByParkDatetime(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getFreeData(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getChargeMoneyData(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getByParkIdAndCardNumber(Integer parkId,String cardNumber);
	
	public List<PosChargeData> getByCardNumberAndPark(String cardNumber,Integer parkId);
	
	public List<PosChargeData> getPageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getPageArrearage(int low, int count);
	
	public List<PosChargeData> getPageArrearageByParkId(int parkId,int start,int count);
	
	public List<PosChargeData> getByRange(int parkId,Date startDate,Date endDate);
	
	public List<PosChargeData> getAllByDay(String date) throws ParseException;
	
	public List<PosChargeData> getByParkAndDay(int parkId,String date) throws ParseException;
	
	public List<PosChargeData> getDebt (String cardNumber) throws Exception;
	
	public List<PosChargeData> getDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber);
	
	public List<PosChargeData> queryDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> queryCurrentDebt (String cardNumber,Date exitDate) throws Exception;
	
	public List<PosChargeData> getParkCarportStatusToday(int parkId);
	
	public int count();
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
		
	public PosChargeData pay(String cardNumber, double money) throws Exception;
	
	public List<PosChargeData> repay(String cardNumber, double money) throws Exception;
	
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception;
	
	public List<PosChargeData> selectPosdataByParkAndRange(Date startDay, Date endDay,int parkId);
	
	public List<PosChargeData> selectPosdataByParkAndRange2(Date startDay, Date endDay,int parkId);
	
	public Map<String, Object> getParkChargeByDay(int parkId, String day);
	
	//月份
	public List<PosChargeData> getMoneyByMonthsParkAndRange(Map<String, Object> map);
	
	public Map<String, Object> getParkChargeByDay2(int parkId, String day);
	
	public List<PosChargeData> getByCardNumber(Integer parkId,String cardNumber);
	
	public List<PosChargeData> getBySearchCardNumber(String cardNumber);
	
	public List<PosChargeData> getByParkName(String parkName);
	
	public List<PosChargeData> getByParkAuthority(String userName);
	
	public List<Map<String, Object>> getFeeOperatorChargeData(Date startDate,Date endDate);
	
	public List<Map<String, Object>> getCarTimesByDateRangeAndParkId(int parkId,Date startDate, Date endDate,int start,int count);
	
	public Map<String, Object> calInByParkAndRange(int parkId, Date startDate, Date endDate);
	
	public Map<String, Object> calOutByParkAndRange(int parkId, Date startDate, Date endDate);
	
	List<PosChargeData> getByParkAndDayRange(int parkId, String startDate,String endDate) throws ParseException;

	List<PosChargeData> getAllByDayRange(String startDate, String endDate) throws ParseException;
	
	public Outsideparkinfo getOutsideparkinfoByOrigin(int parkId,String day);

	public Map<String, Object> getParkChargeCountByDay(int parkId, String day);

	/*List<PosChargeData> getByCardNumber(int parkId, String cardNumber);*/
}
