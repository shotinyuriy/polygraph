package kz.aksay.polygraph.service;

import java.util.List;

import kz.aksay.polygraph.api.IComplexityService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Complexity;

import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComplexityService extends AbstractGenericService<Complexity, Long> implements
		IComplexityService {

	private GenericDao<Complexity, Long> complexityDao;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED, readOnly=true)
	public List<Complexity> findByExample(Complexity example) {
		return findAll();
	}

	@Override
	protected GenericDao<Complexity, Long> getDao() {
		
		return this.complexityDao;
	}

	@Autowired
	public void setComplexityDao(GenericDao<Complexity, Long> complexityDao) {
		this.complexityDao = complexityDao;
	}
	
}
