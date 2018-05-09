package com.profound.common.kit.cache;


public interface IConfigCache {

	public <T> T get(String key) ;
	public void set(String key, Object object);
	public void remove(String key);
}
