/**
 * Package	: infrastructure.datautility
 * File	: GlobalConst.java
 *
 * Company 	: Excel Technology International (Hong Kong) Limited
 * Team    	: UTS
 * Description 	: Global Constant definition
 *
 * The contents of this file are confidential and proprietary to Excel.
 * Copying is explicitly prohibited without the express permission of Excel.
 *
 * Create Date	:
 * Create By	:
 *
 * $Revision: 1 $
 * $History: GlobalConst.java $
 * 
 * *****************  Version 1  *****************
 * User: Aleung       Date: 11/04/06   Time: 14:47
 * Created in $/UTS3.5/CORE/Source/src/infrastructure/common
 * 
 * *****************  Version 8  *****************
 * User: Terence      Date: 6/01/05    Time: 15:16
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : NIL
 * Desc : Core Merge
 * 
 * *****************  Version 7  *****************
 * User: Terence      Date: 5/01/05    Time: 13:47
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * 
 * *****************  Version 6  *****************
 * User: Terence      Date: 4/01/05    Time: 12:26
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * 
 * *****************  Version 5  *****************
 * User: Terence      Date: 3/01/05    Time: 16:25
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * 
 * *****************  Version 4  *****************
 * User: Bwu          Date: 12/16/04   Time: 5:59p
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : NIL
 * Desc : Core Merge enhancement
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
 * User: Aleung       Date: 11/16/04   Time: 10:08a
 * Updated in $/COREMERGE_DEV/Source/src/infrastructure/common
 * Add Big Decimal Constant with 100
 *
 * *****************  Version 1  *****************
 * User: Bwu          Date: 11/04/04   Time: 4:56p
 * Created in $/COREMERGE_DEV/Source/SRC/infrastructure/common
 *
 * *****************  Version 4  *****************
 * User: Bwu          Date: 3/18/04    Time: 12:25p
 * Updated in $/OCBC/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : N/A
 * Desc : Add description for previous version
 *
 * *****************  Version 3  *****************
 * User: Bwu          Date: 3/18/04    Time: 12:02p
 * Updated in $/OCBC/Source/src/infrastructure/common
 * Project : Core
 * CR / SIR No : N/A
 * Desc : Add new Constant value
 *
**/

package com.excelsh.core.common;

import java.math.BigDecimal;

public interface GlobalConst {
  /** Database Type - Oracle */
  public static final String DB_ORACLE = "ORACLE";
  /** Database Type - MSSQL */
  public static final String DB_MSSQL = "MSSQL";

  /** Data Output Type - XML */
  public static final char OUTPUT_XML = 'X';
  /** Data Output Type - Vector */
  public static final char OUTPUT_VECTOR = 'V';

  /** Row Status - Deleted */
  public static final char STATUS_DELETED = 'D';
  /** Row Status - Empty */
  public static final char STATUS_EMPTY = 'E';
  /** Row Status - Loaded */
  public static final char STATUS_LOADED = 'L';
  /** Row Status - Modified */
  public static final char STATUS_MODIFIED = 'M';
  /** Row Status - New */
  public static final char STATUS_NEW = 'N';

  /** Field Data Type - Number */
  public static final String TYPE_NUMBER = "NUMBER";
  /** Field Data Type - Char */
  public static final String TYPE_CHAR = "CHAR";
  /** Field Data Type - Varchar */
  public static final String TYPE_VARCHAR = "VARCHAR";
  /** Field Data Type - Varchar2 */
  public static final String TYPE_VARCHAR2 = "VARCHAR2";
  /** Field Data Type - Date */
  public static final String TYPE_DATE = "DATE";
  /** Field Data Type - Datetime */
  public static final String TYPE_DATETIME = "DATETIME";
  /** Field Data Type - Timestamp */
  public static final String TYPE_TIMESTAMP = "TIMESTAMP";
  /** Field Data Type - Decimal */
  public static final String TYPE_DECIMAL = "DECIMAL";
  /** Field Data Type - Double */
  public static final String TYPE_DOUBLE = "DOUBLE";
  /** Field Data Type - Integer */
  public static final String TYPE_INTEGER = "INTEGER";
  /** DataBuffer Field Data Type - Float */
  public static final String TYPE_FLOAT = "FLOAT";
  /** Field Data Type - BigInt */
  public static final String TYPE_BIGINT = "BIGINT";
  /** Field Data Type - SmallInt */
  public static final String TYPE_SMALLINT = "SMALLINT";
  /** Field Data Type - Numeric */
  public static final String TYPE_NUMERIC = "NUMERIC";
  /** Record Action Type - Insert */
  public static final String ACTION_INSERT = "I";
  /** Record Action Type - Delete */
  public static final String ACTION_DELETE = "D";
  /** Record Action Type - Update */
  public static final String ACTION_UPDATE = "E";
  /** Record Action Type - View */
  public static final String ACTION_VIEW = "V";
  /** Record Action Type - Search */
  public static final String ACTION_SEARCH = "S";

