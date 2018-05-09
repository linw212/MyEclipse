package com.profound.common.controller;

import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.kit.StrKit;
import com.profound.common.annotations.ForeRoute;
import com.profound.common.plugin.system.SystemDicConfig;
import com.profound.common.render.CaptchaRender;

/**
 * 生成验证码
 * 
 * @author Administrator
 *
 */
@Clear
@ForeRoute("/assist")
public class AssistController extends Controller {
	@Before(NoUrlPara.class)
	public void index()
	{
		renderNull();
	}
	
	/**
	 * 产生验证码
	 */
	public void captcha() {
		CaptchaRender img = new CaptchaRender(); 
		render(img);
	}
	
	/**
	 * 加载数据字典
	 */
	public void loadDC()
	{
		String type=getPara("dType");
		String[] notInclude=null;
		if(type.contains("!"))
		{
			String[] temp_type=type.split("!");
			type=temp_type[0];
			notInclude=temp_type.length>1?temp_type[1].split(","):null;
		}
		renderJson(SystemDicConfig.getForCombobox(type,notInclude));
	}
	
	/**
	 * 生成二维码
	 */
	public void qrcode() {
		if(StrKit.isBlank(getPara("code")))
			renderQrCode(getPara(0), getParaToInt(1), getParaToInt(2));
		else
			renderQrCode(getPara("code"), getParaToInt("width"), getParaToInt("height"));
	}
}
