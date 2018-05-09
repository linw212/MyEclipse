<%@page import="com.profound.system.model.SysUser"%>
<%@ page language="java" import="java.util.*,java.text.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	SysUser user = (SysUser)session.getAttribute("user");
	String username = user.getStr("username");
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!-- 避免IE使用兼容模式 -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <meta name="renderer" content="webkit">
    <meta name="keywords" content=''/>
    <title>首页</title>
    <jsp:include page="/resources/include.jsp"></jsp:include>
    <!-- 首页js -->
    <script type="text/javascript" src="<%=path %>/resources/frame/base/static/public/js/topjui.index.js" charset="utf-8"></script>
</head>
<style type="text/css">
    /* right */
   .top_right {
        /*width: 748px;*/
    }

    /* top_link */
    .top_link {
        padding-top: 24px;
        height: 26px;
        line-height: 26px;
        padding-right: 35px;
        text-align: right;
    }

    .top_link i {
        color: #686868;
    }

    .top_link span, .top_link a {
        color: #46AAFE;
    }

    .top_link a {
        font-size: 13px;
    }

    .top_link a:hover {
        text-decoration: underline;
    }

    .nav_bar {
        position: relative;
        z-index: 999;
        color: #333;
        margin-right: 10px;
        height: 50px;
        line-height: 50px;
    }

    .nav_bar ul {
        padding: 0;
    }

    .nav {
        position: relative;
        margin: 0 auto;
        font-family: "Microsoft YaHei", SimSun, SimHei;
        font-size: 14px;
    }

    .nav a {
        color: #333;
    }

    .nav h3 {
        font-size: 100%;
        font-weight: normal;
        height: 50px;
        line-height: 50px;
    }

    .nav h3 a {
        display: block;
        padding: 0 20px;
        text-align: center;
        font-size: 14px;
        color: #fff;
        height: 50px;
        line-height: 50px;
    }

    .nav .m {
        float: left;
        position: relative;
        z-index: 1;
        height: 50px;
        line-height: 50px;
        list-style: none;
    }

    .nav .s {
        float: left;
        width: 3px;
        text-align: center;
        color: #D4D4D4;
        font-size: 12px;
        height: 50px;
        line-height: 50px;
        list-style: none;
    }

    .nav .sub, ul.sub {
        display: none;
        position: absolute;
        left: -3px;
        top: 42px;
        z-index: 999;
        width: 128px;
        border: 1px solid #E6E4E3;
        border-top: 0;
        background: #fff;
    }

    .nav .sub li {
        text-align: center;
        padding: 0 8px;
        margin-bottom: -1px;
        list-style: none;
    }

    .nav .sub li a {
        display: block;
        border-bottom: 1px solid #E6E4E3;
        padding: 8px 0;
        height: 28px;
        line-height: 28px;
        color: #666;
    }

    .nav .sub li a:hover {
        color: #1E95FB;
        cursor: pointer;
    }

    .nav .block {
        height: 3px;
        background: #1E95FB;
        position: absolute;
        left: 0;
        top: 47px;
        overflow: hidden;
    }

    .sub {
        padding: 0;
        background: #f5f5f5;
    }

    .sub li {
        padding: 0 8px;
        list-style: none;
    }

    .sub li:hover {
        background: #f3f3f3;
    }

    .sub li a {
        display: block;
        color: #000;
        height: 34px;
        line-height: 34px;
    }

    .sub li a:hover {
        text-decoration-line: none;
    }

    /* 重用类样式 */
    .f_l {
        float: left !important;
    }

    .f_r {
        float: right !important;
    }

    .no_margin {
        margin: 0px !important;
    }

    .no_border {
        border: 0px !important;
    }

    .no_bg {
        background: none !important;
    }

    .clear_both {
        clear: both !important;
    }

    .display_block {
        display: block !important;
    }

    .text_over {
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
        -moz-binding: url('ellipsis.xml#ellipsis');
    }

    /* 重用自定义样式 */
    .w_100 {
        width: 100%;
    }

    .w_95 {
        width: 95%;
    }

    .indextx {
        width: 980px;
        margin: 0 auto;
        margin-top: 10px;
        background: #FFFFFF;
    }

    .w_min_width {
        min-width: 1200px;
    }

    .w_1200 {
        width: 1200px;
    }

    .w_1067 {
        width: 1067px;
    }

    .w_980 {
        width: 980px;
    }

    .header {
        overflow: hidden
    }
</style>
<body>
<div id="loading" class="loading-wrap">
    <div class="loading-content">
        <div class="loading-round"></div>
        <div class="loading-dot"></div>
    </div>
