package kz.aksay.xml1c.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class XMLMaterialConsumptionList {

	
	private List<XMLMaterialConsumption> xmlMaterialConsumptions = new ArrayList<XMLMaterialConsumption>();

	@XmlElement(name="РасходМатериалов")
	public List<XMLMaterialConsumption> getXmlMaterialConsumptions() {
		return xmlMaterialConsumptions;
	}

	public void setXmlMaterialConsumptions(
			List<XMLMaterialConsumption> xmlMaterialConsumptions) {
		this.xmlMaterialConsumptions = xmlMaterialConsumptions;
	}
}
