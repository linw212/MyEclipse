<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <title>系统菜单</title>
    <jsp:include page="/resources/include.jsp"></jsp:include>
</head>
<body>
	<table id="demo"></table>
<script type="text/javascript">
layui.use('table', function(){
  var table = layui.table;
  //第一个实例
  table.render({
    elem: '#demo'
    ,height: 'full-40'
    ,url: '<%=path %>/sys/menu/findSysMenuPage' //数据接口
    ,method: 'post'
    ,page: true //开启分页
    ,loading:true
    ,cols: [[ //表头
      {field: 'id',type:'checkbox', title: 'ID', align:'center', sort: true, fixed: 'left'}
      ,{field: 'text', title: '菜单名称',align:'center'}
      ,{field: 'state', title: '默认值',align:'center', sort: true}
      ,{field: 'iconCls', title: '图标',align:'center'} 
      ,{field: 'url', title: '访问地址',align:'center'}
      ,{field: 'status', title: '状态',align:'center', sort: true}
      ,{field: 'sort', title: '排序',align:'center',sort: true}
      ,{field: 'createTime', title: '创建时间',align:'center'}
      ,{field: 'createUser', title: '创建人',align:'center'}
      ,{fixed: 'right',title: '操作', width:200, align:'center', toolbar: '#barDemo'}
    ]]
    ,even: true
    ,size: 'lg'
  });
  //监听工具条
	table.on('tool(demo)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
	  var data = obj.data; //获得当前行数据
	  var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
	  var tr = obj.tr; //获得当前行 tr 的DOM对象
	 
	  if(layEvent === 'detail'){ //查看
	  	alert('detail');
	    //do somehing
	  } else if(layEvent === 'del'){ //删除
	    layer.confirm('真的删除行么', function(index){
	      obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
	      layer.close(index);
	      //向服务端发送删除指令
	    });
	  } else if(layEvent === 'edit'){ //编辑
	    //do something
	    alert('edit');
	    //同步更新缓存对应的值
	    obj.update({
	      username: '123'
	      ,title: 'xxx'
	    });
	  }
	});
});
</script>
<script type="text/html" id="barDemo">
  <a class="layui-btn layui-btn-xs" lay-event="detail">查看</a>
  <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>
</body>
</html>