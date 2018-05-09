package com.profound.system.model;

import java.util.List;

import com.profound.common.annotations.TableBind;
import com.profound.common.model.BaseModel;
@SuppressWarnings("serial")
@TableBind(tableName="sys_menu")
public class SysMenu extends BaseModel<SysMenu>{
	
	public static SysMenu dao = new SysMenu();
	
	public List<SysMenu> queryModule(){
		return dao.find("select * from sys_menu where status = 1 and pid=0 order by sort,createTime");
	}
	
	public List<SysMenu> queryMenu(String moduleCode,Integer pid){
		List<SysMenu> parentMenu = dao.find("select * from sys_menu where moduleCode = ? and pid = ? and status = 1",moduleCode,pid);
		for(SysMenu menu:parentMenu){
			List<SysMenu> childrenMenu = dao.find("select * from sys_menu where moduleCode = ? and pid = ? and status = 1",moduleCode,menu.getInt("id"));
			if(childrenMenu.size()>0){
				menu.put("children", childrenMenu);
			}
		}
		return parentMenu;
	}
}
