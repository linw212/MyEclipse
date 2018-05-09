package com.profound.common.controller;

import com.jfinal.core.Controller;
import com.profound.common.annotations.ForeRoute;
import com.profound.common.kit.StringKit;
@ForeRoute("/html")
public class HtmlPageController extends Controller{
	
	public void index(){
		String url = this.getRequest().getRequestURI();
		String querys = this.getRequest().getQueryString();
		String _url = url.substring(url.indexOf("/html")+"/html".length()+1)+".html"+(StringKit.isBlank(querys)?"":"?"+querys);
		render(_url);
	}
}
