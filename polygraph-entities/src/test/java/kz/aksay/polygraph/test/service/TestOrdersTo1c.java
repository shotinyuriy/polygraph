package kz.aksay.polygraph.test.service;

import kz.aksay.polygraph.integration1c.OrderToXMLExporter;
import kz.aksay.polygraph.test.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class TestOrdersTo1c extends Assert {

	private ApplicationContext context = ContextUtils.getApplicationContext();
	
	private OrderToXMLExporter orderToXMLExporter;
	
	@BeforeClass
	public void setUp() {
		orderToXMLExporter = context.getBean(OrderToXMLExporter.class);
	}
	
	@Test
	public void  testDependencyInjection() {
		assertNotNull(orderToXMLExporter);
	}
	
}
