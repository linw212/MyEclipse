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

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.profound.common.kit.XMLKit;

public class SystemRoutes extends Routes {

	protected static final Log LOG = Log.getLog(SystemRoutes.class);
	private String configFile="/config/systemDefine.xml";
	@SuppressWarnings("unchecked")
	public void config() {
		List<Map<String, String>> systems=new ArrayList<Map<String,String>>();
		try {
			File xml_file = new File(PathKit.getRootClassPath()+configFile);
			Document doc=XMLKit.parseXml(xml_file, false);
			Iterator<Element> iter = doc.getRootElement().elementIterator("system");
			while(iter.hasNext())
			{
				Element el=iter.next();
				if("disabled".equals(el.attributeValue("status")))
					continue;
				String routes=el.attributeValue("routes");
				if(StrKit.isBlank(routes))
					continue;
				else
					routes=routes.replaceAll("\\\\", "/");
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
				routes=routes.startsWith("/")?routes:("/"+routes);
				Map<String, String> map=new HashMap<String, String>();
				map.put("id", el.attributeValue("id"));
				map.put("name", el.attributeValue("name"));
				map.put("viewPath", viewPath);
				map.put("routes", routes);
				systems.add(map);
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			return;
		}
		for(Map<String, String> map:systems)
		{
			LOG.info("开始加载系统["+map.get("name")+"]路由映射...");
			String routes=map.get("routes");
			String id=map.get("id");
			File xml_file = new File(PathKit.getRootClassPath()+routes);
			try {
				Document doc=XMLKit.parseXml(xml_file, false);
				Iterator<Element> iter = doc.getRootElement().elementIterator("routes");
				while(iter.hasNext())
				{
					Element el=iter.next();
					String controllerKey=el.attributeValue("controllerKey");
					String viewPath=el.attributeValue("viewPath");
					String controllerClass=el.attributeValue("controllerClass");
					if(!controllerKey.startsWith("/"))
						controllerKey="/"+id+"/"+controllerKey;
					if(!viewPath.startsWith("/"))
						viewPath=map.get("viewPath")+viewPath;
					
					if(StrKit.isBlank(controllerClass))
					{
						LOG.warn("路由配置有误["+controllerKey+"]：controller类未指定");
						continue;
					}
					try
					{
						controllerClass=controllerClass.trim();
						Class<Controller> con_class=(Class<Controller>) Class.forName(controllerClass);
						this.add(controllerKey, con_class, viewPath);
						LOG.info("路由已映射["+controllerKey+"]>>"+controllerClass);
					}catch (Exception e) {
						LOG.warn("路由配置加载异常["+controllerKey+"]["+controllerClass+"]："+e.getMessage());
						continue;
					}
					
				}
			} catch (Exception e) {
				LOG.error("加载系统["+map.get("name")+"]路由异常:"+e.getMessage());
				continue;
			}
			LOG.info("加载系统["+map.get("name")+"]路由完成");
		}
	}

}
