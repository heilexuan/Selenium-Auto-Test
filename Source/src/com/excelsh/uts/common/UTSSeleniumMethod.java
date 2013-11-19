/**
 * 
 */
package com.excelsh.uts.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import module.login.UserLoginTest;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jcajce.provider.symmetric.ARC4.Base;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.collections.CollectionUtils;

import com.excelsh.core.common.BaseLogger;
import com.excelsh.core.common.GlobalConst;
import com.excelsh.core.common.SeleniumMethod;
import com.excelsh.core.common.SeleniumUtil;

/**
 * @author loki
 *
 */
public class UTSSeleniumMethod {
	private final static String LOGIN_PROPERTIES = "LOGIN.PROPERTIES";
	
	@Test
	public static void login(WebDriver driver,String straEnvironment, String straAuthority) throws FileNotFoundException, IOException {
		BaseLogger.info(UTSSeleniumMethod.class, "Start UTSSeleniumMethod.login ......");
		String webSite = SeleniumUtil.getPropManager().getPropValue(LOGIN_PROPERTIES, "WebSite");
		//SeleniumMethod.waiting(4000);
		driver.get(webSite);
		
		String preUrl = driver.getCurrentUrl();
		Set<String> preWindowHandles = driver.getWindowHandles();
		System.out.println(preWindowHandles);
		driver.switchTo().frame("content");
		System.out.println(driver.getWindowHandle());
		String strUserName = SeleniumUtil.getPropManager().getPropValue(LOGIN_PROPERTIES, straEnvironment + "." + straAuthority + ".username");
		String strPassword = SeleniumUtil.getPropManager().getPropValue(LOGIN_PROPERTIES, straEnvironment + "." + straAuthority + ".password");
		
		//enter user name while login page load
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("userID"))
					  .sendKeys(strUserName);
		
		//enter password
		driver.findElement(By.name("password")).sendKeys(strPassword);
		
		//click login button
		driver.findElement(By.name("login")).click();
		
		//switch driver to new window
		SeleniumMethod.waiting(2000);
		
		/*Set<String> afterHandlers = driver.getWindowHandles();
		afterHandlers.removeAll(preWindowHandles);
		String newWinhandler = afterHandlers.iterator().next();
		driver.switchTo().window(newWinhandler);*/
		for (String handler : driver.getWindowHandles()) {
			driver.switchTo().window(handler);
			BaseLogger.info(UTSSeleniumMethod.class, "End UTSSeleniumMethod.login() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
			if(driver.getCurrentUrl().equals(preUrl)){
				driver.close();
			}
		}
	}

	public static void logout(WebDriver driver, boolean isQuitDriver) {
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.logout() start ......");
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.logout() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		driver.switchTo().frame("header");
		driver.findElement(By.xpath("//img[contains(@src,'but_logout.gif')]")).click();
		SeleniumMethod.acceptAlert(driver);
		if(isQuitDriver){
			driver.manage().deleteAllCookies();
			driver.quit();
			driver = null;
		};
		SeleniumMethod.waiting(4000);
	}
	
