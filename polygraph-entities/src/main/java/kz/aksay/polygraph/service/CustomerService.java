package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.ICustomerService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AbstractGenericService<Subject, Long> implements ICustomerService {
	
	private GenericDao<Subject, Long> customerDao;
	
	public GenericDao<Subject, Long> getDao() {
		return customerDao;
	}
	
	@Autowired
	public void setCustomerDao(GenericDao<Subject, Long> customerDao) {
		this.customerDao = customerDao;
	}
}
