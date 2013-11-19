/**
 * 
 */
package com.excelsh.core.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


/**
 * @author Loki
 *
 */
public class SeleniumUtil {

	private static PropManager objPropManager = null;
	
	private static String strJquery;
	
	private final static String MAIN_PROPERTIES = "main.properties";
	private final static String ROOT_PATH_JS = "ROOT_PATH.JS";
	
	@Test
	public static void loadJquery(WebDriver driver){
		String js = "" +
				" (function(){ " +
				    " if(!window.jQuery){ " +
				        " var s = document.createElement('script'); " +
				        " s.type = 'text/javascript'; " +
				        " s.src = 'http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'; " +
				        " document.body.appendChild(s); " +
				 " } " +
				" } " +
				" )();";
		((JavascriptExecutor)driver).executeScript(js);
	}
	
	public static WebDriver getInternetDriver(){
			BaseLogger.info(SeleniumUtil.class, "SeleniumUtil.getInternetDriver() creating new driver ......");
			//System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
			//System.setProperty("webdriver.ie.driver","./bin/IEDriverServer.exe");
			
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			return new InternetExplorerDriver(capabilities);
	}
	
	public static PropManager getPropManager(){
		if (objPropManager == null) {
	      objPropManager = new PropManager();
	      try {
	    	  objPropManager.init();
			} catch (Exception e) {
				BaseLogger.error(SeleniumUtil.class, "SeleniumUtil (getPropManager) - " + e.toString());
		        objPropManager = null;
				e.printStackTrace();
			} 
		}
		objPropManager.setRootPath(objPropManager.getPropPath("ROOT_PATH.PROPERTIES"));
	    return objPropManager;
	}
	
	public static PropManager getPropManager(String straPropFilePath){
		if (objPropManager == null) {
	      objPropManager = new PropManager();
	      try {
	    	  objPropManager.init(straPropFilePath + MAIN_PROPERTIES);
	      } catch (Exception e) {
	        BaseLogger.error(SeleniumUtil.class, "SeleniumUtil (getPropManager) - " + e.toString());
	        objPropManager = null;
	      }
	    }
	    objPropManager.setRootPath(objPropManager.getPropPath("ROOT_PATH.PROPERTIES"));
	    return objPropManager;
	}
}
