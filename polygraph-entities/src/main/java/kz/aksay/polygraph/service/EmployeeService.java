package kz.aksay.polygraph.service;

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
public class EmployeeService extends GenericService<Employee, Long> {
	
	private GenericDao<Employee, Long> employeeDao;
	private PersonService personService;
	private UserService userService;

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

	public PersonService getPersonService() {
		return personService;
	}

	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public UserService getUserService() {
		return userService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	 
}
