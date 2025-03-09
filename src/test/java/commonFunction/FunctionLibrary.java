package commonFunction;

import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;



public class FunctionLibrary {
	public static WebDriver driver;
	public static Properties conpro;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		conpro = new Properties();
		conpro.load(new FileInputStream("./PropertyFiles/Environment.Properties"));
		if (conpro.getProperty("Browser").equalsIgnoreCase("chrome"))
		{
			driver = new ChromeDriver();
			driver.manage().window().maximize();
		}
		else if(conpro.getProperty("Browser").equalsIgnoreCase("firefox"))
		{
			driver = new FirefoxDriver();
			driver.manage().window().maximize();
		}
		else
		{
			Reporter.log("Browser value is not matching",true);
		}
		return driver;
	}
	//method for launching url
	public static void openUrl()
	{
		driver.get(conpro.getProperty("url"));
	}
//method for wait an element
	public static void waitForElement(String LocatorType,String LocaterValue,String TestData)
	{
		WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(Integer.parseInt(TestData)));
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(TestData)));
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.name(TestData)));
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//wait until element is visible
			mywait.until(ExpectedConditions.visibilityOfElementLocated(By.id(TestData)));
		}
	}
	public static void typeAction(String LocatorType,String LocaterValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(TestData)).clear();
			driver.findElement(By.xpath(TestData)).sendKeys(TestData);
		}
		if (LocatorType.equals("name")) 
		{
			driver.findElement(By.name(TestData)).clear();
			driver.findElement(By.name(TestData)).sendKeys(TestData);
		}
		if (LocatorType.equals("id")) 
		{
			driver.findElement(By.id(TestData)).clear();
			driver.findElement(By.id(TestData)).sendKeys(TestData);
		}
	}
	//method for button,checkbox,radio button,images,links
	public static void clickAction(String LocatorType,String LocaterValue)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(LocaterValue)).click();
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(LocaterValue)).click();
		}
		if (LocatorType.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(LocaterValue)).sendKeys(Keys.ENTER);
			
		}

	}
	//method for validate page title
	public static void validateTitle(String Expected_Title)
	{
		String Actual_title = driver.getTitle();
		try {
			assertEquals(Actual_title, Expected_Title,"Title is not Matching");
		} catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	}
	//method for close browser
	public static void closeBrowser()
	{
		driver.quit();
	}
	//method for listbox
	public static void dropDownAction(String LocatorType,String LocaterValue,String TestData)
	{
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			//convert test data cell into int
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.xpath(LocaterValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			//conver testdata cell into int
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.name(LocaterValue)));
			element.selectByIndex(value);
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			//convert testdata cell into int
			int value = Integer.parseInt(TestData);
			Select element = new Select(driver.findElement(By.id(LocaterValue)));
			element.selectByIndex(value);
		}
	}
	//method capture stock number into notepad
	public static void capturestock(String LocatorType,String LocaterValue) throws Throwable
	{
		String StockNumber ="";
		if(LocatorType.equalsIgnoreCase("xpath"))
		{
			StockNumber = driver.findElement(By.xpath(LocaterValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("name"))
		{
			StockNumber =driver.findElement(By.name(LocaterValue)).getAttribute("value");
		}
		if(LocatorType.equalsIgnoreCase("id"))
		{
			StockNumber =driver.findElement(By.id(LocaterValue)).getAttribute("value");
		}
		//writing stock number into notepad
		FileWriter fw = new FileWriter("./CaptureData/Stocknumber.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(StockNumber);
		bw.flush();
		bw.close();
	}
	//verify stocknumber in stock table
	public static void stockTable() throws Throwable
	{
		//read path of notepad file
		FileReader fr = new FileReader("./CaptureData/Stocknumber.txt");
		BufferedReader br = new BufferedReader(fr);
		String Exp_Data =br.readLine();
		//click searc panel if search textbox is not displayd
		if(!driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).isDisplayed());
		//click search panel
		driver.findElement(By.xpath(conpro.getProperty("search-panel"))).click();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).clear();
		driver.findElement(By.xpath(conpro.getProperty("search-textbox"))).sendKeys(Exp_Data);
		driver.findElement(By.xpath(conpro.getProperty("search-button"))).click();
		Thread.sleep(3000);
		String Act_Data = driver.findElement(By.xpath("//table[@class='table ewTable']/tbody/tr[1]/td[8]/div/span/span")).getText();
		Reporter.log(Exp_Data+"        "+Act_Data,true);
		try {
			Assert.assertEquals(Exp_Data,Act_Data, "Stock Number not found in table");
		} catch (Exception a) {
			System.out.println(a.getMessage());
		}
	}
}
