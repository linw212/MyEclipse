<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String nowDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
%>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<link rel="icon" href="<%=path %>/resources/frame/base/favicon.ico">
<link rel="stylesheet" href="<%=path %>/resources/frame/base/layui/css/layui.css" media="all" />
<link rel="stylesheet" href="<%=path %>/resources/frame/base/css/index.css" media="all" />

<!-- jQuery相关引用 -->
<script type="text/javascript" src="<%=path %>/resources/plugin/jquery.min.js"></script>
<script type="text/javascript" src="<%=path %>/resources/plugin/jquery.cookie.js"></script>
<!-- layui相关引用 -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/layui/layui.js"></script>
<script type="text/javascript" src="<%=path %>/resources/frame/base/js/cache.js"></script>
<script type="text/javascript" src="<%=path %>/resources/frame/base/js/index.js"></script>
<script type="text/javascript">
	var path = '<%=path %>';
	var basePath = '<%=basePath %>';
	var nowDate = '<%=nowDate %>';
</script>
<style>
body 
{
	margin: 0 auto; 
	padding: 0 auto;
	border: 0;
}
</style>
