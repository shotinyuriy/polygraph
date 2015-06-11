package kz.aksay.polygraph.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IFullTextIndexService;
import kz.aksay.polygraph.api.IOrderFullTextIndexService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.FullTextIndex;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.OrderFullTextIndex;

@Service
public class OrderFullTextIndexService extends AbstractGenericService<OrderFullTextIndex, Long> 
	implements IOrderFullTextIndexService {

	private IFullTextIndexService fullTextIndexService;
	
	private GenericDao<OrderFullTextIndex, Long> orderFullTextIndexDao;
	
	@Override
	protected GenericDao<OrderFullTextIndex, Long> getDao() {
		return orderFullTextIndexDao;
	}
	
	@Transactional
	public void recreateOrderFullTextIndexes(Order order) throws Exception {
		deleteByOrder(order);
		List<OrderFullTextIndex> orderFullTextIndexes = 
				createOrderFullTextIndexes(order);
		saveAll(orderFullTextIndexes);
	}
	
	@Transactional
	public List<OrderFullTextIndex> createOrderFullTextIndexes(Order order) {
		List<OrderFullTextIndex> orderFullTextIndexes = new ArrayList<>();
		if(order != null) {
			Set<String> texts = new HashSet<>();
			if(order.getId() != null) {
				texts.add(order.getId().toString());
			}
			if(order.getDescription() != null) {
				texts.add(order.getDescription());
			}
			if(order.getCurrentExecutor() != null && order.getCurrentExecutor().getPerson() != null &&
					order.getCurrentExecutor().getPerson().getFullName() != null) {
				texts.add(order.getCurrentExecutor().getPerson().getFullName());
			}
			if(order.getCustomer() != null && order.getCustomer().getFullName() != null) {
				texts.add(order.getCustomer().getFullName());
			}
			
			for(String text : texts) {
				FullTextIndex fullTextIndex = fullTextIndexService.findByText(text);
				if(fullTextIndex == null) {
					fullTextIndex = new FullTextIndex();
					fullTextIndex.setText(text);
				}
				OrderFullTextIndex orderFullTextIndex = new OrderFullTextIndex();
				orderFullTextIndex.setFullTextIndex(fullTextIndex);
				orderFullTextIndex.setOrder(order);
				orderFullTextIndexes.add(orderFullTextIndex);
			}
		}
		return orderFullTextIndexes;
	}
	
	@Transactional
	public int deleteByOrder(Order order) {
		int rowsAffected = 0;
		if(order != null) {
			Query query = getDao().getSession().createQuery(
					"DELETE FROM OrderFullTextIndex WHERE order.id = :orderId");
			query.setParameter("orderId", order.getId());
			rowsAffected = query.executeUpdate();
		}
		return rowsAffected;
	}
	
/*	@Transactional
	public int deleteAll() {
		int rowsAffected = 0;
		Query query = getDao().getSession().createQuery(
				"DELETE FROM OrderFullTextIndex");
		rowsAffected = query.executeUpdate();
		return rowsAffected;
	}*/
	
	@Transactional 
	public void saveAll(List<OrderFullTextIndex> orderFullTextIndexes) throws Exception {
		for(OrderFullTextIndex orderFullTextIndex : orderFullTextIndexes) {
			FullTextIndex fullTextIndex = fullTextIndexService.save(orderFullTextIndex.getFullTextIndex());
			orderFullTextIndex.setFullTextIndex(fullTextIndex);
			save(orderFullTextIndex);
		}
	}
	
	@Autowired
	public void setOrderFullTextIndexDao(
			GenericDao<OrderFullTextIndex, Long> orderFullTextIndexDao) {
		this.orderFullTextIndexDao = orderFullTextIndexDao;
	}

	@Autowired
	public void setFullTextIndexService(
			IFullTextIndexService fullTextIndexService) {
		this.fullTextIndexService = fullTextIndexService;
	}

}