</div>

<div id="mm" class="submenubutton" style="width: 140px;">
    <div id="mm-tabclose" name="6" iconCls="fa fa-refresh">刷新</div>
    <div class="menu-sep"></div>
    <div id="Div1" name="1" iconCls="fa fa-close">关闭</div>
    <div id="mm-tabcloseother" name="3">关闭其他</div>
    <div id="mm-tabcloseall" name="2">关闭全部</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright" name="4">关闭右侧标签</div>
    <div id="mm-tabcloseleft" name="5">关闭左侧标签</div>
</div>

<script>
    $(function () {
        $('#ulMenu>li').hover(
            function () {
                var m = $(this).data('menu');
                if (!m) {
                    m = $(this).find('ul').clone();
                    m.appendTo(document.body);
                    $(this).data('menu', m);
                    var of = $(this).offset();
                    m.css({left: of.left, top: of.top + this.offsetHeight});
                    m.hover(function () {
                        clearTimeout(m.timer);
                    }, function () {
                        m.hide()
                    });
                }
                m.show();
            }, function () {
                var m = $(this).data('menu');
                if (m) {
                    m.timer = setTimeout(function () {
                        m.hide();
                    }, 100);//延时隐藏，时间自定义，100ms
                }
            }
        );
    });
</script>
<div data-toggle="topjui-layout" data-options="id:'index_layout',fit:true">
    <div id="north" class="banner" data-options="region:'north',border:false,split:false"
         style="height: 50px; padding:0;margin:0; overflow: hidden;">
        <table style="float:left;border-spacing:0px;">
            <tr>
                <td class="webname">
                    <!-- <span class="fa fa-envira" style="font-size:26px; padding-right:8px;"></span> -->微信公众号管理平台
                </td>
                <td class="collapseMenu" style="text-align: center;cursor: pointer;">
                    <span class="fa fa-chevron-circle-left" style="font-size: 18px;"></span>
                </td>
                <td>
                    <table id="topmenucontent" cellpadding="0" cellspacing="0">
                    </table>
                </td>
            </tr>
        </table>
        <span style="float: right; padding-right: 10px; height: 50px; line-height: 50px;">
            <a href="javascript:void(0)" data-toggle="topjui-splitbutton"
               data-options="iconCls:'fa fa-user',hasDownArrow:false"
               style="color:#fff;"><%=username %></a>|
            <a href="javascript:void(0)" id="mb3" data-toggle="topjui-splitbutton"
               data-options="menu:'#mm3',iconCls:'fa fa-cog',hasDownArrow:true" style="color:#fff;">设置</a>
            <div id="mm3" style="width:74px;">
                <div data-options="iconCls:'fa fa-info-circle'" onclick="javascript:void(0)">个人信息</div>
                <div class="menu-sep"></div>
                <div data-options="iconCls:'fa fa-key'" onclick="javascript:modifyPwd(0)">修改密码</div>
            </div>|
            <a href="javascript:void(0)" id="mb2" data-toggle="topjui-splitbutton"
               data-options="menu:'#mm2',iconCls:'fa fa-tree',hasDownArrow:true" style="color:#fff;">主题</a>|
            <div id="mm2" style="width:180px;">
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('blue')">默认主题</div>
                <div data-options="iconCls:'fa fa-tree'" onclick="changeTheme('black')">黑色主题</div>
                <div data-options="iconCls:'fa fa-tree'" onclick="changeTheme('blacklight')">黑色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree red'" onclick="changeTheme('red')">红色主题</div>
                <div data-options="iconCls:'fa fa-tree red'" onclick="changeTheme('redlight')">红色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree green'" onclick="changeTheme('green')">绿色主题</div>
                <div data-options="iconCls:'fa fa-tree green'" onclick="changeTheme('greenlight')">绿色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree purple'" onclick="changeTheme('purple')">紫色主题</div>
                <div data-options="iconCls:'fa fa-tree purple'" onclick="changeTheme('purplelight')">紫色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('blue')">蓝色主题</div>
                <div data-options="iconCls:'fa fa-tree blue'" onclick="changeTheme('bluelight')">蓝色主题-亮</div>
                <div data-options="iconCls:'fa fa-tree orange'" onclick="changeTheme('yellow')">橙色主题</div>
                <div data-options="iconCls:'fa fa-tree orange'" onclick="changeTheme('yellowlight')">橙色主题-亮</div>
            </div>
            <a href="javascript:void(0)" onclick="logout()" data-toggle="topjui-splitbutton"
               data-options="iconCls:'fa fa-sign-out',hasDownArrow:false" style="color:#fff;">注销</a>
        </span>
    </div>

