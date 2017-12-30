package cn.parkinfo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Park;
import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.ParkService;

@Controller
public class OutsideParkStatusController {
	@Autowired
	private ParkService parkService;
	@Autowired
	private AuthorityService authService;
	
	@RequestMapping(value="/outsideParkStatus")
	public String outsideParkStatus(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
		return "outsideParkStatus";
	}
	
	@RequestMapping(value="/outsideParkStatusAdmin")
	public String outsideParkStatusAdmin(ModelMap modelMap, HttpServletRequest request, HttpSession session){
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
		return "outsideParkStatusAdmin";
	}
}
