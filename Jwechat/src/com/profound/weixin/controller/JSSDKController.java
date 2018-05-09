package com.profound.weixin.controller;

import java.util.Map;
import java.util.TreeMap;
import com.jfinal.core.JFinal;
import com.jfinal.kit.HashKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.JsTicket;
import com.jfinal.weixin.sdk.api.JsTicketApi;
import com.jfinal.weixin.sdk.api.JsTicketApi.JsApiType;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.profound.common.annotations.ForeRoute;
import com.profound.common.kit.ParamKit;
import com.profound.weixin.kit.AccountConfigKit;
/**
 * 微信JS-SDK使用
 * @author Administrator
 *
 */
@ForeRoute("/wx/jssdk")
public class JSSDKController extends ApiController {
	protected static final Log log = Log.getLog(JSSDKController.class);
	private static final String JSAPI="['onMenuShareTimeline','onMenuShareAppMessage','onMenuShareQQ','onMenuShareWeibo','onMenuShareQZone','startRecord','stopRecord','onVoiceRecordEnd','playVoice','pauseVoice','stopVoice','onVoicePlayEnd','uploadVoice','downloadVoice','chooseImage','previewImage','uploadImage','downloadImage','translateVoice','getNetworkType','openLocation','getLocation','hideOptionMenu','showOptionMenu','hideMenuItems','showMenuItems','hideAllNonBaseMenuItem','showAllNonBaseMenuItem','closeWindow','scanQRCode','chooseWXPay','openProductSpecificView','addCard','chooseCard','openCard']";
//	private static final String WX_JS= "var scriptObj = document.createElement('script');scriptObj.src = 'http://res.wx.qq.com/open/js/jweixin-1.0.0.js';scriptObj.id='wx_js';scriptObj.type = 'text/javascript';document.getElementsByTagName('head')[0].appendChild(scriptObj);";
	public void index()
	{
		getResponse().setHeader("Cache-Control", "no-cache,must-revalidate");
		String apiList=getPara();
		if(StrKit.isBlank(apiList))
			apiList=JSAPI;
		else
			apiList=JsonKit.toJson(apiList.split("-"));
		String ua=getRequest().getHeader("user-agent");
		if (ua.indexOf("MicroMessenger") < 0)
		{
			renderJavascript("alert('请使用微信打开此页面以便获得更好的体验');//用微信扫一扫下方二维码(后期实现)");
			return;
		}
		ApiConfig acf=AccountConfigKit.getApiConfig(ParamKit.get("wx", "accountid"));
		ApiConfigKit.setThreadLocalAppId(acf.getAppId());
		JsTicket jt = JsTicketApi.getTicket(JsApiType.jsapi);
		String referer=getRequest().getHeader("referer");
		if(jt.isAvailable())
		{
//			StringBuffer sb=new StringBuffer(WX_JS);
			String timestamp=String.valueOf(System.currentTimeMillis());
			String noncestr=StrKit.getRandomUUID();
			StringBuffer sb_temp=new StringBuffer();
			Map<String, String> paramMap = new TreeMap<String, String>();
	        paramMap.put("jsapi_ticket", jt.getTicket());
	        paramMap.put("noncestr", noncestr);
	        paramMap.put("timestamp", timestamp);
	        paramMap.put("url", referer);
	        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
	        	sb_temp.append("&").append(entry.toString());
	        }
	        String signature=HashKit.sha1(sb_temp.substring(1));
//	        sb.append("window.onload=function(){wx.config({debug: "+JFinal.me().getConstants().getDevMode()+",appId: '"+acf.getAppId()+"',timestamp: '"+timestamp+"',nonceStr: '"+noncestr+"', signature: '"+signature+"',jsApiList: "+apiList+"})}");
//	        sb.append("document.getElementById('wx_js').onload=function(){wx.config({debug: "+JFinal.me().getConstants().getDevMode()+",appId: '"+acf.getAppId()+"',timestamp: '"+timestamp+"',nonceStr: '"+noncestr+"', signature: '"+signature+"',jsApiList: "+apiList+"})}");
			renderJavascript("wx.config({debug: "+JFinal.me().getConstants().getDevMode()+",appId: '"+acf.getAppId()+"',timestamp: '"+timestamp+"',nonceStr: '"+noncestr+"', signature: '"+signature+"',jsApiList: "+apiList+"})");
			return;
		}
		else
		{
			renderJavascript("$(function(){document.body.innerHTML='';alert('微信js暂不可用，请稍候再试...');});");
			log.error("微信JS-SDK签名失败[错误信息:"+jt.getErrorMsg()+"][引用地址:"+referer+"]");
			return;
		}
	}
}
