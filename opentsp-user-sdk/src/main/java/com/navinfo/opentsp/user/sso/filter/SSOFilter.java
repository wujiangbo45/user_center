package com.navinfo.opentsp.user.sso.filter;

import com.alibaba.fastjson.JSONObject;
import com.navinfo.opentsp.user.sso.constant.SSOConstant;
import com.navinfo.opentsp.user.sso.service.SSORedisService;
import com.navinfo.opentsp.user.sso.util.PatternMatcher;
import com.navinfo.opentsp.user.sso.util.ServletPathMatcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author duxj
 * @version 1.0
 * @date 2016/3/16
 */
public class SSOFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(SSOFilter.class);
	
	/**sso不拦截的URI**/
	private String exclusions;
	private String contextPath;
	private String logoutUrl;
    private String validateTokenUrl;
    private Set<String> excludesPattern;
    private PatternMatcher pathMatcher  = new ServletPathMatcher();
    
	private String cookieKey;//该值不允许为空，并且在redis中不允许相同的key
	private String projectName;
	private SSORedisService ssoRedisService;

	@Override
	public void init(FilterConfig config) throws ServletException {
		try{
			ServletContext context = config.getServletContext();
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  
			ssoRedisService = (SSORedisService)ctx.getBean("ssoRedisService");
			if(ssoRedisService == null)
				throw new ServletException("SSOFilter init method error, because of ssoRedisService bean is null ");
			this.validateTokenUrl = ssoRedisService.getValidateTokenUrl();
			this.exclusions = ssoRedisService.getExclusions();
			this.projectName = ssoRedisService.getProjectName();
			this.logoutUrl = ssoRedisService.getLogoutUrl();
			if(StringUtils.isEmpty(projectName))
				throw new ServletException("SSOFilter init method error, it's projectName is null, because of SSORedisService's property  projectName is null");
			if(StringUtils.isEmpty(this.logoutUrl))
				throw new ServletException("SSOFilter init method error, it's serverUrl is null, because of SSORedisService's property logoutUrl is null");
			
			ssoRedisService.setProjectName(projectName+SSOConstant.REDIS_KEY_PREFIX);
			cookieKey = this.projectName + SSOConstant.PROJECT_SESSION_NAME;
			if (this.exclusions != null && this.exclusions.trim().length() != 0) {
				excludesPattern = new HashSet<String>(Arrays.asList(exclusions.split("\\s*,\\s*")));
			}
			logger.debug("---------validateTokenUrl:" + validateTokenUrl + ",exclusions=" + exclusions + ",projectName=" + projectName + ",cookieKey=" +cookieKey);
			if(StringUtils.isEmpty(this.validateTokenUrl))
				throw new ServletException("SSOFilter init method error, it's validateTokenUrl is null, because of SSORedisService's property validateTokenUrl is null");
		}catch (Exception e) {
			throw new ServletException("SSOFilter init method error",e);
		}

	}
	
	@Override
    public final void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //设置跨域跳转heder
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
        //设置接收options请求
        response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        //设置自定义返回消息头
        this.customSetHeader(response);
        //设置路径不拦截
        if(this.isExceptURI(request.getRequestURI())){
            filterChain.doFilter(servletRequest, servletResponse);//直接放行
        }else {//除不拦截URI以外都拦截
            doSSOFilterInternal(request,response,filterChain);
        }

    }
	
	/**
	 * 自定义设置需要的消息头
	 * @param response
	 */
	public void customSetHeader(HttpServletResponse response){
		
	}

	public boolean isExceptURI(String requestURI) {
		if (this.excludesPattern == null) {
			return false;
		}
		if (this.contextPath != null && requestURI.startsWith(this.contextPath)) {
			requestURI = requestURI.substring(this.contextPath.length());
			if (!requestURI.startsWith("/")) {
				requestURI = "/" + requestURI;
			}
		}
		for (String pattern : this.excludesPattern) {
			if (this.pathMatcher.matches(pattern, requestURI)) {
				return true;
			}
		}
		return false;
	}

	public void doSSOFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		//非静态资源设置不缓存
		this.headerNoCache(response);
		try {
			//获取本系统SSO的jsessionId
			String jSessionId = getSSOJSessionId(request, cookieKey);
			if(StringUtils.isEmpty(jSessionId)){//第一次访问系统，写SSO系统Session
				jSessionId = addSSOCookie(request, response, cookieKey);
			}
			String token = request.getParameter("token");//从请求参数中获取token
			
			Map<Object, Object> sesseionMap = ssoRedisService.getKeyEntries(jSessionId);
			String sessionToken = null;
			if(sesseionMap != null){
				sessionToken = (String)sesseionMap.get(SSOConstant.REDIS_PROPERTY_TOKEN);
			}
			if(!StringUtils.isEmpty(sessionToken)){//有session
				String userId = (String)sesseionMap.get(SSOConstant.REDIS_PROPERTY_USERID);
				String nickname = (String)sesseionMap.get(SSOConstant.REDIS_PROPERTY_NICKNAME);
				String email = (String)sesseionMap.get(SSOConstant.REDIS_PROPERTY_EMAIL);
				email = (String)sesseionMap.get(SSOConstant.REDIS_PROPERTY_TOKEN);
				request.setAttribute(SSOConstant.REDIS_PROPERTY_EMAIL, email);
				request.setAttribute(SSOConstant.REDIS_PROPERTY_USERID, userId);
				request.setAttribute(SSOConstant.REDIS_PROPERTY_TOKEN, token);
				request.setAttribute(SSOConstant.REDIS_PROPERTY_NICKNAME, nickname);
				filterChain.doFilter(request,response);
			}else {
				if(!StringUtils.isEmpty(token)){//有token
					String requestUrl = validateTokenUrl+"?token=" + token + "&serverUrl=" +this.logoutUrl;
					String validateStr = get(requestUrl);
					logger.debug("requestUrl=" + requestUrl + ",validateStr=" + validateStr);
					JSONObject jsonObject = JSONObject.parseObject(validateStr);
					int code = 0;
					if(jsonObject != null)
						code = (Integer)jsonObject.get("code");
					if(code==200){
						JSONObject dataObject = JSONObject.parseObject(jsonObject.get("data").toString());
						String userId = dataObject.get("userId") == null ? null : dataObject.get("userId")+"";
						String nickname = dataObject.get("nickname") == null ? null : dataObject.get("nickname")+"";
						String email = jsonObject.get("email") == null ? null : jsonObject.get("email")+"";
						request.setAttribute(SSOConstant.REDIS_PROPERTY_EMAIL, email);
						request.setAttribute(SSOConstant.REDIS_PROPERTY_USERID, userId);
						request.setAttribute(SSOConstant.REDIS_PROPERTY_TOKEN, token);
						request.setAttribute(SSOConstant.REDIS_PROPERTY_NICKNAME, nickname);
						request.setAttribute(cookieKey, jSessionId);
						if(!ssoRedisService.exists(jSessionId) || !StringUtils.isEmpty(token))//不存在该条记录则写，存在不重新写
							ssoRedisService.createSessionIntoRedis(jSessionId, token, nickname, userId,email);
						filterChain.doFilter(request, response);
					}else {//token验证没通过
						responseWrite(response,"{\"code\":10007,\"message\":\"not login\",\"data\":{}}");
					}
				}else{//无token,无session
					responseWrite(response,"{\"code\":10007,\"message\":\"not login\",\"data\":{}}");
				}
			}
		} catch (Exception e) {
			throw new ServletException("SSOFilter doSSOFilterInternal errror", e);
		}
	}
	@Override
	public void destroy() {

	}

	public void headerNoCache(HttpServletResponse response) {
		//由于服务器需要做cdn缓存，除了图片、应用，其他的都需要加上nocache。如果 要 通知 浏览器 不缓存, 最好 的方式 这个三个 都设置
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", -1);
	}

	/**
	 * @param request 请求对象
	 * @param cookieKey cookie设置的key
	 * @return
	 */
	private String getSSOJSessionId(HttpServletRequest request,String cookieKey) {
		String jSessionId = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0){
			for (Cookie cookie : cookies) {
				if(cookieKey.equalsIgnoreCase(cookie.getName()))
					jSessionId = cookie.getValue();
			}
		}
		if(jSessionId == null )
			jSessionId =	(String) request.getAttribute(cookieKey);
		return  jSessionId;
	}
	/**
	 * 创建ssoCookie
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param cookieKey cookie的key
	 * @return
	 */
	private String addSSOCookie(HttpServletRequest request, HttpServletResponse response, String cookieKey) {
		String jSessionId = UUID.randomUUID().toString().replaceAll("-", "");
		/****写cookie****/
		Cookie cookie = new Cookie(cookieKey, jSessionId);
		cookie.setPath("/");
		cookie.setMaxAge(259200);//设置cookie有效期3天
		response.addCookie(cookie);
		/****写cookie****/
		return jSessionId;
	}

	/**
	 * 发送get请求
	 * @param url 请求url
	 * @return
	 * @throws IOException
	 */
	private  String get(String url) throws IOException{
		URL uri = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();

		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setReadTimeout(30000);
		conn.setRequestProperty("contentType", "utf-8");
		conn.setRequestProperty("accept", "application/json");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
		conn.connect();

		conn.connect();
		int code = conn.getResponseCode();
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			if (code==200) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			}else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
			}

			String msg = null;
			while((msg = br.readLine()) != null){
				sb.append(msg).append("\n");
			}
			if(sb.length() > 0){
				sb.deleteCharAt(sb.length() - 1);
			}
		} finally {
			if(br != null)
				br.close();
		}
		return sb.toString();
	}
	
	/**
	 * 响应输入json
	 * @param response 响应对象
	 * @param data 写的json数据
	 * @throws IOException
	 */
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
