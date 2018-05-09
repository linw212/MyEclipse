package com.profound.system.model;

import java.util.List;

import com.profound.common.annotations.TableBind;
import com.profound.common.model.BaseModel;

@SuppressWarnings("serial")
@TableBind(tableName="sys_module")
public class SysModule extends BaseModel<SysModule>{
	
	public static SysModule dao = new SysModule();
	
	public List<SysModule> queryModule(){
		return dao.find("select * from sys_module where status = 1 order by sort,createTime");
	}
	
}
