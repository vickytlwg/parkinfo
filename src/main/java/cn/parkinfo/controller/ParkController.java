package cn.parkinfo.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Constants;
import cn.parkinfo.model.Park;
import cn.parkinfo.model.ParkDetail;
import cn.parkinfo.model.ParkNews;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.HttpUtil;
import cn.parkinfo.service.ParkService;

import cn.parkinfo.service.Utility;

@Controller
public class ParkController {

	@Autowired
	private ParkService parkService;
	
	@Autowired
	private AuthorityService authService;

	
	private static Log logger = LogFactory.getLog(ParkController.class);
	
	@RequestMapping(value = "/parks", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String parks(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
		
		}
			
		return "park";
	}
	
	@RequestMapping(value = "/outsideParks", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String outsideParks(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
			
			
		}
			
		return "outsidePark";
	}
	
	@RequestMapping(value = "/getPark/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkById(@PathVariable int id, ModelMap modelMap, HttpServletRequest request){
		Park park = parkService.getParkById(id);
	
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", park);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}

	@RequestMapping(value = "/getOutsideParkByStreetId/{streetId}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getOutsideParkByStreetId(@PathVariable("streetId")int streetId){		
		List<Park> parks=parkService.getOutsideParkByStreetId(streetId);
		return Utility.createJsonMsg(1001, "get parks successfully", parks);
	}
	@RequestMapping(value = "/getParkByIds", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByIds(@RequestBody Map<String, Object> args, ModelMap modelMap, HttpServletRequest request){
		List<Integer> ParkIds = (List<Integer>)args.get("Ids");
		List<Park> parks = new ArrayList<Park>();
		for(Integer id : ParkIds){
			Park park = parkService.getParkById(id);
			if(park != null)
				parks.add(park);
		}
		return Utility.createJsonMsg(1001, "get parks successfully", parks);
	}
	@RequestMapping(value="/parkmap")
	public String parkmap(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);
			
		}
		return "parkmap";
	}
	@RequestMapping(value = "/getParkLeftPort/{id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkLeftPort(@PathVariable int id, ModelMap modelMap, HttpServletRequest request){
		Park park = parkService.getParkById(id);
		
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		ret.put("status", 1001);
		if(park != null){
			body.put("leftPort", park.getPortLeftCount());
			body.put("parkId", park.getId());
			body.put("parkName", park.getName());
			body.put("portCount", park.getPortCount());
			ret.put("message", "get park port success");
		}else{
			body.put("leftPort", "null");
			ret.put("message", "park not exist");
		}
		ret.put("body", body);
		
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/getParks", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParks(ModelMap modelMap, HttpServletRequest request, HttpSession session){
		List<Park> parkList = parkService.getParks();
		
		String username = (String) session.getAttribute("username");
		if(username != null)
			parkList = parkService.filterPark(parkList, username);
		else{
			parkList.clear();
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", parkList);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}
	//住
	@RequestMapping(value = "/getNearParks", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getNearParks(@RequestBody Map<String, Object> args){
		
		double longitude = Double.parseDouble(args.get("longitude").toString());
		double latitude = Double.parseDouble(args.get("latitude").toString());
		double radius = Double.parseDouble(args.get("radius").toString());
		List<Park> parkList = parkService.getNearParks(longitude, latitude, radius);
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", parkList);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	}
	
	
	@RequestMapping(value="/getParksByType", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParksByType(@RequestParam(value="type",required=false)Integer type,HttpServletRequest request, HttpSession session){
		
		List<Park> parkList = parkService.getParks();
		String username = (String) session.getAttribute("username");
		if(username != null)
			parkList = parkService.filterPark(parkList, username);
		if (type==null) {
			type=3;
		}
		List<Park> parkl = new ArrayList<>();
		for (Park park : parkList) {
			if (park.getType()==type) {
				parkl.add(park);
			}
		}
		Map<String, Object> ret = new HashMap<String, Object>();
		ret.put("status", 1001);
		ret.put("body", parkl);
		ret.put("message", "get park success");
		return Utility.gson.toJson(ret);
	};
	@RequestMapping(value = "/getParkWithName/{name}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkWithName(@PathVariable String name){
		
	//	logger.info("get park with name: " + name);
		List<Park> parks = parkService.getParkByName(name);
	
		if(parks != null){
			if( parks.size() != 0){
				return Utility.createJsonMsg(1001, "get park success", parks);
			}else{
				logger.debug("park not exist : " + name);
				return Utility.createJsonMsg(1002, "park not exist");
				
			}
				
		}
		return Utility.createJsonMsg(1002, "get park fail");
	}
	
	@RequestMapping(value = "/getParkByName", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByName(@RequestBody Map<String, Object> args){
		String name = (String)args.get("name");
//		logger.info("get park with name: " + name);
		List<Park> parks = parkService.getParkByName(name);
	
		if(parks != null){
			if( parks.size() != 0)
				return Utility.createJsonMsg(1001, "get park success", parks);
			else{
				logger.debug("park not exist : " + name);
				return Utility.createJsonMsg(1002, "park not exist");
			}
				
				
		}
		return Utility.createJsonMsg(1002, "get park fail");
	}
	
	@RequestMapping(value = "/getParkCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = parkService.getParkCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get park detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getOutsideParkCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getOutsideParkCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = parkService.getOutsideParkCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get park detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}

	@RequestMapping(value = "/getParkDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public  String  getParkDetail( @RequestBody Map<String, Object> args,HttpSession session){
		int low = (int)args.get("low");
		int count = (int)args.get("count");
		Map<String, Object> ret = new HashMap<String, Object>();
		List<ParkDetail> parkDetail = parkService.getParkDetail(low, count);
		String username = (String) session.getAttribute("username");
		
		if(username != null){
			
			AuthUser user = authService.getUserByUsername(username);
			if(user != null && user.getRole() != AuthUserRole.ADMIN.getValue())
			{
				parkDetail = parkService.filterParkDetail(parkDetail, username);
				//parkDetail = parkService.getPark();
				//parkDetail = parkService.filterPark(parkDetail, username);
			}
		}			

		ret.put("status", "1001");
		ret.put("message", "get park detail success");
		ret.put("body", parkDetail);

		return Utility.gson.toJson(ret);
		
	}
	
	@RequestMapping(value = "/getOutsideParkDetail", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public  String  getOutsideParkDetail( @RequestBody Map<String, Object> args,HttpSession session){
		int low = (int)args.get("low");
		int count = (int)args.get("count");
		Map<String, Object> ret = new HashMap<String, Object>();
		List<ParkDetail> parkDetail = parkService.getOutsideParkDetail(low, count);
		String username = (String) session.getAttribute("username");		
		if(username != null){			
			AuthUser user = authService.getUserByUsername(username);
			if(user != null && user.getRole() != AuthUserRole.ADMIN.getValue())
			{
				parkDetail = parkService.filterParkDetail(parkDetail, username);
			}
		}			

		ret.put("status", "1001");
		ret.put("message", "get park detail success");
		ret.put("body", parkDetail);

		return Utility.gson.toJson(ret);
		
	}
	
	@RequestMapping(value = "/search/parkBykeywords", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkByLocationkeywords(@RequestBody Map<String, Object> args){
		String str=(String) args.get("keywords");
		List<Park> parkDetail=parkService.getParkDetailByKeywords(str);
		Map<String, Object> ret = new HashMap<String, Object>();
		if(parkDetail != null){
			ret.put("status", "1001");
			ret.put("message", "get park detail success");
			ret.put("body", Utility.gson.toJson(parkDetail));
		}else{
			ret.put("status", "1002");
			ret.put("message", "get park detail fail");
		}
		return Utility.gson.toJson(ret);
	}
	
	@RequestMapping(value = "/insert/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertPark(@RequestBody Park park){
		return parkService.insertPark(park);
	}
	
	@RequestMapping(value = "/update/park", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String updatePark(@RequestBody Park park){
		
		return parkService.updatePark(park);
	}
	

	
	
	@RequestMapping(value = "/delete/park/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@PathVariable int Id){
		return parkService.deletePark(Id);
	}
	
	@RequestMapping(value = "/dynamicNews/queryList", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getSearchParkNews(@RequestBody Map<String, Object> args){
		int pageSize = (int)args.get("pageSize");
		int offset = (int)args.get("offset");
		double longitude = (double)args.get("longitude");
		double latitude = (double)args.get("latitude");
		double radius = (double)args.get("radius");
		List<ParkNews> parkNewsList = parkService.getSearchParkLatestNews(longitude, latitude, radius, offset, pageSize);
		
		return Utility.createJsonMsg(1001, "get news successfully", parkNewsList);
		
	}
	
	@RequestMapping(value = "/insert/parkNews", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertParkNews(@RequestBody ParkNews parkNews){
		parkNews.setDate(new Date());
		parkService.insertParkNews(parkNews);
		return Utility.createJsonMsg(1001, "insert news successfully");
		
	}
	

	
	
	@RequestMapping(value="getLastPark",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getLastPark(){
		Map<String, Object> ret=new HashMap<>();
		Park park=parkService.getLastPark();
		ret.put("park", park);
		return Utility.gson.toJson(ret);
	}

	@RequestMapping(value = "/search/parking", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getSearchParks(@RequestBody Map<String, Object> args){
		
		if(args.containsKey("parkingName")){
			String parkName = (String)args.get("parkingName");
			List<Park> parkList = parkService.getParkByName(parkName);
			return Utility.createJsonMsg(1001, "get park successfully", parkList);
		}
		
		double longitude = (double)args.get("userLocationlongitude");
		double latitude = (double)args.get("userLocationlatitude");
		double radius = (double)args.get("distance");

		List<Park> searchPark = parkService.getNearParks(longitude, latitude, radius);
		if(!args.containsKey("portLeftCount")){
			return Utility.createJsonMsg(1001, "get parks successfully", searchPark);
			
		}else{
			List<Park> filterPark = new ArrayList<Park>();
			
			int portLeftCount = (int)args.get("portLeftCount");
			for(Park park : searchPark){
				if(park.getPortLeftCount() >=  portLeftCount)
					filterPark.add(park);
			}
			return Utility.createJsonMsg(1001, "get parks successfully", filterPark);
		}
		
	}
	

	
}
