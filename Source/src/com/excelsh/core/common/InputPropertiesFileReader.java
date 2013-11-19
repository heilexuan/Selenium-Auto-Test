/**
 * Package	: uts.bo.importexport
 * File	: ExportPropertiesFileReader.java
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: Properties File Reader for Data Export
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
	 * Create Date	: 20021129
	 * Create By	: Ben Wu
	 *
 * $Revision: 1 $
 * $History: ExportPropertiesFileReader.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:57
 * Created in $/UTS3.5/CORE/Source/src/uts/bo/importexport
 * 
 * *****************  Version 2  *****************
 * User: Terence      Date: 21/04/05   Time: 14:35
 * Updated in $/COREMERGE_DEV/Source/src/uts/bo/importexport
 * 
 * *****************  Version 1  *****************
 * User: Bwu          Date: 11/04/04   Time: 5:10p
 * Created in $/COREMERGE_DEV/Source/SRC/uts/bo/importexport
 * 
 * *****************  Version 9  *****************
 * User: Terence      Date: 11/05/04   Time: 11:58
 * Updated in $/Core/Source/src/uts/bo/importexport
 * Project : Core
 * CR / SIR No : N/A
 * Desc : Rearrange Debug/Info Message, for Core Merge
 */
package com.excelsh.core.common;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.tree.ElementNode;
import com.sun.xml.tree.XmlDocument;

public class InputPropertiesFileReader extends XmlReader {
	
	public InputPropertiesFileReader(String propertiesFilenameAndPath) {
		super(propertiesFilenameAndPath);
	}

	public Object[] read() {
		return this.read(null);
	}

	public Object[] read(Map selectedFields) {
		try {
			byte[] xmlFile = this.readFile();
			return this.unpackXML(xmlFile, selectedFields);
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(InputPropertiesFileReader.class, e.getMessage());
			this.lastError = e.getMessage();
			return null;
		}
	}

	//public Map unpackXML(byte[] xml, Map selectedFields) {
	public Object[] unpackXML(byte[] xml, Map selectedFields) {
		try {
			XmlDocument xmlDoc = XmlDocument.createXmlDocument(new ByteArrayInputStream(xml), false);
			if (!xmlDoc.getDocumentElement().getTagName().equalsIgnoreCase("PropertiesFile")) {
				this.lastError = "Incorrect XML";
				return null;
			} else {
				StringBuffer sb = new StringBuffer(500);
				sb.append("<PropertiesFile>");
				Node node1, node2;
				ElementNode eNode;
				NodeList nList1 = xmlDoc.getDocumentElement().getChildNodes(), nList2;
				int count1 = nList1.getLength();
				String tagName, fieldName;
				boolean orderBySequence = false;
				Vector vtr = new Vector();      // For ordering
				Map ht = new Hashtable(); // For searching
				for (int i=0; i<count1; i++) {
					node1 = nList1.item(i);
					if (node1 != null && node1.getNodeType() == XmlDocument.ELEMENT_NODE &&
							node1.getNodeName().equalsIgnoreCase("Field")) {
						eNode = (ElementNode)node1;
						fieldName = eNode.getAttribute("Name").toUpperCase();
						if (selectedFields == null || selectedFields.size() == 0 ||
								selectedFields.containsKey(fieldName)) {
							InputFieldProperties fp = new InputFieldProperties();
							fp.fieldname = fieldName;
							fp.label = eNode.getAttribute("Label");
							nList2 = node1.getChildNodes();
							int count2 = nList2.getLength();
							for (int j=0; j<count2; j++) {
								node2 = nList2.item(j);
								if (node2 != null && node2.getNodeType() == XmlDocument.ELEMENT_NODE) {
									tagName = node2.getNodeName();
									eNode = (ElementNode)node2;
									if (tagName.equalsIgnoreCase("Type")) {
										fp.type = eNode.getAttribute("V");
									} if (tagName.equals("Sequence")) {
										orderBySequence = true;
										String tmp = eNode.getAttribute("V");
										if (tmp == null || tmp.length() == 0) {
											fp.sequence = 0;
										} else {
											fp.sequence = Integer.parseInt(tmp);
										}
									} else if (tagName.equalsIgnoreCase("Default")) {
										fp.defaultValue = eNode.getAttribute("V");
									}
								}
							}
							if (orderBySequence && fp.sequence > 0) {
								//vtr.add(fp);
								this.setToVector(vtr, fp, fp.sequence);
							} else {
								vtr.add(fp);
							}
							ht.put(fp.fieldname, fp);
							sb.append(fp.toXML());
						}
					}
				}
				sb.append("</PropertiesFile>");
				this.xml = sb.toString();
				this.trimVector(vtr);
				return new Object[]{vtr, ht};
			}
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(InputPropertiesFileReader.class, e.getMessage());
			this.lastError = e.getMessage();
			return null;
		}
	}

	private void trimVector(Vector vtr) {
		int i = 0;
		while (i < vtr.size()) {
			if (vtr.elementAt(i) == null) {
				vtr.removeElementAt(i);
			} else {
				i++;
			}
		}
		vtr.trimToSize();
	}

	private void setToVector(Vector vtr, Object obj, int setAt) throws Exception {
		if (setAt < 0) throw new Exception("Invalid value of setAt: " + setAt);
		//if (setAt < 0) throw new Exception(((UTSMsgManager)UTSStartupManager.getMsgManager()).getMsg(objSecurMgr.getLangID(), "80527", String.valueOf(setAt)));
		int size = vtr.size();
		if (setAt >= size) {
			vtr.setSize(setAt + 1);
		}
		vtr.setElementAt(obj, setAt);
	}

}