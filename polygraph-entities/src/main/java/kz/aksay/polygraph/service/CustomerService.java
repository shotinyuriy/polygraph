package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends GenericService<Customer, Long> {
	
	private GenericDao<Customer, Long> customerDao;
	
	public GenericDao<Customer, Long> getDao() {
		return customerDao;
	}
	
	@Autowired
	public void setCustomerDao(GenericDao<Customer, Long> customerDao) {
		this.customerDao = customerDao;
	}
}
