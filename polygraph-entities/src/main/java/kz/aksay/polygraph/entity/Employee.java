package kz.aksay.polygraph.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="employee")
public class Employee extends EntitySupport {
	
	private static final long serialVersionUID = -484790356346509767L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="person_id")
	private Person person;

	@OneToOne(mappedBy="employee", fetch=FetchType.EAGER)
	private User user;
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
