/**
 * 
 */
package com.excelsh.core.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author loki
 *
 */
public class ExcelReader {
	/**
	 * Create work book object
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @date    2013-10-31
	 */
	public static Workbook createWb(String filePath) throws IOException {
	    if(StringUtils.isBlank(filePath)) {
	        throw new IllegalArgumentException("Illegal Argument!!!") ;
	    }
	    if(filePath.trim().toLowerCase().endsWith("xls")) {
	        return new HSSFWorkbook(new FileInputStream(filePath)) ;
	    } else if(filePath.trim().toLowerCase().endsWith("xlsx")) {
	        return new XSSFWorkbook(new FileInputStream(filePath)) ;
	    } else {
	        throw new IllegalArgumentException("Not support file types except xls/xlsx!!!") ;
	    }
	}
	
	public static Sheet getSheet(Workbook wb ,String sheetName) {
	    return wb.getSheet(sheetName) ;
	}

	public static Sheet getSheet(Workbook wb ,int index) {
	    return wb.getSheetAt(index) ;
	}
	
	/**
	  * get row size in a sheet
	  * 
	  * @return row number
	*/
	 public static int getRowSize(Sheet sheet) {
	  return sheet.getLastRowNum() - sheet.getFirstRowNum() + 1;
	 }

	 /**
	  * get column size in one row
	  * 
	  * @return column number
	*/
	 public static int getColumnSize(Row row) {
	  return row.getLastCellNum() - row.getFirstCellNum() + 1;
	 }
	
	/**
	 * get String Value From Cell
	 * @param cell
	 * @return
	 * @date    2013-10-31
	 */
	public static final String getValueFromCell(Cell cell) {
	    if(cell == null) {
	        BaseLogger.error(ExcelReader.class, "Cell is null !!!");
	        return null ;
	    }
	    String value = null ;
	    value = cell.getStringCellValue() ;
	    /*switch(cell.getCellType()) {
	        case Cell.CELL_TYPE_NUMERIC :   //numeric
	            if(HSSFDateUtil.isCellDateFormatted(cell)) {        //date
	                value = new SimpleDateFormat(GlobalConst.FORMAT_DATE).format(cell.getDateCellValue()) ;
	            } else  value = String.valueOf(cell.getNumericCellValue()) ;
	            break ;
	        case Cell.CELL_TYPE_STRING:     //String
	            value = cell.getStringCellValue() ;
	            break ;
	        case Cell.CELL_TYPE_FORMULA:    //Formula
	            // 用数字方式获取公式结果，根据值判断是否为日期类型
	            double numericValue = cell.getNumericCellValue() ;
	            if(HSSFDateUtil.isValidExcelDate(numericValue)) {   //data type
	                value = new SimpleDateFormat(GlobalConst.FORMAT_DATE).format(cell.getDateCellValue()) ;
	            } else  value = String.valueOf(numericValue) ;
	            break ;
	        case Cell.CELL_TYPE_BLANK:              //empty
	            value = GlobalConst.CONST_STRING_EMPTY;
	            break ;
	        case Cell.CELL_TYPE_BOOLEAN:            // Boolean
	            value = String.valueOf(cell.getBooleanCellValue()) ;
	            break ;
	        case Cell.CELL_TYPE_ERROR:              // Error, return error code
	            value = String.valueOf(cell.getErrorCellValue()) ;
	            break ;
	        default:value = StringUtils.EMPTY ;break ;
	    }*/
	    // use[]record index
	    //return value + "["+cell.getRowIndex()+","+cell.getColumnIndex()+"]" ;
	    return value;
	}

}
