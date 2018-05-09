package com.profound.common.kit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * 使用dom4j加载xml.
 * 本工具类简单的封装了xml读取的操作,增加了加载文件前验证文件md5值.同时可持久化xml文档到内存中,供以后使用
 *
 */
public class XMLKit 
{
	static Logger logger = Logger.getLogger(XMLKit.class.getClass());
	private static Map<File,XMLDocument> docMap=new HashMap<File, XMLDocument>();
	
	static class XMLDocument
	{
		private File file;
		private String MD5;
		private Document doc;
	}
	/**
	 * 获取XML文件的Document对象
	 * @param xml_file xml文件对象
	 * @param update_verify 是否检测文件是否更新
	 * @return Document表示的xml
	 * @throws IOException 
	 */
	public static Document parseXml(File xml_file,boolean update_verify) throws IOException
	{
		if(!checkXMLFileType(xml_file))//判断文件是否存在并合法
			return null;
		XMLDocument xml_doc=docMap.get(xml_file);
		
		//查找加载记录中是否已经加载过此文档,并且判断是否需要检查文件是否更新
		if(xml_doc!=null)
		{
			if(update_verify)//判断是否检测文件修改
			{
				if(MD5Kit.getFileMD5String(xml_file).equals(xml_doc.MD5))//通过MD5值判断文件是否被修改过
					return xml_doc.doc;//如MD5相同说明文件没有被修改，直接范围doc文档对象
			}
			else
				return xml_doc.doc;//如果找到直接返回该文档
		}
		XMLDocument xmlDocument=new XMLDocument();
		xmlDocument.file=xml_file;
		xmlDocument.MD5=MD5Kit.getFileMD5String(xml_file);//计算出文件MD5值
		try
		{
			xmlDocument.doc=parseXml(xml_file);
		}catch(DocumentException e)
		{
			throw new IOException("解析XML文件时发生异常:"+xml_file+"\n"+e.getMessage());
		}
		docMap.put(xmlDocument.file, xmlDocument);
		return xmlDocument.doc;
	}
	/**
	 * 读取xml文件
	 * @param xml_file
	 * @return dom4j处理后的Document对象
	 * @throws DocumentException 
	 */
	private static Document parseXml(File xml_file) throws DocumentException
	{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(xml_file);
		return document;
	}
	private static boolean checkXMLFileType(File checkFile)
	{
		if(checkFile==null)
			return false;
		if(!checkFile.isFile())
			return false;
		if(!checkFile.getName().trim().toLowerCase().endsWith(".xml"))
			return false;
		return true;
	}
}
