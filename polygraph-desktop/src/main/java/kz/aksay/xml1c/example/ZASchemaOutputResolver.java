package kz.aksay.xml1c.example;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class ZASchemaOutputResolver extends SchemaOutputResolver {

	public Result createOutput(String arg0, String arg1) throws IOException {
		
		return new StreamResult(new File("channel.xsd"));
	}

}
