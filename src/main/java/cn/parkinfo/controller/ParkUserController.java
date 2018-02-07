package cn.parkinfo.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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

import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserDetail;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.User;
import cn.parkinfo.model.UserDetail;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.UserService;
import cn.parkinfo.service.Utility;

@RequestMapping("usermanage")
@Controller
public class ParkUserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authService;
		
	private static Log logger = LogFactory.getLog(ParkUserController.class);	
	
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
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
		return "userManage";					
	}
	
	@RequestMapping(value = "/getParkUserCount", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getuserCount(){
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = authService.getUserCount();
		body.put("count", count);
	
		ret.put("status", "1001");
		ret.put("message", "get user detail success");
		ret.put("body", Utility.gson.toJson(body));
		
		return Utility.gson.toJson(ret);					
	}
	
	@RequestMapping(value = "/getParkUserDetail", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String accessIndex(@RequestParam("low")int low, @RequestParam("count")int count){	
		List<AuthUser> userList = authService.getUsers();
		List<AuthUserDetail> userDetail = new ArrayList<AuthUserDetail>();
		for(AuthUser user : userList){
			List<String> parkName = authService.getOwnUserParkName(user.getId());
			userDetail.add(new AuthUserDetail(user, parkName));
		}
		return Utility.createJsonMsg("1001", "get user detail success", userDetail);
		
	}
	
	@RequestMapping(value = "/getParkUser", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getParkUser(){	
		List<AuthUser> userList = authService.getUsers();
		
		return Utility.createJsonMsg("1001", "get user detail success", userList);
		
	}
	
	@RequestMapping(value = "/insert/parkUser", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String insertUser(@RequestBody Map<String, Object> argMap){
		AuthUser user = new AuthUser();
		user.setUsername((String) argMap.get("username"));
		user.setPassword((String)argMap.get("password"));
		user.setRole((int)argMap.get("role"));
		List<Integer> parkIds = new ArrayList<Integer>();
		for(Object item : (List<?>)argMap.get("parkList")){
			parkIds.add(Integer.parseInt(item.toString()));
		}
		 
		int count  = authService.insertUser(user, parkIds);
		
		if(count > 0)
			return Utility.createJsonMsg("1001", "insert success");
		else
			return Utility.createJsonMsg("1002", "insert failed");
	}
	
	
	@RequestMapping(value = "/delete/parkUser/{Id}", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deletePark(@PathVariable int Id){
		
		 authService.deleteUser(Id);
		 return Utility.createJsonMsg("1001", "delete success");
	}

	
}
