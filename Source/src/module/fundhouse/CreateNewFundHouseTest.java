/**
 * 
 */
package module.fundhouse;

import java.util.Map;

import module.order.BEIPOOrderTest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import com.excelsh.core.common.BaseLogger;
import com.excelsh.core.common.SeleniumMethod;
import com.excelsh.core.common.SeleniumUtil;
import com.excelsh.core.common.StaticProvider;
import com.excelsh.uts.common.UTSSeleniumMethod;

/**
 * @author loki
 *
 */
public class CreateNewFundHouseTest {
	
	@Test(dataProvider = "InputDataProvider", dataProviderClass = StaticProvider.class)
	public void createNewFundHouse(Map<String, String> data){
		WebDriver driver = SeleniumUtil.getInternetDriver();
		try {
			BaseLogger.init();
			BaseLogger.info(BEIPOOrderTest.class, "CreateNewFundHouseTest.createNewFundHouse() Start ......");
			//maker login
			UTSSeleniumMethod.login(driver, data.get("Environment"), data.get("Authority"));
			// chooseMenu
			UTSSeleniumMethod.chooseMenu(driver, "结构性理财产品", "产品供应商", null);
			//enter new fund house
			enterNewFundHouse(driver,data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void enterNewFundHouse(WebDriver driver, Map<String, String> data) {
		BaseLogger.info(SeleniumMethod.class, "CreateNewFundHouseTest.enterNewFundHouse() start ...... ");
		//find new fund house script to execute
		driver.switchTo().frame("content");
		driver.switchTo().frame("details");
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(SeleniumMethod.visibilityOfElementLocated(By.name("fmSearch")));
		String sd = driver.getPageSource();
		Document doc = Jsoup.parseBodyFragment(sd);
		Elements achors = doc.select("a");
		for (Element a : achors) {
			String href = a.attr("href");
			if (href.contains("action=I")) {
				href = href.substring(href.indexOf("SubmitForm("),
						href.indexOf("}"));
				((JavascriptExecutor) driver).executeScript(href);
			}
		}
		SeleniumMethod.waiting(2000);
		//fill fund house information
		driver.findElement(By.name("FUND_HOUSE_FH_CODE")).sendKeys(data.get("FH_CODE"));
		driver.findElement(By.name("FUND_HOUSE_FH_NAME")).sendKeys(data.get("FH_NAME"));
		Select select = new Select(driver.findElement(By.name("FUND_HOUSE_FH_NAME")));
		select.selectByValue(data.get(""));
		BaseLogger.info(SeleniumMethod.class, "CreateNewFundHouseTest.enterNewFundHouse() current page : " + driver.getCurrentUrl() + " : " + driver.getWindowHandle());
		
	}
}
