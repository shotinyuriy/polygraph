package kz.aksay.polygraph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.api.IBindingSpringService;
import kz.aksay.polygraph.dao.GenericDao;
import  kz.aksay.polygraph.entity.BindingSpring;

@Service
public class BindingSpringService extends AbstractGenericService<BindingSpring, Long>
		implements IBindingSpringService {

	private GenericDao<BindingSpring, Long> bindingSpringDao;

	@Override
	protected GenericDao<BindingSpring, Long> getDao() {
		return bindingSpringDao;
	}
	
	@Autowired
	public void setBindingSpringDao(GenericDao<BindingSpring, Long> bindingSpringDao) {
		this.bindingSpringDao = bindingSpringDao;
	}

}
