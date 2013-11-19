/**
 * Package	: com.excelsh.core.menu
 * File	: MenuReader.java
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: Method related to menu xml file
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
 * Create Date	:
 * Create By	:
 *
 * $Revision: 1 $
 * $History: MenuReader.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:48
 * Created in $/UTS3.5/CORE/Source/src/com.excelsh.core/menu
 * 
 * *****************  Version 5  *****************
 * User: Terence      Date: 4/05/05    Time: 15:06
 * Updated in $/COREMERGE_DEV/Source/src/com.excelsh.core/menu
 * 
 * *****************  Version 4  *****************
 * User: Terence      Date: 6/01/05    Time: 15:15
 * Updated in $/COREMERGE_DEV/Source/src/com.excelsh.core/menu
 * 
 * *****************  Version 3  *****************
 * User: Terence      Date: 23/12/04   Time: 9:06
 * Updated in $/COREMERGE_DEV/Source/src/com.excelsh.core/menu
 * 
 * *****************  Version 2  *****************
 * User: Terence      Date: 29/11/04   Time: 19:17
 * Updated in $/COREMERGE_DEV/Source/src/com.excelsh.core/menu
 * Project : Core
 * CR / SIR No : NIL
 * Desc : Uppercase, Constant String, Using Map/List for common method
 * parameters, etc.
 *
 * *****************  Version 1  *****************
 * User: Bwu          Date: 11/04/04   Time: 4:58p
 * Created in $/COREMERGE_DEV/Source/SRC/com.excelsh.core/menu
 *
 * *****************  Version 3  *****************
 * User: Bwu          Date: 4/21/04    Time: 2:20p
 * Updated in $/OCBC/Source/src/com.excelsh.core/menu
 * Project : Core
 * CR / SIR No : NIL
 * Desc : Remove unused code/imported classes
 *
 */
package com.excelsh.core.menu;

import com.excelsh.core.common.BaseLogger;
import com.excelsh.core.common.GlobalConst;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.ElementNode;
import com.sun.xml.tree.XmlDocument;

public class MenuReader {
	
	private static final String strcXML_Menu = "Menu";
	private static final String strcXML_menu_level = "menu_level";
	private static final String strcXML_menu_desc = "menu_desc";
	private static final String strcXML_menu_link_prefix = "menu_link_prefix";
	private static final String strcXML_menu_link = "menu_link";
	private static final String strcXML_menu_id = "menu_id";
	private static final String strcXML_security = "security";
	private static final String strcXML_menuitem = "menuitem";
	
	private static final String strcCharSetBig5 = "sun.io.CharToByteBig5";
	private static final String strcCharSetGBK = "sun.io.CharToByteGBK";

	private String lastError = GlobalConst.CONST_STRING_EMPTY;
	private MenuItem miMenuItems = null;
	private String strOverallSecurityChecking = GlobalConst.CONST_STRING_EMPTY;
	//Modified by Wilson at 2008-2-25 start
	//private String strCharSetClass = strcCharSetBig5;
	private String strCharSetClass = strcCharSetGBK;
	//Modified by Wilson at 2008-2-25 end
		
	public static final int BIG5 = 1;
	public static final int GB = 2;
	
	public String getLastError() {
		return this.lastError;
	}
	
	public MenuReader() {
		this.miMenuItems = new MenuItem();
	}
	
	public boolean readString(String straXmlStirng, boolean IsCheckSecurity) {
		try {
			return this.unpackXML(straXmlStirng.getBytes(), IsCheckSecurity);
		} catch (Exception e) {
			BaseLogger.error(MenuReader.class, "MenuReader (readString) - " + e.toString());
			return false;
		}
	}
	
	public boolean readFile(
		String straXmlFileNamePath,
		boolean IsCheckSecurity) {
		try {
			byte[] xmlFile = this.getFileContent(straXmlFileNamePath);
			return this.unpackXML(xmlFile, IsCheckSecurity);
		} catch (Exception e) {
			BaseLogger.error(MenuReader.class, "MenuReader (readFile) - " + e.toString());
			return false;
		}
	}
	
	public boolean unpackXML(byte[] xml, boolean IsCheckSecurity) {
		try {
			XmlDocument xmlDoc =
				XmlDocument.createXmlDocument(new ByteArrayInputStream(xml), false);
			if (!xmlDoc.getDocumentElement().getTagName().equalsIgnoreCase(strcXML_Menu)) {
				BaseLogger.error(MenuReader.class, "MenuReader (unpackXML) - Incorrect XML");
				return false;
			} else {
				NodeList nList1 = xmlDoc.getDocumentElement().getChildNodes();
				ElementNode en = (ElementNode)nList1;
				this.strOverallSecurityChecking = en.getAttribute(strcXML_security);
				if (strOverallSecurityChecking
					.equalsIgnoreCase(GlobalConst.CONST_STRING_EMPTY)) {
					strOverallSecurityChecking = GlobalConst.FLAG_NO;
				}
				this.miMenuItems.setMenuSecurity(strOverallSecurityChecking);
				this.readSubMenu(this.miMenuItems, nList1, IsCheckSecurity);
			}
			return true;
		} catch (Exception e) {
			BaseLogger.error(MenuReader.class, "MenuReader (unpackXML) - " + e.toString());
			e.printStackTrace();
			return false;
		}
	}
	
