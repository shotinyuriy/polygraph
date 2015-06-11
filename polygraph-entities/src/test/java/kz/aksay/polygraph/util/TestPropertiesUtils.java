package kz.aksay.polygraph.util;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPropertiesUtils extends Assert {
	
	@Test
	public void testLoadProperties() {
		String fileName = "META-INF/properties/test";
		Properties properties = PropertiesUtils.getCurrentDatabaseProperties(fileName);
		assertNotNull(properties);
		assertEquals(properties.size(), 2, "properties size must be 10");
	}
	
	@Test
	public void testWriteProperties() {
		String fileName = "META-INF/properties/test";
		Properties properties = new Properties();
		properties.setProperty("username", "test");
		properties.setProperty("password", "pwd");
		PropertiesUtils.writeDateBaseProperties(properties, fileName);
	}

}
