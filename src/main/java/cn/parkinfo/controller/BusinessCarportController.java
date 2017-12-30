package cn.parkinfo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import cn.parkinfo.service.ParkService;

import cn.parkinfo.model.Park;

import cn.parkinfo.model.AuthUser;
import cn.parkinfo.model.AuthUserRole;
import cn.parkinfo.model.BusinessCarportDetailSimple;
import cn.parkinfo.model.CarportStatusDetail;
import cn.parkinfo.model.Status;
import cn.parkinfo.service.AuthorityService;

import cn.parkinfo.service.Utility;
import cn.parkinfo.model.Constants;
import cn.parkinfo.service.HttpUtil;

@Controller
public class BusinessCarportController {

	@Autowired
	private AuthorityService authService;
	@Autowired
	ParkService parkService;

	private static Log logger = LogFactory.getLog(BusinessCarportController.class);

	@RequestMapping(value = "/businessCarport", produces = { "application/json;charset=UTF-8" })
	public String getBusinessCarports(ModelMap modelMap, HttpServletRequest request, HttpSession session) {
		String username = (String) session.getAttribute("username");
		AuthUser user = authService.getUserByUsername(username);
		if (user != null) {
			modelMap.addAttribute("user", user);
			boolean isAdmin = false;
			if (user.getRole() == AuthUserRole.ADMIN.getValue())
				isAdmin = true;
			modelMap.addAttribute("isAdmin", isAdmin);
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
		}

		return "businessCarport";
	}

	@RequestMapping(value = "/getBusinessCarportCount", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object getBusinessCarportCount(@RequestParam(value = "parkId", required = false) Integer parkId) {
		String url = Constants.WEBAPIURL + "/getBusinessCarportCount?parkId=" + parkId;
		Map<String, Object> result = HttpUtil.get(url);
		return result.get("body");

	}

	@RequestMapping(value = "/getBusinessCarportDetail", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object accessIndex(@RequestParam("low") int low, @RequestParam("count") int count,
			@RequestParam(value = "parkId", required = false) Integer parkId, HttpServletResponse response) {
		String url = Constants.WEBAPIURL + "/getBusinessCarportDetail?parkId=" + parkId + "&low=" + low + "&count="
				+ count;
		Map<String, Object> result = HttpUtil.get(url);
		return result.get("body");
	}

	@RequestMapping(value = "/getDayCarportStatusDetail", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object getDayCarportStatusDetail(@RequestParam("carportId") int carportId,
			@RequestParam("startDay") String startDay, @RequestParam("endDay") String endDay) {
		String url = Constants.WEBAPIURL + "/getDayCarportStatusDetail?carportId=" + carportId + "&startDay=" + startDay
				+ "&endDay=" + endDay;
		Map<String, Object> result = HttpUtil.get(url);
		return result.get("body");
	}

	@RequestMapping(value = "/getCarportStatusDetail", method = RequestMethod.GET, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	public Object getCarportStatusDetail(@RequestParam("carportId") int carportId) {
		String url = Constants.WEBAPIURL + "/getCarportStatusDetail?carportId=" + carportId;
		Map<String, Object> result = HttpUtil.get(url);
		return result.get("body");
	}

}