	private void readSubMenu(
		MenuItem aParentMenuItem,
		NodeList nlaNodeList,
		boolean IsCheckSecurity)
		throws IOException {
		//int intNoOfNextLevelSubMenu = 0;
		// Added by Helen 2009/02/03 for # Start
		int intSubMenuMaxLength = 0;
		// Added by Helen 2009/02/03 for # End
		for (int i = 0, intLength = nlaNodeList.getLength(); i < intLength; i++) {
			Node node1 = nlaNodeList.item(i);
			if (node1 != null
				&& node1.getNodeType() == XmlDocument.ELEMENT_NODE
				&& node1.getNodeName().equalsIgnoreCase(strcXML_menuitem)) {
				ElementNode eNode = (ElementNode)node1;
				String strSubMenuSecurityChecking = eNode.getAttribute(strcXML_security);
				String strMenu_id = eNode.getAttribute(strcXML_menu_id);
//				System.out.println("MenuReader: IsCheckSecurity = " + IsCheckSecurity + 
//						", strSubMenuSecurityChecking = " + strSubMenuSecurityChecking + 
//						", strMenu_id = " + strMenu_id);
				if (!IsCheckSecurity) {
					MenuItem aMenuItem = new MenuItem();
					aMenuItem.setMenuID(strMenu_id);
					aMenuItem.setMenuLink(eNode.getAttribute(strcXML_menu_link));
					aMenuItem.setMenuLinkPrefix(eNode.getAttribute(strcXML_menu_link_prefix));
					String s = GlobalConst.CONST_STRING_EMPTY;
					try {
						/*Modified by Wilson at 2008-2-25 start 
						sun.io.CharToByteConverter cvt =
							(sun.io.CharToByteConverter)Class
								.forName(strCharSetClass)
								.newInstance();
						s =
							new String(
								cvt.convertAll(eNode.getAttribute(strcXML_menu_desc).toCharArray()));
						*/
						s =eNode.getAttribute(strcXML_menu_desc);
						//Modified by Wilson at 2008-2-25 end
					} catch (Exception er) {
						System.out.println(er.getMessage());
						s = eNode.getAttribute(strcXML_menu_desc);
					}
					
					// Added by Helen 2009/02/03 for # Start
					if(intSubMenuMaxLength < s.length()) intSubMenuMaxLength = s.length();
					// Added by Helen 2009/02/03 for # End
					
					aMenuItem.setMenuDesc(s);
					aMenuItem.setMenuLevel(
						Integer.parseInt(eNode.getAttribute(strcXML_menu_level)));
					aMenuItem.setMenuSecurity(strSubMenuSecurityChecking);
					NodeList childNodeList = node1.getChildNodes();
					if (childNodeList.getLength() > 0) {
						this.readSubMenu(aMenuItem, childNodeList, IsCheckSecurity);
					}
					aParentMenuItem.addSubMenuItem(aMenuItem);
					// Added by Helen 2009/02/03 for # Start
					aParentMenuItem.setSubMenuMaxLength(intSubMenuMaxLength);
					// Added by Helen 2009/02/03 for # End
				}
			}
		}
	}
	
	private byte[] getFileContent(String straXmlFileNamePath) {
		try {
			File file = new File(straXmlFileNamePath);
			FileInputStream fis = new FileInputStream(file);
			byte[] byteArray = new byte[(int)file.length()];
			fis.read(byteArray);
			fis.close();
			return byteArray;
		} catch (Exception e) {
			BaseLogger.error(MenuReader.class, "MenuReader (getFileContent) - " + e.toString());
			return null;
		}
	}
	
	public MenuItem getMenuItem() {
		return this.miMenuItems;
	}
	
	// input parameters: sun.io.CharToByteBig5, sun.io.CharToByteGBK, or .....
	public void setCharSetClassName(String straCharSetClassName) {
		try {
			Class.forName(straCharSetClassName);
			strCharSetClass = straCharSetClassName;
		} catch (Exception e) {
			BaseLogger.error(MenuReader.class, "MenuReader (setCharSetClassName) - " + e.toString());
		}
	}
	
	public void setCharSetClassType(int straCharSetClassType) {
		String strTmpClassName = GlobalConst.CONST_STRING_EMPTY;
		switch (straCharSetClassType) {
			case BIG5 :
				strTmpClassName = strcCharSetBig5;
				break;
			case GB :
				strTmpClassName = strcCharSetGBK;
				break;
			default :
				strTmpClassName = strcCharSetBig5;
		}
		setCharSetClassName(strTmpClassName);
	}
	
}