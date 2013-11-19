package com.excelsh.core.common;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class ExcelDataProvider implements Iterator<Object[]>{
	private final String EXCELDATAPROVIDERS_PROPERTIES = "EXCELDATAPROVIDERS.PROPERTIES";

    private Workbook book = null;
    private Sheet sheet = null;
    private int rowNum = 0;
    private int curRowNo = 0;
    private int columnNum = 0;
    private String[] columnnName;
    
    public ExcelDataProvider(String straDataType, String classname,String methodname){
        try {
        	String strExcelFilePath = SeleniumUtil.getPropManager().getPropPath("ROOT_PATH.EXCEL." + straDataType) + SeleniumUtil.getPropManager().getPropValue(EXCELDATAPROVIDERS_PROPERTIES, classname + "." + straDataType);
            this.book = ExcelReader.createWb(strExcelFilePath);
            this.sheet = book.getSheet(methodname);
            this.rowNum =ExcelReader.getRowSize(sheet);
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            this.columnNum = ExcelReader.getColumnSize(row);
            columnnName = new String[ExcelReader.getColumnSize(row)];
            for(int i = row.getFirstCellNum();i < ExcelReader.getColumnSize(row);i++){
                columnnName[i] = ExcelReader.getValueFromCell(row.getCell(i));
            }
            this.curRowNo ++;
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean hasNext() {
        if (this.rowNum==0 || this.curRowNo>=this.rowNum){
            return false;
        }
        else{
            return true;
        }
    }

    @Override
    public Object[] next() {
        Row row = sheet.getRow(this.curRowNo);        
        Map<String,String> s = new HashMap<String, String>();
        for(int i = 0; i < this.columnNum;i++)
        {
            String temp="";
            try{
                temp = ExcelReader.getValueFromCell(row.getCell(i));
            }
            catch(ArrayIndexOutOfBoundsException ex){
                temp = "";
            }
            s.put(this.columnnName[i], temp);
        }

        Object r[]=new Object[1];
        r[0]=s;
        this.curRowNo++;
        return r;
    }

    @Override
    public void remove() {
         throw new UnsupportedOperationException("remove unsupported.");    
    }
}