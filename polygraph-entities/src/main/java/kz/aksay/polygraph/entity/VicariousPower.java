package kz.aksay.polygraph.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="vicarious_power")
public class VicariousPower extends EntitySupport {

	@NotNull
	@ManyToOne
	@JoinColumn(name="organization_id")
	private Organization organization;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	private Person person;
	
	@Column
	private String personName;
	
	@Column
	private String number;
	
	@Column(name="begin_date")
	private Date beginDate;
	
	@Column(name="end_date")
	private Date endDate;

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
}
