package com.profound.test.controller;

import com.jfinal.aop.Before;
import com.jfinal.log.Log;
import com.jfinal.plugin.redis.Redis;
import com.profound.common.annotations.ForeRoute;
import com.profound.weixin.controller.WeixinApiController;
import com.profound.weixin.interceptor.OAuthInterceptor;
import com.profound.weixin.model.UserInfo;
@Before(OAuthInterceptor.class)
@ForeRoute("/hello")
public class HelloController extends WeixinApiController{

	private static final Log LOG = Log.getLog(HelloController.class);
	
	public void index() {
		UserInfo info = getWxUser();
		Redis.use().set("info", info.toJson());
		LOG.info(Redis.use().get("info").toString());
        render("hello.html");
    }
	
	public void test(){
		render("hello.html");
	}
}
