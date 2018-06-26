package cn.parkinfo.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Outsideparkinfo;
import cn.parkinfo.model.Park;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.model.Posdata;
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

	@RequestMapping(value = "getByCardnumberAuthority", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByCardnumberAuthority(@RequestBody Map<String, String> args, HttpSession session) {
		String cardNumber = args.get("cardNumber");
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Park> parkList = parkService.getParks();
		if (username == null)
			return null;
		if (user.getRole() == AuthUserRole.ADMIN.getValue()) {
			return Utility.createJsonMsg(1001, "success", chargeSerivce.getByCardNumber(cardNumber));
		}
		parkList = parkService.filterPark(parkList, username);
		List<PosChargeData> posChargeDatas = new ArrayList<>();
		for (Park park : parkList) {
			posChargeDatas.addAll(chargeSerivce.getByCardNumberAndPark(cardNumber, park.getId()));
		}
		return Utility.createJsonMsg(1001, "success", posChargeDatas);
	}

	@RequestMapping(value = "/count", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody String count() {
		int count = chargeSerivce.count();
		return Utility.createJsonMsg(1001, "success", count);
	}

	@RequestMapping(value = "/getByParkName", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByParkName(@RequestBody Map<String, String> args) {
		String parkName = args.get("parkName");
		return Utility.createJsonMsg(1001, "success", chargeSerivce.getByParkName(parkName));
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
