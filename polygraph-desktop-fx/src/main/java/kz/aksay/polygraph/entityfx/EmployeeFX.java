package kz.aksay.polygraph.entityfx;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.Employee;

public class EmployeeFX {

	public static final EmployeeFX ALL_EMPLOYEES;
	private Employee employee;
	private PersonFX personFX;
	
	static {  
		ALL_EMPLOYEES = new EmployeeFX(null);
	}
	
	public static Collection<EmployeeFX> contvertListEntityToFX(Collection<Employee> employees) {
		Collection<EmployeeFX> employeesFX = new LinkedList<>();
		for(Employee employee : employees) {
			employeesFX.add(new EmployeeFX(employee));
		}
		return employeesFX;
	}
	
	public EmployeeFX(Employee employee) {
		this.employee = employee;
		if(employee != null) {
			this.personFX = new PersonFX(employee.getPerson());
		}
	}
	
	public Employee getEmployee() {
		return employee;
	}
	
	public Long getId() {
		return employee.getId();
	}
	
	public String getTypeName() {
		if(employee != null && employee.getUser() != null && employee.getUser().getRole() != null) {
			return employee.getUser().getRole().getName();
		}
		return null;
	}
	
	public String getFullName() {
		return personFX.getFullName();
	}
	
	public String getBirthDateString() {
		return personFX.getBirthDateString();
	}
	
	public String toString() {
		if(employee == null) return "Все";
		return personFX.getFullName()+" ("+this.getTypeName()+")";
	}
}
