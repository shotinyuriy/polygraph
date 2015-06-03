package kz.aksay.polygraph.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "organization", uniqueConstraints={@UniqueConstraint(name="UNQ_ORG_NUMBER", columnNames={"inn","kpp"})})
public class Organization extends Subject {

	public static Organization FIRMA_SERVER_PLUS;
	private static final long serialVersionUID = 4073893187869163774L;

	@Column
	private String code1c;
	
	@NotNull(message="Не указано полное наименование организации")
	@Size(min=2, max=512)
	@Column(nullable=false)
	private String fullname;

	@Size(min=2, max=255)
	@Column(nullable=false)
	private String shortname;
	
	@NotNull(message="Не указан ИИН или БИН организации")
	@Size(min=9, max=15)
	@Column(nullable=false)
	private String inn;
	
	@Column
	private String kpp;
	
	@Size(min=1, max=50)
	@Column
	private String directorName;
	
	@NotNull(message="Не указан адрес организации")
	@Embedded
	private Address address;
	
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

	/**
	 * INN or BIN
	 * @return
	 */
	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * KPP or IIK
	 * @return
	 */
	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getDirectorName() {
		return directorName;
	}

	public void setDirectorName(String directorName) {
		this.directorName = directorName;
	}

	public String getCode1c() {
		return code1c;
	}

	public void setCode1c(String code1c) {
		this.code1c = code1c;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
