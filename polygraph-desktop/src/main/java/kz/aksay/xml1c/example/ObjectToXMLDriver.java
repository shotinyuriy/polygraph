package kz.aksay.xml1c.example;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class ObjectToXMLDriver {

	public static final String FILE_NAME = "channel.xml";
	public static void main(String[] args) {
		try {
			Marshaller marshaller = (JAXBContext.newInstance(Channel.class)).createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(new Channel("zaneacademy"), new File(FILE_NAME));
			
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
