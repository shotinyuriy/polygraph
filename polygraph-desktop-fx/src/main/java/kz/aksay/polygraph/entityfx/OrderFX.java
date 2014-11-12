package kz.aksay.polygraph.entityfx;

import java.util.ArrayList;
import java.util.List;

import kz.aksay.polygraph.entity.Customer;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Organization;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.util.FormatUtil;

public class OrderFX {

	private Order order;
	private EmployeeFX currentExecutorFX;

	public static List<OrderFX> convertListEntityToFX(List<Order> orders) {
		 List<OrderFX> ordersFX = new ArrayList<>();
		 for(Order order : orders) {
			 ordersFX.add(new OrderFX(order));
		 }
		 return ordersFX;
	}
	
	public OrderFX(Order order) {
		this.order = order;
		currentExecutorFX = new EmployeeFX(order.getCurrentExecutor());
	}
	
	public Order getOrder() {
		return order;
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateTimeFormatter.format(order.getCreatedAt());
	}
	
	public String getCustomerFullName() {
		Customer customer = order.getCustomer();
		
		if( customer != null ) {
			return customer.getFullName();
		}
		
		return null;
	}
	
	public EmployeeFX getCurrentExecutorFX() {
		return currentExecutorFX;
	}
	
	public String getDescription() {
		return order.getDescription();
	}

	public Long getCustomerId() {
		Customer customer = order.getCustomer();
		if(customer != null) {
			return customer.getId();
		}
		return null;
	}
	
	public String getCurrentExecutorDescription() {
		return currentExecutorFX.toString();
	}
}
