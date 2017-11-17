//package com.navinfo.opentsp.user.service.cache;
//
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import redis.clients.jedis.*;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class RedisSource {
//	private static Logger logger = LoggerFactory.getLogger(RedisSource.class);
//	private JedisSentinelPool pool;
//	private ShardedJedisPool shardedPool;
//	private RedisSourceConfig redisSourceConfig;
//
//	public RedisSource(RedisSourceConfig email){
//		this.redisSourceConfig = email;
//		init();
//	}
//
//	private void init(){
//		if("shared".equals(redisSourceConfig.getMode())){
//			initSharededPool();
//		} else if("sentinel".equals(redisSourceConfig.getMode())) {
//			initSentinelPool();
//		}
//	}
//
//	private void initSentinelPool(){
//		logger.info("开始初始化redis连接池");
//		Set<String> set = new HashSet<String>();
//		set.addAll(redisSourceConfig.getServers());
//		GenericObjectPoolConfig email = new GenericObjectPoolConfig();
//		email.setMaxIdle(Integer.valueOf(redisSourceConfig.getMaxIdle()));
//		email.setMaxTotal(Integer.valueOf(redisSourceConfig.getMaxActive()));
//		email.setMaxWaitMillis(Long.valueOf(redisSourceConfig.getMaxWait()));
//		email.setTestOnBorrow(Boolean.valueOf(redisSourceConfig.getIsTest()));
//		pool = new JedisSentinelPool(redisSourceConfig.getName(), set, email);
//		logger.info("成功初始化Sentinel redis连接池");
//	}
//
//	private void initSharededPool(){
//		logger.info("开始初始化redis连接池");
//		JedisPoolConfig email = new JedisPoolConfig();
//		email.setMaxTotal(Integer.valueOf(redisSourceConfig.getMaxActive()) + Integer.valueOf(redisSourceConfig.getMaxIdle()));
//		email.setMaxIdle(Integer.valueOf(redisSourceConfig.getMaxIdle()));
//		email.setTestOnBorrow(Boolean.valueOf(redisSourceConfig.getIsTest()));
//		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
//		for (String str : redisSourceConfig.getServers()) {
//			JedisShardInfo shardInfo = new JedisShardInfo(str.split(":")[0], Integer.parseInt(str.split(":")[1]));
//			shardInfo.setTimeout(Integer.valueOf(redisSourceConfig.getTimeout()));
//			list.add(shardInfo);
//		}
//		shardedPool = new ShardedJedisPool(email, list);
//		logger.info("成功初始化shared redis连接池");
//	}
//
//	public JedisCommands getRedisTemplate() {
//		JedisCommands jedis = null;
//		if("shared".equals(redisSourceConfig.getMode())){
//			jedis = shardedPool.getResource();
//		} else if("sentinel".equals(redisSourceConfig.getMode())) {
//			jedis = pool.getResource();
//		}
//		return jedis;
//	}
//
//	/**
//	 * 正常连接池回收
//	 * @param jedis
//	 */
//	public void closeJedis(JedisCommands jedis){
//		if(jedis != null){
//			if("shared".equals(redisSourceConfig.getMode()) && (jedis instanceof ShardedJedis)){
//				shardedPool.returnResource((ShardedJedis) jedis);
//			} else if("sentinel".equals(redisSourceConfig.getMode()) && (jedis instanceof Jedis)){
//				pool.returnResource((Jedis) jedis);
//			}
//		}
//	}
//
//	/**
//	 * 异常连接池回收
//	 * @param jedis
//	 */
//	public void closeBreakJedis(JedisCommands jedis){
//		if(jedis != null){
//			if("shared".equals(redisSourceConfig.getMode()) && (jedis instanceof ShardedJedis)){
//				shardedPool.returnBrokenResource((ShardedJedis) jedis);
//			} else if("sentinel".equals(redisSourceConfig.getMode()) && (jedis instanceof Jedis)){
//				pool.returnBrokenResource((Jedis) jedis);
//			}
//		}
//	}
//
//	public static class RedisSourceConfig {
//		private String mode;
//		private Integer maxActive;
//		private Integer maxWait;
//		private Integer maxIdle;
//		private Integer timeout;
//		private String name;
//		private Boolean isTest;
//		private List<String> servers;
//
//		public Boolean getIsTest() {
//			return isTest;
//		}
//
//		public void setIsTest(Boolean isTest) {
//			this.isTest = isTest;
//		}
//
//		public List<String> getServers() {
//			return servers;
//		}
//
//		public void setServers(List<String> servers) {
//			this.servers = servers;
//		}
//
//		public String getName() {
//			return name;
//		}
//
//		public void setName(String name) {
//			this.name = name;
//		}
//
//		public String getMode() {
//			return mode;
//		}
//
//		public void setMode(String mode) {
//			this.mode = mode;
//		}
//
//		public Integer getMaxActive() {
//			return maxActive;
//		}
//
//		public void setMaxActive(Integer maxActive) {
//			this.maxActive = maxActive;
//		}
//
//		public Integer getMaxWait() {
//			return maxWait;
//		}
//
//		public void setMaxWait(Integer maxWait) {
//			this.maxWait = maxWait;
//		}
//
//		public Integer getMaxIdle() {
//			return maxIdle;
//		}
//
//		public void setMaxIdle(Integer maxIdle) {
//			this.maxIdle = maxIdle;
//		}
//
//		public Integer getTimeout() {
//			return timeout;
//		}
//
//		public void setTimeout(Integer timeout) {
//			this.timeout = timeout;
//		}
//	}
//}