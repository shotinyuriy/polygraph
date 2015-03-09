package kz.aksay.polygraph.api;

import java.util.List;

import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.OrderFullTextIndex;

public interface IOrderFullTextIndexService extends IGenericService<OrderFullTextIndex, Long> {
	
	public void recreateOrderFullTextIndexes(Order order) throws Exception;
	
	public List<OrderFullTextIndex> createOrderFullTextIndexes(Order order);
	
	public int deleteByOrder(Order order);
	 
	public void saveAll(List<OrderFullTextIndex> orderFullTextIndexes) throws Exception;

}
