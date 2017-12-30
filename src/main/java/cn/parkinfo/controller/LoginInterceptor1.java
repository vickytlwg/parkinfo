package cn.parkinfo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class LoginInterceptor1 implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		HttpSession session  = request.getSession();
//		session.setAttribute("username", "liumeng");
		
		String url = request.getRequestURI();				
//		HttpSession session  = request.getSession();
//		session.setAttribute("username", "liumeng");
		Object username = session.getAttribute("username");
		if(username != null)
			return true;
		else{
//			session.setAttribute("redirectUrl", url);
			
			request.getRequestDispatcher("/login").forward(request, response);
		}			
					
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	
		
	}

}
