package com.navinfo.opentsp.user.sso.service;

import com.navinfo.opentsp.user.sso.service.SSORedisService;
import com.navinfo.opentsp.user.sso.constant.SSOConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author duxj
 *
 */

public class SSORedisService {
	private static final Logger logger = LoggerFactory.getLogger(SSORedisService.class);
	private String projectName;
	private StringRedisTemplate stringRedisTemplate;
	private String validateTokenUrl;
	private String exclusions;
	private String logoutUrl;
	
	public String getLogoutUrl() {
		return logoutUrl;
	}

	public void setLogoutUrl(String logoutUrl) {
		this.logoutUrl = logoutUrl;
	}

	public String getExclusions() {
		return exclusions;
	}

	public void setExclusions(String exclusions) {
		this.exclusions = exclusions;
	}

	public String getValidateTokenUrl() {
		return validateTokenUrl;
	}

	public void setValidateTokenUrl(String validateTokenUrl) {
		this.validateTokenUrl = validateTokenUrl;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	public String getProjectName() {
		return projectName;
	}

	public StringRedisTemplate getStringRedisTemplate() {
		return stringRedisTemplate;
	}

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	private ValueOperations<String, String> opsForValue() {
		return stringRedisTemplate.opsForValue();
	}

	private HashOperations<String, Object, Object> opsForHash() {
		return stringRedisTemplate.opsForHash();
	}

	private String keys(String key) {
		return this.projectName + key;
	}

	
	public void set(String key,String value){
		opsForValue().set(keys(key), value);
	}
	
	
	public void set(String key, String value, long timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(keys(key), value, timeout, unit);
	}

	
	public void expire(String key, long expire, TimeUnit unit) {
		stringRedisTemplate.expire(keys(key), expire, unit);
	}

	
	public boolean exists(String key) {
		boolean existFlag = false;
		try {
			existFlag = stringRedisTemplate.hasKey(keys(key));
			//if(existFlag)
			//	expire(key, SSOConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);//过期时间设置为30分钟
		}catch (Exception e) {
			logger.error("exists method error: haskey(key) error key=" + keys(key) + e);
		}
		return existFlag;
	}

	
	public void delete(String key) {
		stringRedisTemplate.delete(keys(key));
	}

	
	public void hset(String key, Object hashKey, Object value) {
		this.opsForHash().put(keys(key), hashKey, value);
	}

	
	public Object hget(String key, Object hashKey) {
		Object ret=null;
		try {
			ret = this.opsForHash().get(keys(key), hashKey);
			expire(key, SSOConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);//过期时间设置为30分钟
		} catch (Exception e) {
			logger.error("hget key=" + key + "  hashKey=" + hashKey + "error" + e);
		}
		return ret;
	}

	public Map<Object, Object> getKeyEntries(String key){
		Map<Object, Object> ret=null;
		try {
			if(exists(key)){
				ret = this.opsForHash().entries(keys(key));
				expire(key, SSOConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);//过期时间设置为30分钟
			}
		} catch (Exception e) {
			logger.error("getKeyEntries key=" + key + e);
		}
		return ret;
	}
	
	public void createSessionIntoRedis(String key,String token,String nickname,String userId,String email){
		/**
		 * 问题：
		 *   映射1：jsessionId------userInfo(有效时间随访问时间能刷新30分钟)
		 *   映射2：token-----------jsessionId(不能有效刷新)
		 * 因此映射2的有效时间得设置的尽量大（例如300分钟）
		 * 	 但是写映射2的时候可以判断token的key是否存在，存在则删除重新建立映射 
		 */
		if(email != null)
			hset(key, SSOConstant.REDIS_PROPERTY_EMAIL, email);
		if(nickname != null)
			hset(key, SSOConstant.REDIS_PROPERTY_NICKNAME, nickname);
		if(userId != null)
			hset(key, SSOConstant.REDIS_PROPERTY_USERID, userId);
		hset(key, SSOConstant.REDIS_PROPERTY_TOKEN, token);
		
		//设置token与jsessionId的映射
		delete(token);
		hset(token, SSOConstant.REDIS_PROPERTY_JESSIONID, key);
		
		expire(token, SSOConstant.REDIS_EXPIRE_TIME*10, TimeUnit.SECONDS);//过期时间设置为150分钟
		expire(key, SSOConstant.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);//过期时间设置为15分钟
		logger.debug("key=" + key + ",token=" + token + ",userId=" +userId);
		
	}
	
	public boolean deleteSessionFromRedis(String token){
		boolean flag = true;
		String jSessionId = null;
		try {
			jSessionId = (String)hget(token, SSOConstant.REDIS_PROPERTY_JESSIONID);
			delete(jSessionId);
			delete(token);
			
			logger.debug("SSO deleteSessionFromRedis jSessionId=" + jSessionId );
		} catch (Exception e) {
			logger.error("SSO deleteSessionFromRedis token=" + token + "  jSessionId=" + jSessionId + e);
			flag = false;
		}
		return flag;
	}
	
	
}
