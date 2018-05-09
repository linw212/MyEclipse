package com.profound.common.plugin.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemDicConfig {
	private String id;
	private String name;
	private Map<String, String> codes=new HashMap<String, String>();
	private static Map<String, Map<String, SystemDicConfig>> dictionaryBySystem=new HashMap<String, Map<String,SystemDicConfig>>();
	private static Map<String, SystemDicConfig> dictionary=new HashMap<String, SystemDicConfig>();
	private static Comparator<Map<String, String>> comp=new Comparator<Map<String,String>>() {
		public int compare(Map<String, String> o1, Map<String, String> o2) {
			return o1.get("id").compareTo(o2.get("id"));
		}
	};
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getCodes() {
		return codes;
	}
	public void setCodes(Map<String, String> codes) {
		this.codes = codes;
	}
	
	protected static SystemDicConfig addDictionary(String id,String name,Map<String, String> codes)
	{
		SystemDicConfig d=new SystemDicConfig();
		d.setId(id);
		d.setName(name);
		d.setCodes(codes);
		dictionary.put(id, d);
		return d;
	}
	protected static void addDictionaryBySystem(String systemId,Map<String, SystemDicConfig> map)
	{
		dictionaryBySystem.put(systemId, map);
	}
	/**
	 * 获取combobox组件使用的字典值
	 * @param dTypeId 字典类型id
	 * @param notInclude 不包含的字典值
	 * @return
	 */
	public static List<Map<String,String>> getForCombobox(String dTypeId,String[] notInclude)
	{
		Map<String, String> notIncludeMap=new HashMap<String, String>();
		if(notInclude!=null)
			for(String not_code:notInclude)
				notIncludeMap.put(not_code, "");
		Map<String, String> d = dictionary.get(dTypeId).getCodes();
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(String key:d.keySet())
		{
			Map<String,String> map=new HashMap<String, String>();
			if(notIncludeMap.get(key)!=null)
				continue;
			map.put("id", key);
			map.put("text", d.get(key));
			list.add(map);
		}
		Collections.sort(list, comp);
		return list;
	}
}
