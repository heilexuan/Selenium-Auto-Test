//******************************COPYRIGHT NOTICE*******************************
//All rights reserved.  This material is confidential and proprietary to Excel 
//Technology International (Hong Kong) Limited and no part of this material 
//should be reproduced, published in any form by any means, electronic or 
//mechanical including photocopy or any information storage or retrieval system 
//nor should the material be disclosed to third parties without the express 
//written authorization of Excel Technology International (Hong Kong) Limited.

package com.excelsh.core.common;

/*
 <PRE>
 * **************************VSS GENERATED VERSION NUMBER************************
 * $Revision: $
 * ******************************PROGRAM DESCRIPTION*****************************
 * Program Name   : BaseLogger.java
 * Description    : [Description]
 * Creation Date  : 2010/01/04
 * Creator        : Tom Zheng
 * ******************************MODIFICATION HISTORY****************************
 * Modify Date    : [Date in YYYY/MM/DD]
 * Modifier       : [Developer Name]
 * CR / SIR No.   : [Excel's CR / SIR No.]
 * Description    : [Description]
 * ******************************************************************************
 </PRE>
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.Formatter;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * {Class Overview: What does it do for your program?}
 * 
 * <DT><B>Copyright:</B>
 * <DD>The contents of this file are confidential and proprietary to Excel.</DD>
 * <DD>Copying is explicitly prohibited without the express permission of Excel.</DD>
 * 
 * @author fangenghong
 * @version 1.0.0
 *
 */
public class BaseLogger extends Logger {

	// It's usually a good idea to add a dot suffix to the fully
	// qualified class name. This makes caller localization to work
	// properly even from classes that have almost the same fully
	// qualified class name as UTSLogger.
	static String FQCN = BaseLogger.class.getName() + ".";

	// It's enough to instantiate a factory once and for all.	
	protected static Logger mylogger = BaseLogger.getLogger(BaseLogger.class);
	private static boolean bIsInit = false;

	private static final String strDefaultLogPropertiesKey = "LOG4J.PROPERTIES";
	
	public static final String DEBUG = "D";
    public static final String SQLPARAM = "P";
    public static final String SQL = "S";
    public static final String INFO = "I";
    public static final String ERROR = "E";
    
    protected static final String MSG_SEPARATOR = " || ";
    protected static final String MSG_SQLPM = "SQLPM" + MSG_SEPARATOR;
    protected static final String MSG_SQL = "SQL  " + MSG_SEPARATOR;
    protected static final String MSG_PRINT = "PRINT" + MSG_SEPARATOR;
    protected static final String MSG_DEBUG = "DEBUG" + MSG_SEPARATOR;
    protected static final String MSG_INFO = "INFO " + MSG_SEPARATOR;
    protected static final String MSG_ERROR = "ERROR" + MSG_SEPARATOR;
    protected static String strcDebugPrint = GlobalConst.CONST_STRING_EMPTY;    
	/**
	 Just calls the parent constuctor.
	 */
	public BaseLogger(String name) {
		super(name);
	}    
	/**
	 * init UTSLogger. The init has 2 part.  
	 * first: init a default config .
	 * second: init the event log config for all event module.
	 * @author lipengcheng
	 * @param aCfgPath 
	 * @param aMainPropPath
	 * @throws Exception
	 * 
	 */
	public static synchronized void init()
			throws Exception {
		try {
			if (!bIsInit) {
				//Hierarchy hierarchy = null;
				//Properties lprop = readProperyFile(aMainPropPath);
				Properties lprop = SeleniumUtil.getPropManager().getPropMain();
				String defLogPropFileName = lprop.getProperty(strDefaultLogPropertiesKey);
				// init the default log.
				//read the config file logutil.properties. set it as the default log.
				if (defLogPropFileName == null || defLogPropFileName.length() <= 0) {
					throw new Exception("No default Log properties.");
				} else {
					PropertyConfigurator.configure(SeleniumUtil.getPropManager().getPropPath("ROOT_PATH.PROPERTIES") + defLogPropFileName);
				}		
				mylogger = BaseLogger.getLogger(BaseLogger.class);
				setDebugPrint(mylogger.isDebugEnabled()?DEBUG:mylogger.isInfoEnabled()?INFO:ERROR);
				bIsInit = true;
			}
		} catch (Exception er) {
			throw er;
		}
	}

