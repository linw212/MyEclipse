package com.profound.common.kit;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ParamConfig {
	private static Map<String,ParamConfig> configs=new HashMap<String, ParamConfig>();
	private String name;
	private File file;
	private Map<String, Map<String, String>> params=new HashMap<String, Map<String, String>>();
	protected ParamConfig(){}//不可直接构造
	protected ParamConfig(String name,File file)
	{
		this.name=name;
		this.file=file;
	}
	protected static void load(ParamConfig pc)
	{
		configs.put(pc.name, pc);
	}
	public static ParamConfig use(String configname)
	{
		return configs.get(configname);
	}
	protected static void loadConfig(ParamConfig pc)
	{
		configs.put(pc.name, pc);
	}
	protected void addParam(String code,String value,String describe,String options,String remark) throws Exception
	{
		Map<String, String> map=params.get(code);
		if(map==null)
			params.put(code, map=new HashMap<String, String>());
		
		if(map.get(code)!=null)
			throw new Exception("加载参数异常:元素["+code+"]已经存在--配置文件["+name+":"+file+"]");
		Map<String, String> param=new HashMap<String, String>();
		param.put("configname", name);
		param.put("code",code);
		param.put("value",value);
		param.put("describe",describe);
		param.put("options",options);
		param.put("remark",remark);
		param.put("index", String.valueOf(map.size()));
		params.put(code, param);
	}
	public Collection<Map<String, String>> getParams()
	{
		return params.values();
	}
	public String get(String code)
	{
		return params.get(code).get("value");
	}
	public void update(String code,String value)
	{
		params.get(code).put("value",value);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public File getFile() {
		return file;
	}
	public void setPath(File file) {
		this.file = file;
	}
	
}
