package kz.aksay.polygraph.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee extends EntitySupport {
	
	private static final long serialVersionUID = -484790356346509767L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="person_id")
	private Person person;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="employee_type_id")
	private EmployeeType type;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public EmployeeType getType() {
		return type;
	}

	public void setType(EmployeeType type) {
		this.type = type;
	}
}
