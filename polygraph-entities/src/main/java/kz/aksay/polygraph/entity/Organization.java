package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "organization")
public class Organization extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4073893187869163774L;

	@Size(min=2, max=512)
	@Column(nullable=false)
	private String fullname;

	@Size(min=2, max=255)
	@Column(nullable=false)
	private String shortname;

	@Size(min=10, max=14)
	@Column(nullable=false)
	private String inn;
	
	@Size(min=9, max=14)
	@Column(nullable=false)
	private String kpp;

	public String getFullname() {
		return fullname;
	}
	
	@Override
	public String getFullName() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}
}
