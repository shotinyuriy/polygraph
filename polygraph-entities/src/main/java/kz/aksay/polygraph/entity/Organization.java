package kz.aksay.polygraph.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organization")
public class Organization extends Customer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4073893187869163774L;

	private String fullname;

	private String shortname;

	private String inn;
	
	private String kpp;

	public String getFullname() {
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
