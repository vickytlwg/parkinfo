package cn.parkinfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import cn.parkinfo.model.Zonecenter;
import cn.parkinfo.service.AuthorityService;

import cn.parkinfo.service.Utility;
import cn.parkinfo.service.ZoneCenterService;

@Controller
@RequestMapping("zoneCenter")
public class ZoneCenterController {
	@Autowired
	private ZoneCenterService zoneCenterService;
	@Autowired
	private AuthorityService authService;
	

	
	@RequestMapping(value="insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String insert(@RequestBody Zonecenter zoneCenter){
		Map<String, Object> result=new HashMap<>();
		int num=zoneCenterService.insert(zoneCenter);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="delete/{id}",method=RequestMethod.GET,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String delete(@PathVariable("id")int id){
		Map<String, Object> result=new HashMap<>();
		int num=zoneCenterService.deleteByPrimaryKey(id);
		if (num==1) {
			result.put("status", 1001);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="update",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String update(@RequestBody Zonecenter zoneCenter){
		Map<String, Object> result=new HashMap<>();
		int num=zoneCenterService.updateByPrimaryKeySelective(zoneCenter);
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
		Zonecenter zoneCenter=zoneCenterService.selectByPrimaryKey(id);
		if (zoneCenter!=null) {
			result.put("status", 1001);
			result.put("body", zoneCenter);
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
		int count=zoneCenterService.getCount();
		result.put("count", count);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/getByStartAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByStartAndCount(@RequestParam("start")int start,@RequestParam("count")int count,HttpSession session){
		String username=(String) session.getAttribute("username");
		Map<String, Object> result=new HashMap<>();
		List<Zonecenter> zoneCenters=zoneCenterService.getByUserName(username);
		if (zoneCenters!=null) {
			result.put("status", 1001);
			result.put("body", zoneCenters);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
}
