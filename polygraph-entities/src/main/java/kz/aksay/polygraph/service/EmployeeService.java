package kz.aksay.polygraph.service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kz.aksay.polygraph.api.IEmployeeService;
import kz.aksay.polygraph.api.IPersonService;
import kz.aksay.polygraph.api.IUserService;
import kz.aksay.polygraph.dao.GenericDao;
import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.Order;
import kz.aksay.polygraph.entity.Person;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.User.Role;
import kz.aksay.polygraph.entity.report.EmployeeWorkloadReport;

import org.hibernate.Query;
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
	

	@Override
	@Transactional(readOnly=true)
	public Map<Employee, EmployeeWorkloadReport> getEmployeesWorkload() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT new EmployeeWorkloadReport(");
		sb.append(" o.id, e , o.complexity , o.dateEndPlan, o.state ) ");
		sb.append(" FROM Order o ");
		sb.append(" RIGHT OUTER JOIN o.currentExecutor as e ");
		sb.append(" WHERE o.state in (:states) ");
		sb.append(" AND e.user.role = :userRole ");
		
		Set<Order.State> states = new HashSet<Order.State>();
		states.add(Order.State.NEW);
		states.add(Order.State.PROCESSING);
		
		Query query = getDao().getSession().createQuery(sb.toString());
		query.setParameterList("states", states);
		query.setParameter("userRole", User.Role.DESIGNER);
		
		Date date = new Date();
		
		Map<Employee, EmployeeWorkloadReport> report = 
				new HashMap<Employee, EmployeeWorkloadReport>();
		
		List<EmployeeWorkloadReport> employees = query.list();
		for(EmployeeWorkloadReport empWlRep : employees) {
			empWlRep.calcWorkload(date);
			EmployeeWorkloadReport existentEWLR = report.get(empWlRep.getEmployee());
			if(existentEWLR != null) {
				existentEWLR.add(empWlRep);
			}
			else {
				report.put(empWlRep.getEmployee(), empWlRep);
			}
		}
		
		return report;
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

	@Override
	public List<Employee> findAllByUserRole(Role role) {
		Query query = getDao().getSession().createQuery(
				"SELECT emp FROM Employee emp WHERE emp.user.role = :userRole")
				.setParameter("userRole", role);
				
		return query.list();
	}

	 
}
