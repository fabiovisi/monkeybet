package macaco.coleta.negocio;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ManipulaProperties {

	   public static Properties getProp() throws IOException {
	        Properties props = new Properties();
	        FileInputStream file = new FileInputStream(
	                "./properties/config.properties");
	        props.load(file);
	        return props;
	 
	    }
	
}
