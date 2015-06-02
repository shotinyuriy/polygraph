package kz.aksay.xml1c.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="КлассификаторАдреса")
public class XMLAddress {

	private String city;
	
	private String street;
	
	private String house;
	
	private String apartment;

	@XmlElement(name="Город")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@XmlElement(name="Улица")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	@XmlElement(name="НомерДома")
	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	@XmlElement(name="НомерКвартиры")
	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
}
