package com.profound.common.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class QueryParameter 
{
	private Pager pager;
	private Map<String, Object> params=new HashMap<String, Object>();
	public Map<String, QueryCondition> conditions=new HashMap<String, QueryCondition>();
	protected String sortField;
	protected String sortOrder;
	/**
	 * 设定分页参数
	 * @param pageSize 每页条数
	 * @param pageIndex 起始页数
	 * @param sortField 排序字段
	 * @param sortOrder 排序方式
	 * @return
	 */
	public Pager setPager(int page,int limit)
	{
		Pager pager=new Pager(page, limit);
		this.pager=pager;
		return this.pager;
	}
	public void setPager(Pager pager)
	{
		this.pager=pager;
	}
	/**
	 * 获取所有查询条件的name集合
	 * @return
	 */
	public Set<String> getParamNames()
	{
		return this.params.keySet();
	}
	/**
	 * 添加查询参数
	 * @param p_name 参数名称
	 * @param p_value 参数值
	 * @return 是否添加成功
	 */
	public boolean addParamter(String p_name,Object p_value)
	{
		if(StringKit.isBlank(p_name))//参数名为空剔除
			return false;
		p_name=p_name.trim();
		Object obj=initParam(p_value);
		if(obj!=null)//处理后的参数值为空的剔除
		{
			this.params.put(p_name, obj);
			return true;
		}
		return false;
	}
	/**
	 * 添加查询参数,重载的方法
	 * @param p_name 参数名称
	 * @param p_value 参数值
	 * @param condition 应用的查询表达式
	 */
	public void addParamter(String p_name,Object p_value,QueryCondition condition)
	{
		p_name=p_name.trim();
		if(addParamter(p_name, p_value))
			conditions.put(p_name, condition);
	}
	/**
	 * 移除查询参数
	 * @param p_name 参数名称
	 */
	public Object removeParamter(String p_name)
	{
		conditions.remove(p_name);
		return params.remove(p_name);
	}
	/**
	 * 获取参数值
	 * @param p_name 参数名称
	 * @return
	 */
	public Object getParam(String p_name)
	{
		return params.get(p_name);
	}
	/**
	 * 设定查询参数集合
	 * @param params 参数集合
	 */
	public void setParams(Map<String, Object> params)
	{
		if(params==null)
			return;
		this.params.clear();
		for(String key:params.keySet())
		{
			Object obj=params.get(key);
			this.addParamter(key, obj);
		}
		
	}
	/**
	 * 处理参数的值,主要针对非空,数组和集合中空值的处理,
	 * 此操作会剔除空值
	 * @param param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Object initParam(Object param)
	{
		if(StringKit.isBlank(param))
			return null;
		if(param.getClass().isArray())
		{
			@SuppressWarnings("serial")
			List<Object> list=new ArrayList<Object>(){
				@Override
				public String toString() {
					String s= super.toString();
					return s.substring(1, s.length()-1);
				}
			};
			for(Object o:(Object[])param)
			{
				if(StringKit.notBlank(o))
					list.add(o);
			}
			return list.size()>0?list:null;
		}
		else if(param instanceof Collection)
		{
			return initParam(((Collection<Object>)param).toArray());
		}
		else
			return param.toString().trim();
	}
	/**
	 * 获取分页对象
	 * @return
	 */
	public Pager getPager() {
		return pager;
	}
	/**
	 * 获取所有的查询参数
	 * @return
	 */
	public Map<String, Object> getParams() {
		return params;
	}
	/**
	 * 获取排序字段
	 * @return 排序的字段名
	 */
	public String getSortField() {
		if(StringKit.notBlank(sortField))
			return sortField;
		else
			return "";
	}
	/**
	 * 获取排序字段带模式名
	 * @param translation 模式名
	 * @return 带模式名的排序字段名
	 */
	public String getSortField(String translation) {
		if(StringKit.notBlank(sortField))
		{
			if(StringKit.isBlank(translation))
				translation="";
			else
				translation=translation+".";
			String[] fields=sortField.split(",");
			StringBuffer sb=new StringBuffer();
			for(String field:fields)
				sb.append(","+translation+field);
			
			return sb.toString().replaceFirst(",", "");
		}
		else
			return "";
	}
	/**
	 * 获取排序字符串
	 * @param translation 表的别名(如:原始sql select * from table_temp t;则此时你的translation 传 t)
	 * @return 若没有排序字段则返会空字符串,否则返回order by xxx asc/desc
	 */
	public String getOrderStr(String translation)
	{
		if(StringKit.isBlank(sortField))
			return "";
		return " order by "+getSortField(translation)+" "+getSortOrder();
	}
	public void setSortField(String sortField) {
		this.sortField = sortField;
	}
	public String getSortOrder() {
		if(StringKit.notBlank(sortOrder))
			return sortOrder;
		else
			return "asc";
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * 添加查询参数的表达式
	 * @param param 应用表达式的列/属性名
	 * @param qc 表达式
	 */
	public void addCondition(String param,QueryCondition qc)
	{
		if(StringKit.notBlank(param))
			conditions.put(param.trim(), qc);
	}
	/**
	 * 获取查询参数的表达式
	 * @param param 应用表达式的列/属性名
	 * @return 表达式(若此列不存在则返回null)
	 */
	public QueryCondition getCondition(String param)
	{
		if(StringKit.notBlank(params.get(param))&&conditions.get(param)==null)
				return QueryCondition.equal;
		return conditions.get(param);
	}
	/**
	 * 将查询参数和查询表达式处理成sql的where条件表达式语句
	 * @param translation 模式名(可空)
	 * @return sql的where表达式语句(不包含where关键字)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String transformationCondition(String translation)
	{
		if(StringKit.isBlank(translation))
			translation="";
		else
			translation=translation+".";
		StringBuffer sb=new StringBuffer(" 1=1 ");
		for(String key : params.keySet() )
		{
			Object value=params.get(key);
			//若值为数组或集合时将多个值处理成,号分割的字符串
			if(value!=null&&(value.getClass().isArray()||value instanceof Collection))
			{
				if(value.getClass().isArray())//数组类型转换
					value=StringKit.arrayToString((Object[])value, ",");
				if(value instanceof List)
				{
					value=StringKit.arrayToString(((List<Object>)value).toArray(), ",");//集合类型转换
				}
			}
			if(StringKit.isBlank(value))
				continue;
			int indexof=key.indexOf("$");
			String fn_s="";
			String fn_e="";
			if(indexof!=-1)
			{
				String[] fn=key.substring(indexof+1).split("\\?");
				fn_s=fn[0];
				fn_e=fn[1];
				key=key.substring(0,indexof);
			}
			if(conditions.get(key)!=null)
			{
				QueryCondition qc=conditions.get(key);
				if(key.contains("#"))
					key=key.substring(0, key.indexOf("#"));
				if(qc==QueryCondition.equal)
					sb.append(" and "+fn_s+translation+key+fn_e+" = '"+value+"'");
				else if(qc==QueryCondition.not_equal)
					sb.append(" and "+fn_s+translation+key+fn_e+" != '"+value+"'");
				else if(qc==QueryCondition.large)
					sb.append(" and "+fn_s+translation+key+fn_e+" > '"+value+"'");
				else if(qc==QueryCondition.large_equal)
					sb.append(" and "+fn_s+translation+key+fn_e+" >= '"+value+"'");
				else if(qc==QueryCondition.small)
					sb.append(" and "+fn_s+translation+key+fn_e+" < '"+value+"'");
				else if(qc==QueryCondition.small_equal)
					sb.append(" and "+fn_s+translation+key+fn_e+" <= '"+value+"'");
				else if(qc==QueryCondition.like_anywhere)
					sb.append(" and "+fn_s+translation+key+fn_e+" like '%"+value+"%'");
				else if(qc==QueryCondition.like_end)
					sb.append(" and "+fn_s+translation+key+fn_e+" like '%"+value+"'");
				else if(qc==QueryCondition.like_start)
					sb.append(" and "+fn_s+translation+key+fn_e+" like '"+value+"%'");
				else if(qc==QueryCondition.in)
				{
					String in_str=StringKit.change_in(value.toString());
					if(StringKit.notBlank(in_str))
						sb.append(" and "+fn_s+translation+key+fn_e+" in ("+in_str+")");
				}
				else if(qc==QueryCondition.not_in)
				{
					String in_str=StringKit.change_in(value.toString());
					if(StringKit.notBlank(in_str))
					sb.append(" and "+fn_s+translation+key+fn_e+" not in ("+in_str+")");
				}
				else if(qc==QueryCondition.if_null)
				{
					if(value.equals("1"))
						sb.append(" and ("+fn_s+translation+key+fn_e+" is null or "+translation+key+"='')");
					else if(value.equals("0"))
						sb.append(" and "+fn_s+translation+key+fn_e+" is not null");
				}
				else if(qc==QueryCondition.between)
				{
					Object[] b_obj=new Object[2];
					if(params.get(key) instanceof Object[])
						b_obj=(Object[])params.get(key);
					if(params.get(key) instanceof Collection)
						b_obj=((Collection)params.get(key)).toArray();
					sb.append(" and "+fn_s+translation+key+fn_e+" BETWEEN '"+b_obj[0]+"' AND '"+b_obj[1]+"'");
				}
				else if(qc==QueryCondition.not_between)
				{
					Object[] b_obj=(Object[])params.get(key);
					sb.append(" and "+fn_s+translation+key+fn_e+" NOT BETWEEN '"+b_obj[0]+"' AND '"+b_obj[1]+"'");
				}
				else if(qc==QueryCondition.large_small)
				{
					Object[] b_obj=new Object[2];
					if(params.get(key) instanceof Object[])
						b_obj=(Object[])params.get(key);
					if(params.get(key) instanceof Collection)
						b_obj=((Collection)params.get(key)).toArray();
					if(StringKit.notBlank(b_obj[0]))
						sb.append(" and "+fn_s+translation+key+fn_e+" >= '"+b_obj[0]+"'");
					if(StringKit.notBlank(b_obj[1]))
						sb.append(" and "+fn_s+translation+key+fn_e+" <= '"+b_obj[1]+"'");
				}
			}
			else
			{
				if(key.contains("#"))
					key=key.substring(0, key.indexOf("#"));
				sb.append(" and "+fn_s+translation+key+fn_e+"='"+value+"'");
			}
		}
		return sb.toString();
	}
	/**
	 * 此方法为了方便在列表查询时判断用户的视野权限,当查询参数中不存在判断的权限字段时则替换为replaceValue
	 * 中的值
	 * @param controlName
	 * @param replaceValue
	 * @return
	 */
	public String checkJurisdictionControl(String controlName,String replaceValue,QueryCondition qc)
	{
		if(StringKit.isBlank(getParam(controlName)))
		{
			addParamter(controlName, replaceValue,qc);
			return replaceValue;
		}
		else
			return getParam(controlName).toString();
	}
}
