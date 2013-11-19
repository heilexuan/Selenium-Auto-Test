package module.login;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.excelsh.core.common.BaseLogger;
import com.excelsh.core.common.GlobalConst;
import com.excelsh.core.common.SeleniumMethod;
import com.excelsh.core.common.SeleniumUtil;
import com.excelsh.uts.common.UTSSeleniumMethod;
import com.thoughtworks.selenium.Selenium;


public class UserLoginTest {
	private WebDriver driver;
	private static UserLoginTest objUserLogin = null;
	
	private final String LOGIN_PROPERTIES = "LOGIN.PROPERTIES";
	
	@BeforeClass
	public void setUp() throws Exception {
		System.setProperty("javax.xml.parsers.DocumentBuilderFactory","com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl");
		System.setProperty("webdriver.ie.driver","./bin/IEDriverServer.exe");
	}
	
	@Parameters({"straEnvironment", "straAuthority"})
	@Test
	public void userLogin(String straEnvironment, String straAuthority) throws FileNotFoundException, IOException {
		driver = SeleniumUtil.getInternetDriver();
		try {
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
			//driver.switchTo().window(driver.getWindowHandle());
			SeleniumMethod.waiting(4000);
			UTSSeleniumMethod.logout(driver, true);
			BaseLogger.info(UserLoginTest.class, "End UserLoginTest.userLogin() end ...... ");
		} catch (org.openqa.selenium.WebDriverException e) {
			System.out.println("Err:" + e.getMessage());
			e.printStackTrace();
			if(driver != null){
				driver.quit();
			}
		}
	}
	
	@AfterClass
	public void tearDown() throws Exception {
    	if(driver != null){
			driver.quit();
		}
	}
}
