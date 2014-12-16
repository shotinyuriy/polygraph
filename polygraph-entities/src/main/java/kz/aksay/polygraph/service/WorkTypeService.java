package kz.aksay.polygraph.service;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.MaterialType;
import kz.aksay.polygraph.entity.WorkType;

@Service
public class WorkTypeService extends GenericService<WorkType, Long> {

	private GenericDao<WorkType, Long> workTypeDao;
	
	public WorkType findByName(String workTypeName) {
		WorkType workType = null;
		
		if(workTypeName != null && !workTypeName.isEmpty()) {
			Criteria findbyName = getDao().getSession().createCriteria(getDao().clazz());
			findbyName.add(Restrictions.ilike("name", workTypeName));
			workType = getDao().readUniqueByCriteria(findbyName);
		}
		
		return workType;
	}
	
	@Override
	protected GenericDao<WorkType, Long> getDao() {
		return workTypeDao;
	}
	
	@Autowired
	public void setWorkTypeDao(GenericDao<WorkType, Long> workTypeDao) {
		this.workTypeDao = workTypeDao;
	}

}
