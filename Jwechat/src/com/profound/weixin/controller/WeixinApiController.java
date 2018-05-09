package com.profound.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.jfinal.kit.JsonKit;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.MenuApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.jfinal.weixin.sdk.jfinal.ApiController;
import com.profound.common.annotations.ForeRoute;
import com.profound.weixin.exception.WxException;
import com.profound.weixin.model.UserInfo;
@ForeRoute("/wx/api")
public class WeixinApiController extends ApiController {
	private UserInfo _wxUser;
	  public void index() {
	    render("/api/index.html");
	  }
	 
	  /**
	   * 获取公众号菜单
	   */
	  public void getMenu() {
	    ApiResult apiResult = MenuApi.getMenu();
	    if (apiResult.isSucceed())
	      renderText(apiResult.getJson());
	    else
	      renderText(apiResult.getErrorMsg());
	  }
	 
	  /**
	   * 获取公众号关注用户
	   */
	  public void getFollowers() {
	    ApiResult apiResult = UserApi.getFollows();
	    renderText(apiResult.getJson());
	  }
	  
	  @SuppressWarnings("unchecked")
		public UserInfo getWxUser(){
			UserInfo wxUser = getAttr("_wxUser");
			if(wxUser==null)
			{
				if(_wxUser==null)
				{
					String wu_cookie=ApiConfigKit.getApiConfig().getAppId()+"_wxUser";
					String wxUser_json=getCookie(wu_cookie);
					if(wxUser_json==null)
						return null;
					try {
						wxUser_json=URLDecoder.decode(wxUser_json,"UTF-8");
					} catch (UnsupportedEncodingException e) {
						throw new WxException("授权获取用户信息失败:"+e.getMessage());
					}
					return _wxUser=new UserInfo().put(JsonKit.parse(wxUser_json, Map.class));
				}
				else
					return _wxUser;
			}
			else
				return wxUser;
		}
		public String getOpenid()
		{
			UserInfo uf=getWxUser();
			return uf==null?null:uf.getStr(UserInfo.OPENID);
		}
}
