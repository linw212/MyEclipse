package config;

import java.util.List;

import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.profound.common.annotations.BackRoute;
import com.profound.common.kit.ClassKit;
import com.profound.common.kit.ClassSearcher;
import com.profound.system.interceptor.LoginInterceptor;

public class BackRoutes extends Routes{

	private static final Log LOG = Log.getLog(BackRoutes.class);
	
	@Override
	public void config() {
		// TODO Auto-generated method stub
		List<Class<? extends Controller>> controllerList = ClassSearcher.of(Controller.class).search();
		for(Class<? extends Controller> clazz : controllerList){
			if(ClassKit.hasClassAnnotation(clazz, BackRoute.class)){
				String routeKey = ClassKit.getClassAnnotation(clazz, BackRoute.class).value();
				String viewPath = ClassKit.getClassAnnotation(clazz, BackRoute.class).viewPath();
				this.add(routeKey, clazz, ClassKit.getClassAnnotation(clazz, BackRoute.class).viewPath());
				LOG.info("【ControllerKey】"+routeKey+",【Controller】"+clazz.getName()+",【viewPath】"+viewPath);
			}
		}
		//后台添加登陆拦截
		this.addInterceptor(new LoginInterceptor());
	}

}
