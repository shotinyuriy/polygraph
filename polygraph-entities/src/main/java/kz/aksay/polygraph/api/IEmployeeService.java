package kz.aksay.polygraph.api;

import java.util.List;
import java.util.Map;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.User;
import kz.aksay.polygraph.entity.report.EmployeeWorkloadReport;

public interface IEmployeeService extends IGenericService<Employee, Long> {
	
	
	public Employee checkPersonAndSave(Employee employee) throws Exception;
	
	public void checkPersonAndUserAndSave(User user) throws Exception;
	
	public List<Employee> findAllByUserRole(User.Role role);
	
	public Map<Employee, EmployeeWorkloadReport> getEmployeesWorkload();
	
	public List<Employee> findAllByName(String name);
}
