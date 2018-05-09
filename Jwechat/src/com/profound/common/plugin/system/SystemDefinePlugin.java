package com.profound.common.plugin.system;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;
import com.profound.common.kit.ParamKit;
import com.profound.common.kit.XMLKit;

public class SystemDefinePlugin implements IPlugin {

	protected static final Log LOG = Log.getLog(SystemDefinePlugin.class);
	private Map<String, String> defaultButtons=new HashMap<String, String>();
	public SystemDefinePlugin()
	{
		defaultButtons.put("add", "新增");
		defaultButtons.put("update", "修改");
		defaultButtons.put("delete", "删除");
		defaultButtons.put("export", "导出");
		defaultButtons.put("print", "打印");
	}
	public SystemDefinePlugin(String configFile)
	{
		this.configFile=configFile;
	}
	private String configFile="/config/systemDefine.xml";
	@Override
	public boolean start() {
		File xml_file = new File(PathKit.getRootClassPath()+configFile);
		try {
			Document doc=XMLKit.parseXml(xml_file, false);
			@SuppressWarnings("unchecked")
			Iterator<Element> iter = doc.getRootElement().elementIterator("system");
			while(iter.hasNext())
			{
				Element el=iter.next();
				String id=el.attributeValue("id");
				if("disabled".equals(el.attributeValue("status")))
					continue;
				String viewPath=el.attributeValue("viewPath");
				if(StrKit.isBlank(viewPath))
					viewPath="/";
				else
				{
					viewPath = viewPath.trim();
					if (!viewPath.startsWith("/"))					// "/" added to prefix
						viewPath = "/" + viewPath;
					
					if (!viewPath.endsWith("/"))					// "/" added to postfix
						viewPath = viewPath + "/";
				}
				SystemDefineConfig.addDefine(id, el.attributeValue("name"),el.attributeValue("rotues"), viewPath, el.attributeValue("icon"), el.attributeValue("homePage"));
				//加载菜单模块信息
				String moduleFilePath=el.attributeValue("module");
				if(StrKit.isBlank(moduleFilePath))
					continue;
				else
					moduleFilePath=moduleFilePath.trim().replaceAll("\\\\", "/");
				moduleFilePath=moduleFilePath.startsWith("/")?moduleFilePath:("/"+moduleFilePath);
				xml_file = new File(PathKit.getRootClassPath()+moduleFilePath);
				if(xml_file.isFile())
				{
					List<Map<String, String>> list=loadModule(xml_file);
					if(list!=null&&list.size()>0)
						SystemDefineConfig.addModule(id, list);
				}
				//加载数据字典内容
				String dictionaryFile=el.attributeValue("dictionary");
				if(StrKit.notBlank(dictionaryFile))
					loadDictionary(id, new File(PathKit.getRootClassPath()+dictionaryFile));
				//加载系统参数配置文件
				String paramFile=el.attributeValue("param");
				if(StrKit.notBlank(paramFile))
					ParamKit.use(id, new File(PathKit.getRootClassPath()+paramFile));
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}
	@SuppressWarnings("unchecked")
	private List<Map<String, String>> loadModule(File moduleFile)
	{
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		try {
			Document doc=XMLKit.parseXml(moduleFile, false);
			Iterator<Element> iter = doc.getRootElement().elementIterator("module");
			int index=0;
			while(iter.hasNext())
			{
				Element el=iter.next();
				Map<String, String> map=new HashMap<String, String>();
				if(StrKit.isBlank(el.attributeValue("id")))
				{
					LOG.warn("菜单配置文件:["+moduleFile.getName()+"]中检测到未指定id的菜单配置信息,此菜单项无效");
					continue;
				}
				map.put("id", el.attributeValue("id"));
				map.put("name", el.attributeValue("name"));
				map.put("icon", el.attributeValue("icon"));
				map.put("extcode", el.attributeValue("extcode"));
				map.put("pid", el.attributeValue("pid"));
				map.put("page", el.attributeValue("page"));
				map.put("viewType", el.attributeValue("viewType"));
				map.put("index", String.valueOf(index++));
				if(map.get("id").equals(map.get("pid")))
				{
					LOG.warn("菜单配置文件:["+moduleFile.getName()+"]中检测到菜单["+map.get("name")+"]id和pid相同,此菜单的pid已置为空");
					map.put("pid", "");
				}
				List<Map<String, String>> buttons=new ArrayList<Map<String,String>>();
				String buttonsStr = el.attributeValue("buttons");
				if(StrKit.notBlank(buttonsStr))
				{
					for(String btnCode:buttonsStr.split(","))
					{
						if(defaultButtons.get(btnCode)!=null)
						{
							Map<String, String> bm=new HashMap<String, String>();
							bm.put("id", btnCode);
							bm.put("text", defaultButtons.get(btnCode));
							buttons.add(bm);
						}
					}
				}
				List<Element> registButtonsEl=el.elements("registButtons");
				for(Element e1:registButtonsEl)
				{
					List<Element> btns=e1.elements("btn");
					for(Element e2:btns)
					{
						Map<String, String> bm=new HashMap<String, String>();
						bm.put("id",e2.attributeValue("code"));
						bm.put("text",e2.attributeValue("name"));
						buttons.add(bm);
					}
				}
				
				map.put("buttons", JsonKit.toJson(buttons));
				list.add(map);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return list;
	}
	@SuppressWarnings("unchecked")
	private void loadDictionary(String systemId,File dictionaryFile)
	{
		try {
			Document doc=XMLKit.parseXml(dictionaryFile, false);
			Iterator<Element> iter = doc.getRootElement().elementIterator("dType");
			Map<String, SystemDicConfig> dictionary=new HashMap<String, SystemDicConfig>();
			while(iter.hasNext())
			{
				Element el=iter.next();
				if(StrKit.isBlank(el.attributeValue("id")))
					continue;
				String id=systemId+"@"+el.attributeValue("id");
				String name=el.attributeValue("name");
				List<Element> dCodes=el.elements("dCode");
				Map<String, String> map=new HashMap<String, String>();
				for(Element e1:dCodes)
				{
					map.put(e1.attributeValue("value"), e1.attributeValue("text"));
				}
				SystemDicConfig d= SystemDicConfig.addDictionary(id, name, map);
				dictionary.put(id, d);
			}
			SystemDicConfig.addDictionaryBySystem(systemId, dictionary);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}
	public void setConfigFile(String filePath)
	{
		configFile=filePath;
	}

	public String getConfigFile() {
		return configFile;
	}
}