	private static Properties readProperyFile(String astrFileName) {

		FileInputStream lfs = null;
		try {
			lfs = new FileInputStream(astrFileName);
			Properties lprop = new Properties();
			lprop.load(lfs);
			return lprop;
		} catch (Exception er) {
			//er.printStackTrace();

		} finally {
			try {
				if (lfs != null)
					lfs.close();
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * jspInfo
	 * - logging using the JSP info level
	 * @param aObj
	 */
	public void jspInfo(String aStr) {
		info("JSP INFO: " + aStr);
	}

	/**
	 * jspWarn
	 * - logging using the JSP Warning level
	 * @param aObj
	 */
	public void jspWarn(String aStr) {
		warn("JSP WARN: " + aStr);
	}

	/**
	 * jspError
	 * - logging using the JSP Error level
	 * @param aObj
	 */
	public void jspError(String aStr) {
		error("JSP ERROR: " + aStr);
	}
    public boolean isDebug(){
    	return this.isDebugEnabled();
    }
    public boolean isInfo(){
    	return this.isInfoEnabled();
    }
    /**
     * Description : Check the debug level with the exiting mode
     * 							Eg. if level is 'INFO', only 'INFO' and 'ERROR' message will be shown
     * @param	      straMode String
     * @return	     	boolean
     */
    public boolean chkDebugPrint( String straMode ) {    	
        String strDebugPrint = this.isDebugEnabled()?DEBUG:this.isInfoEnabled()?INFO:ERROR;
        if ( strDebugPrint.equals( ERROR ) && straMode.equals( ERROR ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( INFO ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( SQL ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) || straMode.equals( SQL ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( SQLPARAM ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) || straMode.equals( SQL ) || straMode.equals( SQLPARAM ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( DEBUG ) ) {
            return true;
        }
        return false;
    }
    /**
     * Description : Check the debug level with the exiting mode
     * 							Eg. if level is 'INFO', only 'INFO' and 'ERROR' message will be shown
     * @param	      straMode String
     * @return	     	boolean
     */
    public static boolean chkCommonDebugPrint(String straMode ) {
        String strDebugPrint = getDebugPrint();
        if ( strDebugPrint.equals( ERROR ) && straMode.equals( ERROR ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( INFO ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( SQL ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) || straMode.equals( SQL ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( SQLPARAM ) && ( straMode.equals( INFO ) || straMode.equals( ERROR ) || straMode.equals( SQL ) || straMode.equals( SQLPARAM ) ) ) {
            return true;
        }
        else if ( strDebugPrint.equals( DEBUG ) ) {
            return true;
        }
        return false;
    }
    /**
     * Description : Get the debug level
     * @return	     	straDebugPrint String
     */
    public static String getDebugPrint() {
        if ( strcDebugPrint == null ) {
            return GlobalConst.CONST_STRING_EMPTY;
        }
        else if ( strcDebugPrint.length() == 0 ) {
            return INFO;
        }
        else {
            return strcDebugPrint;
        }
    }
    /**
     * Description : Set the debug level
     * 							DEBUG = "D"
     * 							SQLPARAM = "P"
     * 							SQL = "S"
     * 							INFO = "I"
     * 							ERROR = "E"
     * @param	      straDebugPrint String
     */
    public synchronized static void setDebugPrint( String straDebugPrint ) {
        strcDebugPrint = straDebugPrint;
    }
    public static void debug(Class aclass, String strMessage ) {
    	mylogger.debug(strMessage);    	
    }
    public static void debug(Class aclass, String strMessage, Object... argus) {
    	Formatter fm = new Formatter();    	
    	mylogger.debug(fm.format(strMessage, argus));    	
    }
    public static void info(Class aclass, String strMessage ) {
    	mylogger.info(strMessage);
    }
    public static void info(Class aclass, String strMessage, Object... argus) {
    	Formatter fm = new Formatter();    	    	
    	mylogger.info(fm.format(strMessage, argus));
    }
    public static void error(Class aclass, String strMessage ) {
    	mylogger.error(strMessage);
    }
    public static void error(Class aclass, String strMessage, Object... argus ) {
    	Formatter fm = new Formatter();
    	mylogger.error(fm.format(strMessage, argus));
    }
    public static void sql(Class aclass, String strMessage ) {
    	mylogger.debug(new StringBuffer().append(MSG_SQL).append(strMessage));    	
    }
    public static void sqlparam(Class aclass, String strMessage ) {
    	mylogger.debug(new StringBuffer().append(MSG_SQLPM).append(strMessage));    	
    }    
}

