package com.profound.common.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.profound.common.kit.QueryCondition;
import com.profound.common.kit.QueryParameter;
import com.profound.common.model.ResultMsg;

public class BaseController extends Controller {
	protected static final Log log = Log.getLog(BaseController.class);
	private static Gson gson=new Gson();
	private QueryParameter queryParameter;
	protected <T> T getObjectFromJson(String name,Class<T> objectClass)
	{
		String value=getPara(name);
		if(StrKit.isBlank(value))
			return null;
		return gson.fromJson(value, objectClass);
	}
	protected QueryParameter getQueryParameter()
	{
		if(queryParameter!=null)
			return queryParameter;
		QueryParameter qp=new QueryParameter();
		@SuppressWarnings("unchecked")
		Map<String, Object> map=getObjectFromJson("@params", Map.class);
		if(map!=null)
		{
			for(String key:map.keySet())
			{
				Object val=map.get(key);
				//若查询条件第一个字符为@则作为查询的条件表达式
				if(key.indexOf("@")==0)
				{
					QueryCondition qc=null;
					try{
						qc=QueryCondition.valueOf(String.valueOf(val));
					}catch (Exception e) {
						qc=QueryCondition.equal;
						log.warn("查询参数["+key+"]使用的表达式["+val+"]不在表达式枚举的范围内,已作为equal默认值!");
					}
					qp.addCondition(key=key.replaceFirst("@", ""),qc);
				}
				else
					qp.addParamter(key, val);
			}
		}
		qp.setSortField(getPara("sortField"));
		qp.setSortOrder(getPara("sortOrder"));
		qp.setPager(getParaToInt("page",0),getParaToInt("limit",20));
		return queryParameter=qp;
	}
	protected void renderSuccess(String msg)
	{
		renderJson(new ResultMsg("ok", msg));
	}
	protected void renderFail(String err_code,String msg)
	{
		renderJson(new ResultMsg(err_code, msg));
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	protected String getUUID() {
		return StrKit.getRandomUUID();
	}

	/**
	 * 获取ip地址
	 * 
	 * @param request
	 * @return
	 */
	protected String getIP() {
		HttpServletRequest request = getRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	public void renderFile(String fileName,String filePath)
			throws ServletException, IOException {
		try
		{
			fileName = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		} catch (UnsupportedEncodingException e)
		{
			fileName="download_file.file";
		}
		getResponse().reset();
		getResponse().setContentType("application/octet-stream");   
		getResponse().setHeader("Content-disposition","attachment;filename="+fileName);
		File file=new File(filePath);
		InputStream fis=new BufferedInputStream(new FileInputStream(file));
		byte[] buffer=new byte[fis.available()];
		fis.read(buffer);
		fis.close();
		OutputStream toClient=new BufferedOutputStream(getResponse().getOutputStream());
		toClient.write(buffer);
		toClient.flush();
		toClient.close();
		renderNull();
		return;
	}
	public String toJson(Object obj)
	{
		return gson.toJson(obj);
	}
	public double getParaToDouble(String name)
	{
		String value=getPara(name);
		if(StrKit.isBlank(value))
			return 0;
		return Double.parseDouble(value);
	}
}
