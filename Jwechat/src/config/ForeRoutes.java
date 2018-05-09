package config;

import java.util.List;
import com.jfinal.config.Routes;
import com.jfinal.core.Controller;
import com.jfinal.log.Log;
import com.profound.common.annotations.ForeRoute;
import com.profound.common.kit.ClassKit;
import com.profound.common.kit.ClassSearcher;

public class ForeRoutes extends Routes{

	private static final Log LOG = Log.getLog(ForeRoutes.class);
	
	@Override
	public void config() {
		// TODO Auto-generated method stub
		List<Class<? extends Controller>> controllerList = ClassSearcher.of(Controller.class).search();
		for(Class<? extends Controller> clazz : controllerList){
			if(ClassKit.hasClassAnnotation(clazz, ForeRoute.class)){
				String routeKey = ClassKit.getClassAnnotation(clazz, ForeRoute.class).value();
				String viewPath = ClassKit.getClassAnnotation(clazz, ForeRoute.class).viewPath();
				this.add(routeKey, clazz, ClassKit.getClassAnnotation(clazz, ForeRoute.class).viewPath());
				LOG.info("【ControllerKey】"+routeKey+",【Controller】"+clazz.getName()+",【viewPath】"+viewPath);
			}
		}
	}

}
