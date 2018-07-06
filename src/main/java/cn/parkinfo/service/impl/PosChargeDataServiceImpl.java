package cn.parkinfo.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.parkinfo.dao.PosChargeDataDAO;
import cn.parkinfo.dao.PosdataDAO;
import cn.parkinfo.model.Constants;

import cn.parkinfo.model.Outsideparkinfo;
import cn.parkinfo.model.Park;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.model.Posdata;

import cn.parkinfo.service.OutsideParkInfoService;
import cn.parkinfo.service.ParkService;
import cn.parkinfo.service.PosChargeDataService;
import cn.parkinfo.service.PosdataService;

@Transactional
@Service
public class PosChargeDataServiceImpl implements PosChargeDataService {

	@Autowired
	PosChargeDataDAO chargeDao;

	@Autowired
	ParkService parkService;
	
	@Autowired
	PosdataService posdataService;
	
	@Autowired
	private OutsideParkInfoService outsideParkInfoService;
	@Override
	public PosChargeData getById(int id) {
		return chargeDao.getById(id);
	}

	@Override
	public List<PosChargeData> get() {
		return chargeDao.get();
	}

	@Override
	public List<PosChargeData> getUnCompleted() {
		return chargeDao.getUnCompleted();
	}

	@Override
	public List<PosChargeData> getPage(int low, int count) {
		return chargeDao.getPage(low, count);
	}

	@Override
	public int count() {
		return chargeDao.count();
	}

	@Override
	public int insert(PosChargeData item) {
		return chargeDao.insert(item);
	}

	@Override
	public int update(PosChargeData item) {
		return chargeDao.update(item);
	}
	public List<PosChargeData> getCharges(String cardNumber) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		
		for (PosChargeData tmpcharge:charges){
			if (tmpcharge.getExitDate() == null) {
			this.calExpense(tmpcharge, new Date(),false);}
		}
		return charges;
	}
	@Override
	public List<PosChargeData> getDebt(String cardNumber) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, new Date(),false);
		}
		return tmPosChargeDatas;
	}

	@Override
	public PosChargeData pay(String cardNumber, double money) throws Exception {
		double theMoney=money;
		List<PosChargeData> charges = this.getCharges(cardNumber);
//		for (PosChargeData charge : charges) {
//			if (money >= charge.getUnPaidMoney()) {
//				money -= charge.getUnPaidMoney();
//				charge.setPaidCompleted(true);
//				this.update(charge);
//			}
//		}
		int count = charges.size();
		PosChargeData lastCharge = charges.get(0);
		money -= lastCharge.getUnPaidMoney();
		
		Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
		if (money >= 0) {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setPaidCompleted(true);
			DecimalFormat df = new DecimalFormat("0.00"); 
			String data=df.format(lastCharge.getChangeMoney() + money);
			lastCharge.setChangeMoney(Double.parseDouble(data));				
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
			outsideparkinfo.setPossigndate(new Date());
						
		}
		else {
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			lastCharge.setUnPaidMoney(lastCharge.getUnPaidMoney()-theMoney);
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+theMoney));
			outsideparkinfo.setPossigndate(new Date());
		}
		outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
		this.update(lastCharge);
		return lastCharge;
	}

	@Override
	public void calExpense(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		
		
	}

	@Override
	public List<PosChargeData> getDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, exitDate,false);
		}
		return tmPosChargeDatas;
	}

	@Override
	public void calExpenseLargeCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {
		
	}

	@Override
	public void calExpenseSmallCar(PosChargeData charge, Date exitDate,Boolean isQuery) throws Exception {


		
	}

	@Override
	public List<PosChargeData> queryDebt(String cardNumber, Date exitDate) throws Exception {
		// TODO Auto-generated method stub
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				this.calExpense(charge, exitDate,true);
			}
		}
		return charges;
	}

	@Override
	public List<PosChargeData> selectPosdataByParkAndRange(Date startDay, Date endDay, int parkId) {
		// TODO Auto-generated method stub
		return chargeDao.selectPosdataByParkAndRange(startDay, endDay, parkId);
	}
	
	@Override
	public List<PosChargeData> selectPosdataByParkAndRange2(Date startDay, Date endDay, int parkId) {
		// TODO Auto-generated method stub
		return chargeDao.selectPosdataByParkAndRange2(startDay, endDay, parkId);
	}

	@Override
	public Map<String, Object> getParkChargeByDay(int parkId, String day) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Map<String, Object> tmMap=calMoneyByParkAndRange(parkId, parsedStartDay, parsedEndDay);
	//	List<PosChargeData> posChargeDatas=selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		Map<String, Object> retmap=new HashMap<>();
		float chargeTotal=0;
		float realReceiveMoney=0;
		if (tmMap!=null) {
			 chargeTotal=(float)(double) tmMap.get("chargeMoney");
			realReceiveMoney=(float)((double) tmMap.get("givenMoney")+(double)tmMap.get("paidMoney")-(double)tmMap.get("changeMoney"));
		}
		
		