  /** Date Format String - yyyy/MM/dd */
  public static final String FORMAT_DATE = "yyyy/MM/dd";
  /** Date Format String - yyyy-MM-dd */
  public static final String FORMAT_LOGIN_DATE = "yyyy-MM-dd";
  /** Datetime Format String - yyyy/MM/dd HH:mm:ss */
  public static final String FORMAT_DATETIME = "yyyy/MM/dd HH:mm:ss";
  /** Datetime Format String for Year */
  public static final String FORMAT_YEAR = "yyyy";
  /** Datetime Format String for Month */
  public static final String FORMAT_MONTH = "MM";
  /** Datetime Format String for Day */
  public static final String FORMAT_DAY = "dd";
  /** Datetime Format String for Time */
  public static final String FORMAT_TIME = "HH:mm:ss";
  /** Datetime Format String for Timestamp - Date */
  public static final String FORMAT_TIMESTAMP_DATE = "yyyyMMdd";
  /** Datetime Format String for Timestamp - Time */
  public static final String FORMAT_TIMESTAMP_TIME = "HHmmss";
  /** Datetime Format String for Timestamp */
  public static final String FORMAT_TIMESTAMP = FORMAT_TIMESTAMP_DATE + FORMAT_TIMESTAMP_TIME;
  /** Decimal Format String */
  public static final String FORMAT_DECIMAL = "#,###.##########";
  public static final String FORMAT_DECIMAL_1_8 = "#.########";
  
  /** Datetime Year Field */
  public static final int DATE_FIELD_YEAR = java.util.Calendar.YEAR;
  /** Datetime Month Field */
  public static final int DATE_FIELD_MONTH = java.util.Calendar.MONTH;
  /** Datetime Date Field */
  public static final int DATE_FIELD_DATE = java.util.Calendar.DATE;

  /** Report Header Tag - RH */
  public static final String REPORT_HEADER = "RH";
  /** Report Page Header Tag - PH */
  public static final String REPORT_PAGE_HEADER = "PH";
  /** Report Footer Tag - RF */
  public static final String REPORT_FOOTER = "RF";
  /** Report Page Footer Tag - PF */
  public static final String REPORT_PAGE_FOOTER = "PF";
  /** Report Field Tag -  TagFName */
  public static final String REPORT_TAG_FIELD ="TagFName";

  public static final String SQL_CONCAT_DELIM = "+";

  /** Flag with 'Y' */
	public static final String FLAG_YES = "Y";
	/** Flag with 'N' */
	public static final String FLAG_NO = "N";
	/** Flag with 'ON' */
	public static final String FLAG_ON = "ON";
	/** Flag with 'OFF' */
	public static final String FLAG_OFF = "OFF";
	/** Flag with 'T' */
	public static final String FLAG_TRUE = "T";
	/** Flag with 'F' */
	public static final String FLAG_FALSE = "F";

