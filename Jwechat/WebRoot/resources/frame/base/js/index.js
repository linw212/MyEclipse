var $,tab,dataStr,layer;
var path = getRootPath_web();
var isFullScreen = false;
var moduleIndex = 'resources/frame/base/page/main.html';
layui.config({
	base : path+"/resources/frame/base/js/"
}).extend({
	"bodyTab" : "bodyTab"
});
layui.use(['bodyTab','form','element','layer','jquery'],function(){
	var form = layui.form,
		element = layui.element;
		$ = layui.$;
    	layer = parent.layer === undefined ? layui.layer : top.layer;
		tab = layui.bodyTab({
			openTabNum : "50",  //最大可打开窗口数量
			//url : path+"/resources/frame/base/json/navs.json" //获取菜单json地址
			url : path+'/sys/menu/querySysMenu',
		});
	function initModule(){
		$('#indexa').attr('data-url',moduleIndex);
		$('#indexIframe').attr('src',moduleIndex);
		$.get(path+'/sys/module/querySysModule',{},function(data){
	    	var pcArr = new Array();
	    	var mbArr = new Array();
	    	$.each(data, function (i, e) {
	    		if(e['spread'] == true || e['spread'] == 'true'){
	    			pcArr.push('<li class="layui-nav-item layui-this" data-menu="'+e['code']+'">');
		    		mbArr.push('<dd class="layui-this" data-menu="'+e['code']+'">');
	    		}else{
	    			pcArr.push('<li class="layui-nav-item" data-menu="'+e['code']+'">');
		    		mbArr.push('<dd data-menu="'+e['code']+'">');
	    		}
	    		if(e['icon'].indexOf('&#xe')!=-1){
	    			pcArr.push('<a href="javascript:;"><i class="layui-icon" data-icon="'+e['icon']+'">'+e['icon']+'</i><cite>'+e['title']+'</cite></a>');
	    			mbArr.push('<a href="javascript:;"><i class="layui-icon" data-icon="'+e['icon']+'">'+e['icon']+'</i><cite>'+e['title']+'</cite></a>');
	    		}else{
	    			pcArr.push('<a href="javascript:;"><i class="iconfont '+e['icon']+'" data-icon="'+e['icon']+'"></i><cite>'+e['title']+'</cite></a>');
	    			mbArr.push('<a href="javascript:;"><i class="iconfont '+e['icon']+'" data-icon="'+e['icon']+'"></i><cite>'+e['title']+'</cite></a>');
	    		}
	    		pcArr.push('</li>');
	    		mbArr.push('</dd>');
	    	});
	    	$('#topLevelMenus').html(pcArr.join(''));
	    	$('#mobileTopLevelMenus').html(mbArr.join(''));
	    	
	    	//页面加载时判断左侧菜单是否显示
			//通过顶部菜单获取左侧菜单
			$(".topLevelMenus li,.mobileTopLevelMenus dd").click(function(){
				if($(this).parents(".mobileTopLevelMenus").length != "0"){
					$(".topLevelMenus li").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
				}else{
					$(".mobileTopLevelMenus dd").eq($(this).index()).addClass("layui-this").siblings().removeClass("layui-this");
				}
				$(".layui-layout-admin").removeClass("showMenu");
				$("body").addClass("site-mobile");
				getData($(this).data("menu"));
				//渲染顶部窗口
				tab.tabMove();
			});
	    });
		
		
	}
	//通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
	function getData(json){
		$.getJSON(tab.tabConfig.url,{'moduleCode':json,'pid':0},function(data){
			/*if(json == "contentManagement"){
				dataStr = data.contentManagement;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "memberCenter"){
				dataStr = data.memberCenter;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "systemeSttings"){
				dataStr = data.systemeSttings;
				//重新渲染左侧菜单
				tab.render();
			}else if(json == "seraphApi"){
                dataStr = data.seraphApi;
                //重新渲染左侧菜单
                tab.render();
            }*/
			dataStr = data;
			//重新渲染左侧菜单
			tab.render();
			console.log(dataStr);
		})
	}

	//隐藏左侧导航
	$(".hideMenu").click(function(){
		if($(".topLevelMenus li.layui-this a").data("url")){
			layer.msg("此栏目状态下左侧菜单不可展开");  //主要为了避免左侧显示的内容与顶部菜单不匹配
			return false;
		}
		$(".layui-layout-admin").toggleClass("showMenu");
		//渲染顶部窗口
		tab.tabMove();
	})
	
	//初始化顶部模块
	initModule();
	
	//通过顶部菜单获取左侧二三级菜单   注：此处只做演示之用，实际开发中通过接口传参的方式获取导航数据
	getData("contentManagement");

	//手机设备的简单适配
    $('.site-tree-mobile').on('click', function(){
		$('body').addClass('site-mobile');
	});
    $('.site-mobile-shade').on('click', function(){
		$('body').removeClass('site-mobile');
	});

	// 添加新窗口
	$("body").on("click",".layui-nav .layui-nav-item a:not('.mobileTopLevelMenus .layui-nav-item a')",function(){
		//如果不存在子级
		if($(this).siblings().length == 0){
			addTab($(this));
			$('body').removeClass('site-mobile');  //移动端点击菜单关闭菜单层
		}
		$(this).parent("li").siblings().removeClass("layui-nav-itemed");
	});
	
	//清除缓存
	$(".clearCache").click(function(){
		window.sessionStorage.clear();
        window.localStorage.clear();
        var index = layer.msg('清除缓存中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("缓存清除成功！");
        },1000);
    });

	//清除缓存
	$(".fullScreen").click(function(){
		if (isFullScreen) {
            exitFullScreen();
            isFullScreen = false;
        } else {
            setFullScreen();
            isFullScreen = true;
        }
    });
	
	//刷新后还原打开的窗口
    if(cacheStr == "true") {
        if (window.sessionStorage.getItem("menu") != null) {
            menu = JSON.parse(window.sessionStorage.getItem("menu"));
            curmenu = window.sessionStorage.getItem("curmenu");
            var openTitle = '';
            for (var i = 0; i < menu.length; i++) {
                openTitle = '';
                if (menu[i].icon) {
                    if (menu[i].icon.split("-")[0] == 'icon') {
                        openTitle += '<i class="seraph ' + menu[i].icon + '"></i>';
                    } else {
                        openTitle += '<i class="layui-icon">' + menu[i].icon + '</i>';
                    }
                }
                openTitle += '<cite>' + menu[i].title + '</cite>';
                openTitle += '<i class="layui-icon layui-unselect layui-tab-close" data-id="' + menu[i].layId + '">&#x1006;</i>';
                element.tabAdd("bodyTab", {
                    title: openTitle,
                    content: "<iframe src='" + menu[i].href + "' data-id='" + menu[i].layId + "'></frame>",
                    id: menu[i].layId
                })
                //定位到刷新前的窗口
                if (curmenu != "undefined") {
                    if (curmenu == '' || curmenu == "null") {  //定位到后台首页
                        element.tabChange("bodyTab", '');
                    } else if (JSON.parse(curmenu).title == menu[i].title) {  //定位到刷新前的页面
                        element.tabChange("bodyTab", menu[i].layId);
                    }
                } else {
                    element.tabChange("bodyTab", menu[menu.length - 1].layId);
                }
            }
            //渲染顶部窗口
            tab.tabMove();
        }
    }else{
		window.sessionStorage.removeItem("menu");
		window.sessionStorage.removeItem("curmenu");
	}
})

