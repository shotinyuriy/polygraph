package kz.aksay.polygraph.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContextUtils {
	private static ApplicationContext context;
	
	public static synchronized ApplicationContext getApplicationContext() {
		if(context == null) {
			context = new ClassPathXmlApplicationContext(
					"classpath*:/META-INF/application-context.xml");
		}
		return context;
	}
}
