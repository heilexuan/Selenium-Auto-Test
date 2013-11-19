/**
 * Package	: infrastructure.common
 * File	: PropManager.java
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: Properties Manager
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
 * Create Date	:
 * Create By	:
 *
 * $Revision: 1 $
 * $History: PropManager.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:47
 * Created in $/UTS3.5/CORE/Source/src/infrastructure/common
 * 
 * *****************  Version 3  *****************
 * User: Terence      Date: 29/11/04   Time: 19:17
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : NIL
 * Desc : Uppercase, Constant String, Using Map/List for common method
 * parameters, etc.
 *
 * *****************  Version 2  *****************
 * User: Bwu          Date: 11/18/04   Time: 9:53a
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : N/A
 * Desc : Base on the Code Review
 * 1. Minimize concate string
 * 2. Remove unused code
 * 3. Add method description and comment
 * 4. Revisit the coding is efficient or not and change if needed
 * 5. Reduce unnecessary type convertion
 * 6. Assign the .Size() into integer for looping purpose
 * 7. Remove all the toUpperCase()
 * 8. Change SQL Statement to a static string and in Uppercase
 *
 *
 */
package com.excelsh.core.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class PropManager {
  private Properties propMain = null;
  private Hashtable htPropObj = null;
	private String strcRootPath = GlobalConst.CONST_STRING_EMPTY;

	/**
	* Description : Get the main properties
	* @return       Properties
	*/
  public Properties getPropMain(){
    return propMain;
  }

	/**
	* Description : Get the complete set of properties stored
	* @return       Hashtable
	*/
  public Hashtable getAllPropObj(){
    return htPropObj;
  }
  
  	/**
	* Description : Initialize the properties manager
	* @param	     	straPropFilePath String
	*/
	public void init() throws FileNotFoundException, IOException{
	  propMain = new Properties();
	  htPropObj = new Hashtable();
	  getPropMain().load(PropManager.class.getResourceAsStream("/main.properties"));
		//Matthew
		if (propMain != null) {
			setRootPath((String)propMain.get("ROOT_PATH.PROPERTIES"));
		}
	}
	/**
	* Description : Initialize the properties manager
	* @param	     	straPropFilePath String
	*/
  public void init(String straPropFilePath) throws FileNotFoundException, IOException{
    propMain = new Properties();
    htPropObj = new Hashtable();
    FileInputStream fiPropFile = new FileInputStream(straPropFilePath.trim());
    getPropMain().load(fiPropFile);
	//Matthew
	if (propMain != null) {
		setRootPath((String)propMain.get("ROOT_PATH.PROPERTIES"));
	}
  }

	/**
	* Description : Destroy the properties manager
	*/
  public void destroy(){
  	if (getPropMain() != null){
      getPropMain().clear();
      propMain = null;
  	}
  	if (getAllPropObj() != null){
  		getAllPropObj().clear();
  		htPropObj = null;
  	}
  }

	/**
	* Description : Get the properties object from the hashtable, load it if not yet initialized
	* @param	     	straPropName String
	* @return	     	Properties
	*/
  public Properties getPropObj(String straPropName)throws FileNotFoundException, IOException{
    if (getPropMain() == null || getPropMain().isEmpty()){
      return null;
    }

    if (getAllPropObj().get(straPropName) != null){
      return (Properties)getAllPropObj().get(straPropName);
    }else{
      return loadProp(straPropName);
    }
  }

	/**
	* Description : Load properties into cache object
	* @param	     	straPropName String
	* @return	     	Properties
	*/
  private synchronized Properties loadProp(String straPropName)throws FileNotFoundException, IOException{
    if (getAllPropObj().get(straPropName) != null){
      return (Properties)getAllPropObj().get(straPropName);
    }

    if (BaseLogger.chkCommonDebugPrint(BaseLogger.INFO)){
    	BaseLogger.info(PropManager.class, "Loading Properties : " + straPropName);
    }
    String strPropFilePath = getPropMain().getProperty(straPropName, GlobalConst.CONST_STRING_EMPTY);
    if (BaseLogger.chkCommonDebugPrint(BaseLogger.INFO)){
    	BaseLogger.info(PropManager.class, "Properties File : " + strcRootPath + strPropFilePath);
    }
    if (strPropFilePath.length() == 0){
      return null;
    }

		FileInputStream fiPropFile = null;
		Properties propNewProp = new Properties();
		try{
			if (strcRootPath.length() > 0){
				fiPropFile = new FileInputStream (strcRootPath + strPropFilePath);
				propNewProp.load(fiPropFile);
			}
		}catch (Exception e){
			fiPropFile = new FileInputStream (strPropFilePath);
			propNewProp.load(fiPropFile);
		}
    if (BaseLogger.chkCommonDebugPrint(BaseLogger.INFO)){
    	BaseLogger.info(PropManager.class, "Loading Properties Finished");
    }
    getAllPropObj().put(straPropName, propNewProp);
    return propNewProp;
  }

	/**
	* Description : Get the properties file path
	* @param	     	straPropName String
	* @return	     	String
	*/
  public String getPropPath(String straPropName){
    if (getPropMain() == null || getPropMain().isEmpty())
      return GlobalConst.CONST_STRING_EMPTY;
    else
      return getPropMain().getProperty(straPropName, GlobalConst.CONST_STRING_EMPTY);
  }
  
	/**
	* Description : Get the properties value from a properties file
	* @param	     	straPropName String
	* @param	     	straPropKey String
	* @return	     	String
	*/
  public String getPropValue(String straPropName, String straPropKey){
    try{
      if (getPropObj(straPropName) == null) return null;
      return getPropObj(straPropName).getProperty(straPropKey, GlobalConst.CONST_STRING_EMPTY);
    }catch(Exception e){
      return null;
    }
  }

	/**
	* Description : Set a root path for properties
	* @param	     	straRootPath String
	*/
  public void setRootPath(String straRootPath){
  	strcRootPath = straRootPath;
  }

}