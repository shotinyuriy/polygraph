package kz.aksay.polygraph.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4348411584806091385L;

	@Column(name = "first_name", nullable= false)
	private String firstName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "birth_date")
	private Date birthDate;
	
	@Override
	public String getFullName() {
		StringBuffer fullName = new StringBuffer();
		fullName.append(lastName);
		if(firstName != null) {
			fullName.append(" ").append(firstName);
		}
		if(middleName != null) {
			fullName.append(" ").append(middleName);
		}
		return fullName.toString();
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
}
