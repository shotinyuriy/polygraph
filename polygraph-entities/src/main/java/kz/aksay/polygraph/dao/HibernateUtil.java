package kz.aksay.polygraph.dao;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.StandardServiceInitiators;

public class HibernateUtil {

	private static SessionFactory sessionFactory = createSessionFactory();
	private static ServiceRegistry serviceRegistry;
	
	public static SessionFactory createSessionFactory() {
		File file = new File("src/main/resources/META-INF/hibernate.cfg.xml");
		Configuration configuration = new Configuration().configure(file);
		
		serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings( configuration.getProperties() )
				.build();
		
		return configuration.buildSessionFactory(serviceRegistry);
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}