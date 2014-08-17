package kz.aksay.polygraph.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="family")
public class Family extends EntitySupport {
	
	@OneToOne(cascade=CascadeType.REFRESH)
	@JoinColumn(name="person_id")
	private Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	} 
}
