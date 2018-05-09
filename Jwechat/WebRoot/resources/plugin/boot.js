(function($){
    //备份jquery的ajax方法
    var _ajax=$.ajax;
     
    //重写jquery的ajax方法
    $.ajax=function(opt){
        //备份opt中error和success方法
        var fn = {
            error:function(XMLHttpRequest, textStatus, errorThrown){},
            success:function(data){}
        }
        if(opt.error){
            fn.error=opt.error;
        }
        if(opt.success){
            fn.success=opt.success;
        }
        if(opt.loadMsg)
        {
        	opt.beforeSend=function(){_ajax_layer_index=layer.open({type: 2,shadeClose:false,content:typeof opt.loadMsg=='boolean'?'':opt.loadMsg});};
        	opt.complete=function(){layer.close(_ajax_layer_index);};
        }
        //扩展增强处理
        var _opt = $.extend(opt,{
            error:function(XMLHttpRequest, textStatus, errorThrown){
                //错误方法增强处理
            	if(XMLHttpRequest.status==601)
        		{
        			layer.open({
        				  type: 1
        				  ,content: XMLHttpRequest.responseText
        				  ,anim: 'up'
        				  ,style: 'position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;'
        				});
        			return;
        		}
        		layer.open({
					shadeClose:true,
				    title: [
				      '请求错误',
				      'background-color: #CD2626; color:#fff;'
				    ],
				    style: 'font-size:1.0em',
				    content: XMLHttpRequest.responseText
				});
        		fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success:function(data){
        		//成功回调方法增强处理
                fn.success(data);
            }
        });
        _ajax(_opt);
    };
})(jQuery);
