package kz.aksay.polygraph.service;

import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService extends GenericService<Employee, Long>{
	
	private GenericDao<Employee, Long> employeeDao;
	private PersonService personService;

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

	public PersonService getPersonService() {
		return personService;
	}

	@Autowired
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
}
