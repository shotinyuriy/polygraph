package kz.aksay.polygraph.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="address")
public class Address extends EntitySupport {

	private static final long serialVersionUID = 605856462137721952L;
	
	private String city;
	private String street;
	private String house;
	private String apartment;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getHouse() {
		return house;
	}
	public void setHouse(String house) {
		this.house = house;
	}
	public String getApartment() {
		return apartment;
	}
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
}
