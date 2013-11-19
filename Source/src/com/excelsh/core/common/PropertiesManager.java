/**
 * 
 */
package com.excelsh.core.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * @author loki
 *
 */
public class PropertiesManager {
	
	public Properties loadProperties(String straPropFilePath,String strapropName){
		//InputStream input = this.getClass().;
		Properties prop = new Properties();
		
		return prop;
	}
	public static void main(String[] args) throws FileNotFoundException, IOException {
		PropManager propManager = new PropManager();
		propManager.init("E:/uts3/SeleniumTest/Source/properties/main.properties");
		String strFrontEndMaker = propManager.getPropValue("LOGIN.PROPERTIES", "FrontEnd.maker.username");
		System.out.println(strFrontEndMaker);
	}
}
