package com.profound.weixin.interceptor;


import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.profound.common.kit.ParamKit;
import com.profound.weixin.controller.WeixinApiController;
import com.profound.weixin.exception.WxException;
import com.profound.weixin.kit.AccountConfigKit;
/**
 * 用户是否关注检查
 * @author 陈江林
 *
 */
public class SubscribeInterceptor implements Interceptor {
	@Override
	public void intercept(Invocation inv) {
		Controller controller=inv.getController();
		String requestType = controller.getHeader("X-Requested-With");
		ApiConfig ac=ApiConfigKit.getApiConfig();
		String ck=ac.getAppId()+"_sub";
		if("0".equals(controller.getCookie(ck, "0")))
		{
			String openid=((WeixinApiController)controller).getOpenid();
			ApiResult userinfo=UserApi.getUserInfo(openid);
			Integer subscribe=userinfo.get("subscribe");
			if(subscribe==null)
				throw new WxException("获取用户信息失败:"+userinfo.getErrorMsg());
			else if(subscribe==1)
			{
				controller.setCookie(ck, "1", -1, true);
				inv.invoke();
			}
			else
			{
				if("XMLHttpRequest".equals(requestType))
				{
					controller.getResponse().setStatus(601);//自定义必须关注的响应代码
				}
				controller.renderJsp("/WEB-INF/common/601Error.jsp");
				controller.setAttr("accountInfo", AccountConfigKit.getAccountConfigBase(ParamKit.get("wx", "accountid")));
				return;
			}
		}
		else
			inv.invoke();
	}
}
