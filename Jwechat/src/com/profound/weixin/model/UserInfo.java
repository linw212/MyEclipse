package com.profound.weixin.model;

import com.profound.common.annotations.TableBind;
import com.profound.common.model.BaseModel;

@TableBind(tableName="wx_userinfo",pkName="openid")
public class UserInfo extends BaseModel<UserInfo>{
	private static final long serialVersionUID = 1L;
	public static UserInfo dao=new UserInfo();
	public static final String OPENID="openid";
	public static final String NICKNAME="nickname";
	public static final String SEX="sex";
	public static final String COUNTRY="country";
	public static final String PROVINCE="province";
	public static final String CITY="city";
	public static final String LANGUAGE="language";
	public static final String HEADIMGURL="headimgurl";
	public static final String SUBSCRIBE="subscribe";
	public static final String SUBSCRIBE_TIME="subscribe_time";
	public static final String UNSUBSCRIBE_TIME="unsubscribe_time";
	public static final String UNIONID="unionid";
	public static final String GROUPID="groupid";
	public static final String SCENEID="sceneid";
	public static final String REMARK="remark";
	public static final String PRIVILEGE="privilege";
}
