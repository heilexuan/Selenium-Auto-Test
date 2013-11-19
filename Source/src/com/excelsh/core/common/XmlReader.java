/**
 * Package	: uts.bo.importexport
 * File	: XmlReader
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: XML Reader
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
	 * Create Date	: 20021129
	 * Create By	: Ben Wu
	 *
 * $Revision: 1 $
 * $History: XmlReader.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:57
 * Created in $/UTS3.5/CORE/Source/src/uts/bo/importexport
 * 
 * *****************  Version 1  *****************
 * User: Bwu          Date: 11/04/04   Time: 5:10p
 * Created in $/COREMERGE_DEV/Source/SRC/uts/bo/importexport
 * 
 * *****************  Version 3  *****************
 * User: Terence      Date: 11/05/04   Time: 11:58
 * Updated in $/Core/Source/src/uts/bo/importexport
 * Project : Core
 * CR / SIR No : N/A
 * Desc : Rearrange Debug/Info Message, for Core Merge
 */
package com.excelsh.core.common;

import java.io.*;

public class XmlReader {

	protected String xml = null;
	protected String xmlFile = null;
	protected String lastError = null;

	public XmlReader(String xmlFilenameAndPath) {
		this.xmlFile = xmlFilenameAndPath;
	}

	public String getLastError() {
		return this.lastError;
	}

	public String getXML() {
		return this.xml;
	}

	protected byte[] readFile() throws Exception {
		BaseLogger logWriter = new BaseLogger("Selenium");
		logWriter.init();
		try {
			if (logWriter.chkDebugPrint(logWriter.INFO)) {
				logWriter.info("Read XML File: " + this.xmlFile);
			}
			File file = new File(this.xmlFile);
			FileInputStream fis = new FileInputStream(file);
			byte[] byteArray = new byte[(int)file.length()];
			fis.read(byteArray);
			fis.close();
			return byteArray;
		} catch (Exception e) {
			e.printStackTrace();
			this.lastError = e.getMessage();
			return null;
		}
	}
}