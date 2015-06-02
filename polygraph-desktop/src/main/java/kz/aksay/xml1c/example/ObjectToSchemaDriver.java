package kz.aksay.xml1c.example;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class ObjectToSchemaDriver {

	public static void main(String[] args) {
		try {
			(JAXBContext.newInstance(Channel.class)).generateSchema(new ZASchemaOutputResolver());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
