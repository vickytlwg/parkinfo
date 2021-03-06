package cn.parkinfo.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.parkinfo.model.Park;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.model.UserPark;

@Repository
public interface PosChargeDataDAO {
	
	public PosChargeData getById(int id);

	public List<PosChargeData> get();
	
	public List<PosChargeData> getPage(int low, int count);
	
	public List<PosChargeData> getByDateDiffNoOut(@Param("parkId")int parkId,@Param("days")int days,@Param("start")int start,@Param("count")int count);
	
	public List<PosChargeData> getParkingData(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public Map<String, Object> calMoneyByParkAndRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	//查询停车场总金额
	public List<Park> getParkByMoney(Map<String, Object> map);
	
	//查询收费总笔数、收费总金额、各渠道收费统计
	public String getByDateAndParkCount2(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	public String getByDateAndParkCount4(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate);
	//各渠道收费统计
	public String getByDateAndParkCount(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("payType")int payType);
	public String getByDateAndParkCount3(@Param("parkId")int parkId,@Param("startDate")String startDate,@Param("endDate")String endDate,@Param("payType")int payType);
	
	//渠道
	public Map<String, Object> calDaysChannelParkChargeByRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	//月份
	public List<PosChargeData> getMoneyByMonthsParkAndRange(Map<String, Object> map);
	
	public Map<String, Object> calInByParkAndRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public Map<String, Object> calOutByParkAndRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getByParkDatetime(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getFreeData(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getChargeMoneyData(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getByParkIdAndCardNumber(@Param("parkId")int parkId,@Param("cardNumber")String cardNumber);
	
	public List<PosChargeData> getByCardNumberAndPark(@Param("cardNumber")String cardNumber,@Param("parkId")int parkId);
	
	public List<PosChargeData> getBySearchByOperatorId(@Param("parkId")int parkId,@Param("operatorId")String operatorId);
	
	public List<PosChargeData> getSearchByOperatorId(@Param("operatorId")String operatorId,@Param("parkId")int parkId);
	
	public List<PosChargeData> getPageByParkId(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
	
	public List<PosChargeData> getPageArrearage(int low, int count);
	
	public List<PosChargeData> getPageArrearageByParkId(@Param("parkId")int parkId,@Param("start")int start,@Param("count")int count);
	
	public List<PosChargeData> getByRange(@Param("parkId")int parkId,@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getAllByDay(@Param("startDate")Date startDate,@Param("endDate")Date endDate);
	
	public List<PosChargeData> getParkCarportStatusToday(@Param("parkId")int parkId,@Param("day")String day);
	
	public int count();
	
	public int insert(PosChargeData item);
	
	public int update(PosChargeData item);
	
	public List<PosChargeData> getDebt(String cardNumber);
	
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber);

	public List<PosChargeData> getUnCompleted();
	
	public List<PosChargeData> selectPosdataByParkAndRange(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
	public List<PosChargeData> selectPosdataByExitDateAndParkAndRange(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
	public List<PosChargeData> selectPosdataByParkAndRange2(@Param("startDay") Date startDay, @Param("endDay") Date endDay,@Param("parkId")int parkId);
	
	public List<PosChargeData> getByCardNumber(@Param("parkId")Integer parkId,@Param("cardNumber")String cardNumber);
	
	public List<PosChargeData> getBySearchCardNumber(@Param("cardNumber")String cardNumber);
	
	public List<PosChargeData> getByParkName(@Param("parkName")String parkName);
	
	public List<Map<String, Object>> getCarTimesByDateRangeAndParkId(@Param("parkId")int parkId,@Param("startDate") Date startDate, @Param("endDate") Date endDate,@Param("start")int start,@Param("count")int count);
	
	public List<Map<String, Object>> getFeeOperatorChargeData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
