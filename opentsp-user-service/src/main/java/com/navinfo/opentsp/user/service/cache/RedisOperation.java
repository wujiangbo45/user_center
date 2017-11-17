//package com.navinfo.opentsp.user.service.cache;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.JedisCommands;
//
//import java.io.IOException;
//
//@Component
//public class RedisOperation {
//	private static final Logger logger = LoggerFactory.getLogger(RedisOperation.class);
//
//	@Autowired
//	private RedisSource redisSource;
//
//	public <T> T execute(RedisOperate<T> operate){
//		JedisCommands jedis = redisSource.getRedisTemplate();
//		T obj = null;
//
//		try {
//			obj = operate.doInRedis(jedis);
//		} catch (Exception e) {
//			redisSource.closeBreakJedis(jedis);
//			logger.error(e.getMessage(), e);
//			e.printStackTrace();
//		} finally {
//			redisSource.closeJedis(jedis);
//		}
//
//		return obj;
//	}
//
//	public void set(final String key, final String value){
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) {
//				jedis.set(key, value);
//				return null;
//			}
//		});
//	}
//
//	public void set(final String key, final String value, final int seconds){
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) {
//				jedis.set(key, value);
//				jedis.expire(key, seconds);
//				return null;
//			}
//		});
//	}
//
//	public void hset(final String mapKey, final String key, final String value) {
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) {
//				jedis.hset(mapKey, key, value);
//				return null;
//			}
//		});
//	}
//
//	public String get(final String key){
//		return this.execute(new RedisOperate<String>() {
//			@Override
//			public String doInRedis(JedisCommands jedis) throws IOException {
//				String json = jedis.get(key);
//				return json;
//			}
//		});
//	}
//
//	public void del(final String key){
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) throws Exception {
//				jedis.del(key);
//				return null;
//			}
//		});
//	}
//
//	public Long incr(final String key){
//		return this.execute(new RedisOperate<Long>() {
//			@Override
//			public Long doInRedis(JedisCommands jedis) throws Exception {
//				Long r = jedis.incr(key);
//				return r;
//			}
//		});
//	}
//
//	public Long hincrBy(final String mapKey, final String key, final long increment){
//		return this.execute(new RedisOperate<Long>() {
//			@Override
//			public Long doInRedis(JedisCommands jedis) throws Exception {
//				Long r = jedis.hincrBy(mapKey, key, increment);
//				return r;
//			}
//		});
//	}
//
//	public String hget(final String mapKey, final String key){
//		return this.execute(new RedisOperate<String>() {
//			@Override
//			public String doInRedis(JedisCommands jedis) throws Exception {
//				String json = jedis.hget(mapKey, key);
//				return json;
//			}
//		});
//	}
//
//	public void hdel(final String mapKey, final String key){
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) throws Exception {
//				jedis.hdel(mapKey, key);
//				return null;
//			}
//		});
//	}
//
//	public void expire(final String key, final int seconds){
//		this.execute(new RedisOperate<Object>() {
//			@Override
//			public Object doInRedis(JedisCommands jedis) throws Exception {
//				jedis.expire(key, seconds);
//				return null;
//			}
//		});
//	}
//
//	public static interface RedisOperate<T>{
//		public T doInRedis(JedisCommands jedis) throws Exception;
//	}
//
//}