package kz.aksay.polygraph.service;

import java.util.HashSet;
import java.util.List;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.ProducedWork;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService extends GenericService<Order, Long> {

	private GenericDao<Order, Long> orderDao;
	
	private ProducedWorkService producedWorkService;
	
	@Override
	@Transactional
	public Order find(Long id) {
		Order order = super.find(id);
		if(order != null) {
			List<ProducedWork> producedWorks = producedWorkService.findAllByOrderId(id);
			order.setProducedWorks(new HashSet<ProducedWork>());
			order.getProducedWorks().addAll(producedWorks);
		}
		return order;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Order save(Order order) throws Exception {
		order = super.save(order);
		if(order.getProducedWorks() != null) {
			for(ProducedWork producedWork : order.getProducedWorks()) {
				producedWork.setOrder(order);
				producedWorkService.save(producedWork);
			}
			for(ProducedWork producedWork : order.getProducedWorks()) {
				producedWork.setDirty(false);
			}
		}
		return order;
	}
	
	@Override
	protected GenericDao<Order, Long> getDao() {
		return orderDao;
	}
	
	@Autowired
	public void setOrderDao(GenericDao<Order, Long> orderDao) {
		this.orderDao = orderDao;
	}

	@Autowired
	public void setProducedWorkService(ProducedWorkService producedWorkService) {
		this.producedWorkService = producedWorkService;
	}
}
