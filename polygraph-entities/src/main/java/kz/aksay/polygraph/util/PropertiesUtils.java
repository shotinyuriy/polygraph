package kz.aksay.polygraph.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {
	
	public static final String DRIVER_KEY = "hibernate.connection.driverClass";
	public static final String USERNAME_KEY = "hibernate.connection.username";
	public static final String PASSWORD_KEY = "hibernate.connection.password"; 
	public static final String URL_KEY = "hibernate.connection.url";
	public static final String DIALECT_KEY = "hibernate.connection.dialect";
	
	public static final String EXT_PROPERTIES_DIR = "ext.properties.dir";
	
	public static Properties getCurrentDatabaseProperties(String fileName) {
		
		Properties properties = new Properties();
	    InputStream is = null;
	 
	    try {
		    // First try loading from the current directory
		    try {
		        File f = new File(fileName+".properties");
		        is = new FileInputStream( f );
		    }
		    catch ( Exception e ) { is = null; }
		    
		 
		    try {
		        if ( is == null ) {
		            // Try loading from classpath
		            is = PropertiesUtils.class.getResourceAsStream("/"+fileName+".properties");
		        }
		 
		        // Try loading properties from the file (if found)
		        properties.load( is );
		    }
		    catch ( Exception e ) { e.printStackTrace(); }
	    } finally {
	    	if(is != null ) {
	    		try { is.close(); } catch (IOException ioe) {}
	    	}
	    }
		
		return properties;
	}
	
	public static void writeDateBaseProperties(Properties properties, String fileName) {
		try {
			
			String path = null;
			OutputStream out = null;
			
			if(path == null) {
				path = fileName+".properties";	
			}
	        
			File f = new File(path);
			
			System.out.println("path "+f.getAbsolutePath());
			
			if(!f.exists()) {
				File parentFile = f.getParentFile();
				if(parentFile != null) {
					parentFile.mkdirs();
				}
				f.createNewFile();
			}
			
	        out = new FileOutputStream( f );
	        properties.store(out, "Database properties");
	    }
	    catch (Exception e ) {
	        e.printStackTrace();
	    }
	}

}