<div id="west"
         data-options="region:'west',split:true,width:230,border:false,headerCls:'border_right',bodyCls:'border_right'"
         title="" iconCls="fa fa-dashboard">
        <div id="RightAccordion"></div>
        <!--<div id="menuTab" class="topjui-tabs" data-options="fit:true,border:false">
            <div title="导航菜单" data-options="iconCls:'fa fa-sitemap'" style="padding:0;">
                <div id="RightAccordion" class="topjui-accordion"></div>
            </div>
            <div title="常用链接" data-options="iconCls:'fa fa-star',closable:true">
                <ul id="channgyongLink"></ul>
            </div>
        </div>-->
    </div>

    <div id="center" data-options="region:'center',border:false" style="overflow:hidden;">
        <div id="index_tabs" style="width:100%;height:100%">
            <div title="系统首页" iconCls="fa fa-home" data-options="border:true,iframe:true,
            content:'<iframe src=\'<%=path %>/resources/frame/base/html/portal/index.html\' scrolling=\'auto\' frameborder=\'0\' style=\'width:100%;height:100%;\'></iframe>'"></div>
        </div>
    </div>

    <div data-options="region:'south',border:true"
         style="text-align:center;height:30px;line-height:30px;border-bottom:0;overflow:hidden;">
        <span style="float:left;padding-left:5px;width:30%;text-align: left;">当前用户：<%=username %></span>
        <span style="padding-right:5px;width:40%">
            版权所有 © 2014-2017
            <a href="https://www.ewsd.cn" target="_blank">深圳易网时代信息技术有限公司</a>
            <a href="http://www.miitbeian.gov.cn" target="_blank">粤ICP备16028103号-1</a>
        </span>
        <span style="float:right;padding-right:5px;width:30%;text-align: right;">版本：<script>document.write(topJUI.version)</script></span>
    </div>
</div>

<!--[if lte IE 8]>
<div id="ie6-warning">
    <p>您正在使用低版本浏览器，在本页面可能会导致部分功能无法使用，建议您升级到
        <a href="http://www.microsoft.com/china/windows/internet-explorer/" target="_blank">IE9或以上版本的浏览器</a>
        或使用<a href="http://se.360.cn/" target="_blank">360安全浏览器</a>的极速模式浏览
    </p>
</div>
<![endif]-->

<div id="themeStyle" data-options="iconCls:'fa fa-tree'" style="display:none;width:600px;height:340px">
    <table style="width:100%; padding:20px; line-height:30px;text-align:center;">
        <tr>
            <td>
                <div class="skin-common skin-black"></div>
                <input type="radio" name="themes" value="black" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-red"></div>
                <input type="radio" name="themes" value="red" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-green"></div>
                <input type="radio" name="themes" value="green" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-purple"></div>
                <input type="radio" name="themes" value="purple" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-blue"></div>
                <input type="radio" name="themes" value="blue" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-yellow"></div>
                <input type="radio" name="themes" value="yellow" class="topjuiTheme"/>
            </td>
        </tr>
        <tr>
            <td>
                <div class="skin-common skin-blacklight"></div>
                <input type="radio" name="themes" value="blacklight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-redlight"></div>
                <input type="radio" name="themes" value="redlight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-greenlight"></div>
                <input type="radio" name="themes" value="greenlight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-purplelight"></div>
                <input type="radio" name="themes" value="purplelight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-bluelight"></div>
                <input type="radio" name="themes" value="bluelight" class="topjuiTheme"/>
            </td>
            <td>
                <div class="skin-common skin-yellowlight"></div>
                <input type="radio" name="themes" value="yellowlight" class="topjuiTheme"/>
            </td>
        </tr>
    </table>
    <table style="width: 100%; padding: 20px; line-height: 30px; text-align: center;">
        <tr>
            <td>
                <input type="radio" name="menustyle" value="accordion" checked="checked"/>手风琴
            <td>
                <input type="radio" name="menustyle" value="tree"/>树形
            </td>
            <td>
                <input type="checkbox" checked="checked" name="topmenu" value="topmenu"/>开启顶部菜单
            </td>
        </tr>
    </table>
</div>

<form id="pwdDialog"
      data-options="title: '修改密码',
      iconCls:'fa fa-key',
      width: 400,
      height: 300,
      href: '<%=path %>/resources/frame/base/html/user/modifyPassword.html'"></form>
</body>
</html>