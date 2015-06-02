package kz.aksay.xml1c;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class Schema1cOutputResolver extends SchemaOutputResolver {

	public static final String SCHEMA_LOCATION = "1c-orders.xsd";
	public Result createOutput(String arg0, String arg1) throws IOException {
		
		return new StreamResult(new File(SCHEMA_LOCATION));
	}

}
