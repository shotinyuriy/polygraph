package kz.aksay.polygraph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kz.aksay.polygraph.entity.ProducedWork;
import kz.aksay.polygraph.dao.GenericDao;

@Service
public class ProducedWorkService extends GenericService<ProducedWork, Long> {
	
	private GenericDao<ProducedWork, Long> producedWorkDao;

	@Override
	protected GenericDao<ProducedWork, Long> getDao() {
		return producedWorkDao;
	}

	@Autowired
	public void setProducedWorkDao(GenericDao<ProducedWork, Long> producedWorkDao) {
		this.producedWorkDao = producedWorkDao;
	}
}
