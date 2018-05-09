<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@page import="com.profound.system.model.SysUser"%>
<%
	String path = request.getContextPath();
	
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String nowDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    SysUser user = (SysUser)session.getAttribute("user");
	String username = user.getStr("username");
%>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />

<!-- 浏览器标签图片 -->
<link rel="shortcut icon" href="<%=path %>/resources/frame/base/topjui/image/favicon.ico"/>
<!-- TopJUI框架样式 -->
<link type="text/css" href="<%=path %>/resources/frame/base/topjui/css/topjui.core.min.css" rel="stylesheet">
<link type="text/css" href="<%=path %>/resources/frame/base/topjui/themes/default/topjui.red.css" rel="stylesheet" id="dynamicTheme"/>
<!-- FontAwesome字体图标 -->
<link type="text/css" href="<%=path %>/resources/frame/base/static/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet"/>
<!-- layui框架样式 -->
<link type="text/css" href="<%=path %>/resources/frame/base/static/plugins/layui/css/layui.css" rel="stylesheet"/>
<!-- jQuery相关引用 -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/static/plugins/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=path %>/resources/frame/base/static/plugins/jquery/jquery.cookie.js"></script>
<!-- bootstrap -->

<!-- TopJUI框架配置 -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/static/public/js/topjui.config.js"></script>
<!-- TopJUI框架核心 -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/topjui/js/topjui.core.js"></script>
<!-- TopJUI中文支持 -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/topjui/js/locale/topjui.lang.zh_CN.js"></script>
<!-- layui框架js -->
<script type="text/javascript" src="<%=path %>/resources/frame/base/static/plugins/layui/layui.js" charset="utf-8"></script>
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
