package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.Employee;

public class EmployeeFX {

	private Employee employee;
	private PersonFX personFX;
	
	public static List<EmployeeFX> contvertListEntityToFX(List<Employee> employees) {
		List<EmployeeFX> employeesFX = new LinkedList<>();
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
	
	public Long getId() {
		return employee.getId();
	}
	
	public String getTypeName() {
		return employee.getType().getName();
	}
	
	public String getFullName() {
		return personFX.getFullName();
	}
	
	public String getBirthDateString() {
		return personFX.getBirthDateString();
	}
}
