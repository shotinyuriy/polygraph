package kz.aksay.polygraph.service;

import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.User;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService extends AbstractGenericService<Employee, Long> implements IEmployeeService {
	
	private GenericDao<Employee, Long> employeeDao;
	private IPersonService personService;
	private IUserService userService;

	@Autowired
	public void setEmployeeDao(GenericDao<Employee, Long> employeeDao) {
		this.employeeDao = employeeDao;
	}

	@Override
	protected GenericDao<Employee, Long> getDao() {
		return employeeDao;
	}

	@Transactional
	public Employee checkPersonAndSave(Employee employee) throws Exception {
		
		Person similarPerson = personService.findSimilarPerson(employee.getPerson());
		if(similarPerson != null) {
			employee.setPerson(similarPerson);
		}
		else {
			personService.save(employee.getPerson());
		}
		
		return save(employee);
	}
	
	@Transactional
	public void checkPersonAndUserAndSave(User user) throws Exception {
		Employee employee = checkPersonAndSave(user.getEmployee());
		if(employee != null) {
			userService.save(user);
		}
	}

	public IPersonService getPersonService() {
		return personService;
	}

	@Autowired
	public void setPersonService(IPersonService personService) {
		this.personService = personService;
	}

	public IUserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	 
}
