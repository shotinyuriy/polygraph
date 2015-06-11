package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.ILaminateService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Laminate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaminateService extends AbstractGenericService<Laminate, Long> implements
		ILaminateService {

	private GenericDao<Laminate, Long> laminateDao;

	@Override
	protected GenericDao<Laminate, Long> getDao() {
		return this.laminateDao;
	}
	
	@Autowired
	public void setLaminateDao(GenericDao<Laminate, Long> laminateDao) {
		this.laminateDao = laminateDao;
	}

	
}
