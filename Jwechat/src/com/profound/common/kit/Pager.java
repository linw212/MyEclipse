package com.profound.common.kit;

import java.util.List;

public class Pager {
	/**默认每页数据条数*/
	private static final int DEFAULT_PAGE_SIZE = 20;
	/**默认当前页码*/
	private static final int DEFAULT_CURRENT_PAGE = 1;
	/**成功的状态码，默认：0*/
	protected int code;
	/**状态信息的字段名称 */
	protected String msg;
	/**每页的数据条数*/
	protected int limit;
	/**当前页码*/
	protected int page;
	/**总记录条数*/
	protected int count;
	/**分页查询的数据集合*/
	protected List<?> data;
	/**汇总合计数据*/
	protected Object sumData;
	public Pager(int page, int limit) {
		this.page = page<1?1:page;
		this.limit = limit;
	}

	public Pager(int page) {
		this(page,DEFAULT_PAGE_SIZE);
	}

	public Pager() {
		this.page = DEFAULT_CURRENT_PAGE;
		this.limit = DEFAULT_PAGE_SIZE;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<?> getData() {
		return data;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public int getLimit() {
		return limit;
	}

	public int getPage() {
		return page;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		if(limit==-1)
			return data==null?0:data.size();
		else
			return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Object getSumData() {
		return sumData;
	}

	public void setSumData(Object sumData) {
		this.sumData = sumData;
	}

}
