package kz.aksay.xml1c;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import kz.aksay.xml1c.entity.XMLMaterialConsumptionContainer;
import kz.aksay.xml1c.entity.XMLOrderContainer;

public class MaterialConsumptionToXMLDriver {

	public void exportToXML(XMLMaterialConsumptionContainer materialConsumptionContainer, File file) throws JAXBException {
		Marshaller marshaller = (JAXBContext.newInstance(
				XMLMaterialConsumptionContainer.class)).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(materialConsumptionContainer, file);
	}
}
