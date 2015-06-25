package kz.aksay.xml1c;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import kz.aksay.xml1c.entity.XMLOrderContainer;

public class OrdersToXMLDriver {

	public void exportToXML(XMLOrderContainer orderContainer, File file) throws JAXBException {
		Marshaller marshaller = (JAXBContext.newInstance(
				XMLOrderContainer.class)).createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(orderContainer, file);
	}
}