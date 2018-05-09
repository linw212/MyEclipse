package com.profound.system.controller;

import com.jfinal.log.Log;
import com.profound.common.annotations.BackRoute;
import com.profound.common.controller.BaseController;
import com.profound.common.kit.Pager;
import com.profound.common.kit.QueryParameter;
import com.profound.common.kit.StringKit;
import com.profound.system.model.SysMenu;

@BackRoute("/sys/menu")
public class SysMenuController extends BaseController {
	
	public static final Log LOG = Log.getLog(SysMenuController.class);
	
	public void querySysModule(){
		try {
			renderJson(SysMenu.dao.queryModule());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			renderJson("");
		}
	}
	
	public void querySysMenu(){
		try {
			if(StringKit.notBlank(getPara("moduleCode")) && StringKit.notBlank(getPara("pid"))){
				renderJson(SysMenu.dao.queryMenu(getPara("moduleCode"),getParaToInt("pid")));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
		}
	}
	
	public void findSysMenuPage(){
		QueryParameter qp = this.getQueryParameter();
		Pager p = SysMenu.dao.queryPager(qp);
		renderJson(p);
	}
	
}
