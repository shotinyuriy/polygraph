package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.EmployeeType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeTypeService extends GenericService<EmployeeType, Long> {
	private GenericDao<EmployeeType, Long> employeeTypeDao;
	
	public EmployeeType findByName(String name) {
		EmployeeType employeeType = null;
		
		if(name != null && !name.isEmpty()) {
			Criteria criteria = getDao().getSession().createCriteria(getDao().clazz());
			criteria.add(Restrictions.like("name", name));
			employeeType = getDao().readUniqueByCriteria(criteria);
		}
		
		return employeeType;
	}

	@Autowired
	public void setEmployeeTypeDao(GenericDao<EmployeeType, Long> employeeTypeDao) {
		this.employeeTypeDao = employeeTypeDao;
	}

	@Override
	protected GenericDao<EmployeeType, Long> getDao() {
		return employeeTypeDao;
	}
}
