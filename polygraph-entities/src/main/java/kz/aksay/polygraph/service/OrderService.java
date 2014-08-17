package kz.aksay.polygraph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Order;

@Service
public class OrderService extends GenericService<Order, Long> {

	private GenericDao<Order, Long> orderDao;
	
	@Override
	protected GenericDao<Order, Long> getDao() {
		return orderDao;
	}
	
	@Autowired
	public void setOrderDao(GenericDao<Order, Long> orderDao) {
		this.orderDao = orderDao;
	}

}
