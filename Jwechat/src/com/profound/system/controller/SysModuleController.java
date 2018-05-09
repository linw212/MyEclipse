package com.profound.system.controller;

import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.profound.common.annotations.BackRoute;
import com.profound.system.model.SysModule;

@BackRoute("/sys/module")
public class SysModuleController extends Controller {

	public static final Log LOG = Log.getLog(SysModuleController.class);
	
	public void querySysModule(){
		try {
			renderJson(SysModule.dao.queryModule());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			LOG.error(e.getMessage(), e);
			renderJson("");
		}
	}
}
