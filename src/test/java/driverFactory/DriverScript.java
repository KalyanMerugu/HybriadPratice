package driverFactory;

import org.openqa.selenium.WebDriver;

import commonFunction.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
WebDriver driver;
String inputpath = "./FileInput/Controller.xlsx";
String outputpath = "./FileOutput/HybridResults.xlsx";
String TCsheet ="MasterTestCases";
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
			//iterate all rows in Tcmodule 
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				//read cell from TCModule sheet
				String Description =xl.getCellData(TCModule, j, 0);
				String ObjectType =xl.getCellData(TCModule, j, 1);
				String LType =xl.getCellData(TCModule, 0, 2);
				String Lvalue =xl.getCellData(TCModule, j, 3);
				String TestData =xl.getCellData(TCModule, j, 4);
				try {
					if(ObjectType.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
					}
					if(ObjectType.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl();
					}
					if(ObjectType.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(LType, Lvalue, TestData);
					}
					if(ObjectType.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(LType, Lvalue, TestData);
					}
					if(ObjectType.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.typeAction(LType, Lvalue, TestData);
					}
					if(ObjectType.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(TestData);
					}
					if(ObjectType.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser();
					}
					//write as pass into status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "pass", outputpath);
					Module_Status ="True";
				} catch (Exception e) {
					System.out.println(e.getMessage());
					//write as fail in status cell in TCModule sheet
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					Module_New ="false";
				}
				if(Module_Status.equalsIgnoreCase("True"))
				{
					//write as pass into TCSheet in status cell
					xl.setCellData(TCsheet, i, 3, "pass", outputpath);
				}
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
