import com.sun.javafx.PlatformUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class FlightBookingTest {

	public WebDriver driver;

	@BeforeTest
	public void before(){
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		setDriverPath();
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);	
		driver.get("https://www.cleartrip.com/");
	}

	@Test
	public void testThatResultsAppearForAOneWayJourney() throws InterruptedException {
		
		driver.findElement(By.id("OneWay")).click();
		
		selectValueAjax("FromTag", "ui-id-1", "Bangalore");
		
		selectValueAjax("toTag", "ui-id-2", "Delhi");

		// select current date
		driver.findElement(By.xpath(".//*[@id='ui-datepicker-div']/div[1]/table//a[@class='ui-state-default ui-state-highlight ui-state-active']")).click();

		//all fields filled in. Now click on search
		driver.findElement(By.id("SearchBtn")).click();

		explicitWait(driver, By.className("searchSummary"));
		
		//verify that result appears for the provided journey search
		Assert.assertTrue(isElementPresent(By.className("searchSummary")));



	}

	@AfterTest
	public void afterTest(){
		//close the browser
		driver.quit();
	}
	
	
	
	private void waitFor(int durationInMilliSeconds) {
		try {
			Thread.sleep(durationInMilliSeconds);
		} catch (InterruptedException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	
	public void selectValueAjax(String textboxID, String dropdownID, String searchValue){
		try {
			driver.findElement(By.id(textboxID)).clear();
			driver.findElement(By.id(textboxID)).sendKeys(searchValue);

			//wait for the auto complete options to appear for the origin
			explicitWait(driver, By.id(dropdownID));
			List<WebElement> originOptions = driver.findElement(By.id(dropdownID)).findElements(By.tagName("li"));
			originOptions.get(0).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void explicitWait(WebDriver driver,By locator){
		try {
			WebDriverWait wait = new WebDriverWait(driver, 45);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private void setDriverPath() {
		if (PlatformUtil.isMac()) {
			System.setProperty("webdriver.chrome.driver", "chromedriver");
		}
		if (PlatformUtil.isWindows()) {
			System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		}
		if (PlatformUtil.isLinux()) {
			System.setProperty("webdriver.chrome.driver", "chromedriver_linux");
		}
	}
}
