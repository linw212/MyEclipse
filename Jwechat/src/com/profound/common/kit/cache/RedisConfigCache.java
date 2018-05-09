package com.profound.common.kit.cache;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

public class RedisConfigCache implements IConfigCache{
	private String CONFIG_PREFIX = "main_config:";
	private final Cache CACHE;

	public RedisConfigCache() {
		this.CACHE = Redis.use();
	}

	public RedisConfigCache(String cacheName) {
		this.CACHE = Redis.use(cacheName);
	}

	public RedisConfigCache(Cache cache) {
		this.CACHE = cache;
	}
	public RedisConfigCache(String cacheName,String prefix) {
		this.CACHE = Redis.use(cacheName);
		this.CONFIG_PREFIX = prefix;
	}
	public RedisConfigCache(Cache cache,String prefix) {
		this.CACHE = cache;
		this.CONFIG_PREFIX = prefix;
	}
	public <T> T get(String key) {
		return this.CACHE.get(CONFIG_PREFIX + key);
	}

	public void set(String key, Object object) {
		this.CACHE.set(CONFIG_PREFIX + key, object);
	}

	public void remove(String key) {
		this.CACHE.del(CONFIG_PREFIX + key);
	}
}
