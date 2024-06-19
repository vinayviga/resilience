package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
	
	public static String propertyReader(String propertyKey)
	{
		
		  Properties properties = new Properties();
	        InputStream input = null;
	        String propertyValue = "";

	        try {
	            // Specify the path to the properties file
	            String propertiesFilePath = "src\\test\\resources\\testData.properties";
	            input = new FileInputStream(propertiesFilePath);

	            // Load the properties file
	            properties.load(input);

	            // Retrieve properties
	            propertyValue = properties.getProperty(propertyKey);
	            System.out.println("Property Value: " + propertyValue);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (input != null) {
	                try {
	                    input.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return propertyValue;
	    }
	
	public static void main(String[] args) {
		
		System.out.println(Property.propertyReader("authenticationtestEmail"));
		
	}
		
	}

