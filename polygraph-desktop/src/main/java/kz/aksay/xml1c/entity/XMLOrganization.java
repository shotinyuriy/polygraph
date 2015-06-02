package kz.aksay.xml1c.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Организация")
public class XMLOrganization {
	
	private String code;
	
	private String inn;
	
	private String name;
	
	private XMLAddress address;

	@XmlElement(name="Code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name="ИИН_БИН")
	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	@XmlElement(name="Наименование")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name="Адрес")
	public XMLAddress getAddress() {
		return address;
	}

	public void setAddress(XMLAddress address) {
		this.address = address;
	}
}
