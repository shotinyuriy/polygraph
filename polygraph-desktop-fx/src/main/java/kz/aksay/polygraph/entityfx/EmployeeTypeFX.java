package kz.aksay.polygraph.entityfx;

import java.util.LinkedList;
import java.util.List;

import kz.aksay.polygraph.entity.EmployeeType;
import kz.aksay.polygraph.util.FormatUtil;

public class EmployeeTypeFX {

	private final EmployeeType employeeType;
	
	public static List<EmployeeTypeFX> contvertListEntityToFX(List<EmployeeType> employeeTypes) {
		List<EmployeeTypeFX> employeeTypesFX = new LinkedList<>();
		for(EmployeeType employeeType : employeeTypes) {
			employeeTypesFX.add(new EmployeeTypeFX(employeeType));
		}
		return employeeTypesFX;
	}
	
	public EmployeeTypeFX(EmployeeType employeeType) {
		this.employeeType = employeeType;
	}
	
	public EmployeeType getEmployeeType() {
		return employeeType;
	}
	
	public String getName() {
		return employeeType.getName();
	}
	
	public String getCreatedByLogin() {
		return employeeType.getCreatedBy().getLogin();
	}
	
	public String getCreatedAtString() {
		return FormatUtil.dateFormatter.format(employeeType.getCreatedAt());
	}
	
	public String toString() {
		return employeeType.getName();
	}
}