	/** String Constant with Empty */
	public static final String CONST_STRING_EMPTY = "";
	/** String Constant with Space */
	public static final String CONST_STRING_SPACE = " ";
	/** String Constant with Equal */
	public static final String CONST_STRING_EQUAL = "=";
	/** String Constant with Dot */
	public static final String CONST_STRING_DOT = ".";
	/** String Constant with Underscore */
	public static final String CONST_STRING_UNDERSCORE = "_";
	/** String Constant with Hyphen */
	public static final String CONST_STRING_HYPHEN = "-";
	public static final String CONST_STRING_HYPHEN_2 = " - ";
	/** String Constant with Comma */
	public static final String CONST_STRING_COMMA = ",";
	/** String Constant with Single Quote */
	public static final String CONST_STRING_SINGLE_QUOTE = "'";
	/** String Constant with 0 */
	public static final String CONST_STRING_ZERO = "0";
	/** String Constant Slash */
	public static final String CONST_STRING_SLASH = "/";
	/** String Constant Stroke */
	public static final String CONST_STRING_STROKE = "\\";
	/** String Constant Colon */
	public static final String CONST_STRING_COLON = ":";
	/** String Constant @ */
	public static final String CONST_STRING_AT = "@";
	/** String Constant # */
	public static final String CONST_STRING_CHENG = "#";
	/** String Constant ? */
	public static final String CONST_STRING_QUESTION_MARK = "?";
	/** String Constant & */
	public static final String CONST_STRING_AMPERSEND = "&";
	/** String Constant "true" */
	public static final String CONST_STRING_true = "true";
	/** String Constant "false" */
	public static final String CONST_STRING_false = "false";
	/** Big Decimal Constant with 0 */
	public static final BigDecimal CONST_BIGDECIMAL_ZERO = new BigDecimal("0");
	/** Big Decimal Constant with 1 */
	public static final BigDecimal CONST_BIGDECIMAL_ONE = new BigDecimal("1");
	/** Big Decimal Constant with 100 */
	public static final BigDecimal CONST_BIGDECIMAL_ONE_HUNDRED = new BigDecimal("100");
	/** Double Constant with 0 */
	public static final Double CONST_DOUBLE_ZERO = new Double(0);
	/** Double Constant with 1 */
	public static final Double CONST_DOUBLE_ONE = new Double(1);
	/** Integer Constant with 0 */
	public static final Integer CONST_INTEGER_ZERO = new Integer(0);
	/** Integer Constant with 1 */
	public static final Integer CONST_INTEGER_ONE = new Integer(1);
	
	/** Product Types defined in input excel */
	public static final String PRODUCT_TYPE_ONSHORE = "境内基金";
	public static final String PRODUCT_TYPE_OFFSHORE = "境外基金";
	public static final String PRODUCT_TYPE_SD = "结构性理财产品";
	public static final String PRODUCT_TYPE_SN = "结构性票据";
	public static final String PRODUCT_TYPE_PP = "投资组合";
	public static final String PRODUCT_TYPE_OT = "其他产品";
	
	/** Aggregate Orders deel type f*/
	public static final String AGG_ORDER_DEEL_TYPE_SUB = "U";
	public static final String AGG_ORDER_DEEL_TYPE_RED = "R";
	public static final String AGG_ORDER_DEEL_TYPE_SWH = "S";
	
	/** Aggregate Orders type*/
	public static final String AGG_ORDER_TYPE_SUB = "subscription";
	public static final String AGG_ORDER_TYPE_RED = "redemption";
	public static final String AGG_ORDER_TYPE_SWH = "switching";
	
	/** IPO Monitor action */
	public static final String IPO_MONITOR_ACTION_CLOSE = "but_closeipo.gif";
	public static final String IPO_MONITOR_ACTION_ABORT = "but_abort.gif";
	public static final String IPO_MONITOR_ACTION_PROCEED = "but_proceed.gif";
	public static final String IPO_MONITOR_ACTION_APPROVE = "but_approve.gif";
	
	/** Fund IPO status */
	public static final String FUND_IPO_STATUS_IPO = "认购";
	public static final String FUND_IPO_STATUS_PPA = "继续进行（待审批）";
	public static final String FUND_IPO_STATUS_PA = "继续进行（已审批）";
	public static final String FUND_IPO_STATUS_CPA = "结束认购（待审批）";
	public static final String FUND_IPO_STATUS_CA = "结束认购（已审批）";
	public static final String FUND_IPO_STATUS_AP = "中断（待审批）";
	public static final String FUND_IPO_STATUS_AA = "中断（已审批）";
}
