package com.profound.weixin.kit;

import com.jfinal.weixin.sdk.api.AccessToken;
import com.jfinal.weixin.sdk.api.AccessTokenApi;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.profound.common.kit.ParamKit;
import com.profound.common.kit.cache.DefaultConfigCache;
import com.profound.common.kit.cache.IConfigCache;
import com.profound.weixin.model.AccountConfigBase;

public class AccountConfigKit {
	/**
	 * 获取微信公众号基础配置信息
	 * @param id
	 * @return
	 */
	public static AccountConfigBase getAccountConfigBase(String id)
	{
		AccountConfigBase acb=accountConfigCache.get(id);
		if(acb==null)
		{
			acb=AccountConfigBase.dao.findById(id);
			if(acb!=null)
			{
				ApiConfig ac=new ApiConfig();
				ac.setAppId(acb.getStr("appid"));
				ac.setAppSecret(acb.getStr("appsecret"));
				ac.setToken(acb.getStr("token"));
				ac.setEncryptMessage("true".equalsIgnoreCase(acb.getStr("encryptMessage"))?true:false);
				ac.setEncodingAesKey(acb.getStr("encodingaeskey"));
				acb.put("apiConfig", ac);
				accountConfigCache.set(id, acb);
			}
			else
				throw new RuntimeException("系统中不存在["+id+"]的微信公众号信息");
		}
		return acb;
	}
	public static ApiConfig getApiConfig(String id)
	{
		return (ApiConfig)getAccountConfigBase(id).get("apiConfig");
	}
	public static void setAccountConfigBase(AccountConfigBase acb)
	{
		if(acb!=null)
		{
			ApiConfig ac=new ApiConfig();
			ac.setAppId(acb.getStr("appid"));
			ac.setAppSecret(acb.getStr("appsecret"));
			ac.setToken(acb.getStr("token"));
			ac.setEncryptMessage(acb.getBoolean("encryptMessage"));
			ac.setEncodingAesKey(acb.getStr("encodingaeskey"));
			acb.put("apiConfig", ac);
			accountConfigCache.set(acb.get("id").toString(), acb);
		}
	}
	public static void removeAccountConfigBase(String id)
	{
		AccountConfigBase acb=accountConfigCache.get(id);
		if(acb!=null)
		{
			accountConfigCache.remove(id);
			ApiConfigKit.getAccessTokenCache().remove(acb.getStr("appid"));
		}
	}
	public static AccessToken getAccessToken(String id)
	{
		ApiConfig config = (ApiConfig) getAccountConfigBase(id).get("apiConfig");
		ApiConfigKit.setThreadLocalAppId(config.getAppId());
		return AccessTokenApi.getAccessToken();
	}
	public static String getAccessTokenStr(String id)
	{
		return getAccessToken(id).getAccessToken();
	}
	static IConfigCache accountConfigCache = new DefaultConfigCache();

	public static void setAccountConfigCache(IConfigCache configCache) {
		AccountConfigKit.accountConfigCache = configCache;
	}

	public static IConfigCache getAccountConfigCache() {
		return accountConfigCache;
	}
}
