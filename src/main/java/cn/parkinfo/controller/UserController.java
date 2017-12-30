package cn.parkinfo.controller;


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
//import org.aspectj.apache.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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
import cn.parkinfo.model.User;
import cn.parkinfo.model.UserDetail;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.UserService;
import cn.parkinfo.service.Utility;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AuthorityService authService;

	private static Log logger = LogFactory.getLog(UserController.class);

	@RequestMapping(value = "/getUserCount", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String getuserCount() {
		Map<String, Object> ret = new HashMap<String, Object>();
		Map<String, Object> body = new HashMap<String, Object>();
		int count = userService.getUserCount();
		body.put("count", count);

		ret.put("status", "1001");
		ret.put("message", "get user detail success");
		ret.put("body", Utility.gson.toJson(body));

		return Utility.gson.toJson(ret);
	}

	@RequestMapping(value = "/getUserDetail", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String accessIndex(@RequestParam("low") int low,
			@RequestParam("count") int count) {
		Map<String, Object> ret = new HashMap<String, Object>();
		List<UserDetail> userDetail = userService.getUserDetail(low, count);
		if (userDetail != null) {
			ret.put("status", "1001");
			ret.put("message", "get user detail success");
			ret.put("body", Utility.gson.toJson(userDetail));
		} else {
			ret.put("status", "1002");
			ret.put("message", "get user detail fail");
		}
		return Utility.gson.toJson(ret);

	}

	@RequestMapping(value = "/insert/user", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String insertUser(@RequestBody User user,HttpServletResponse response) {
		logger.info("userName: " + user.getUserName() + " number: "
				+ user.getNumber());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		return userService.insertUser(user);
	}



	@RequestMapping(value = "/user/passwd", method = RequestMethod.POST)
	@ResponseBody
	public String getUserPasswd(@RequestParam("userName") String userName) {
		return userService.getUserPassword(userName);
	}

	@RequestMapping(value = "/register/user")
	public String registerUser(User user) {
		return "registerUser";
	}

	@RequestMapping(value = "/user/login", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String appUserLogin(@RequestBody Map<String, Object> args) {
		String username = (String) args.get("userName");
		String password = (String) args.get("password");
		User user = userService.getUserByUsername(username);
		if (user == null)
			return Utility.createJsonMsg(1002, "no this user");
		if (!user.getPasswd().equals(password))
			return Utility.createJsonMsg(1003, "password is incorrect");
		else
			return Utility.createJsonMsg(1001, "login successfully", user);

	}

	@RequestMapping(value = "/user/changePassword", method = RequestMethod.POST, produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public String changeUserPassword(@RequestBody Map<String, Object> args) {
		String username = (String) args.get("userName");
		String password = (String) args.get("newPassword");
		if (userService.changeUserPassword(username, password) > 0)
			return Utility.createJsonMsg(1001, "change password successfully");
		else
			return Utility.createJsonMsg(1002, "user no exists");
	}



}
