package com.profound.common.plugin.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;

public class SystemDefineConfig {
	private static Map<String,Map<String, String>> sysDefine=new HashMap<String,Map<String, String>>();
	private static Map<String,List<Map<String, String>>> sysModule=new HashMap<String,List<Map<String, String>>>();
	private static Map<String,Map<String, String>> idModules=new HashMap<String,Map<String, String>>();
	private static Map<String,String> codeModules=new HashMap<String,String>();
	private static Comparator<Map<String, Object>> com1=new Comparator<Map<String, Object>>() {
		public int compare(Map<String, Object> o1, Map<String, Object> o2) {
			return o1.get("index").toString().compareTo(o2.get("index").toString());
		}
	};
	private static Comparator<Map<String, String>> com2=new Comparator<Map<String, String>>() {
		public int compare(Map<String, String> o1, Map<String, String> o2) {
			return o1.get("index").toString().compareTo(o2.get("index").toString());
		}
	};
	protected static Map<String, String> addDefine(String id,String name,String rotues,String viewPath,String icon,String homePage)
	{
		Map<String, String> business=new HashMap<String, String>();
		business.put("id",id);
		business.put("name",name);
		business.put("rotues",rotues);
		business.put("viewPath",viewPath);
		business.put("icon",icon);
		business.put("homePage",homePage);
		business.put("status","enabled");
		business.put("index", sysDefine.size()+"");
		return sysDefine.put(id,business);
	}
	protected static void addModule(String sysId,List<Map<String, String>> modules)
	{
//		String path=JFinal.me().getServletContext().getContextPath();
		for(Map<String, String> map:modules)
		{
			map.put("id", sysId+"@"+map.get("id"));
			map.put("pid", StrKit.isBlank(map.get("pid"))?"":(sysId+"@"+map.get("pid")));
			map.put("icon", StrKit.isBlank(map.get("icon"))?"":map.get("icon"));
			idModules.put(map.get("id"), map);
			if(StrKit.notBlank(map.get("code")))
				codeModules.put(map.get("code"), map.get("id"));
		}
		sysModule.put(sysId,modules);
	}
	public static List<Map<String, String>> getSysDefines()
	{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>(sysDefine.values());
		Collections.sort(list, com2);
		return list;
	}
	public static Map<String, String> getSysDefineById(String sysId)
	{
		return sysDefine.get(sysId);
	}
	public static List<Map<String, String>> getSysModules(String sysId)
	{
		return new ArrayList<Map<String,String>>(sysModule.get(sysId));
	}
	public static Map<String, String> getSysModuleById(String id)
	{
		return idModules.get(id);
	}
	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> load(Set<String> moduleIds,boolean includeDisabled)
	{
		List<Map<String, Object>> completeMap=new ArrayList<Map<String,Object>>();
		if(moduleIds==null)
		{
			for(Map<String, String> sys:sysDefine.values())
			{
				if(!includeDisabled&&sys.get("status").equals("disabled"))
					continue;
				Map<String, Object> m=new HashMap<String, Object>();
				m.putAll(sys);
				m.remove("viewPath");
				m.remove("status");
				List<Map<String, String>> modules=new ArrayList<Map<String,String>>();
				if(sysModule.get(sys.get("id"))!=null)
				{
					for(Map<String, String> module:sysModule.get(sys.get("id")))
					{
						Map<String, String> map=new HashMap<String, String>();
						map.put("id", module.get("id"));
						map.put("name", module.get("name"));
						map.put("icon", module.get("icon"));
						map.put("pid", module.get("pid"));
						modules.add(map);
					}
				}
				m.put("modules", modules);
				completeMap.add(m);
			}
		}
		else
		{
			Map<String, Map<String, Object>> temp_m=new HashMap<String, Map<String, Object>>();
			for(String moduleId:moduleIds)
			{
				Map<String, String> sys=sysDefine.get(moduleId.split("@")[0]);
				if(sys==null)
					continue;
				if(!includeDisabled&&sys.get("status").equals("disabled"))
					continue;
				if(idModules.get(moduleId)==null)
					continue;
				if(temp_m.get(sys.get("id"))==null)
				{
					Map<String, Object> m=new HashMap<String, Object>();
					m.putAll(sys);
					m.remove("viewPath");
					m.remove("status");
					m.put("modules",new LinkedList<Map<String, String>>());
					temp_m.put(sys.get("id"), m);
				}
				Map<String, String> temp_module=new HashMap<String, String>();
				Map<String, String> module=idModules.get(moduleId);
				temp_module.put("id", module.get("id"));
				temp_module.put("name", module.get("name"));
				temp_module.put("icon", module.get("icon"));
				temp_module.put("pid", module.get("pid"));
				temp_module.put("index", module.get("index"));
				((List<Map<String, String>>)temp_m.get(sys.get("id")).get("modules")).add(temp_module);
			}
			for(String sysId:temp_m.keySet())
			{
				List<Map<String, String>> modules=(List<Map<String, String>>)temp_m.get(sysId).get("modules");
				Set<Map<String, String>> set=new HashSet<Map<String,String>>(modules);
				completeRelate(sysId,set);
				modules.clear();
				modules.addAll(set);
				Collections.sort(modules, com2);
			}
			completeMap.addAll(temp_m.values());
		}
		Collections.sort(completeMap, com1);
		return completeMap;
	}
	private static void completeRelate(String sysId,Set<Map<String, String>> modules)
	{
		Set<Map<String, String>> p_modules=new HashSet<Map<String,String>>();
		for(Map<String, String> map:modules)
		{
			if(StrKit.notBlank(map.get("pid"))&&idModules.get(map.get("pid"))!=null)
				p_modules.add(idModules.get(map.get("pid")));
		}
		if(p_modules.size()>0)
		{
			modules.addAll(p_modules);
			completeRelate(sysId,p_modules);
		}
	}
	
	public static String toJson()
	{
		return JsonKit.toJson(sysDefine.values());
	}
	public static void disableDefine(String id)
	{
		if(sysDefine.get(id)!=null)
		{
			sysDefine.get(id).put("status", "disabled");
		}
	}
	public static void enableDefine(String id)
	{
		if(sysDefine.get(id)!=null)
		{
			sysDefine.get(id).put("status", "enabled");
		}
	}
	public static void disableModule(String id)
	{
		if(idModules.get(id)!=null)
		{
			idModules.get(id).put("status", "disabled");
		}
	}
	public static void enableModule(String id)
	{
		if(idModules.get(id)!=null)
		{
			idModules.get(id).put("status", "enabled");
		}
	}
}
