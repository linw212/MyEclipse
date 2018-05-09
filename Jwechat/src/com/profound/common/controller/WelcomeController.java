package com.profound.common.controller;

import com.jfinal.core.Controller;
import com.profound.common.annotations.BackRoute;
@BackRoute("/")
public class WelcomeController extends Controller{

	public void index(){
		String url = this.getRequest().getRequestURI();
		String[] arr = url.split("/");
		for(int i=2;i<arr.length;i++){
			if(arr[i].length()>0){
				renderError(404, "/WEB-INF/common/404Error.jsp");
			}
		}
		render("index.jsp");
	}
	
	public void jump(){
		String page = this.getPara("page");
		String querys = this.getRequest().getQueryString();
		if(page.contains("?") && page.contains("=")){
			page = page +  querys.substring(querys.indexOf(page.split("\\?")[1])+page.split("\\?")[1].length());
		}
		render(page);
	}
}
