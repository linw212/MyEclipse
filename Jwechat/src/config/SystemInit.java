package config;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.ActionReporter;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.druid.DruidStatViewHandler;
import com.jfinal.plugin.druid.IDruidStatViewAuth;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.cache.RedisAccessTokenCache;
import com.profound.common.kit.DateKit;
import com.profound.common.plugin.tablebind.AutoTableBindPlugin;
import com.profound.weixin.model.AccountConfigBase;

public class SystemInit extends JFinalConfig {
	private static final Log LOG = Log.getLog(JFinalConfig.class);
	private static Prop config = PropKit.use("config/init.properties");
    
	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(config.getBoolean("sys.devMode"));
		me.setError404View("/WEB-INF/common/404.jsp");
		me.setError500View("/WEB-INF/common/500Error.jsp");
		me.setViewType(ViewType.JSP);
		ActionReporter.setReportAfterInvocation(false);
		ApiConfigKit.setDevMode(me.getDevMode());
		me.setBaseUploadPath(config.get("uploadPath"));//文件上传的跟目录
		me.setLogFactory(new Log4jLogFactory());
		me.setJsonFactory(new MixedJsonFactory());
	}

	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		LOG.info("JFinal configRoute report ======== "+DateKit.getStringDate()+" ==============================");
		LOG.info("开始加载后台路由【BackRoutes】。。。。。。");
		me.add(new BackRoutes());
		LOG.info("开始加载前台路由【ForeRoutes】。。。。。。");
		me.add(new ForeRoutes());
		LOG.info("=====================================================================================");
	}

	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		/*本地系统连接*/
		DruidPlugin dp=new DruidPlugin(config.get("db.url"), config.get("db.username"),config.get("db.password"),config.get("db.driverClassName"),"stat");
		me.add(dp);
		AutoTableBindPlugin atbp=new AutoTableBindPlugin(dp);//model自动绑定插件
		atbp.setShowSql(true);
		atbp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//设置自带明大小写不敏感,同时列名转小写
		atbp.setDevMode(PropKit.getBoolean("sys.devMode"));//设为开发模式
		atbp.setDialect(new MysqlDialect());//设定方言为mysql
		atbp.autoScan(false);//不自动扫描jar包
		me.add(atbp);//添加modelBean自动绑定插件
		me.add(new EhCachePlugin());//添加缓存插件
		RedisPlugin redisPlugin = new RedisPlugin("weixin","192.168.136.132",6379, "linwan");
		me.add(redisPlugin);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		me.add(new ContextPathHandler("path"));
		/*添加druid监控*/
		if(config.getBoolean("sys.devMode"))
		{
			me.add(new DruidStatViewHandler("/druid",new IDruidStatViewAuth(){
			    public boolean isPermitted(HttpServletRequest request) {
			        return true;
			      }
			}));
		}
	}
	@Override
	public void afterJFinalStart() {
	    /**
	     * 多个公众号时，重复调用ApiConfigKit.putApiConfig(ac)依次添加即可，第一个添加的是默认。
	     */
		//支持redis存储access_token、js_ticket，需要先启动RedisPlugin
		ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));
		ApiConfigKit.putApiConfig(AccountConfigBase.dao.getUsingConfig());
		//ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));
		//AccountConfigKit.setAccountConfigCache(new RedisConfigCache("weixin","wx_acount_base:"));
	    
	    
	    /**
	     * 1.9 新增LocalTestTokenCache用于本地和线上同时使用一套appId时避免本地将线上AccessToken冲掉
	     * 
	     * 设计初衷：https://www.oschina.net/question/2702126_2237352
	     * 
	     * 注意：
	     * 1. 上线时应保证此处isLocalDev为false，或者注释掉该不分代码！
	     * 
	     * 2. 为了安全起见，此处可以自己添加密钥之类的参数，例如：
	     * http://localhost/weixin/api/getToken?secret=xxxx
	     * 然后在WeixinApiController#getToken()方法中判断secret
	     * 
	     * @see WeixinApiController#getToken()
	     */
//	     if (isLocalDev) {
//	     	String onLineTokenUrl = "http://localhost/weixin/api/getToken";
//	        ApiConfigKit.setAccessTokenCache(new LocalTestTokenCache(onLineTokenUrl));
//	     }
//	     WxaConfig wc = new WxaConfig();
//	     wc.setAppId("wx4f53594f9a6b3dcb");
//	     wc.setAppSecret("eec6482ba3804df05bd10895bace0579");
//	     WxaConfigKit.setWxaConfig(wc);
}
}
