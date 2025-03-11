package driverFactory;

import java.lang.System.Logger;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunction.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
WebDriver driver;
String inputpath = "./FileInput/Controller.xlsx";
String outputpath = "./FileOutput/HybridResults.xlsx";
String TCsheet ="MasterTestCases";
ExtentReports reports;
ExtentTest Logger;
public void startTest() throws Throwable
{
	String Module_Status ="";
	String Module_New	=""	;
	//creat referance opbject for accessing excell methid
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in TCsheet 
	for(int i=1;i<=xl.rowCount(TCsheet);i++)
	{
		if(xl.getCellData(TCsheet, i, 2).equals("Y"))
		{
			//read test case form TCsheet
			String TCModule =xl.getCellData(TCsheet, i, 1);
			//define path of html reports
			reports = new ExtentReports("./target/Reports/"+TCModule+"--------"+FunctionLibrary.generateDate()+".html");
			Logger = reports.startTest(TCModule);
			Logger.assignAuthor("kalyan");
			//iterate all rows in Tcmodule 
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//read cell from TCModule sheet
				String Description =xl.getCellData(TCModule, j, 0);
				String ObjectType =xl.getCellData(TCModule, j, 1);
				String LType =xl.getCellData(TCModule, j, 2);
				String Lvalue =xl.getCellData(TCModule, j, 3);
				String TestData =xl.getCellData(TCModule, j, 4);
				try {
					if(ObjectType.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(LType, Lvalue, TestData);
						Logger.log(LogStatus.INFO, Description);
				}
					if(ObjectType.equalsIgnoreCase("typeAction"))
					{
						
						FunctionLibrary.typeAction(LType, Lvalue, TestData);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(LType, Lvalue);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(TestData);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser();
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("dropDownAction"))
					{
						FunctionLibrary.dropDownAction(LType, Lvalue, TestData);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("capturestock"))
					{
						FunctionLibrary.capturestock(LType, Lvalue);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("stockTable"))
					{
						FunctionLibrary.stockTable();
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase(""))
					{
						FunctionLibrary.capturesup(LType, Lvalue);
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable();
						Logger.log(LogStatus.INFO, Description);
					}
					if(ObjectType.equals("caturecus"))
					{
						FunctionLibrary.caturecus(LType, Lvalue);
						Logger.log(LogStatus.INFO, Description);
					
					}
					if(ObjectType.equalsIgnoreCase("customertable"))
					{
						FunctionLibrary.customertable();
						Logger.log(LogStatus.INFO, Description);
					}
					//write as pass into status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "pass", outputpath);
					Module_Status ="True";
					Logger.log(LogStatus.PASS, Description);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					//write as fail in status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					Module_New ="false";
					Logger.log(LogStatus.FAIL, Description);
				}
				if(Module_Status.equalsIgnoreCase("True"))
				{
					//write as pass into TCSheet in status cell
					xl.setCellData(TCsheet, i, 3, "pass", outputpath);
				}
				reports.endTest(Logger);
				reports.flush();
			}
			if(Module_New.equalsIgnoreCase("Fail"))
			{
				//write as fail into tcsheet  in status cell 
				xl.setCellData(TCsheet, i, 3, "Fail", outputpath);
			}
		}
		else
		{
			//ready as blocked testcase which are flaged to N
			xl.setCellData(TCsheet, i, 3, "Blocked", outputpath);
		}
	}
}
}
