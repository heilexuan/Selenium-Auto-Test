package module.order;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import module.login.UserLoginTest;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.excelsh.core.common.BaseLogger;
import com.excelsh.core.common.GlobalConst;
import com.excelsh.core.common.SeleniumMethod;
import com.excelsh.core.common.SeleniumUtil;
import com.excelsh.core.common.StaticProvider;
import com.excelsh.uts.common.UTSSeleniumMethod;

public class BEIPOOrderTest {
	private WebDriver driver;
	
	private String strOrderNo = null;
	private String strAggregateOrderNo = null;
	
	@BeforeClass
	public void setUp() throws Exception {
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("webdriver.ie.driver","./bin/IEDriverServer.exe");
	}
  
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class)
  public void createNewIPOOrder(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.createNewIPOOrder() Start ......");
		    UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
			// chooseMenu
		    UTSSeleniumMethod.chooseMenu(driver, data.get("ProductType"), "客户订单录入", null);
			//switch to detail frame
			System.out.println(driver.getCurrentUrl() + " : " + driver.getWindowHandle());
			
			//enter new order page
			strOrderNo = UTSSeleniumMethod.enterNewOrder(driver,data);
			
	  } catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.createNewIPOOrder() End ......");
	    //return bResult;
  }
  
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class,dependsOnMethods={"createNewIPOOrder"})
  public void approveNewOrder(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.approveNewOrder() Start ......");
		    
		    //approve if order no can be read
		    if(!StringUtils.isEmpty(strOrderNo)){
		    	UTSSeleniumMethod.approveOrder(driver, data, strOrderNo);
		    }
			
	  } catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.approveNewOrder() End ......");
	    //return bResult;
  }
	
  //,dependsOnMethods={"approveNewOrder"}
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class)
  public void processFundIpoStatus(Map<String, String> data){
	  boolean bResult = true;
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.processFundIpoStatus() Start ......");
		    
		    //if(!StringUtils.isEmpty(strOrderNo)){
	    		//1. change order refer fund IPO date to system date
		    	driver = SeleniumUtil.getInternetDriver();
	    		UTSSeleniumMethod.changeIpoMonitorDate2SysDate(driver, data);
	    		//2. approve Ipo Monitor Date Change
	    		driver = SeleniumUtil.getInternetDriver();
	    		data.put("Authority", "checker");
	    		UTSSeleniumMethod.approveIpoMonitorDateChange(driver, data);
	    		//3. close fund ipo
	    		driver = SeleniumUtil.getInternetDriver();
	    		data.put("Authority", "maker");
	    		UTSSeleniumMethod.changeFundIpoStatus(driver, data, GlobalConst.IPO_MONITOR_ACTION_CLOSE);
	    		//4. approve fund ipo close
	    		driver = SeleniumUtil.getInternetDriver();
	    		data.put("Authority", "checker");
	    		UTSSeleniumMethod.changeFundIpoStatus(driver, data, GlobalConst.IPO_MONITOR_ACTION_APPROVE);
		    	//5. proceed fund ipo
	    		driver = SeleniumUtil.getInternetDriver();
		    	data.put("Authority", "maker");
		    	UTSSeleniumMethod.changeFundIpoStatus(driver, data, GlobalConst.IPO_MONITOR_ACTION_PROCEED);
		    	//6. approve fund ipo proceed
		    	driver = SeleniumUtil.getInternetDriver();
		    	data.put("Authority", "checker");
		    	UTSSeleniumMethod.changeFundIpoStatus(driver, data, GlobalConst.IPO_MONITOR_ACTION_APPROVE);
		    	
		    //}
			
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.processFundIpoStatus() End ......");
	    //return bResult;
  }
 
  //,dependsOnMethods={"processFundIpoStatus"}
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class,dependsOnMethods={"processFundIpoStatus"})
  public void aggregateAllOrders(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.aggregateAllOrders() Start ......");
		    
		    //approve if order no can be read
	    	UTSSeleniumMethod.aggregateAll(driver, data,GlobalConst.AGG_ORDER_DEEL_TYPE_SUB);
		    
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.aggregateAllOrders() End ......");
	    //return bResult;
  }
  
  //,dependsOnMethods={"aggregateAllOrders"}
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class,dependsOnMethods={"aggregateAllOrders"})
  public void confirmAggregateOrder(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.confirmAggregateOrder() Start ......");
		    
		    //confirm aggregate order
	    	UTSSeleniumMethod.confirmAggregateOrder(driver, data, GlobalConst.AGG_ORDER_TYPE_SUB);
		    
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.confirmAggregateOrder() End ......");
	    //return bResult;
  }
  //,dependsOnMethods={"confirmAggregateOrder"}
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class,dependsOnMethods={"confirmAggregateOrder"})
  public void processAllCustomerSettlement(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.processAllCustomerSettlement() Start ......");
		    
		    //mark All Customer Settlement Success by fund code
	    	UTSSeleniumMethod.markAllCustomerSettlementSuccessByFund(driver, data);
	    	//approve
	    	driver = SeleniumUtil.getInternetDriver();
    		data.put("Authority", "checker");
    		UTSSeleniumMethod.approveAllCustomerSettlementSuccess(driver, data);
		    
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.processAllCustomerSettlement() End ......");
	    //return bResult;
  }
  
  @Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class,dependsOnMethods={"processAllCustomerSettlement"})
  public void approveAggregateOrder(Map<String, String> data) {
	  boolean bResult = true;
	  driver = SeleniumUtil.getInternetDriver();
	  try {
		    BaseLogger.init();
		    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.approveAggregateOrder() Start ......");
		    
		    //approve if order no can be read
	    	UTSSeleniumMethod.approveAggregateOrder(driver, data,GlobalConst.AGG_ORDER_TYPE_SUB);
		    
		} catch (Exception e) {
			e.printStackTrace();
			BaseLogger.error(BEIPOOrderTest.class, e.getMessage());
			bResult = false;
		}finally{
			if(driver != null){
				driver.manage().deleteAllCookies();
				driver.quit();
			}
		}
	    BaseLogger.info(BEIPOOrderTest.class, "BEIPOOrderTest.approveAggregateOrder() End ......");
	    //return bResult;
  }
  
    @AfterClass
	public void tearDown() throws Exception {
    	if(driver != null){
			driver.quit();
		}
	}
}
