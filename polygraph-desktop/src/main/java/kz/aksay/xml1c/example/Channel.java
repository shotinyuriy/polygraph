package kz.aksay.xml1c.example;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Channel")
public class Channel {
	private String name;

	public Channel() {
		
	}
	
	public Channel(String name) {
		super();
		this.name = name;
	}

	@XmlElement(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
