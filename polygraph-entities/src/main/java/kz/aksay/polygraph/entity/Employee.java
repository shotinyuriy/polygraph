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
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if(! (obj instanceof Employee) ) return false;
		Employee other = (Employee) obj;
		if(other.getId() != null) {
			if(this.id == null) return false;
			return other.getId().equals(this.id);
		} else {
			if(this.id == null) return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		if(id != null)
			return id.hashCode();
		return -1;
	}
	
}
