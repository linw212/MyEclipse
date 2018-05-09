package com.profound.system.controller;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Clear;
import com.jfinal.log.Log;
import com.profound.common.annotations.BackRoute;
import com.profound.common.controller.BaseController;
import com.profound.common.kit.DateKit;
import com.profound.common.kit.StringKit;
import com.profound.common.render.CaptchaRender;
import com.profound.system.model.SysUser;
@Clear
@BackRoute("/sys/login")
public class SysLoginController extends BaseController{

	private static final Log LOG = Log.getLog(SysLoginController.class);
	
	public void index(){
		render("login.jsp");
	}
	
	public void submit(){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			String username = getPara("username");
			String password = getPara("password");
			String captcha = getPara("captcha");
			String referer = getPara("referer");
			
			/*SysUser.dao.set("username", username)
			.set("password", password)
			.set("nickname", "超级管理员").save();*/
			
			if (StringKit.isBlank(username)) {
				LOG.info("【用户登录失败】--【失败】--【输入帐号为空】");
				result.put("code", "usernameIsNull");
				result.put("msg", "请输入帐号");
				renderJson(result);
				return;
	        }
			
			if (StringKit.isBlank(password)) {
				LOG.info("【用户登录失败】--【失败】--【输入密码为空】");
				result.put("code", "passwordIsNull");
				result.put("msg", "请输入密码");
				renderJson(result);
				return;
	        }
			
			if (StringKit.isBlank(captcha)) {
				LOG.info("【用户登录失败】--【失败】--【验证码为空】");
				result.put("code", "captchaIsNull");
				result.put("msg", "请输入验证码");
				renderJson(result);
				return;
	        }
			if(!CaptchaRender.validate(this, captcha.toUpperCase(),false)){
				LOG.info("【用户登录】--【失败】--【验证码错误】");
				result.put("code", "captchaIsError");
				result.put("msg", "验证码错误");
				renderJson(result);
				return;
			}
			
			SysUser user = SysUser.dao.getUserByUserName(username);
	        
			if(user==null){
				LOG.info("【用户登录】--【失败】--【不存在此帐号】");
				result.put("code", "userIsNull");
				result.put("msg", "不存在此帐号，请检查后在输入。");
				renderJson(result);
				return;
			}
			
			if(!password.equals(user.getStr("pwd"))){
				LOG.info("【用户登录】--【用户："+username+"，ip："+this.getIP()+"】--【密码错误】");
				result.put("code", "passwordIsError");
				result.put("msg", "密码错误,请重新输入。");
				renderJson(result);
				return;
			}
			
			if(2==(user.getInt("status"))){
				LOG.info("【用户登录】--【用户："+username+"，ip："+this.getIP()+"】--【帐号被冻结】");
				result.put("code", "frozen");
				result.put("msg", "帐号被冻结,请联系管理员。");
				renderJson(result);
				return;
			}else{
				LOG.info("【用户登录】--【用户："+username+"，ip："+this.getIP()+"】--【登录成功】");
				result.put("code", "success");
				result.put("msg", "登录成功");
				result.put("referer", referer);
				getSession().setAttribute("user", user);
				user.set("lastLoginTime", DateKit.getStringDate()).update();
				CaptchaRender.clearCaptchaCookie(this);
				renderJson(result);
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			return;
		}
	}
	
	public void logout(){
		getSession().removeAttribute("user");
		render("login.jsp");
	}
}
