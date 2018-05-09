<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String path = request.getContextPath(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>404错误</title>
<!-- content="60，即60秒后返回主页，可根据需要修改或者删除这段代码 -->
<link href="<%=path %>/resources/frame/404/css/error.css" rel="stylesheet" type="text/css" />
</head>
<body>
<!-- 代码 开始 -->
<div id="container"><img class="png" src="<%=path %>/resources/frame/404/img/404.png" /> 
	<img class="png msg" src="<%=path %>/resources/frame/404/img/404_msg.png" />
	<p>
  		<a href="http://www.jb51.net/" target="_blank">
  			<img class="png" src="<%=path %>/resources/frame/404/img/404_to_index.png" />
  		</a> 
	</p>
</div>
<div id="cloud" class="png"></div>
<!-- 代码 结束 -->

 
</body>
</html>