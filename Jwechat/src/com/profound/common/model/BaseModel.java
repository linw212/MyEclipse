package com.profound.common.model;

import java.util.List;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;
import com.profound.common.kit.Pager;
import com.profound.common.kit.QueryParameter;
import com.profound.common.kit.StringKit;

public abstract class BaseModel<M extends BaseModel<M>> extends Model<M>{
	private static final long serialVersionUID = 1L;
	public Pager queryPager(QueryParameter qp)
	{
		Pager p=qp.getPager()==null?new Pager():qp.getPager();
		if(p.getPage()==-1)
		{
			List<M> list=queryList(qp);
			p.setData(list);
			p.setCount(list.size());
			p.setLimit(list.size());
			return p;
		}
		Table t=TableMapping.me().getTable(this.getClass());
		String sql=" from "+t.getName()+" where "+qp.transformationCondition(null)+qp.getOrderStr(null);
		Page<M> page=paginate(p.getPage(), p.getLimit(),false, "select * ", sql);
		p.setCode(0);
		p.setMsg("数据加载成功");
		p.setData(page.getList());
		p.setCount(page.getTotalRow());
		return p;
	}
	public Pager queryPager(String select, String sqlExceptSelect,Pager pager,Object... params)
	{
		Pager p=pager==null?new Pager():pager;
		if(p.getPage()==-1)
		{
			List<M> list=find(select+" "+sqlExceptSelect);
			p.setData(list);
			p.setCount(list.size());
			p.setLimit(list.size());
			return p;
		}
		Page<M> page=paginate(p.getPage(), p.getLimit(), select, sqlExceptSelect,params);
		p.setData(page.getList());
		p.setCount(page.getTotalRow());
		return p;
	}
	public List<M> queryList(QueryParameter qp)
	{
		Table t=TableMapping.me().getTable(this.getClass());
		String sql="select * from "+t.getName();
		if(qp!=null)
			sql=sql+" where "+qp.transformationCondition(null)+qp.getOrderStr(null);
		return find(sql);
	}
	public M queryFirst(QueryParameter qp)
	{
		Table t=TableMapping.me().getTable(this.getClass());
		String sql="select * from "+t.getName();
		if(qp!=null)
			sql=sql+" where "+qp.transformationCondition(null)+qp.getOrderStr(null);
		return findFirst(sql);
	}
	/**
	 * 根据Id删除
	 * @param ids
	 * @return
	 */
	public int deleteByIds(String ids)
	{
		Table t=TableMapping.me().getTable(this.getClass());
		String sql="delete from "+t.getName()+" where "+t.getPrimaryKey()[0]+" in ("+StringKit.change_in(ids)+")";
		return Db.use(getConfig().getName()).update(sql);
	}
	/**
	 * 是否存在数据
	 * 
	 * @param qp
	 * @return
	 */
	public boolean isAlreadyExistsbByQp(QueryParameter qp) {
		Table t = TableMapping.me().getTable(this.getClass());
		String sql = "select 1 from " + t.getName();
		if (qp != null)
			sql = sql + " where " + qp.transformationCondition(null);
		return findFirst(sql) != null;
	}
}
