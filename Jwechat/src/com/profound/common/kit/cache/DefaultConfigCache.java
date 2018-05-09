package com.profound.common.kit.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConfigCache implements IConfigCache {
	private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) this.map.get(key);
	}
	public void set(String key, Object value) {
		this.map.put(key, value);
	}

	public void remove(String key) {
		this.map.remove(key);
	}
}
