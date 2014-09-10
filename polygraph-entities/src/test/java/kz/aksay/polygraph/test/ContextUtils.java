package kz.aksay.polygraph.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class ContextUtils {
	private static ApplicationContext context;
	
	public static synchronized ApplicationContext getApplicationContext() {
		if(context == null) {
			context = new FileSystemXmlApplicationContext(
					"src/test/resources/META-INF/test-application-context.xml");
			//context = kz.aksay.polygraph.util.ContextUtils.getApplicationContext();
		}
		return context;
	}
}
