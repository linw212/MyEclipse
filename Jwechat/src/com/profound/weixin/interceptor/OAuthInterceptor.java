package com.profound.weixin.interceptor;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.SnsApi;
import com.profound.common.kit.DateKit;
import com.profound.common.kit.MD5Kit;
import com.profound.weixin.exception.WxException;
import com.profound.weixin.model.UserInfo;
/**
 * 网页授权
 *
 */
public class OAuthInterceptor implements Interceptor {
	private static final Log LOG = Log.getLog(OAuthInterceptor.class);
	@SuppressWarnings("unchecked")
	@Override
	public void intercept(Invocation inv) {
		try {
			Controller controller=inv.getController();
			HttpServletRequest request=controller.getRequest();
			ApiConfig ac=ApiConfigKit.getApiConfig();
			String wu_cookie=ac.getAppId()+"_wxUser";
			String wxUser_cookie=controller.getCookie(wu_cookie);
			String requestType = controller.getHeader("X-Requested-With");
			if(StrKit.isBlank(wxUser_cookie))
			{
				if("XMLHttpRequest".equals(requestType))
				{
					controller.getResponse().setStatus(500);
					controller.renderText("用户信息已过期,请刷新页面后再试");
					return;
				}
				String code=controller.getPara("code");
				String dateStr=MD5Kit.getMD5String(ac.getAppId()+"_OAuth_"+DateKit.getStringDateShort());
				if(StrKit.isBlank(code))
				{
					String requestUrl=request.getRequestURL().toString()+(StrKit.notBlank(request.getQueryString())?"?"+request.getQueryString():"");
					String qru=SnsAccessTokenApi.getAuthorizeURL(ac.getAppId(), requestUrl,dateStr,false);
					controller.redirect(qru);
					return;
				}
				else if(dateStr.equals(controller.getPara("state")))
				{
					SnsAccessToken sat=SnsAccessTokenApi.getSnsAccessToken(ac.getAppId(), ac.getAppSecret(), code);
					LOG.info("获取accessToken["+JsonKit.toJson(sat)+"]");
					ApiResult ar=SnsApi.getUserInfo(sat.getAccessToken(), sat.getOpenid());
					if(ar.getErrorCode()==null||ar.getErrorCode()==0)
					{
						String ui=null;
						try {
							ui=URLEncoder.encode(ar.getJson(), "UTF-8");
						} catch (UnsupportedEncodingException e) {
							throw new WxException("授权获取用户信息失败:"+e.getMessage());
						}
						/**将微信用户信息保存到cookie中,时效为1天*/
						controller.setCookie(wu_cookie,ui,86400,true);
						/**将微信用户信息临时保存到request中*/
						controller.setAttr("_wxUser", new UserInfo().put(JsonKit.parse(ar.getJson(), Map.class)));
						LOG.info("网页授权获取用户信息完成[用户信息:"+ar.getJson()+"]");
						inv.invoke();
						return;
					}else{
						LOG.error(ar.getErrorMsg());
						throw new WxException("授权获取用户信息失败:"+ar.getErrorMsg());
					}
				}
				else
				{
					LOG.error("网页授权非法或已过期");
					throw new WxException("网页授权非法或已过期");
				}
			}
			else
			{
				if(!"XMLHttpRequest".equals(requestType))
				{
					try {
						wxUser_cookie=URLDecoder.decode(wxUser_cookie,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						LOG.error(e.getMessage(),e);
						throw new WxException("授权获取用户信息失败:"+e.getMessage());
					}
					controller.setAttr("_wxUser", new UserInfo().put(JsonKit.parse(wxUser_cookie, Map.class)));
				}
				inv.invoke();
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOG.error(e.getMessage(),e);
			e.printStackTrace();
			return;
		}
	}
}
