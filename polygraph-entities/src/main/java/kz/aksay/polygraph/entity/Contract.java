package kz.aksay.polygraph.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="contract")
public class Contract extends EntitySupport {

	@NotNull
	@ManyToOne
	@JoinColumn(name="party1_id")
	private Organization party1;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="party2_id")
	private Organization party2;
	
	@NotNull
	@Column
	private String number;
	
	@NotNull
	@Column(name="begin_date")
	private Date beginDate;
	
	@NotNull
	@Column(name="end_date")
	private Date endDate;

	public Organization getParty1() {
		return party1;
	}

	public void setParty1(Organization party1) {
		this.party1 = party1;
	}

	public Organization getParty2() {
		return party2;
	}

	public void setParty2(Organization party2) {
		this.party2 = party2;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
