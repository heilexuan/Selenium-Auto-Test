package com.excelsh.core.common;

public class InputFieldProperties {

	public String fieldname = null;
	public String label = null;
	public String type = null;
	public int sequence = 0;
	/** default value, used if no value in the resultset */
	public String defaultValue = null;
	
	public String toString() {
		String str = "ExportFieldProperties: [";
		str += "fieldname=" + this.fieldname;
		str += ", label=" + this.label;
		str += ", type=" + this.type;
		str += ", sequence=" + this.sequence;
		str += ", default=" + this.defaultValue;
		str += "]";
		return str;
	}

	public String toXML() {
		StringBuffer sb = new StringBuffer(500);
		sb.append("<Field Name=\"");
		if (this.fieldname != null) sb.append(this.fieldname);
		sb.append("\" Label=\"");
		if (this.label != null) sb.append(this.label);
		sb.append("\"><Type V=\"");
		if (this.type != null) sb.append(this.type);
		sb.append("\"/><Sequence V=\"");
		sb.append(this.sequence);
		sb.append("\"/>");
		if (this.defaultValue != null) 
			sb.append("<Default V=\"").append(this.defaultValue).append("\"/>");
		sb.append("</Field>");
		return sb.toString();
	}

}