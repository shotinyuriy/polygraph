package kz.aksay.polygraph.api;

import java.util.List;

import kz.aksay.polygraph.entity.Employee;
import kz.aksay.polygraph.entity.User;

import org.springframework.stereotype.Service;

public interface IEmployeeService extends IGenericService<Employee, Long> {
	
	
	public Employee checkPersonAndSave(Employee employee) throws Exception;
	
	public void checkPersonAndUserAndSave(User user) throws Exception;
	
	public List<Employee> findAllByUserRole(User.Role role);
}
