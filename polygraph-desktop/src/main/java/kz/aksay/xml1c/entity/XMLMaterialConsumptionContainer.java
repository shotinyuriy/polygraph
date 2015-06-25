package kz.aksay.xml1c.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ВыгрузкаРасходаМатериалов")
public class XMLMaterialConsumptionContainer {
	
	
	XMLMaterialConsumptionList xmlMaterialConsumptionList;

	@XmlElement(name="РасходМатериалов")
	public XMLMaterialConsumptionList getXmlMaterialConsumptionList() {
		return xmlMaterialConsumptionList;
	}

	public void setXmlMaterialConsumptionList(
			XMLMaterialConsumptionList xmlMaterialConsumptionList) {
		this.xmlMaterialConsumptionList = xmlMaterialConsumptionList;
	}
}
