package com.profound.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;
import com.profound.system.model.SysUser;

public class LoginInterceptor implements Interceptor {

	private static final Log LOG = Log.getLog(LoginInterceptor.class);
	
	@Override
	public void intercept(Invocation inv) {
		// TODO Auto-generated method stub
		try {
			if("com.jfinal.weixin.sdk.jfinal.ApiController".equals(inv.getController().getClass().getSuperclass().getName()) 
				|| "com.jfinal.weixin.sdk.jfinal.MsgController".equals(inv.getController().getClass().getSuperclass().getName())){
				inv.invoke();
				return;
			}else{
				HttpSession session = inv.getController().getSession();  
				HttpServletRequest request=inv.getController().getRequest();
		        if(session == null){  
		        	inv.getController().redirect("/sys/login");  
		        	return;
		        }  
		        else{  
		            SysUser user = (SysUser) session.getAttribute("user");  
		            if(user != null && StrKit.notBlank(user.getStr("username"))) {  
		            	String requestUrl=request.getRequestURL().toString()+(StrKit.notBlank(request.getQueryString())?"?"+request.getQueryString():"");
		            	inv.getController().redirect(requestUrl);
		            	inv.invoke();
						return;
		            }  
		            else {  
		            	inv.getController().redirect("/sys/login");  
		            	return;
		            }  
		        } 
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			inv.getController().render("/WEB-INF/common/404.jsp");  
			return;
		}
	}
}
