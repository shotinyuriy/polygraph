package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService extends GenericService<Employee, Long>{
	
	private GenericDao<Employee, Long> employeeDao;

	@Autowired
	public void setEmployeeDao(GenericDao<Employee, Long> employeeDao) {
		this.employeeDao = employeeDao;
	}

	@Override
	protected GenericDao<Employee, Long> getDao() {
		return employeeDao;
	}
}
