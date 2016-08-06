//package com.jec.module.inteceptor;
//
//import com.jec.module.sysmanage.extern.UserService;
//import org.springframework.context.ApplicationContext;
//import org.springframework.web.context.support.WebApplicationContextUtils;
//
//import java.io.IOException;
//import java.util.regex.Pattern;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//public class AuthFilter implements Filter {
//
//	private ApplicationContext context;
//
//	private String urlReg;
//
//	public void destroy() {
//		// TODO Auto-generated method stub
//
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws IOException, ServletException {
//		// TODO Auto-generated method stub
//		try {
//			HttpServletRequest httpRequest = (HttpServletRequest) request;
//			HttpServletResponse httpResponse = (HttpServletResponse) response;
//			if(satisfyUrl(httpRequest)) {
//				chain.doFilter(request,response);
//				return;
//			}
//			HttpSession session = httpRequest.getSession();
//			String userId = (String)session.getAttribute("userId");
//			if(userId == null){
//				httpResponse.sendRedirect("/tsc");
//				return;
//			}
//			UserService userService = context.getBean("userService", UserService.class);
//			if(!userService.exist(userId)) {
//				httpResponse.sendRedirect("/tsc");
//				return;
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		chain.doFilter(request,response);
//	}
//
//	public void init(FilterConfig config) throws ServletException {
//		// TODO Auto-generated method stub
//		context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
//		urlReg = config.getInitParameter("urlReg");
//	}
//
//	private boolean satisfyUrl(HttpServletRequest request){
//		try {
//			return Pattern.matches(urlReg, request.getPathInfo());
//		}catch (Exception e){
//			return true;
//		}
//	}
//
//}
