package com.profound.weixin.model;

import com.jfinal.weixin.sdk.api.ApiConfig;
import com.profound.common.annotations.TableBind;
import com.profound.common.kit.StringKit;
import com.profound.common.model.BaseModel;
import com.profound.weixin.kit.AccountConfigKit;

@TableBind(tableName="wx_account_config_base")
public class AccountConfigBase extends BaseModel<AccountConfigBase>{
	private static final long serialVersionUID = 1L;
	public static AccountConfigBase dao=new AccountConfigBase();
	
	@Override
	public boolean update() {

		boolean rs= super.update();
		if(rs)
			AccountConfigKit.setAccountConfigBase(this);
		return rs;
	}
	@Override
	public boolean save() {
		boolean rs= super.save();
		if(rs)
			AccountConfigKit.setAccountConfigBase(this);
		return rs;
	}
	@Override
	public boolean delete() {
		boolean rs= super.delete();
		if(rs)
			AccountConfigKit.removeAccountConfigBase(this.get("id").toString());
		return rs;
	}
	@Override
	public boolean deleteById(Object idValue) {
		boolean rs= super.deleteById(idValue);
		if(rs)
			AccountConfigKit.removeAccountConfigBase(idValue.toString());
		return rs;
	}
	@Override
	public int deleteByIds(String ids) {
		int num=0;
		for(String id :ids.split(","))
		{
			num+=deleteById(id)?1:0;
		}
		return num;
	}
	@Override
	public boolean deleteById(Object... idValues) {
		int num=0;
		for(Object id :idValues)
		{
			num+=deleteById(id)?1:0;
		}
		return num>0?true:false;
	}
	
	public ApiConfig getUsingConfig(){
		AccountConfigBase wxConfig = AccountConfigBase.dao.findFirst(" select * from wx_account_config_base where status = 1 ");
		
        ApiConfig ac = new ApiConfig();
        // 配置微信 API 相关参数
        ac.setToken(wxConfig.getStr("token"));
        ac.setAppId(wxConfig.getStr("appId"));
        ac.setAppSecret(wxConfig.getStr("appSecret"));
        /**
         *  是否对消息进行加密，对应于微信平台的消息加解密方式：
         *  1：true进行加密且必须配置 encodingAesKey
         *  2：false采用明文模式，同时也支持混合模式
         */
        boolean encryptMessage = (StringKit.notBlank(wxConfig.getStr("encryptMessage")) && "true".equalsIgnoreCase(wxConfig.getStr("encryptMessage")))?true:false;
        if(encryptMessage){
        	ac.setEncryptMessage(true);
        	ac.setEncodingAesKey(wxConfig.getStr("encodingAesKey"));
        }else{
        	ac.setEncryptMessage(false);
        }
        return ac;
	}
}
