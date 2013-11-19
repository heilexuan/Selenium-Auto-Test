/**
 * 
 */
package com.excelsh.core.common;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author loki
 *
 */
public class SeleniumMethod {
	
	public static boolean isElementExist(WebDriver driver,By by){
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static WebElement waitForElementLoad(WebDriver driver,long timeOutInSeconds,final By by){
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		return wait.until(SeleniumMethod.visibilityOfElementLocated(by));
	}
	
	public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By by) {    
	    return new ExpectedCondition<WebElement>() {    
	          public WebElement apply(WebDriver driver) {    
	            WebElement element = driver.findElement(by);
	            return element.isDisplayed() ? element : null;    
	          }    
	    };    
	}
	
	public static void waiting(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();

		}
	}
	
	public static String acceptAlert(WebDriver driver){
		String alertText = null;
		if(isAlertExist(driver)){
			Alert alert = driver.switchTo().alert();
			alertText = alert.getText();
            alert.accept();
		}
		BaseLogger.info(SeleniumMethod.class, "SeleniumMethod.acceptAleart() alert massage : " + alertText);
		return alertText;
	}
	
	public static boolean isAlertExist(WebDriver driver){  
        try  
        {  
            driver.switchTo().alert();  
            return true;  
        }     
        catch (NoAlertPresentException Ex)  
        {  
            return false;  
        }  
	}
	
	public static void keyInputDataByName(WebDriver driver,String straInputFieldName, String straInputFieldValue){
		if(!StringUtils.isEmpty(straInputFieldValue)){
			WebElement elField = driver.findElement(By.name(straInputFieldName));
			elField.clear();
			elField.sendKeys(straInputFieldValue);
		}
	}
	
}
