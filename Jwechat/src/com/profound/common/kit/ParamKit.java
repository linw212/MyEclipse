package com.profound.common.kit;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;

import com.jfinal.log.Log;

public class ParamKit {
	protected static final Log LOG = Log.getLog(ParamKit.class);
	public static final String DEFAULT_CONFIGNAME="main";
	public static ParamConfig use(String configname)
	{
		return ParamConfig.use(configname);
	}
	public static ParamConfig use(File configfile)
	{
		if(use(DEFAULT_CONFIGNAME)!=null)
			return use(DEFAULT_CONFIGNAME);
		else
			return use(DEFAULT_CONFIGNAME,configfile);
		
	}
	public static ParamConfig use(String configname,File configfile)
	{
		if(use(configname)!=null)
			return use(configname);
		ParamConfig pc=new ParamConfig(configname,configfile);
		if(init(pc))
		{
			ParamConfig.load(pc);
			return pc;
		}
		return null;
	}
	/**
	 * 更新配置参数的值(使用默认的configname)
	 * @param code
	 * @param value
	 */
	public static String get(String code)
	{
		return use(DEFAULT_CONFIGNAME).get(code);
	}
	public static String get(String configname,String code)
	{
		return use(configname).get(code);
	}
	/**
	 * 更新配置参数的值(使用默认的configname)
	 * @param code
	 * @param value
	 */
	public static void update(String code,String value)
	{
		use(DEFAULT_CONFIGNAME).update(code, value);
	}
	/**
	 * 更新配置参数的值
	 * @param configname
	 * @param code
	 * @param value
	 */
	public static void update(String configname,String code,String value)
	{
		use(configname).update(code, value);
	}
	
	private static boolean init(ParamConfig pc) {
		File xml_file = pc.getFile();
		try {
			Document doc=XMLKit.parseXml(xml_file, false);
			@SuppressWarnings("unchecked")
			Iterator<Element> iter = doc.getRootElement().elementIterator("paramter");
			while(iter.hasNext())
			{
				try {
				Element el=iter.next();
				String code=el.attributeValue("code");
				String value=el.attributeValue("value");
				String describe=el.elementText("describe");
				String options=el.elementText("options");
				String remark=el.elementText("remark");
				pc.addParam(code, value, describe, options,remark);
				} catch (Exception e) {
					LOG.error(e.getMessage());
				}
			}
		} catch (Exception e) {
			LOG.error("参数文件加载异常:"+e.getMessage());
			return false;
		}
		return true;
	}
}
