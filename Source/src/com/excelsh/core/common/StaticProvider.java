package com.excelsh.core.common;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import jxl.read.biff.BiffException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;

public class StaticProvider {
	
	@DataProvider(name = "InputDataProvider")
	public static Iterator<Object[]> getInputDataByTestMethodName(Method method) throws BiffException, IOException {
		return new ExcelDataProvider("INPUTDATA",method.getDeclaringClass().getSimpleName(),method.getName());
	}
	
	@DataProvider(name = "ExpectedDataProvider")
	public static Iterator<Object[]> getExceptedDataByTestMethodName(Method method) throws BiffException, IOException {
		return new ExcelDataProvider("EXPECTEDRESULT",method.getDeclaringClass().getSimpleName(),method.getName());
	}
}