package cn.parkinfo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import cn.parkinfo.model.Area;
import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.service.AreaService;
import cn.parkinfo.service.AuthorityService;

import cn.parkinfo.service.Utility;


@Controller
@RequestMapping("area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private AuthorityService authService;

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
		return "area";
	}
	
	@RequestMapping(value="insert",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String insert(@RequestBody Area area,HttpServletResponse response){
		//浏览器允许跨域
		response.setHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> result=new HashMap<>();
		int num=areaService.insert(area);
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
		int num=areaService.deleteByPrimaryKey(id);
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
	public String update(@RequestBody Area area){
		Map<String, Object> result=new HashMap<>();
		int num=areaService.updateByPrimaryKeySelective(area);
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
		Area area=areaService.selectByPrimaryKey(id);
		if (area!=null) {
			result.put("status", 1001);
			result.put("body", area);
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
		int count=areaService.getCount();
		result.put("count", count);
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="getByZoneCenterId/{zoneid}",produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByZoneCenterId(@PathVariable("zoneid")int zoneid){
		Map<String, Object> result=new HashMap<>();
		List<Area> areas=areaService.getByZoneCenterId(zoneid);
		if (areas!=null) {
			result.put("status", 1001);
			result.put("body", areas);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
	@RequestMapping(value="/getByStartAndCount",method=RequestMethod.POST,produces={"application/json;charset=utf-8"})
	@ResponseBody
	public String getByStartAndCount(@RequestParam("start")int start,@RequestParam("count")int count){
		Map<String, Object> result=new HashMap<>();
		List<Area> areas=areaService.getByStartAndCount(start, count);
		if (areas!=null) {
			result.put("status", 1001);
			result.put("body", areas);
		}
		else {
			result.put("status", 1002);
		}
		return Utility.gson.toJson(result);
	}
}
