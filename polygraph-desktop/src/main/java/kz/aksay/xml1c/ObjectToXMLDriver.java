package kz.aksay.xml1c;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import kz.aksay.xml1c.entity.XMLOrder;
import kz.aksay.xml1c.entity.XMLOrderContainer;
import kz.aksay.xml1c.entity.XMLOrderList;
import kz.aksay.xml1c.entity.XMLProducedWork;
import kz.aksay.xml1c.entity.XMLProducedWorkList;

import org.xml.sax.SAXException;

public class ObjectToXMLDriver {

	public static final String FILE_NAME = "orders.xml";
	public static void main(String[] args) {
		try {
			XMLOrderContainer orderContainer = new XMLOrderContainer();
			List<XMLOrder> orders = new ArrayList<XMLOrder>();
			XMLOrderList xmlOrderList = new XMLOrderList();
			XMLOrder order = new XMLOrder();
			order.setNumber("1123");
			orders.add(order);
			xmlOrderList.setOrders(orders);
			orderContainer.setOrderList(xmlOrderList);
			order.setContractNumber("123435");
			order.setVicariousPowerNumber("54321");
			XMLProducedWorkList producedWorkList = new XMLProducedWorkList();
			List<XMLProducedWork> producedWorks = new ArrayList<XMLProducedWork>();
			XMLProducedWork producedWork = new XMLProducedWork();
			producedWork.setCode("000123");
			producedWork.setName("Черно-белая печать");
			producedWork.setQuantity(2);
			producedWork.setPrice(120.0);
			producedWork.setCost(240.0);
			producedWorks.add(producedWork);
			XMLProducedWork producedWork2 = new XMLProducedWork();
			producedWork2.setCode("000124");
			producedWork2.setName("Цветная печать");
			producedWork2.setQuantity(3);
			producedWork2.setPrice(220.0);
			producedWork2.setCost(660.0);
			producedWorks.add(producedWork2);
			producedWorkList.setProducedWorks(producedWorks);
			order.setProducedWorkList(producedWorkList);
			
			(JAXBContext.newInstance(XMLOrderContainer.class))
				.generateSchema(new Schema1cOutputResolver());
			SchemaFactory schemaFactory = SchemaFactory.newInstance(
					javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(
					new File(Schema1cOutputResolver.SCHEMA_LOCATION));
			
			
			Marshaller marshaller = (JAXBContext.newInstance(
					XMLOrderContainer.class)).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setSchema(schema);
			marshaller.marshal(orderContainer, new File(FILE_NAME));
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
