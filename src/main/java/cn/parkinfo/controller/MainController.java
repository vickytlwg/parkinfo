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

import cn.parkinfo.service.AuthorityService;
import cn.parkinfo.service.ParkService;

import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.Park;

@Controller
@RequestMapping("/")
public class MainController {
	@Autowired
	ParkService parkService;
	@Autowired
	private AuthorityService authService;


	@RequestMapping("index")
	public String index1(){
		return "index";
	}
	@RequestMapping("visualization")
	public String visualization(){
		return "visualization";
	}
	@RequestMapping("report")
	public String report(){
		return "report";
	}
	@RequestMapping("trade")
	public String trade(){
		return "trade";
	}
	@RequestMapping("operation")
	public String operation(){
		return "operation";
	}
	@RequestMapping("service")
	public String service(){
		return "service";
	}
	@RequestMapping("set")
	public String set(){
		return "set";
	}
	@RequestMapping("reconciliation")
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
	@RequestMapping("set/zoneCenter")
	public String setzoneCenter(){
		return "set/zoneCenter";
	}
	@RequestMapping("set/area")
	public String setarea(){
		return "set/area";
	}
	@RequestMapping("set/street")
	public String setstreet(){
		return "set/street";
	}
	@RequestMapping("set/park")
	public String setpark(){
		return "set/park";
	}
	@RequestMapping("set/feeCriterion")
	public String setfeeCriterion(){
		return "set/feeCriterion";
	}
	
	@RequestMapping("report/finance")
	public String reportfinance(){
		return "report/finance";
	}
	
	@RequestMapping("report/month")
	public String reportmonth(){
		return "report/month";
	}
	
//	@RequestMapping("report/monthUser")
//	public String reportmonthUser(){
//		return "report/monthUser";
//	}
	
	@RequestMapping("report/user")
	public String reportuser(){
		return "report/user";
	}
	
	@RequestMapping("report/searchArrearage")
	public String reportsearchArrearage(){
		return "report/searchArrearage";
	}
	
	@RequestMapping("report/carstop")
	public String reportcarstop(){
		return "report/carstop";
	}
	
	@RequestMapping("report/searchParking")
	public String reportsearchParking(){
		return "report/searchParking";
	}
	
	@RequestMapping("report/CarportStatus")
	public String carportStatus(){
		return "report/carportStatus";
	}
	
	@RequestMapping("report/infoToday")
	public String reportinfoToday(){
		return "report/infoToday";
	}
	
	@RequestMapping("outsideParkStatus")
	public String outsideParkStatus(){
		return "outsideParkStatus";
	}
	
	@RequestMapping("operation/unpaid")
	public String operationunpaid(){
		return "operation/unpaid";
	}
	
	@RequestMapping("operation/illegalpage")
	public String operationillegalpage(){
		return "operation/illegalpage";
	}
}
