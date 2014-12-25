package kz.aksay.polygraph.test.service;

import java.util.List;

import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.service.OrderService;
import kz.aksay.polygraph.test.ContextUtils;

import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestOrderService extends Assert {

	private ApplicationContext context = ContextUtils.getApplicationContext();
	
	private OrderService orderService;
	
	@BeforeClass
	public void setUp() {
		orderService = context.getBean(OrderService.class);
	}
	
	@Test
	public List<Order>  testFindAll() {
		List<Order> orders = orderService.findAll();
		return orders;
	}
}