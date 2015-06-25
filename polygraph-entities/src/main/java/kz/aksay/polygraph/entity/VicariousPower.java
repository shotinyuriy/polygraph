package kz.aksay.polygraph.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import kz.aksay.polygraph.validation.Comparison;
import kz.aksay.polygraph.validation.DateEquation;
import kz.aksay.polygraph.validation.DateEquations;

import org.hibernate.annotations.ForeignKey;

@Entity
@Table(name="vicarious_power")
@DateEquations(dateEquations={
		@DateEquation(comparison=Comparison.GREATER_THAN, date1Name="endDate", date2Name="beginDate", 
				message="Дата окончания доверенности должна быть больше даты начала ее действия")
	})
public class VicariousPower extends EntitySupport {

	@NotNull
	@ManyToOne
	@ForeignKey(name="FK_VICARIOUS_POWER_ORGANIZATION")
	@JoinColumn(name="organization_id")
	private Organization organization;
	
	@ManyToOne
	@ForeignKey(name="FK_VICARIOUS_POWER_PERSON")
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
