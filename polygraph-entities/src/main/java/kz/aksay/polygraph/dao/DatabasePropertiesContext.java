package kz.aksay.polygraph.dao;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabasePropertiesContext {

	
	public PropertyPlaceholderConfigurer properties() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		return ppc;
	}
}
