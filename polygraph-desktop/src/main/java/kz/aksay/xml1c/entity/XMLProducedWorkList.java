package kz.aksay.xml1c.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class XMLProducedWorkList {

	private List<XMLProducedWork> producedWorks;

	@XmlElement(name="ВыполненнаяРабота")
	public List<XMLProducedWork> getProducedWorks() {
		return producedWorks;
	}

	public void setProducedWorks(List<XMLProducedWork> producedWorks) {
		this.producedWorks = producedWorks;
	}
	
	
}
