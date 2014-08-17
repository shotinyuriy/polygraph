package kz.aksay.polygraph.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.WorkType;

@Service
public class WorkTypeService extends GenericService<WorkType, Long> {

	private GenericDao<WorkType, Long> workTypeDao;
	
	@Override
	protected GenericDao<WorkType, Long> getDao() {
		return workTypeDao;
	}
	
	@Autowired
	public void setWorkTypeDao(GenericDao<WorkType, Long> workTypeDao) {
		this.workTypeDao = workTypeDao;
	}

}