//		for(PosChargeData posData:posChargeDatas){
//			chargeTotal+=posData.getChargeMoney();
//			realReceiveMoney+=posData.getGivenMoney()+posData.getPaidMoney()-posData.getChangeMoney();
//		}
		retmap.put("totalMoney", chargeTotal);
		retmap.put("realMoney", realReceiveMoney);
		return retmap;
	}
	
	@Override
	public Map<String, Object> getParkChargeByDay2(int parkId, String day) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		List<PosChargeData> posChargeDatas=selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		Map<String, Object> retmap=new HashMap<>();
		float chargeTotal=0;
		float realReceiveMoney=0;
		for(PosChargeData posData:posChargeDatas){
			chargeTotal+=posData.getChargeMoney();
			realReceiveMoney+=posData.getGivenMoney()+posData.getPaidMoney()-posData.getChangeMoney();
		}
		retmap.put("totalMoney", chargeTotal);
		retmap.put("realMoney", realReceiveMoney);
		return retmap;
	}

	@Override
	public List<PosChargeData> queryCurrentDebt(String cardNumber, Date exitDate) throws Exception {
		List<PosChargeData> charges = chargeDao.getDebt(cardNumber);
		List<PosChargeData> tmPosChargeDatas = new ArrayList<>();
		for (PosChargeData charge : charges) {
			if (charge.getExitDate() == null) {
				tmPosChargeDatas.add(charge);				
			}
		}
		for (PosChargeData tmpcharge:tmPosChargeDatas){
			this.calExpense(tmpcharge, exitDate,true);
		}
		return tmPosChargeDatas;
	}

	@Override
	public List<PosChargeData> getBySearchCardNumber(String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getBySearchCardNumber(cardNumber);
	}

	@Override
	public List<PosChargeData> getByParkName(String parkName) {
		// TODO Auto-generated method stub
		return chargeDao.getByParkName(parkName);
	}

	@Override
	public List<PosChargeData> repay(String cardNumber, double money) throws Exception {
		double theMoney=money;
		List<PosChargeData> charges = this.getCharges(cardNumber);
		for (PosChargeData charge : charges) {
			if (money >= charge.getUnPaidMoney()) {
				money -= charge.getUnPaidMoney();
				charge.setPaidCompleted(true);
				this.update(charge);
			}
		}

		if (money >= 0) {
			int count = charges.size();
			PosChargeData lastCharge = charges.get(0);
			lastCharge.setChangeMoney(lastCharge.getChangeMoney() + money);
			lastCharge.setGivenMoney(theMoney+lastCharge.getGivenMoney());
			Outsideparkinfo outsideparkinfo=outsideParkInfoService.getByParkidAndDate(lastCharge.getParkId());
			outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+lastCharge.getPaidMoney()+lastCharge.getGivenMoney()-lastCharge.getChangeMoney()));
			outsideparkinfo.setPossigndate(new Date());
			outsideParkInfoService.updateByPrimaryKeySelective(outsideparkinfo);
			this.update(lastCharge);
		}
		return charges;
	}

	@Override
	public List<Map<String, Object>> getFeeOperatorChargeData(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getFeeOperatorChargeData(startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByRange(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getByRange(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByParkAndDay(int parkId, String date) throws ParseException  {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date startDate=sFormat.parse(date+" 00:00:00");
		Date endDate=sFormat.parse(date+" 23:59:59");
		return chargeDao.getByRange(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getAllByDay(String date) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date startDate=sFormat.parse(date+" 00:00:00");
		Date endDate=sFormat.parse(date+" 23:59:59");
		return chargeDao.getAllByDay(startDate,endDate);
	}
	@Override
	public List<PosChargeData> getByParkAndDayRange(int parkId, String startDate,String endDate) throws ParseException  {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date dstartDate=sFormat.parse(startDate+" 00:00:00");
		Date dendDate=sFormat.parse(endDate+" 23:59:59");
		return chargeDao.getByRange(parkId, dstartDate, dendDate);
	}

	@Override
	public List<PosChargeData> getAllByDayRange(String startDate,String endDate) throws ParseException {
		// TODO Auto-generated method stub
		SimpleDateFormat sFormat=new SimpleDateFormat(Constants.DATEFORMAT);
		Date dstartDate=sFormat.parse(startDate+" 00:00:00");
		Date dendDate=sFormat.parse(endDate+" 23:59:59");
		return chargeDao.getAllByDay(dstartDate,dendDate);
	}

	@Override
	public List<PosChargeData> getPageArrearage(int low, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageArrearage(low, count);
	}

	@Override
	public List<PosChargeData> getPageArrearageByParkId(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageArrearageByParkId(parkId, start, count);
	}

	@Override
	public List<PosChargeData> getPageByParkId(int parkId, int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getPageByParkId(parkId, start, count);
	}

	@Override
	public List<PosChargeData> getByParkIdAndCardNumber(Integer parkId, String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getByParkIdAndCardNumber(parkId, cardNumber);
	}

	@Override
	public List<PosChargeData> getParkCarportStatusToday(int parkId) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date tmpdate=new Date(new Date().getTime()-1000*60*60*24*5);
		
		String day=sdf.format(tmpdate)+" 00:00:00";
		return chargeDao.getParkCarportStatusToday(parkId, day);
	}

	@Override
	public Outsideparkinfo getOutsideparkinfoByOrigin(int parkId, String day) {
		// TODO Auto-generated method stub
		Park park = parkService.getParkById(parkId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Outsideparkinfo outsideparkinfo=new Outsideparkinfo();
		List<PosChargeData> posChargeDatas = selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		outsideparkinfo.setParkid(parkId);
		outsideparkinfo.setCarportcount(park.getPortCount());
		outsideparkinfo.setUnusedcarportcount(park.getPortLeftCount());
		if (!posChargeDatas.isEmpty()) {			
			outsideparkinfo.setPossigndate(posChargeDatas.get(0).getEntranceDate());
			for(PosChargeData posChargeData : posChargeDatas){
				outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);
				outsideparkinfo.setAmountmoney((float) (outsideparkinfo.getAmountmoney()+posChargeData.getChargeMoney()));
				if (posChargeData.getExitDate()==null) {
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);
				}
				else {				
					outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
				}
				if (posChargeData.isPaidCompleted()) {
					outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getGivenMoney()+posChargeData.getPaidMoney()-posChargeData.getChangeMoney()));
				}
				else {
					outsideparkinfo.setRealmoney((float) (outsideparkinfo.getRealmoney()+posChargeData.getPaidMoney()));
				}
			}	
		}
		else {
			List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(park.getName(), parsedStartDay, parsedEndDay);			
			//outsideparkinfo=outsideParkInfoService.getByParkidAndDate(parkId);
			//从数据表中获取数据
			if (posdatas.isEmpty()) {
				return outsideparkinfo;
			}
			outsideparkinfo.setPossigndate(posdatas.get(0).getStarttime());
			for(Posdata posdata:posdatas){
				outsideparkinfo.setAmountmoney(outsideparkinfo.getAmountmoney()+posdata.getMoney().floatValue());
				
				if (posdata.getIsarrearage()==false) {
					if (posdata.getMode()==0) {	
					outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);															
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);																				
				}
					else {
						outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
						outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()+1);
						outsideparkinfo.setRealmoney(outsideparkinfo.getRealmoney()+posdata.getGiving().floatValue()+posdata.getRealmoney().floatValue()-posdata.getReturnmoney().floatValue());
					}
				}
				
				else {
					outsideparkinfo.setRealmoney(outsideparkinfo.getRealmoney()+posdata.getGiving().floatValue()+posdata.getRealmoney().floatValue()-posdata.getReturnmoney().floatValue());
				}
				
			}
			/*List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(park.getName(), parsedStartDay, parsedEndDay);
			outsideparkinfo.setPossigndate(posdatas.get(0).getStarttime());
			for(Posdata posdata :posdatas){
				outsideparkinfo.setEntrancecount(outsideparkinfo.getEntrancecount()+1);
				outsideparkinfo.setAmountmoney(outsideparkinfo.getAmountmoney()+posdata.getMoney().floatValue());
				if (posdata.getEndtime()==null) {
					outsideparkinfo.setUnusedcarportcount(outsideparkinfo.getUnusedcarportcount()-1);
				}
				else{
					outsideparkinfo.setOutcount(outsideparkinfo.getOutcount()+1);
				}
				if (posdata.get) {
					
				}
			}*/
		}
		return outsideparkinfo;
	}

	@Override
	public List<PosChargeData> getArrearageByCardNumber(String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getArrearageByCardNumber(cardNumber);
	}

	@Override
	public List<PosChargeData> getByParkAuthority(String userName) {
		// TODO Auto-generated method stub
		List<Park> parkList = parkService.getParks();
		if (userName != null)
			parkList = parkService.filterPark(parkList, userName);
		int num=120/parkList.size();
		List<PosChargeData> posChargeDatas=new ArrayList<PosChargeData>();
		for(Park park:parkList){
			List<PosChargeData> tmPosChargeDatas=getPageByParkId(park.getId(), 0, num);
			posChargeDatas.addAll(tmPosChargeDatas);
		}
		return posChargeDatas;
	}

	@Override
	public List<PosChargeData> getByCardNumberAndPark(String cardNumber, Integer parkId) {
		// TODO Auto-generated method stub
		return chargeDao.getByCardNumberAndPark(cardNumber, parkId);
	}

	@Override
	public List<PosChargeData> getByCardNumber(Integer parkId,String cardNumber) {
		// TODO Auto-generated method stub
		return chargeDao.getByCardNumber(parkId,cardNumber);
	}

	@Override
	public List<PosChargeData> getParkingData(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getParkingData(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getFreeData(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getFreeData(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByParkDatetime(int parkId,Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getByParkDatetime(parkId,startDate, endDate);
	}

	@Override
	public List<PosChargeData> getChargeMoneyData(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.getChargeMoneyData(parkId, startDate, endDate);
	}

	@Override
	public List<PosChargeData> getByDateDiffNoOut(int parkId,int days, int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getByDateDiffNoOut(parkId,days, start, count);
	}

	@Override
	public List<Map<String, Object>> getCarTimesByDateRangeAndParkId(int parkId, Date startDate, Date endDate,
			int start, int count) {
		// TODO Auto-generated method stub
		return chargeDao.getCarTimesByDateRangeAndParkId(parkId, startDate, endDate, start, count);
	}

	@Override
	public Map<String, Object> getByDayDateDiffNoOut(int parkId, String day) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		List<Map<String , Object>> results=getCarTimesByDateRangeAndParkId(parkId, parsedStartDay, parsedEndDay, 0, 3000);
		Map<String, Object> ret=new HashMap<>();
		ret.put("count", results.size());
		return ret;
	}
	@Override
	public Map<String, Object> getParkChargeCountByDay(int parkId, String day) {
		// TODO Auto-generated method stub
	
	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		long incount=0;
		long outcount=0;
	//	List<PosChargeData> posChargeDatas=selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
	//	List<PosChargeData> posChargeDatasout=chargeDao.selectPosdataByExitDateAndParkAndRange(parsedStartDay, parsedEndDay, parkId);
		Map<String, Object> incountMap=calInByParkAndRange(parkId, parsedStartDay, parsedEndDay);
		Map<String, Object> outcountMap=calOutByParkAndRange(parkId, parsedStartDay, parsedEndDay);
		if (incountMap!=null) {
			incount=(long) incountMap.get("count");
		}
		if (outcountMap!=null) {
			outcount=(long) outcountMap.get("count");
		}
		Map<String, Object> retmap=new HashMap<>();
		
		
			
			retmap.put("in", incount);
			retmap.put("out",outcount);
		
		
		return retmap;
	}

	@Override
	public Map<String, Object> calMoneyByParkAndRange(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.calMoneyByParkAndRange(parkId, startDate, endDate);
	}

	@Override
	public Map<String, Object> calInByParkAndRange(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.calInByParkAndRange(parkId, startDate, endDate);
	}

	@Override
	public Map<String, Object> calOutByParkAndRange(int parkId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return chargeDao.calOutByParkAndRange(parkId, startDate, endDate);
	}

	@Override
	public Map<String, Object> getYearsParkChargeByRange(int parkId, String day) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(day + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(day + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Map<String, Object> tmMap=calMoneyByParkAndRange(parkId, parsedStartDay, parsedEndDay);
	//	List<PosChargeData> posChargeDatas=selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
		Map<String, Object> retmap=new HashMap<>();
		float chargeTotal=0;
		float realReceiveMoney=0;
		if (tmMap!=null) {
			 chargeTotal=(float)(double) tmMap.get("chargeMoney");
			realReceiveMoney=(float)((double) tmMap.get("givenMoney")+(double)tmMap.get("paidMoney")-(double)tmMap.get("changeMoney"));
		}
		return retmap;
	}

}
