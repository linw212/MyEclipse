package com.profound.test.model;

import com.profound.common.annotations.TableBind;
import com.profound.common.model.BaseModel;

@SuppressWarnings("serial")
@TableBind(tableName="test",pkName="ID")
public class Person extends BaseModel<Person>{

	public static Person dao=new Person();
}
