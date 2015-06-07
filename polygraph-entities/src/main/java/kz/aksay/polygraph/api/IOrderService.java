package kz.aksay.polygraph.api;

import java.util.Date;
import java.util.List;

import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.OrderProceedReport;

public interface IOrderService extends IGenericService<Order, Long> {

	public Order find(Long id);
	
	public Order save(Order order) throws Exception;
	
	public List<Order> findByExample(Order example);
	
	public List<Order> findByExampleAndSearchString(Order example, String searchString);

	public List<OrderProceedReport> generateProceedReport(Date dateFrom, Date dateTo);
	
}
