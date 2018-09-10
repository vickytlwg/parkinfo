package cn.parkinfo.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Monthuser;
import cn.parkinfo.model.Outsideparkinfo;
import cn.parkinfo.model.Park;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.model.Posdata;
import cn.parkinfo.model.UserPark;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.ExcelExportService;
import cn.parkinfo.service.MonthUserService;
import cn.parkinfo.service.ParkService;
import cn.parkinfo.service.PosChargeDataService;
import cn.parkinfo.service.Utility;

@Controller
@RequestMapping("/pos/charge")
public class PosChargeDataController {

	@Autowired
	ParkService parkService;
	@Autowired
	private AuthorityService authService;

	@Autowired
	PosChargeDataService chargeSerivce;

	@Autowired
	private ExcelExportService excelService;
	
	@Autowired
	private MonthUserService monthUserService;
	
	//查询停车场总金额
	@RequestMapping(value = "/getParkByMoney",produces = {"application/json;charset=utf-8" })
	@ResponseBody
	public Object getParkByMoney(@RequestBody Map<String, Object> args) throws Exception{
		@SuppressWarnings("unused")
		Map<String, Object> retMap = new HashMap<String, Object>();
		/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();*/
		Object usernameObj= args.get("username");
		Object startDateObj=   args.get("startDate");
		Object endDateObj=   args.get("endDate");
		String username=String.valueOf(usernameObj);
		String startDate=String.valueOf(startDateObj);
		String endDate=String.valueOf(endDateObj);
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map.put("username", username);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		/*String startDate="2018-09-01";
		String endDate=sdf.format(date);*/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDate + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDate + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Park> listparkId = chargeSerivce.getParkByMoney(map);
		/*Park a=listparkId.get(0);*/
		int totalCount = 0;
		int alipayCount=0;
		int wechartCount=0;
		int cashCount=0;
		int otherCount=0;
		int unionPayCount=0;
		int cbcCount=0;
		
		Double totalAmount = 0.0;
		Double alipayAmount=0.0;
		Double wechartAmount=0.0;
		Double cashAmount=0.0;
		Double otherAmount=0.0;
		Double unionPayAmount=0.0;
		Double cbcAmount=0.0;
		
		for (int i = 0; i <listparkId.size(); i++) {
			int userId = listparkId.get(i).getId();
			int parkId = listparkId.get(i).getParkId();
			map2.put("parkId", parkId);
			map2.put("startDate", startDate);
			map2.put("endDate", endDate);
			Map<String, Object> mapmap = getByDateAndParkCount(map2);
			for(String key : mapmap.keySet()){
				if(key.equals("totalCount")){
					totalCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("alipayCount")){
					alipayCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("wechartCount")){
					wechartCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("cashCount")){
					cashCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("otherCount")){
					otherCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("unionPayCount")){
					unionPayCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("cbcCount")){
					cbcCount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("totalAmount")){
					totalAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("alipayAmount")){
					alipayAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("wechartAmount")){
					wechartAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("cashAmount")){
					cashAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("otherAmount")){
					otherAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("unionPayAmount")){
					unionPayAmount += Double.parseDouble(mapmap.get(key).toString());
				}
				if(key.equals("cbcAmount")){
					cbcAmount += Double.parseDouble(mapmap.get(key).toString());
				}
			}
		}
		
		Map<String, Object> map33 = new HashMap<String, Object>();
		map33.put("totalCount", totalCount);
		map33.put("alipayCount", alipayCount);
		map33.put("wechartCount", wechartCount);
		map33.put("cashCount", cashCount);
		map33.put("otherCount", otherCount);
		map33.put("unionPayCount", unionPayCount);
		map33.put("cbcCount", cbcCount);
		
		map33.put("totalAmount", totalAmount);
		map33.put("alipayAmount", alipayAmount);
		map33.put("wechartAmount", wechartAmount);
		map33.put("cashAmount", cashAmount);
		map33.put("otherAmount", otherAmount);
		map33.put("unionPayAmount", unionPayAmount);
		map33.put("cbcAmount", cbcAmount);
		return Utility.createJsonMsg(1001, "success", map33);
		
	}
	
	
	//查询收费总笔数、收费总金额、各渠道收费统计
		@RequestMapping(value = "/getByDateAndParkCount", produces = {"application/json;charset=utf-8" })
		@ResponseBody
			public Map<String,Object> getByDateAndParkCount(@RequestBody Map<String, Object> args) throws Exception{
				int parkId=0;
				Object parkIdObj=args.get("parkId");
				String parkIdStr=parkIdObj.toString();
				int parkids=Integer.parseInt(parkIdStr);
				parkId=Integer.parseInt(args.get("parkId").toString());
				String startDate=(String)args.get("startDate");
				String endDate=(String)args.get("endDate");
				Double totalAmount=0.0;
				Double alipayAmount=0.0;
				Double wechartAmount=0.0;
				Double cashAmount=0.0;
				Double otherAmount=0.0;
				Double unionPayAmount=0.0;
				Double cbcAmount=0.0;
				
				int totalCount=0;
				int alipayCount=0;
				int wechartCount=0;
				int cashCount=0;
				int otherCount=0;
				int unionPayCount=0;
				int cbcCount=0;
				
				Map<String, Object> retMap = new HashMap<String, Object>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date parsedStartDay = null;
				try {
					parsedStartDay = sdf.parse(startDate + " 00:00:00");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Date parsedEndDay = null;
				try {
					parsedEndDay = sdf.parse(endDate + " 00:00:00");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				int payTypezfb=0;
				int payTypewx=1;
				int payTypexj=2;
				int payTypeqt=3;
				int payTypeyl=4;
				int payTypegh=5;
				/*int payTypedj=9;*/
				//查询收费总笔数、收费总金额、各渠道收费统计
				String results2=chargeSerivce.getByDateAndParkCount2(parkId,startDate, endDate);
				String resultszfbbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypezfb);
				String resultswxbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypewx);
				String resultsxjbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypexj);
				String resultsqtbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypeqt);
				String resultsylbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypeyl);
				String resultsghbs=chargeSerivce.getByDateAndParkCount(parkId,startDate, endDate,payTypegh);
				
				String results4=chargeSerivce.getByDateAndParkCount4(parkId,startDate, endDate);
				String resultszfbje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypezfb);
				String resultswxje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypewx);
				String resultsxjje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypexj);
				String resultsqtje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypeqt);
				String resultsylje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypeyl);
				String resultsghje=chargeSerivce.getByDateAndParkCount3(parkId,startDate, endDate,payTypegh);
				
				retMap.put("totalAmount", results4==null?new BigDecimal("0"):new BigDecimal(results4));
				retMap.put("alipayAmount", resultszfbje==null?new BigDecimal("0"):new BigDecimal(resultszfbje));
				retMap.put("wechartAmount", resultswxje==null?new BigDecimal("0"):new BigDecimal(resultswxje));
				retMap.put("cashAmount", resultsxjje==null?new BigDecimal("0"):new BigDecimal(resultsxjje));
				retMap.put("unionPayAmount", resultsylje==null?new BigDecimal("0"):new BigDecimal(resultsylje));
				retMap.put("cbcAmount", resultsghje==null?new BigDecimal("0"):new BigDecimal(resultsghje));
				retMap.put("otherAmount", resultsqtje==null?new BigDecimal("0"):new BigDecimal(resultsqtje));
				
				retMap.put("totalCount", results2==null?new BigDecimal("0"):new BigDecimal(results2));
				retMap.put("alipayCount", resultszfbbs==null?new BigDecimal("0"):new BigDecimal(resultszfbbs));
				retMap.put("wechartCount", resultswxbs==null?new BigDecimal("0"):new BigDecimal(resultswxbs));
				retMap.put("cashCount", resultsxjbs==null?new BigDecimal("0"):new BigDecimal(resultsxjbs));
				retMap.put("unionPayCount", resultsylbs==null?new BigDecimal("0"):new BigDecimal(resultsylbs));
				retMap.put("cbcCount", resultsghbs==null?new BigDecimal("0"):new BigDecimal(resultsghbs));
				retMap.put("otherCount", resultsqtbs==null?new BigDecimal("0"):new BigDecimal(resultsqtbs));
				
				double results2double=0;
				if(results2 != null){
					results2double=Double.parseDouble(results2);
				}
				double resultszfbbsdouble=0;
				if(resultszfbbs !=null){
					resultszfbbsdouble=Double.parseDouble(resultszfbbs);
				}
				double resultswxbsdouble=0;
				if(resultswxbs !=null){
					resultswxbsdouble=Double.parseDouble(resultswxbs);
				}
				double resultsxjbsdouble=0;
				if(resultsxjbs !=null){
					resultsxjbsdouble=Double.parseDouble(resultsxjbs);
				}
				double resultsqtbsdouble=0;
				if(resultsqtbs !=null){
					resultsqtbsdouble=Double.parseDouble(resultsqtbs);
				}
				double resultsylbsdouble=0;
				if(resultsylbs !=null){
					resultsylbsdouble=Double.parseDouble(resultsylbs);
				}
				double resultsghbsdouble=0;
				if(resultsghbs !=null){
					resultsghbsdouble=Double.parseDouble(resultsghbs);
				}
				
				double results4double=0;
				if(results4 !=null){
					results4double=Double.parseDouble(results4);
				}
				double resultszfbjedouble=0;
				if(resultszfbje !=null){
					resultszfbjedouble=Double.parseDouble(resultszfbje);
				}
				double resultswxjedouble=0;
				if(resultswxje !=null){
					resultswxjedouble=Double.parseDouble(resultswxje);
				}
				double resultsxjjedouble=0;
				if(resultsxjje !=null){
					resultsxjjedouble=Double.parseDouble(resultsxjje);
				}
				double resultsqtjedouble=0;
				if(resultsqtje !=null){
					resultsqtjedouble=Double.parseDouble(resultsqtje);
				}
				double resultsyljedouble=0;
				if(resultsylje !=null){
					resultsyljedouble=Double.parseDouble(resultsylje);
				}
				double resultsghjedouble=0;
				if(resultsghje !=null){
					resultsghjedouble=Double.parseDouble(resultsghje);
				}
				totalCount+=results2double+resultszfbbsdouble+resultswxbsdouble+resultsxjbsdouble+resultsqtbsdouble
						+resultsylbsdouble+resultsghbsdouble;
				alipayCount+=resultszfbbsdouble;
				wechartCount+=resultswxbsdouble;
				cashCount+=resultsxjbsdouble;
				otherCount+=resultsqtbsdouble;
				unionPayCount+=resultsylbsdouble;
				cbcCount+=resultsghbsdouble;
				
				totalAmount+=results4double+resultszfbjedouble+resultswxjedouble+resultsxjjedouble+resultsqtjedouble
						+resultsyljedouble+resultsghjedouble;
				alipayAmount+=resultszfbjedouble;
				wechartAmount+=resultswxjedouble;
				cashAmount+=resultsxjjedouble;
				otherAmount+=resultsqtjedouble;
				unionPayAmount+=resultsyljedouble;
				cbcAmount+=resultsghjedouble;
				

				
				Map<String, Object> map222 = new HashMap<String , Object>();
				map222.put("totalAmount", totalAmount);
				map222.put("alipayAmount", alipayAmount);
				map222.put("wechartAmount", wechartAmount);
				map222.put("cashAmount", cashAmount);
				map222.put("otherAmount", otherAmount);
				map222.put("unionPayAmount", unionPayAmount);
				map222.put("cbcAmount", cbcAmount);
				
				map222.put("totalCount", totalCount);
				map222.put("alipayCount", alipayCount);
				map222.put("wechartCount", wechartCount);
				map222.put("cashCount", cashCount);
				map222.put("otherCount", otherCount);
				map222.put("unionPayCount", unionPayCount);
				map222.put("cbcCount", cbcCount);
				return map222;
			}
	
	
	@RequestMapping(value = "/getMonthuserCountsByPark",produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public void getMonthuserCountsByPark(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, ParseException, FileNotFoundException{
		String startDate = request.getParameter("startDate");
		/*startDate=startDate+ " 00:00:00";*/
		String endDate = request.getParameter("endDate");
		/*endDate=endDate+ " 23:59:59";*/
		String parkId = request.getParameter("parkId");
		String count = request.getParameter("count");
		String type = request.getParameter("type");
 		List<Map<String, Object>> datas=monthUserService.getMonthuserCountsByDateRangeAndPark(Integer.parseInt(parkId), startDate,endDate,Integer.parseInt(type), Integer.parseInt(count));
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "姓名", "停车次数", "车牌号","类型"};
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "monthusercount.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceMonthCountsInfoExcel("停车次数", headers, datas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "monthusercount.xlsx", response);
	}

	@RequestMapping(value = "/detail", produces = { "application/json;charset=UTF-8" })
	public String feeDetailIndex(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "feeDetail";
	}

	@RequestMapping(value = "/record", produces = { "application/json;charset=UTF-8" })
	public String record(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "record";
	}

	@RequestMapping(value = "/record1", produces = { "application/json;charset=UTF-8" })
	public String record1(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "record1";
	}

	@RequestMapping(value = "/taopaiche", produces = { "application/json;charset=UTF-8" })
	public String taopaiche(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "taopaiche";
	}

	@RequestMapping(value = "/flowbill", produces = { "application/json;charset=UTF-8" })
	public String flowbill(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "flowbill";
	}

	@RequestMapping(value = "/reconciliation", produces = { "application/json;charset=UTF-8" })
	public String reconciliation(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "reconciliation";
	}

	@RequestMapping(value = "/feeoperatorCharge", produces = { "application/json;charset=UTF-8" })
	public String feeoperatorCharge(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "feeOperatorChargeData";
	}

	@RequestMapping(value = "/arrearage", produces = { "application/json;charset=UTF-8" })
	public String arrearage(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username != null)
			parkList = parkService.filterPark(parkList, username);
		List<Park> outsideparks = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType() == 3) {
				outsideparks.add(park);
			}
		}
		modelMap.addAttribute("parks", outsideparks);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "arrearage";
	}

	@RequestMapping(value = "/getByParkAndRange", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getByParkAndRange(@RequestBody Map<String, Object> args) {
		int parkId = 1;
		try {
			parkId = Integer.parseInt((String) args.get("parkId"));
		} catch (Exception e) {
			// TODO: handle exception
			parkId = (int) args.get("parkId");
		}
		String startDay = (String) args.get("startDay");
		String endDay = (String) args.get("endDay");
		Map<String, Object> retMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PosChargeData> posChargeDatas = chargeSerivce.selectPosdataByParkAndRange(parsedStartDay, parsedEndDay,
				parkId);
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}
	/**
	 * 获取某个时间段内 按天的车辆次数
	 * */
	@RequestMapping(value = "/getByDayAndDateDiffNoOut", method = RequestMethod.POST, produces = {
	"application/json;charset=utf-8" })
	@ResponseBody
	public String getByDayAndDateDiffNoOut(@RequestBody Map<String, Object> args){
		int parkId=Integer.parseInt((String)args.get("parkId"));
		String startDay=(String)args.get("startDay");
		String endDay=(String)args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Calendar start =Calendar.getInstance(); 
		start.setTime(parsedStartDay);
		Long startTime = start.getTimeInMillis();
		Calendar end = Calendar.getInstance();
		end.setTime(parsedEndDay);
		Long endTime = end.getTimeInMillis();
		Long oneDay = 1000 * 60 * 60 * 24l;
		Long time = startTime;  
		
		Map<Long, Object> comparemap=new TreeMap<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  while (time <= endTime) {  
		        Date d = new Date(time);  
		        Map<String, Object> dMap=chargeSerivce.getByDayDateDiffNoOut(parkId,  df.format(d));
		        comparemap.put(d.getTime(), dMap);
		        time += oneDay;  
		    }     
		return  Utility.gson.toJson(comparemap);
		
	}
	/**
	 * 获取某个时间段内 车牌与其对应的停车次数
	 * */
	@RequestMapping(value = "/getByDateDiffNoOut", method = RequestMethod.POST, produces = {
	"application/json;charset=utf-8" })
	@ResponseBody
	public String getByDateDiffNoOut(@RequestBody Map<String, Object> args){
		int days=(int) args.get("days");
		int parkId=(int) args.get("parkId");
		int start=args.get("start")!=null?(int) args.get("start"):0;
		int count=args.get("count")!=null?(int)args.get("count"):300;
		List<PosChargeData> posChargeDatas = chargeSerivce.getByDateDiffNoOut(parkId, days, start, count);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	};
	
	//按年份获取每月应收实收费用
	@RequestMapping(value="/getMonthsParkChargeByRange", method = RequestMethod.POST, produces = {
	"application/json;charset=utf-8" })
	@ResponseBody
	public Object getMonthsParkChargeByRange(Integer parkId,String year){
		List<PosChargeData> list = null;
		try {
			Map<String, Object> map = new HashMap<String,Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			Date year2 = sdf.parse(year);
			map.put("parkId", parkId);
			map.put("startDate", year2);
			list = chargeSerivce.getMoneyByMonthsParkAndRange(map);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
		
	}
	
	@RequestMapping(value = "/getCarTimesByDateRangeAndParkId", method = RequestMethod.POST, produces = {
	"application/json;charset=utf-8" })
@ResponseBody
public String getCarTimesByDateRangeAndParkId(@RequestBody Map<String, Object> args) throws ParseException{
		String startDatestr = (String) args.get("startDate");
		String endDatestr = (String) args.get("endDate");		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(startDatestr);
		Date endDate = sdf.parse(endDatestr);
		int parkId=(int) args.get("parkId");
		int start=args.get("start")!=null?(int) args.get("start"):0;
		int count=args.get("count")!=null?(int)args.get("count"):300;
		List<Map<String , Object>> results=chargeSerivce.getCarTimesByDateRangeAndParkId(parkId, startDate, endDate, start, count);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (results.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", results);
		}
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value = "/getByParkDatetime", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getByParkDatetime(@RequestBody Map<String, Object> args) throws ParseException{
		int parkId = 1;
		try {
			parkId = Integer.parseInt((String) args.get("parkId"));
		} catch (Exception e) {
			// TODO: handle exception
			parkId = (int) args.get("parkId");
		}
		String startDatestr = (String) args.get("startDate");
		String endDatestr = (String) args.get("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(startDatestr);
		Date endDate = sdf.parse(endDatestr);
		List<PosChargeData> posChargeDatas = chargeSerivce.getByParkDatetime(parkId,startDate, endDate);
		
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}

	@RequestMapping(value = "/getParkingData", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getParkingData(@RequestBody Map<String, Object> args) throws ParseException {
		int parkId = 1;
		try {
			parkId = Integer.parseInt((String) args.get("parkId"));
		} catch (Exception e) {
			// TODO: handle exception
			parkId = (int) args.get("parkId");
		}
		String startDatestr = (String) args.get("startDate");
		String endDatestr = (String) args.get("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(startDatestr);
		Date endDate = sdf.parse(endDatestr);
		List<PosChargeData> posChargeDatas = chargeSerivce.getParkingData(parkId, startDate, endDate);
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}

	@RequestMapping(value = "/getChargeMoneyData", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getChargeMoneyData(@RequestBody Map<String, Object> args) throws ParseException {
		int parkId = 1;
		try {
			parkId = Integer.parseInt((String) args.get("parkId"));
		} catch (Exception e) {
			// TODO: handle exception
			parkId = (int) args.get("parkId");
		}
		String startDatestr = (String) args.get("startDate");
		String endDatestr = (String) args.get("endDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = sdf.parse(startDatestr);
		Date endDate = sdf.parse(endDatestr);
		List<PosChargeData> posChargeDatas = chargeSerivce.getChargeMoneyData(parkId, startDate, endDate);
		/*System.out.println(posChargeDatas);*/
		Map<String, Object> retMap = new HashMap<String, Object>();
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}
	
	@RequestMapping(value = "/getFreeData", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getFreeData(@RequestBody Map<String, Object> args) throws ParseException {
	int parkId = 1;
	try {
		parkId = Integer.parseInt((String) args.get("parkId"));
	} catch (Exception e) {
		// TODO: handle exception
		parkId = (int) args.get("parkId");
	}
	String startDatestr = (String) args.get("startDate");
	String endDatestr = (String) args.get("endDate");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Date startDate = sdf.parse(startDatestr);
	Date endDate = sdf.parse(endDatestr);
	List<PosChargeData> posChargeDatas = chargeSerivce.getFreeData(parkId, startDate, endDate);
	Map<String, Object> retMap = new HashMap<String, Object>();
	if (posChargeDatas.isEmpty()) {
		retMap.put("status", 1002);
	} else {
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posChargeDatas);
	}
	return Utility.gson.toJson(retMap);
	}

	@RequestMapping(value = "/getByParkAndRange2", method = RequestMethod.POST, produces = {
			"application/json;charset=utf-8" })
	@ResponseBody
	public String getByParkAndRange2(@RequestBody Map<String, Object> args) {
		int parkId = 1;
		try {
			parkId = Integer.parseInt((String) args.get("parkId"));
		} catch (Exception e) {
			// TODO: handle exception
			parkId = (int) args.get("parkId");
		}
		String startDay = (String) args.get("startDay");
		String endDay = (String) args.get("endDay");
		Map<String, Object> retMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<PosChargeData> posChargeDatas = chargeSerivce.selectPosdataByParkAndRange2(parsedStartDay, parsedEndDay,
				parkId);
		if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
		} else {
			retMap.put("status", 1001);
			retMap.put("message", "success");
			retMap.put("body", posChargeDatas);
		}
		return Utility.gson.toJson(retMap);
	}

	@RequestMapping(value = "getByCardnumber", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCardnumber(@RequestBody Map<String, String> args) {
		String cardNumber = args.get("cardNumber");
		int parkId = Integer.parseInt(args.get("parkId"));
		/* System.out.println(parkId); */
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumber(parkId, cardNumber));
	}
	
	@RequestMapping(value = "getBySearchCardNumber", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
@ResponseBody
public String getBySearchCardNumber(@RequestBody Map<String, String> args, HttpSession session) {
		String cardNumber = args.get("cardNumber");
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {
			return Utility.createJsonMsg(1001, "success", chargeSerivce.getBySearchCardNumber(cardNumber));
		}
		parkList = parkService.filterPark(parkList, username);
		List<PosChargeData> posChargeDatas = new ArrayList<>();
		for (Park park : parkList) {
			posChargeDatas.addAll(chargeSerivce.getByCardNumberAndPark(cardNumber, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
}

	@RequestMapping(value = "getByCardnumberAuthority", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCardnumberAuthority(@RequestBody Map<String, String> args, HttpSession session) {
		String cardNumber = args.get("cardNumber");
		/*String username = (String) session.getAttribute("username");*/
		int parkId=Integer.parseInt((String)args.get("parkId"));
		/*AuthUser user = authService.getUserByUsername(username);	
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {*/
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumber(parkId,cardNumber));
		/*}
		parkList = parkService.filterPark(parkList, username);
		List<PosChargeData> posChargeDatas = new ArrayList<>();
		for (Park park : parkList) {
			posChargeDatas.addAll(chargeSerivce.getByCardNumberAndPark(cardNumber, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);*/
	}
	
	//查询操作员
	@RequestMapping(value = "getBySearchByOperatorId", method = RequestMethod.POST, produces = {
	"application/json;charset=UTF-8" })
	@ResponseBody
	public String getBySearchByOperatorId(@RequestBody Map<String, String> args, HttpSession session) {
		String operatorId = args.get("operatorId");
		/*String username = (String) session.getAttribute("username");*/
		int parkId=Integer.parseInt((String)args.get("parkId"));
		/*AuthUser user = authService.getUserByUsername(username);	
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {*/
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getBySearchByOperatorId(parkId,operatorId));
		/*}
		parkList = parkService.filterPark(parkList, username);
		List<PosChargeData> posChargeDatas = new ArrayList<>();
		for (Park park : parkList) {
			posChargeDatas.addAll(chargeSerivce.getSearchByOperatorId(operatorId, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);*/
		}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String count() {
		int count = chargeSerivce.count();
		return Utility.createJsonMsg(1001, "success", count);
	}

	@RequestMapping(value = "/getByParkName", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByParkName(@RequestBody Map<String, String> args, HttpSession session) {
		String parkName = args.get("parkName");
		/*String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);	
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {*/
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByParkName(parkName));
		/*}
		parkList = parkService.filterPark(parkList, username);
		List<PosChargeData> posChargeDatas = new ArrayList<>();
		for (Park park : parkList) {
			posChargeDatas.addAll(chargeSerivce.getByCardNumberAndPark(parkName, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);*/
	}

	@RequestMapping(value = "/page", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String page(@RequestBody Map<String, Object> args, HttpSession session) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		String userName = (String) session.getAttribute("username");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByParkAuthority(userName));
	}

	@RequestMapping(value = "/getByCount", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCount(@RequestBody Map<String, Object> args) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		List<PosChargeData> posChargeDatas = chargeSerivce.getPage(low, count);
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
	}
	//计算应收实收
	@RequestMapping(value="/getParkChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getParkChargeByRange(@RequestBody Map<String, Object> args){
		int parkId=Integer.parseInt((String)args.get("parkId"));
		String startDay=(String)args.get("startDay");
		String endDay=(String)args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		Calendar start =Calendar.getInstance(); 
		start.setTime(parsedStartDay);
		Long startTime = start.getTimeInMillis();
		Calendar end = Calendar.getInstance();
		end.setTime(parsedEndDay);
		Long endTime = end.getTimeInMillis();
		Long oneDay = 1000 * 60 * 60 * 24l;
		Long time = startTime;  
		Map<Long, Object> comparemap=new TreeMap<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  while (time <= endTime) {  
		        Date d = new Date(time);            
		        Map<String, Object> posChargeDatas = chargeSerivce.getParkChargeByDay(parkId, df.format(d));
		        comparemap.put(d.getTime(), posChargeDatas);
		        time += oneDay;  
		    }     
		return  Utility.gson.toJson(comparemap);
	}
	
	//渠道
		@RequestMapping(value="/getDaysChannelParkChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
		@ResponseBody
		public String getDaysChannelParkChargeByRange(@RequestBody Map<String, Object> args){
			int parkId=Integer.parseInt((String)args.get("parkId"));
			String startDay=(String)args.get("startDay");
			String endDay=(String)args.get("endDay");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			Date parsedStartDay = null;
			try {
				parsedStartDay = sdf.parse(startDay + " 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}	
			Date parsedEndDay  = null;
			try {
				parsedEndDay = sdf.parse(endDay + " 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}	
			
			Calendar start =Calendar.getInstance(); 
			start.setTime(parsedStartDay);
			Long startTime = start.getTimeInMillis();
			Calendar end = Calendar.getInstance();
			end.setTime(parsedEndDay);
			Long endTime = end.getTimeInMillis();
			Long oneDay = 1000 * 60 * 60 * 24l;
			Long time = startTime;  
			Map<Long, Object> comparemap=new TreeMap<>();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			  while (time <= endTime) {  
			        Date d = new Date(time);            
			        Map<String, Object> posChargeDatas = chargeSerivce.getDaysChannelParkChargeByRange(parkId, df.format(d));
			        comparemap.put(d.getTime(), posChargeDatas);
			        time += oneDay;  
			    }     
			return  Utility.gson.toJson(comparemap);
		}
	
	
	@RequestMapping(value="/getParkRecordsCountByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getParkRecordsCountByRange(@RequestBody Map<String, Object> args){
		int parkId=Integer.parseInt((String)args.get("parkId"));
		String startDay=(String)args.get("startDay");
		String endDay=(String)args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		Date parsedEndDay  = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}	
		
		Calendar start =Calendar.getInstance(); 
		start.setTime(parsedStartDay);
		Long startTime = start.getTimeInMillis();
		Calendar end = Calendar.getInstance();
		end.setTime(parsedEndDay);
		Long endTime = end.getTimeInMillis();
		Long oneDay = 1000 * 60 * 60 * 24l;
		Long time = startTime;  
		Map<Long, Object> comparemap=new TreeMap<>();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		  while (time <= endTime) {  
		        Date d = new Date(time);            
		        Map<String, Object> posChargeDatas = chargeSerivce.getParkChargeCountByDay(parkId, df.format(d));
		        comparemap.put(d.getTime(), posChargeDatas);
		        time += oneDay;  
		    }     
		return  Utility.gson.toJson(comparemap);
	}

	@RequestMapping(value = "/pageByParkId", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageByParkId(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		int start = (int) args.get("start");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPageByParkId(parkId, start, count));
	}

	@RequestMapping(value = "/pageArrearage", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageArrearage(@RequestBody Map<String, Object> args) {
		int low = (int) args.get("low");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPageArrearage(low, count));
	}

	@RequestMapping(value = "/getParkCarportStatusToday", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String getParkCarportStatusToday(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getParkCarportStatusToday(parkId));
	}

	@RequestMapping(value = "/pageArrearageByParkId", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String pageArrearageByParkId(@RequestBody Map<String, Object> args) {
		int parkId = (int) args.get("parkId");
		int start = (int) args.get("start");
		int count = (int) args.get("count");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getPageArrearageByParkId(parkId, start, count));
	}

	@RequestMapping(value = "/get", method = { RequestMethod.GET, RequestMethod.POST }, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String get(@RequestParam(value = "cardNumber", required = false) String cardNumber) {
		List<PosChargeData> charges = null;
		if (cardNumber != null) {
			try {
				charges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
			}
		} else {
			charges = chargeSerivce.getUnCompleted();
		}

		return Utility.createJsonMsg(1001, "success", charges);
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String insert(@RequestBody PosChargeData charge) throws ParseException {

		int parkId = charge.getParkId();
		Park park = parkService.getParkById(parkId);
		// Outsideparkinfo outsideparkinfo =
		// outsideParkInfoService.getByParkidAndDate(parkId,
		// charge.getEntranceDate());

		if (park == null || park.getFeeCriterionId() == null) {
			return Utility.createJsonMsg(1002, "请先绑定计费标准到停车场");
		}

		if (charge.getEntranceDate() == null) {
			charge.setEntranceDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		int ret = chargeSerivce.insert(charge);
		if (ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String modify(@RequestBody PosChargeData charge) {

		int ret = chargeSerivce.update(charge);
		if (ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}

	@RequestMapping(value = "/updateYj", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String updateYj(@RequestBody Map<String, String> args) {
		int parkId = Integer.parseInt(args.get("parkId"));
		String cardNumber = (String) args.get("cardNumber");
		Double yajin = Double.parseDouble(args.get("yajin"));
		List<PosChargeData> posChargeDatas = chargeSerivce.getByParkIdAndCardNumber(parkId, cardNumber);
		if (posChargeDatas.isEmpty()) {
			return Utility.createJsonMsg(1002, "no record");
		}

		int ret = chargeSerivce.update(posChargeDatas.get(0));
		if (ret == 1)
			return Utility.createJsonMsg(1001, "success");
		else
			return Utility.createJsonMsg(1002, "failed");
	}

	@RequestMapping(value = "/query", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String query(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				queryCharges = chargeSerivce.queryDebt(cardNumber, new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/getArrearageByCardNumber", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getArrearageByCardNumber(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		List<PosChargeData> queryCharges = chargeSerivce.getArrearageByCardNumber(cardNumber);
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/queryCurrent", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	public @ResponseBody String queryCurrent(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");
		List<PosChargeData> queryCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				queryCharges = chargeSerivce.queryCurrentDebt(cardNumber, new Date());
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}
		return Utility.createJsonMsg(1001, "success", queryCharges);
	}

	@RequestMapping(value = "/unpaid", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String getDebt(@RequestBody Map<String, Object> args) throws ParseException {
		String cardNumber = (String) args.get("cardNumber");
		String exitDate = (String) args.get("exitDate");

		List<PosChargeData> unpaidCharges = null;
		if (exitDate != null) {
			Date eDate = new SimpleDateFormat(Constants.DATEFORMAT).parse(exitDate);
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber, eDate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		} else {
			try {
				unpaidCharges = chargeSerivce.getDebt(cardNumber);
			} catch (Exception e) {
				return Utility.createJsonMsg(1002, "请先绑定停车场计费标准");
			}
		}

		return Utility.createJsonMsg(1001, "success", unpaidCharges);
	}

	@RequestMapping(value = "/pay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String pay(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");

		PosChargeData payRet = null;
		try {
			payRet = chargeSerivce.pay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准");
		}

		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/payById", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String payById(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");
		Integer chargeId = (Integer) args.get("id");
		PosChargeData payRet = chargeSerivce.getById(chargeId);
		if (payRet != null && payRet.getCardNumber().equals(cardNumber)) {
			if (payRet.getUnPaidMoney() <= money) {
				payRet.setGivenMoney(money);
				money -= payRet.getUnPaidMoney();
				payRet.setPaidCompleted(true);
				String data = new DecimalFormat("0.00").format(money);
				payRet.setChangeMoney(Double.parseDouble(data));
				chargeSerivce.update(payRet);
			} else {
				payRet.setGivenMoney(money + payRet.getGivenMoney());
				// payRet.setPaidMoney(payRet.getPaidMoney()+money);
				payRet.setUnPaidMoney(payRet.getUnPaidMoney() - money);
				chargeSerivce.update(payRet);
			}
		} else {
			return Utility.createJsonMsg(1002, "付费失败");
		}

		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping(value = "/getFeeOperatorChargeData", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getFeeOperatorChargeData(@RequestBody Map<String, String> args) throws ParseException {
		String startDay = (String) args.get("startDay");
		String endDay = (String) args.get("endDay");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date parsedStartDay = null;
		try {
			parsedStartDay = sdf.parse(startDay + " 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date parsedEndDay = null;
		try {
			parsedEndDay = sdf.parse(endDay + " 23:59:59");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Map<String, Object>> results = chargeSerivce.getFeeOperatorChargeData(parsedStartDay, parsedEndDay);
		return Utility.createJsonMsg(1001, "success", results);
	}

	@RequestMapping(value = "/repay", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String repay(@RequestBody Map<String, Object> args) {
		String cardNumber = (String) args.get("cardNumber");
		double money = (double) args.get("money");

		List<PosChargeData> payRet = null;
		try {
			payRet = chargeSerivce.repay(cardNumber, money);
		} catch (Exception e) {
			return Utility.createJsonMsg(1002, "没有欠费条目或请先绑定停车场计费标准");
		}
		return Utility.createJsonMsg(1001, "success", payRet);
	}

	@RequestMapping("/getExcel")
	@ResponseBody
	public void getExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
		List<PosChargeData> posdatas = chargeSerivce.getPage(0, 50000);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "实收费", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByParkAndDay")
	@ResponseBody
	public void getExcelByParkAndDay(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String date = request.getParameter("date");
		String parkId = request.getParameter("parkId");

		List<PosChargeData> posdatas = chargeSerivce.getByParkAndDay(Integer.parseInt(parkId), date);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "实收费", "应收费", "补交", "返还", "进场时间", "离场时间","渠道" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByDay")
	@ResponseBody
	public void getExcelByDay(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String date = request.getParameter("date");
		List<PosChargeData> posdatas = chargeSerivce.getAllByDay(date);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "实收费", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByParkAndDayRange")
	@ResponseBody
	public void getExcelByParkAndDayRange(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String parkId = request.getParameter("parkId");

		List<PosChargeData> posdatas = chargeSerivce.getByParkAndDayRange(Integer.parseInt(parkId), startDate, endDate);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "实收费", "应收费", "补交", "返还", "进场时间", "离场时间","渠道" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByDayRange")
	@ResponseBody
	public void getExcelByDayRange(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		List<PosChargeData> posdatas = chargeSerivce.getAllByDayRange(startDate, endDate);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "车牌", "停车场名", "车位号", "操作员id", "收费状态", "实收费", "应收费", "补交", "返还", "进场时间", "离场时间" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "poschargedata.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelService.produceExceldataPosChargeData("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "poschargedata.xlsx", response);
	}
}