//打开新窗口
function addTab(_this){
	tab.tabAdd(_this);
}

//捐赠弹窗
function donation(){
	layer.tab({
		area : ['260px', '367px'],
		tab : [{
			title : "微信",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img src='images/wechat.jpg'></div>"
		},{
			title : "支付宝",
			content : "<div style='padding:30px;overflow:hidden;background:#d2d0d0;'><img src='images/alipay.jpg'></div>"
		}]
	})
}

//图片管理弹窗
function showImg(){
    $.getJSON(path+'/resources/frame/base/json/images.json', function(json){
        var res = json;
        layer.photos({
            photos: res,
            anim: 5
        });
    });
}

function getRootPath_web() {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName + '/');
}

var setFullScreen = function () {
    var docEle = document.documentElement;
    if (docEle.requestFullscreen) {
        //W3C
        docEle.requestFullscreen();
    } else if (docEle.mozRequestFullScreen) {
        //FireFox
        docEle.mozRequestFullScreen();
    } else if (docEle.webkitRequestFullScreen) {
        //Chrome等
        docEle.webkitRequestFullScreen();
    } else if (docEle.msRequestFullscreen) {
        //IE11
        docEle.msRequestFullscreen();
    } else {
        $.iMessager.alert('温馨提示', '该浏览器不支持全屏', 'messager-warning');
    }
};

//退出全屏 判断浏览器种类
var exitFullScreen = function () {
    // 判断各种浏览器，找到正确的方法
    var exitMethod = document.exitFullscreen || //W3C
        document.mozCancelFullScreen ||    //Chrome等
        document.webkitExitFullscreen || //FireFox
        document.msExitFullscreen; //IE11
    if (exitMethod) {
        exitMethod.call(document);
    }
    else if (typeof window.ActiveXObject !== "undefined") {//for Internet Explorer
        var wscript = new ActiveXObject("WScript.Shell");
        if (wscript !== null) {
            wscript.SendKeys("{F11}");
        }
    }
};