	public static void chooseMenu(WebDriver driver,String firstMenu, String secondMenu, String thirdMenu){
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.chooseMenu() start ......");
		WebDriverWait waitLoginSu = new WebDriverWait(driver, 30);
		waitLoginSu.until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver d) {
				d.switchTo().frame("content");
				return d.findElement(By.cssSelector("td.menuchain"));
			}
		});
		driver.switchTo().window(driver.getWindowHandle());
		PageFactory.initElements(driver, driver.getPageSource());
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.chooseMenu() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		
		Actions actions = null;
		if(!StringUtils.isEmpty(firstMenu)){
			actions = new Actions(driver);
			driver.switchTo().frame("header");
			WebElement firstMenuHoverLink = driver.findElement(By.xpath("//td[@class='menu_top' and contains(text(),'"+firstMenu+"')]"));
			actions.moveToElement(firstMenuHoverLink);
			actions.perform();
			driver.switchTo().window(driver.getWindowHandle());
			if(!StringUtils.isEmpty(secondMenu)){
				driver.switchTo().frame("content");
				WebElement secondMenuHoverLink = driver.findElement(By.xpath("//td[@class='menu_sub' and contains(text(),'"+secondMenu+"')]"));
				
				if(!StringUtils.isEmpty(thirdMenu)){
					WebElement thirdMenuHoverLink = driver.findElement(By.xpath("//td[@class='menu_sub' and contains(text(),'"+thirdMenu+"')]"));
					((JavascriptExecutor)driver).executeScript("arguments[0].click();",thirdMenuHoverLink);
				}else{
					//StaleElementReferenceException
					((JavascriptExecutor)driver).executeScript("arguments[0].click();",secondMenuHoverLink);
				}
			}
		}
		driver.switchTo().window(driver.getWindowHandle());
		SeleniumMethod.waiting(2000);
	}
	
	public static String enterNewOrder(WebDriver driver,Map<String, String> data) throws Exception{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.enterNewOrder() start ...... ");
		String strOrderNo = null;
		// fill account no and click order type link
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("ac_no"))
        			  .sendKeys(data.get("InvestmentAccountNo"));

		driver.findElement(By.linkText(data.get("OrderType"))).click();
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.enterNewOrder() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(2000);
		//fill product code and order unit
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("ORDERS_FUND_FUND_CODE_0"))
		              .sendKeys(data.get("FundCode"));
		if(data.get("ProductType").equals(GlobalConst.PRODUCT_TYPE_ONSHORE)){
			SeleniumMethod.keyInputDataByName(driver, "ORDERS_FUND_ORDER_GROSS_AMT_0", data.get("OrderUnit"));
		}else{
			SeleniumMethod.keyInputDataByName(driver, "ORDERS_FUND_ORDER_UNIT_0", data.get("OrderUnit"));
		}
		//click save and submit button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_save_submit.gif')]")).click();
		//accept popup page if have
		SeleniumMethod.acceptAlert(driver);
		//get the created new order's no for return
		driver.switchTo().window(driver.getWindowHandle());
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		if(!SeleniumMethod.isElementExist(driver, By.name("ORDER_LIST_ORDER_NO_0"))){
			if(SeleniumMethod.isElementExist(driver, By.id("message"))){
				BaseLogger.error(UTSSeleniumMethod.class, driver.findElement(By.id("message")).getText());
				//throw new Exception("order create failed ......");
			}
		}else{
			strOrderNo = driver.findElement(By.name("ORDER_LIST_ORDER_NO_0")).getAttribute("value");
		}
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		return strOrderNo;
	}
	
	public static void approveOrder(WebDriver driver, Map<String, String> data, String straOrderNo) throws FileNotFoundException, IOException{
	    BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveOrder() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "客户订单审批", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		//input search criteria order no
		UTSSeleniumMethod.inputSearchCritria(driver,straOrderNo,"ORDER_NO");
		
		//click order no link, enter approve page
		driver.findElement(By.linkText(straOrderNo))
		      .click();
		//click approve and close button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_app_close.gif')]"))
					  .click();
		//if approve confirm alert popup, accept it
		SeleniumMethod.acceptAlert(driver);
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveOrder() End ......");
	}
	
	/**
	 * change IPO fund's date to system date in order to stop IPO status next
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,FundCode
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void changeIpoMonitorDate2SysDate(WebDriver driver,Map<String, String> data) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeIpoMonitor() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "认购监控", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeIpoMonitor() FundCode : " + data.get("FundCode"));
		//input search criteria fund code
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("SEARCH_TEXT"))
					  .sendKeys(data.get("FundCode"));
		//select search criteria by
		Select selectFundCode = new Select(driver.findElement(By.name("SEARCH_BY")));
		selectFundCode.selectByValue("FUND_CODE");
		//select SN Status Code IPO
		Select selectSnStatusCode = new Select(driver.findElement(By.name("SEARCH_SEARCH_SN_STATUS_CODE")));
		selectSnStatusCode.selectByValue("IPO");
		//click search button
		driver.findElement(By.id("IMG_BUT_GO"))
					  .click();
		
		//click fund code link
		driver.findElement(By.linkText(data.get("FundCode")))
			  .click();
		//click edit button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_edit.gif')]"))
		  			  .click();
		
		//change SN_LAST_ORDER_DNT,SN_TRADE_DNT,SN_SETT_DNT to system date
		HashMap<String,String> mSysDate = getSystemDateFromMenuChain(driver);
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_LAST_ORDER_DNT_DATE", mSysDate.get("SystemDay"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_LAST_ORDER_DNT_MONTH", mSysDate.get("SystemMonth"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_LAST_ORDER_DNT_YEAR", mSysDate.get("SystemYear"));
		
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_TRADE_DNT_DATE", mSysDate.get("SystemDay"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_TRADE_DNT_MONTH", mSysDate.get("SystemMonth"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_TRADE_DNT_YEAR", mSysDate.get("SystemYear"));
		
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_SETT_DNT_DATE", mSysDate.get("SystemDay"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_SETT_DNT_MONTH", mSysDate.get("SystemMonth"));
		SeleniumMethod.keyInputDataByName(driver, "STRUCT_NOTES_STATUS_SN_SETT_DNT_YEAR", mSysDate.get("SystemYear"));
		
		//click save button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_save.gif')]"))
			  .click();
		SeleniumMethod.acceptAlert(driver);
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeIpoMonitor() End ......");
	}
	
	/**
	 * approve IPO fund's date change
	 * @param driver
	 * @param data -- Environment,Authority,SmObjectCode,SysObjectKeyValue
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void approveIpoMonitorDateChange(WebDriver driver,Map<String, String> data) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveIpoMonitorDateChange() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, "系统", "一般维护审批", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//input search criteria object code
		UTSSeleniumMethod.inputSearchCritria(driver,data.get("SmObjectCode"),"SM_OBJECT_CODE");
		
		//click object code link
		driver.findElement(By.linkText(data.get("SmObjectCode")))
			  .click();
		
		//find correct row no and select the checkbox
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_approve.gif')]"));
		String script = "var keyColumn = arguments[0]; keyColumn.parentNode.firstChild.firstChild.click();";
		WebElement weKeyValue = driver.findElement(By.xpath("//table[@id='TBLAuthorization']//td[contains(text()[2],'"+data.get("SysObjectKeyValue")+"')]"));
		((JavascriptExecutor)driver).executeScript(script,weKeyValue);
		
		//click the approve button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_approve.gif')]"))
			  .click();
		SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeIpoMonitor() End ......");
	}
	
	/**
	 * change fund IPO status
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,FundCode
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void changeFundIpoStatus(WebDriver driver,Map<String, String> data,String straIpoAction) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeFundIpoStatus() Start -- " + straIpoAction);
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "认购监控", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		String strPreurl = driver.getCurrentUrl();
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//input search criteria fund code
		UTSSeleniumMethod.inputSearchCritria(driver,data.get("FundCode"),"FUND_CODE");
		
		//click fund code link
		driver.findElement(By.linkText(data.get("FundCode")))
			  .click();
		
		//click ipo action button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'"+straIpoAction+"')]"))
		  			  .click();
		//confirm action
		SeleniumMethod.acceptAlert(driver);
		driver.switchTo().window(driver.getWindowHandle());
		String strPreHandler = driver.getWindowHandle();
		if(straIpoAction.equals(GlobalConst.IPO_MONITOR_ACTION_PROCEED)){
			SeleniumMethod.waiting(3000);
			for (String handler : driver.getWindowHandles()) {
				driver.switchTo().window(handler);
				BaseLogger.info(UTSSeleniumMethod.class, "End UTSSeleniumMethod.changeFundIpoStatus() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
				if(SeleniumMethod.isElementExist(driver, By.xpath("//a/img[contains(@src,'but_ok.gif')]"))){
					WebElement elConfirm = driver.findElement(By.xpath("//a/img[contains(@src,'but_ok.gif')]"));
					((JavascriptExecutor)driver).executeScript("arguments[0].parentNode.click();",elConfirm);
					SeleniumMethod.acceptAlert(driver);
				}
			}
		}
		//logout
		driver.switchTo().window(strPreHandler);
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.changeFundIpoStatus() End ......");
	}
	
	public static HashMap<String,String> getSystemDateFromMenuChain(WebDriver driver) {
		HashMap<String,String> mSysDate = new HashMap<String,String>();
		driver.switchTo().window(driver.getWindowHandle());
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		String userInfo = driver.findElement(By.xpath("//table[@class='userinfo']//tr/td[2]")).getText();
		String strSysDate = userInfo.substring(userInfo.indexOf(":")+2,userInfo.indexOf(":")+12);
		String strDay = strSysDate.substring(0,2);
		String strMonth = strSysDate.substring(3,5);
		String strYear = strSysDate.substring(6,10);
		mSysDate.put("SystemDate", strSysDate);
		mSysDate.put("SystemDay", strDay);
		mSysDate.put("SystemMonth", strMonth);
		mSysDate.put("SystemYear", strYear);
		driver.switchTo().window(driver.getWindowHandle());
		return mSysDate;
	}
	
	public static void aggregateAll(WebDriver driver,Map<String, String> data, String aggOrderType) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.aggregateAll() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "汇总订单录入", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		//input fund code 
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("AGGREGATE_NEW_LIST_FUND_CODE"))
					  .sendKeys(data.get("FundCode"));
		//select order type
		Select select = new Select(driver.findElement(By.name("AGGREGATE_NEW_LIST_ORDER_DEAL_TYPE_F")));
		select.selectByValue(aggOrderType);
		
		//click confirm button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_ok.gif')]")).click();
		
		//click submit button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_submit_all.gif')]"))
		  			  .click();
		//if approve confirm alert popup, accept it
		SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		//logout
	    UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.aggregateAll() End ......");
	}

	public static void aggregateOrders(WebDriver driver,Map<String, String> data, String aggOrderDeelType,List<String> orders) throws FileNotFoundException, IOException {
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.aggregateOrders() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "汇总订单录入", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		//input fund code 
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("AGGREGATE_NEW_LIST_FUND_CODE"))
					  .sendKeys(data.get("FundCode"));
		//select order type
		Select select = new Select(driver.findElement(By.name("AGGREGATE_NEW_LIST_ORDER_DEAL_TYPE_F")));
		select.selectByValue(aggOrderDeelType);
		
		//click confirm button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_ok.gif')]")).click();
		
		//choose orders
		if(orders != null && !orders.isEmpty()){
			for (String orderNo : orders) {
				WebElement element = driver.findElement(By.xpath("//td/span[contains(@id,'SPAN_AGGREGATE_NEW_LIST_ORDER_NO') and contains(text(),'"+orderNo+"')]"));
				String elementId = element.getAttribute("id");
				String orderRowNo = elementId.substring(elementId.length()-1);
				//click order record check box
				WebElement checkBox = driver.findElement(By.name("Mark_Approve_" + orderRowNo));
				if(!checkBox.isSelected()){
					checkBox.click();
				}
			}
		}
		
		//click submit button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_submit.gif')]"));
		//if approve confirm alert popup, accept it
		String alertText = SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.aggregateOrders() End ......");
	}
	
	/**
	 * confirm Aggregate Order
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,FundCode
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void confirmAggregateOrder(WebDriver driver,Map<String, String> data,String straAggType) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.confirmAggregateOrder() Start ...... " + straAggType);
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "汇总订单摘要", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//input search criteria fund code
		UTSSeleniumMethod.inputSearchCritria(driver,data.get("FundCode"),"FUND_CODE");
		
		//click aggregate order type tab
		SeleniumMethod.waitForElementLoad(driver, 30, By.name(straAggType))
					  .click();
		/*
		//find aggregate order record row no
		WebElement elAggDate = SeleniumMethod.waitForElementLoad(driver, 10, 
				//By.xpath("//td/span[contains(@id,'SPAN_AGGREGATE_LIST_FH_ORDER_AGGR_D') and contains(text(),'" + UTSSeleniumMethod.getSystemDateFromMenuChain(driver).get("SystemDate") + "')]"));
				
		String strAggDateId = elAggDate.getAttribute("id");
		String strRowNo = strAggDateId.substring(strAggDateId.length()-1);
		//get aggregate order no and click order link
		WebElement elAggOrderLink = driver.findElement(By.id("SPAN_AGGREGATE_LIST_FH_ORDER_NO_" + strRowNo));
		*/
		//can only process first record
		WebElement elAggOrderLink = driver.findElement(By.id("SPAN_AGGREGATE_LIST_FH_ORDER_NO_0"));
		String strAggregateOrderNo = elAggOrderLink.getText();
		elAggOrderLink.click();
		//click edit button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_edit.gif')]")).click();
		
		//input all mandatory fields
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_UNLY_INI_1_PRICE", data.get("挂钩标的初始价1"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_UNLY_INI_2_PRICE", data.get("挂钩标的初始价2"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_UNLY_INI_3_PRICE", data.get("挂钩标的初始价3"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_UNLY_INI_4_PRICE", data.get("挂钩标的初始价4"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_UNLY_INI_5_PRICE", data.get("挂钩标的初始价5"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_CCY_FH_ORDER_NET_SETT_AMT_0", data.get("结算金额"));
		Select select = new Select(driver.findElement(By.name("FH_ORDER_DEAL_FH_ORDER_OPTION_CCY_CODE")));
		select.selectByValue(data.get("期权币种"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_FX_RATE", data.get("期权汇率(期权原币种到产品币种)"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_START_D_DATE", data.get("期权生效日-Day"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_START_D_MONTH", data.get("期权生效日-Month"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_START_D_YEAR", data.get("期权生效日-Year"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_END_D_DATE", data.get("期权期满日-Day"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_END_D_MONTH", data.get("期权期满日-Month"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_OPTION_END_D_YEAR", data.get("期权期满日-Year"));
		SeleniumMethod.keyInputDataByName(driver, "FH_ORDER_DEAL_FH_ORDER_NOTIONAL_1_FUND_AMT", data.get("名义本金(产品币种)"));

		//click the save button
		driver.findElement(By.xpath("//a/img[contains(@src,'but_save.gif')]"))
			  .click();
		SeleniumMethod.acceptAlert(driver);
		//click the submit button
		SeleniumMethod.waitForElementLoad(driver, 20, By.xpath("//a/img[contains(@src,'but_submit.gif')]"))
			  		  .click();
		SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.confirmAggregateOrder() End ......");
	}
	
	/**
	 * Mark All Customer Settlement Success
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,FundCode
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void markAllCustomerSettlementSuccessByFund(WebDriver driver,Map<String, String> data) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "客户结算", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//input search criteria fund code
		UTSSeleniumMethod.inputSearchCritria(driver,data.get("FundCode"),"FUND_CODE");
		
		//click edit button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_edit.gif')]"))
					  .click();
		//click select all button
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("selectall"))
					  .click();
		//click success button
		markSettlementSuccess(driver);
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() End ......");
	}
	/**
	 * Mark Customer Settlement Success by order list
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,OrderList
	 * the format of "OrderList" should be "U0044602,U0044618,U0044642"
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void markCustomerSettlementSuccessByOrderList(WebDriver driver,Map<String, String> data) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "客户结算", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		List<String> liOrderList = Arrays.asList(data.get("OrderList").split(",")) ;
		for (String orderNo : liOrderList) {
			//switch to detail frame
			driver.switchTo().frame("content");
			driver.switchTo().frame("details");
			
			//input search criteria settlement refer code
			UTSSeleniumMethod.inputSearchCritria(driver,orderNo,"SETT_REF_CODE");
			
			//click edit button
			SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_edit.gif')]"))
						  .click();
			//select order record checkbox
			driver.findElement(By.name("Mark_Select_0"))
				  .click();
			
			//click success button
			markSettlementSuccess(driver);
		}
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() End ......");
	}

	private static void markSettlementSuccess(WebDriver driver) {
		//click success button
		String strPreHandler = driver.getWindowHandle();
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_success.gif')]"))
		  .click();
		SeleniumMethod.waiting(3000);
		for (String handler : driver.getWindowHandles()) {
			driver.switchTo().window(handler);
			BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
			if(SeleniumMethod.isElementExist(driver, By.name("success_txt"))){
				driver.findElement(By.name("success_txt"))
					  .sendKeys("auto test " + System.currentTimeMillis());
				driver.findElement(By.xpath("//a/img[contains(@src,'but_ok.gif')]"))
					  .click();
				//SeleniumMethod.acceptAlert(driver);
			}
		}
		driver.switchTo().window(strPreHandler);
	}
	
	/**
	 * Mark All Customer Settlement Success
	 * @param driver
	 * @param data -- Environment,Authority
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void approveAllCustomerSettlementSuccess(WebDriver driver,Map<String, String> data) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveAllCustomerSettlementSuccess() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, "系统", "客户结算审批", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//click select all button
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("selectall"))
					  .click();
		//click approve button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_approve.gif')]"))
					  .click();
		SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.MarkAllCustomerSettlementSuccess() End ......");
	}
	
	/**
	 * approve Aggregate Order
	 * @param driver
	 * @param data -- Environment,Authority,ProductType,FundCode
	 * @throws FileNotFoundException
	 * @throws IOException
	 * 
	 */
	public static void approveAggregateOrder(WebDriver driver,Map<String, String> data,String straAggType) throws FileNotFoundException, IOException{
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveAggregateOrder() Start ......");
	    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
		// chooseMenu
	    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "汇总订单确认审批", null);
		System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		SeleniumMethod.waiting(3000);
		
		//switch to detail frame
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		
		//input search criteria fund code
		UTSSeleniumMethod.inputSearchCritria(driver,data.get("FundCode"),"FUND_CODE");
		
		//click aggregate order type tab
		SeleniumMethod.waitForElementLoad(driver, 30, By.name(straAggType))
					  .click();
		//can only process first record
		WebElement elAggOrderLink = driver.findElement(By.id("SPAN_AGGREGATE_LIST_FH_ORDER_NO_0"));
		String strAggregateOrderNo = elAggOrderLink.getText();
		elAggOrderLink.click();
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveAggregateOrder() AggregateOrderNo : " + strAggregateOrderNo);
		//click approve button
		SeleniumMethod.waitForElementLoad(driver, 30, By.xpath("//a/img[contains(@src,'but_approve.gif')]"))
					  .click();
		SeleniumMethod.acceptAlert(driver);
		SeleniumMethod.acceptAlert(driver);
		
		driver.switchTo().window(driver.getWindowHandle());
		//logout
    	UTSSeleniumMethod.logout(driver, true);
		BaseLogger.info(UTSSeleniumMethod.class, "UTSSeleniumMethod.approveAggregateOrder() End ......");
	}
	
	public static void inputSearchCritria(WebDriver driver,String straSearchText,String straSearchBy){
		//input search criteria fund code
		SeleniumMethod.waitForElementLoad(driver, 30, By.name("SEARCH_TEXT"))
					  .sendKeys(straSearchText);
		//select search criteria by
		Select selectFundCode = new Select(driver.findElement(By.name("SEARCH_BY")));
		selectFundCode.selectByValue(straSearchBy);
		//click search button
		driver.findElement(By.id("IMG_BUT_GO"))
					  .click();
	}
	
	public static void main(String[] args) {
		String userInfo = "系统日期: 06/11/2013 (DD/MM/YYYY)";
		String strSysDate = userInfo.substring(userInfo.indexOf(":")+2,userInfo.indexOf(":")+12);
		String strDay = strSysDate.substring(0,2);
		String strMonth = strSysDate.substring(3,5);
		String strYear = strSysDate.substring(6,10);
		System.out.println(strSysDate);
		System.out.println(strDay);
		System.out.println(strMonth);
		System.out.println(strYear);
	}

}
