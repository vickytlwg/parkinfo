package cn.parkinfo.controller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import cn.parkinfo.model.Park;
import cn.parkinfo.model.Posdata;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.ExcelExportService;
import cn.parkinfo.service.ParkService;
import cn.parkinfo.service.PosChargeDataService;
import cn.parkinfo.service.PosdataService;

import cn.parkinfo.service.Utility;


@Controller
@RequestMapping("/pos")
public class PosdataController {
	
@Autowired
private PosdataService posdataService;
@Autowired
private ParkService parkService;
@Autowired
private AuthorityService authService;
@Autowired
PosChargeDataService chargeSerivce;

@Autowired
private ExcelExportService excelService;
private static Log logger = LogFactory.getLog(PosdataController.class);


@RequestMapping(value="/getChargeDetail", produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getChargeDetail(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectAll();
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/chargeDetail")
public String chargeDetail(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	String username = (String) session.getAttribute("username");
	List<Park> parkList = parkService.getParks();
	if(username != null)
		parkList = parkService.filterPark(parkList, username);
	List<Park> parkl = new ArrayList<>();
	for (Park park : parkList) {
		if (park.getType()==3) {
			parkl.add(park);
		}
	}
	modelMap.addAttribute("parks", parkl);
	AuthUser user = authService.getUserByUsername(username);
	if(user != null){
		modelMap.addAttribute("user", user);
		boolean isAdmin = false;
		if(user.getRole() == AuthUserRole.ADMIN.getValue())
			isAdmin=true;
		modelMap.addAttribute("isAdmin", isAdmin);
		
	}
	return "posChargeDetail";
}
@RequestMapping(value="/carportUsage")
public String carportUsage(ModelMap modelMap, HttpServletRequest request, HttpSession session){
	List<Park> parkList = parkService.getParks();
	String username = (String) session.getAttribute("username");
	if(username != null)
		parkList = parkService.filterPark(parkList, username);

	List<Park> parkl = new ArrayList<>();
	for (Park park : parkList) {
		if (park.getType()==3) {
			parkl.add(park);
		}
	}
	modelMap.addAttribute("parks", parkl);
	
	AuthUser user = authService.getUserByUsername(username);
	if(user != null){
		modelMap.addAttribute("user", user);
		boolean isAdmin = false;
		if(user.getRole() == AuthUserRole.ADMIN.getValue())
			isAdmin=true;
		modelMap.addAttribute("isAdmin", isAdmin);
		
		
	}
	
	return "carportUsage";
}
@RequestMapping(value="/getPosdataCount",method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getPosdataCount(){
	Map<String, Object> retMap = new HashMap<String, Object>();
	Integer count=posdataService.getPosdataCount();
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("count", count);
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/getPosdataCountByPark/{parkId}",method=RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
@ResponseBody
public String getPosdataCountByPark(@PathVariable("parkId")Integer parkId){
	Map<String, Object> retMap = new HashMap<String, Object>();
	Integer count=posdataService.getPosdataCountByPark(parkId);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("count", count);
	return Utility.gson.toJson(retMap);
}

@RequestMapping("/getExcel")
@ResponseBody
public void getExcel(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
	List<Posdata> posdatas=posdataService.selectPosdataByPage(0,100000);
	String docsPath = request.getSession().getServletContext().getRealPath("/");
	final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	String[] headers={"车牌","停车场名","车位号","出入场","操作员id","终端机号","应收费","押金","补交","返还","进场时间","离场时间"};
	OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR+ "posdata.xlsx");
	XSSFWorkbook workbook = new XSSFWorkbook();
	excelService.produceExceldataPosData("收费明细", headers, posdatas, workbook);
	try {
		workbook.write(out);
	} catch (IOException e) {
		e.printStackTrace();
	}
	Utility.download(docsPath + FILE_SEPARATOR+ "posdata.xlsx", response);
}
@RequestMapping("/getExcelByDayRangeAndPark")
@ResponseBody
public void getExcelByDayRangeAndPark(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
	int parkId=Integer.parseInt(request.getParameter("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=request.getParameter("startday");
	String endDay=request.getParameter("endday");
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
	List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(parkName, parsedStartDay, parsedEndDay);
	String docsPath = request.getSession().getServletContext().getRealPath("/");
	final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	String[] headers={"车牌","停车场名","车位号","出入场","操作员id","终端机号","应收费","押金","补交","返还","进场时间","离场时间"};
	OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR+ "posdata.xlsx");
	XSSFWorkbook workbook = new XSSFWorkbook();
	excelService.produceExceldataPosData("收费明细", headers, posdatas, workbook);
	try {
		workbook.write(out);
	} catch (IOException e) {
		e.printStackTrace();
	}
	Utility.download(docsPath + FILE_SEPARATOR+ "posdata.xlsx", response);
}
@RequestMapping("/getExcelByDayAndPark")
@ResponseBody
public void getExcelByDayAndParkid(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException{
	int parkId=Integer.parseInt(request.getParameter("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=request.getParameter("startday");
//	String endDay=request.getParameter("endday");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay + " 00:00:00");
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(startDay + " 23:59:59");
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(parkName, parsedStartDay, parsedEndDay);
	String docsPath = request.getSession().getServletContext().getRealPath("/");
	final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
	String[] headers={"车牌","停车场名","车位号","出入场","操作员id","终端机号","应收费","押金","补交","返还","进场时间","离场时间"};
	OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR+ "posdata.xlsx");
	XSSFWorkbook workbook = new XSSFWorkbook();
	excelService.produceExceldataPosData("收费明细", headers, posdatas, workbook);
	try {
		workbook.write(out);
	} catch (IOException e) {
		e.printStackTrace();
	}
	Utility.download(docsPath + FILE_SEPARATOR+ "posdata.xlsx", response);
}
@RequestMapping(value="/getParkCharge",method=RequestMethod.GET)
@ResponseBody
public String getParkCharge(@RequestParam("parkId") int parkId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getParkCharge(parkId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
}

@RequestMapping(value="/getCarportCharge",method=RequestMethod.GET)
@ResponseBody
public String getCarportCharge(@RequestParam("carport") int carportId, @RequestParam("startDay")String startDay, @RequestParam("endDay")String endDay ){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	Date parsedStartDay = null;
	try {
		parsedStartDay = sdf.parse(startDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Date parsedEndDay  = null;
	try {
		parsedEndDay = sdf.parse(endDay);
	} catch (ParseException e) {
		e.printStackTrace();
	}	
	Map<String, Object> result = posdataService.getCarportCharge(carportId, parsedStartDay, parsedEndDay);
	return Utility.createJsonMsg(1001, "success", result);
}


@RequestMapping(value="/selectPosdataByPage",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByPage(@RequestBody Map<String,Object> args){
	int low=(Integer)args.get("low");
	int count=(Integer)args.get("count");
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectPosdataByPage(low, count);
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}
@RequestMapping(value="/selectPosdataByPageAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByPageAndPark(@RequestBody Map<String,Object> args){
	int low=(Integer)args.get("low");
	int count=(Integer)args.get("count");
	int parkId=(Integer)args.get("parkId");
	Map<String, Object> retMap = new HashMap<String, Object>();
	List<Posdata> posdatas=posdataService.selectPosdataByPageAndPark(parkId, low, count);
	if(posdatas!=null)
	{
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}
	else
	{
		retMap.put("status", 1002);
		retMap.put("message", "failure");
	}
	return Utility.gson.toJson(retMap);
}

@RequestMapping(value="/selectPosdataByParkAndRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByParkAndRange(@RequestBody Map<String,Object> args){
	int parkId=1;
	try {
		 parkId=Integer.parseInt((String)args.get("parkId"));
	} catch (Exception e) {
		// TODO: handle exception
		parkId=(int) args.get("parkId");
	}
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	Map<String, Object> retMap = new HashMap<String, Object>();
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
	List<Posdata> posdatas=posdataService.selectPosdataByParkAndRange(parkName, parsedStartDay, parsedEndDay);
	if (posdatas.isEmpty()) {
	//	List<PosChargeData> posChargeDatas = chargeSerivce.selectPosdataByParkAndRange(parsedStartDay, parsedEndDay, parkId);
	//	if (posChargeDatas.isEmpty()) {
			retMap.put("status", 1002);
	//	}
	//	retMap.put("status", 1001);
	//	retMap.put("message", "success");
	//	retMap.put("body", posChargeDatas);
	}
	else {
		retMap.put("status", 1001);
		retMap.put("message", "success");
		retMap.put("body", posdatas);
	}	
	return Utility.gson.toJson(retMap);
}


@RequestMapping(value="/selectPosdataByParkAndCarportId",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String selectPosdataByParkAndCarportId(@RequestBody Map<String,Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	String carportId=(String)args.get("carportId");
	Map<String, Object> retMap = new HashMap<String, Object>();
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
	List<Posdata> posdatas=posdataService.getPosdataByCarportAndRange(parkName,carportId, parsedStartDay, parsedEndDay);
	retMap.put("status", 1001);
	retMap.put("message", "success");
	retMap.put("body", posdatas);
	return Utility.gson.toJson(retMap);
}


@RequestMapping(value="/getParkChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getParkChargeByRange(@RequestBody Map<String, Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
//	Park park = parkService.getParkById(parkId);
//	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
//	Map<String, Object> retMap = new HashMap<>();
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
        Map<String,Object> tmpmap=posdataService.getParkChargeByDay(parkId, df.format(d));
        comparemap.put(d.getTime(), tmpmap);
        time += oneDay;  
    }     	
	return Utility.gson.toJson(comparemap);
}

@RequestMapping(value="/getParkChargeByRange2",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getParkChargeByRange2(@RequestBody Map<String, Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
//	Park park = parkService.getParkById(parkId);
//	String parkName=park.getName();
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
//	Map<String, Object> retMap = new HashMap<>();
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
        Map<String,Object> tmpmap=posdataService.getParkChargeByDay2(parkId, df.format(d));
        comparemap.put(d.getTime(), tmpmap);
        time += oneDay;  
    }     	
	return Utility.gson.toJson(comparemap);
}

@RequestMapping(value="/getCountsByCard",produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCountsByCard(){
	Map<String, Object> ret=new HashMap<>();
	List<Map<String, Object>> data=posdataService.getCountByCard();
	if (data!=null) {
		ret.put("status", 1001);
	}
	ret.put("body", data);
	return Utility.gson.toJson(ret);
}
@RequestMapping(value="/getCarportChargeByRange",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
@ResponseBody
public String getCarportChargeByRange(@RequestBody Map<String, Object> args){
	int parkId=Integer.parseInt((String)args.get("parkId"));
	Park park = parkService.getParkById(parkId);
	String parkName=park.getName();
	String carportId=(String)args.get("carportId");
	String startDay=(String)args.get("startDay");
	String endDay=(String)args.get("endDay");
	Map<String, Object> retMap = new HashMap<>();
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
	          
	        Map<String,Object> tmpmap=posdataService.getCarpotChargeByDay(parkId, carportId, df.format(d));
	        comparemap.put(d.getTime(), tmpmap);
	    //    retMap.put(df.format(d), tmpmap);
	        time += oneDay;  
	    }
	 return Utility.gson.toJson(comparemap);
}

}
