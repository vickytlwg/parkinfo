package cn.parkinfo.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.service.AuthorityService;

@Controller
public class IndexController {

	@Autowired
	private AuthorityService authService;
	
	private static Log logger = LogFactory.getLog(IndexController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
	public String accessIndex1(ModelMap modelMap,HttpSession session){
		String username=(String) session.getAttribute("username");
		if (username==null) {
			return "login";
		}
		else {
			AuthUser user = authService.getUserByUsername(username);
			if(user != null){
				modelMap.addAttribute("user", user);
				boolean isAdmin = false;
				if(user.getRole() == AuthUserRole.ADMIN.getValue())
					isAdmin=true;
				modelMap.addAttribute("isAdmin", isAdmin);
				
			}
			return "index";
		}
	}
	
	@RequestMapping(value = "authority", method = RequestMethod.POST)
	public String authority(ModelMap modelMap, @RequestParam("username") String username,@RequestParam("password") String password,HttpSession session,HttpServletRequest request){
		if(authService.checkUserAccess(username, password)){
			logger.error("登录失败 ");
			session.setAttribute("username", username);			
			//String url=request.getServletPath();
	//		AuthUser user = authService.getUserByUsername(username);			
		
			//用户名密码输入正确重定向到首页
			return "redirect:index";
		}else{
			//用户名密码输入错误重定向跳转到重定向后的登录页面
			return "redirect:login";
		}	
	}
	@RequestMapping(value = "login")
	public String login(HttpServletRequest request, HttpServletResponse response,HttpSession session){
		if (session.getAttribute("username")!=null) {
			session.removeAttribute("username");
		}
		return "login";
	}

	@RequestMapping("/parkingInfo")
	public String data2(ModelMap modelMap,HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);		
		
		}
		return "parkingInfo";
	}
	
	@RequestMapping("/generalDataView")
	public String generalDataView(ModelMap modelMap,HttpSession session){
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if(user != null){
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if(user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin=true;
			modelMap.addAttribute("isAdmin", isAdmin);

		}
		return "generalDataView";
	}



}
