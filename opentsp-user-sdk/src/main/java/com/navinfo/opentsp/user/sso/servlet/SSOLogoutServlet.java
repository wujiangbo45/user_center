package com.navinfo.opentsp.user.sso.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.navinfo.opentsp.user.sso.service.SSORedisService;

public class SSOLogoutServlet extends GenericServlet{

	private static final long serialVersionUID = 2076148188609098174L;
	private SSORedisService ssoRedisService;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();  
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  
		ssoRedisService = (SSORedisService)ctx.getBean("ssoRedisService");
		if(ssoRedisService == null)
			throw new ServletException("SSOLogoutServlet init method error, because of ssoRedisService is null");
	}
	
	@Override
	public void service(ServletRequest req, ServletResponse res)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String token = request.getParameter("token");
		
		if(!StringUtils.isEmpty(token)){
			if(ssoRedisService.deleteSessionFromRedis(token)){
				responseWrite(response,"{\"code\":200,\"message\":\"\",\"data\":{}}");
			}else {
				responseWrite(response,"{\"code\":500,\"message\":\"error\",\"data\":{}}");
			}
		}else if(StringUtils.isEmpty(token)){
			responseWrite(response,"{\"code\":400,\"message\":\"token is null\",\"data\":{}}");
		}
	}
	
	private void responseWrite(HttpServletResponse response,String data) throws IOException{
		PrintWriter writer =null;
		try {
			writer = response.getWriter();
			writer.write(data);
		} catch (IOException e) {
			throw e;
		}finally{
			if(writer != null)
				writer.close();
		}
	}
}
