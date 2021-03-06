package cn.parkinfo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;


import cn.parkinfo.service.UserParkService;
import cn.parkinfo.dao.MonthuserDAO;
import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Monthuser;
import cn.parkinfo.model.Monthuserpark;
import cn.parkinfo.model.Park;
import cn.parkinfo.model.PosChargeData;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.ExcelExportService;
import cn.parkinfo.service.MonthUserParkService;
import cn.parkinfo.service.MonthUserService;
import cn.parkinfo.service.ParkService;
import cn.parkinfo.service.PosChargeDataService;
import cn.parkinfo.service.Utility;

@Controller
@RequestMapping("monthUser")
public class MonthUserController {
	@Autowired
	private MonthUserService monthUserService;
	@Autowired
	private MonthUserParkService monthUserParkService;
	@Autowired
	private AuthorityService authService;
	@Autowired
	private UserParkService userParkService;
	@Autowired
	private ExcelExportService excelExportService;
	@Autowired
	ParkService parkService;
	@Autowired
	private PosChargeDataService chargeSerivce;
	@Autowired
	private MonthuserDAO mdao;


	@RequestMapping(value = "/getExcel")
	@ResponseBody
	public void getExcelByPark(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String parkId = request.getParameter("parkId");

		List<Monthuser> posdatas = monthUserService.getByPark(Integer.parseInt(parkId));
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "停车场名", "卡号", "车主姓名", "车牌号", "描述", "类型", "开始时间", "结束时间", "支付金额", "状态"};
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "monthusers.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelExportService.produceExceldataMonthUser("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "monthusers.xlsx", response);
	}

	@RequestMapping(value = "/getExcelByParkAndDayRange")
	@ResponseBody
	public void getExcelByParkAndDayRange(HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, NumberFormatException, ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String parkId = request.getParameter("parkId");

		List<Monthuser> posdatas = monthUserService.getByParkAndDayRange(Integer.parseInt(parkId), startDate, endDate);
		String docsPath = request.getSession().getServletContext().getRealPath("/");
		final String FILE_SEPARATOR = System.getProperties().getProperty("file.separator");
		String[] headers = { "停车场名", "卡号", "车主姓名", "车牌号", "描述", "类型", "开始时间", "结束时间", "支付金额", "状态" };
		OutputStream out = new FileOutputStream(docsPath + FILE_SEPARATOR + "monthusers.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook();
		excelExportService.produceExceldataMonthUser("收费明细", headers, posdatas, workbook);
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utility.download(docsPath + FILE_SEPARATOR + "monthusers.xlsx", response);
	}

	@RequestMapping(value = "/getExcelDownload")
	@ResponseBody
	public void download(HttpServletRequest request,HttpServletResponse response){
		//获取下载模板文件路径
		String filename=request.getParameter("monthUser");
		//下载的文件模板名
		response.setHeader("Content-disposition","attachment;filename="+filename);
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		//获取项目中文件模板路劲
		String filepath=request.getSession().getServletContext().getRealPath("/WEB-INF/excel")+"/"+filename;
		filepath=filepath.replace("\\", "/");
		InputStream input=null;
		OutputStream output=null;
		try {
			input=new FileInputStream(filepath); 
			output=response.getOutputStream();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] b=new byte[2048];
		int len;
		try {
			while((len=input.read(b))!=-1){
				output.write(b,0,len);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	@RequestMapping(value="")
	public String index(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);


		}
		return "monthUser";
	}

	@RequestMapping(value = "getByPlateNumber", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByPlateNumber2(@RequestBody Map<String, String> args, HttpSession session) {
		String platenumber = args.get("platenumber");
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Monthuser>  monthusersResult=new ArrayList<>();
		if (user.getRole() == AuthUserRole.ADMIN.getValue()){
			monthusersResult=monthUserService.getByPlateNumber(platenumber);
		}
		else {
			List<Park> parkList=parkService.getParks();
			parkList = parkService.filterPark(parkList, username);
			for (Park park : parkList) {
				List<Monthuser> monthusers=monthUserService.getByCarnumberAndPark(platenumber, park.getId());
				if (!monthusers.isEmpty()) {
					monthusersResult.addAll(monthusers);
				}
			}
		}
		return Utility.createJsonMsg(1001, "success", monthusersResult);
	}
	
	@RequestMapping(value = "getByPlateNumber2", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8" })
	@ResponseBody
	public String getByPlateNumber3(@RequestBody Map<String, String> args, HttpSession session) {
		String platenumber = args.get("platenumber");
		String type=args.get("type");
		int resultType=-1;
		switch(type){
		case "包":
			resultType=0;
			break;
		case "包月":
			resultType=0;
			break;
		case "包月用户":
			resultType=0;
			break;
		case "预约":
			resultType=1;
			break;
		case "预":
			resultType=1;
			break;
		case "月卡":
			resultType=2;
			break;
		case "月卡A":
			resultType=2;
			break;
		case "月卡A1":
			resultType=2;
			break;
		case "月卡A2":
			resultType=3;
			break;
		case "月卡B":
			resultType=4;
			break;
		case "月卡D":
			resultType=5;
			break;
		case "月卡E":
			resultType=6;
			break;
		}
		String owner=args.get("owner");
		String certificatetype=args.get("certificateType");
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Monthuser>  monthusers=monthUserService.getByPlateNumberBytype(platenumber, resultType,owner,certificatetype);
		List<Monthuser>  monthusersResult=new ArrayList<>();
		if (user.getRole() == AuthUserRole.ADMIN.getValue()){
			monthusersResult=monthusers;
		}
		else {
			List<Park> parkList=parkService.getParks();
			parkList = parkService.filterPark(parkList, username);
			for (Monthuser mm : monthusers) {
				for (Park park : parkList) {
					/*System.err.println("park"+park.toString());
					System.err.println("mm"+mm.toString());*/
					if (park.getId()==mm.getParkid().intValue()) {
						monthusersResult.add(mm);
					}
				}}

		}
		return Utility.createJsonMsg(1001, "success", monthusersResult);
	}

	@RequestMapping(value="order")
	public String indexOrder(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "monthUserOrder";
	}
	@RequestMapping(value="getParkNamesByUserId/{userId}",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getParkNamesByUserId(@PathVariable("userId")int userId){
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> parkNames=monthUserParkService.getOwnParkName(userId);
		result.put("status", 1001);
		result.put("body", parkNames);
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="getUsersByParkId/{parkId}",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getUsersByParkId(@PathVariable("parkId")int parkId){
		Map<String, Object> result=new HashMap<>();
		List<Map<String, Object>> users=monthUserParkService.getUsersByParkId(parkId);
		result.put("status", 1001);
		result.put("body", users);
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="deletePark/{id}",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String deletePark(@PathVariable("id")int id){
		Map<String, Object> result=new HashMap<>();
		Monthuserpark monthuserpark=monthUserParkService.selectByPrimaryKey(id);
		monthUserParkService.deleteByPrimaryKey(id);
		int num=monthUserService.deleteByPrimaryKey(monthuserpark.getMonthuserid());
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	
	

	@RequestMapping(value="insertPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String insertPark(@RequestBody Monthuserpark monthUserPark){
		Map<String, Object> result=new HashMap<>();
		Monthuser monthuser=monthUserService.selectByPrimaryKey(monthUserPark.getMonthuserid());
		monthuser.setParkid(monthUserPark.getParkid());
		monthuser.setId(null);
		int num= monthUserService.insertSelective(monthuser);
		monthUserParkService.insert(monthUserPark);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="deletePark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String deletePark(@RequestBody Monthuserpark monthUserPark){
		Map<String, Object> result=new HashMap<>();
		int num= monthUserService.deleteByPrimaryKey(monthUserPark.getMonthuserid());
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String insert(@RequestBody Monthuser monthUser){
		Map<String, Object> result=new HashMap<>();
		List<Monthuser> monthusers=monthUserService.getByCarnumberAndPark2(monthUser.getPlatenumber(), monthUser.getParkid());

		if (!monthusers.isEmpty()) {
			result.put("status", 1002);
			result.put("message", "用户已存在");  
			return Utility.gson.toJson(result);
		}


		int num=monthUserService.insertSelective(monthUser);

		if (num==1) {
			result.put("status", Integer.valueOf(1001));

		}
		else {
			result.put("status", Integer.valueOf(1002));
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="insertOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String insertOrder(@RequestBody Monthuser monthUser){
		Map<String, Object> result=new HashMap<>();
		monthUser.setType(1);
		//	Date startd=monthUser.getStarttime();
		//	Date endd=monthUser.getEndtime();
		//	if (endd.getTime()-startd.getTime()>1000*60*60*24) {
		//		result.put("status", 1002);
		//		result.put("message", "时间不能超过24小时");
		//		return Utility.gson.toJson(result);
		//	}
		int num=monthUserService.insert(monthUser);
		if (num==1) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "failed");
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="delete/{id}",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String delete(@PathVariable("id")int id){
		Map<String, Object> result=new HashMap<>();
		int num=monthUserService.deleteByPrimaryKey(id);
		if (num==1||num>=1) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "failed");
		}
		return Utility.gson.toJson(result);
	}

	//删除预约
	@RequestMapping(value="deleteByIdandnumber",method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String deleteByIdandnumber(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		/*Object objectId= args.get("id");*/
		int id=(int) args.get("id");
		Monthuser user=monthUserService.getByPlateNumberById(id);
		String platenumber=user.getPlatenumber();   

		Object platenumberObj= args.get("plateNumber");
		String platenumber2="";
		int resultnum=0;
		if(platenumberObj==null){
			result.put("status", 1002);
			result.put("message", "无法预约删除！");
		}else{
			platenumber2=args.get("plateNumber").toString();
			if(platenumber.equals(platenumber2)){
				resultnum=monthUserService.deleteByPrimaryKey(id);
				result.put("status", 1001);
				result.put("message", "删除预约成功！");
			}else{
				result.put("status", 1003);
				result.put("message", "删除预约失败！");
			}
		}
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="deleteByNameAndParkOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String deleteByNameAndParkOrder(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		String username = (String) args.get("username");
		int parkid = (int) args.get("parkid");
		List<Monthuser> monthusers=monthUserService.getByUsernameAndPark(username, parkid);
		if (monthusers.isEmpty()) {
			result.put("status", 1002);
			result.put("message", "没有预约");
			return Utility.gson.toJson(result);
		}
		int num=0;
		for (Monthuser monthuser : monthusers) {
			if (monthuser.getType()==1) {
				num=monthUserService.deleteByPrimaryKey(monthuser.getId());
			}
		}

		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
			result.put("message", "删除失败");
		}
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="deleteByNameAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String deleteByNameAndPark(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		String username = (String) args.get("username");
		int parkid = (int) args.get("parkid");
		List<Monthuser> monthusers=monthUserService.getByUsernameAndPark(username, parkid);
		if (monthusers.isEmpty()) {
			result.put("status", 1002);	
			result.put("message", "没有预约");
			return Utility.gson.toJson(result);
		}
		int num=0;
		for (Monthuser monthuser : monthusers) {
			if (monthuser.getType()==0) {
				num=monthUserService.deleteByPrimaryKey(monthuser.getId());
			}
		}

		if (num==1) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "删除失败");
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="deleteByCarnumberAndPark",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String deleteByCarnumberAndPark(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		String carnumber = (String) args.get("carnumber");
		int parkid = (int) args.get("parkid");
		List<Monthuser> monthusers=monthUserService.getByCarnumberAndPark(carnumber, parkid);
		if (monthusers.isEmpty()) {
			result.put("status", 1002);
			result.put("message", "没有预约");
			return Utility.gson.toJson(result);
		}
		int num=0;
		for (Monthuser monthuser : monthusers) {
			if (monthuser.getType()==0) {
				num=monthUserService.deleteByPrimaryKey(monthuser.getId());
			}
		}

		if (num==1) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "删除失败");
		}
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="updateBatchRenewal",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String updateBatchRenewal(@RequestBody Monthuser monthUser){
		Map<String, Object> result=new HashMap<>();
		int num=monthUserService.updateBatchRenewal(monthUser);
		if (num>0) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "failed");
		}
		return Utility.gson.toJson(result);
	}

	@RequestMapping(value="update",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String update(@RequestBody Monthuser monthUser){
		Map<String, Object> result=new HashMap<>();
		int num=monthUserService.updateByPrimaryKeySelective(monthUser);
		if (num==1) {
			result.put("status", 1001);
			result.put("message", "ok");
		}
		else {
			result.put("status", 1002);
			result.put("message", "failed");
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="updateOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String updateOrder(@RequestBody Monthuser monthUser){
		Map<String, Object> result=new HashMap<>();
		monthUser.setType(1);
		//	Date startd=monthUser.getStarttime();
		//	Date endd=monthUser.getEndtime();
		//	if (endd.getTime()-startd.getTime()>1000*60*60*24) {
		//		result.put("status", 1002);
		//		result.put("message", "时间不能超过24小时");
		//		return Utility.gson.toJson(result);
		//	}
		int num=monthUserService.updateByPrimaryKeySelective(monthUser);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="selectByPrimaryKey/{id}",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String selectByPrimaryKey(@PathVariable("id")int id){
		Map<String, Object> result=new HashMap<>();
		Monthuser monthUser=monthUserService.selectByPrimaryKey(id);
		if (monthUser!=null) {
			result.put("status", 1001);
			result.put("body", monthUser);
		}
		else{
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="getCount",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getCount(){
		Map<String, Object> result=new HashMap<>();
		int count=monthUserService.getCount();
		result.put("count", count);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="getCountByParkId",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getCountByParkId(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		int parkId=(int) args.get("parkId");
		int count=monthUserService.getCountByParkId(parkId);
		result.put("count", count);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="getByParkIdAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByParkIdAndCount(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		int parkId=(int) args.get("parkId");
		int start=(int) args.get("start");
		int count=(int) args.get("count");
		List<Monthuser> monthusers=monthUserService.getByParkIdAndCount(parkId, start, count);
		if (monthusers!=null) {
			result.put("status", 1001);
			result.put("body", monthusers);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="getByParkIdAndCountOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByParkIdAndCountOrder(@RequestBody Map<String, Object> args){
		Map<String, Object> result=new HashMap<>();
		int parkId=(int) args.get("parkId");
		int start=(int) args.get("start");
		int count=(int) args.get("count");
		List<Monthuser> monthusers=monthUserService.getByParkIdAndCountOrder(parkId, start, count,1);
		if (monthusers!=null) {
			result.put("status", 1001);
			result.put("body", monthusers);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/getByStartAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByStartAndCount(@RequestParam("start")int start,@RequestParam("count")int count,HttpSession session){
		Map<String, Object> result=new HashMap<>();
		List<Monthuser> monthusers=monthUserService.getByStartAndCount(start, count);
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		List<Integer> filterParkIds = userParkService.getOwnParkId(user.getId());
		Set<Integer> filterParkIdSet = new HashSet<Integer>(filterParkIds);
		List<Monthuser> fiList=new ArrayList<>();
		if(user.getRole() == AuthUserRole.ADMIN.getValue()){
		}
		else {

			for (Monthuser monthuser : monthusers) {
				if(filterParkIdSet.contains(monthuser.getParkid())){
					fiList.add(monthuser);
				}  
			}
			monthusers=fiList;
		}
		if (monthusers!=null) {
			result.put("status", 1001);
			result.put("body", monthusers);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/getByStartAndCountOrder",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByStartAndCountOrder(@RequestParam("start")int start,@RequestParam("count")int count){
		Map<String, Object> result=new HashMap<>();
		List<Monthuser> monthusers=monthUserService.getByStartAndCountOrder(start, count,1);
		if (monthusers!=null) {
			result.put("status", 1001);
			result.put("body", monthusers);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}

}
