package kz.aksay.polygraph.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ContextUtils {
	private static ApplicationContext context;
	
	public static synchronized ApplicationContext getApplicationContext() {
		if(context == null) {
			context = new ClassPathXmlApplicationContext(
					"classpath*:/META-INF/application-context.xml");
		}
		return context;
	}
	
	public static synchronized void resetContext() {
		context = null;
	}
	
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}
	
	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}
}
