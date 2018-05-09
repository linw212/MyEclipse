package com.profound.system.model;

import com.profound.common.annotations.TableBind;
import com.profound.common.model.BaseModel;

@SuppressWarnings("serial")
@TableBind(tableName="sys_user")
public class SysUser extends BaseModel<SysUser>{
	
	public static SysUser dao = new SysUser();
	
	public SysUser getUserByUserName(String username){
		return this.dao().findFirst("select *,CONVERT(GROUP_CONCAT(PASSWORD) USING utf8) as pwd from sys_user where username = ? and status = 1 ", username);
	}
}
