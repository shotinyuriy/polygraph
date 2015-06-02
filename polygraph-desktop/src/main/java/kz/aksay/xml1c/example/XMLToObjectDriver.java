package kz.aksay.xml1c.example;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public class XMLToObjectDriver {

	public static final String FILE_NAME = "channel.xml";
	public static void main(String[] args) {
		try {
			Channel channel = (Channel) ((JAXBContext.newInstance(Channel.class))
					.createUnmarshaller()).unmarshal(new File(FILE_NAME));
			System.out.println("channel name: "+channel.getName());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
