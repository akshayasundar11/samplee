package automate;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.Select;

import utils.ExcelUtilities;
import utils.UserFunctions;

public class MercuryTours {

	public static void main(String[] args) throws InterruptedException, IOException {


		/***************************INITIALISE DRIVER*****************************/

		System.setProperty("webdriver.chrome.driver","D:\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://www.newtours.demoaut.com/");
	


		/***************************INITIALISE EXCEL SHEET************************/


		ExcelUtilities excelLoginCredentials = new ExcelUtilities("ExcelData/data.xlsx","SHeet");
		int numberOfUsersForLogin = excelLoginCredentials.getRowCount();


		/***************************LOOP FOR LOGIN*********************************/
		for(int i = 1; i<=numberOfUsersForLogin-1;i++) {
			driver.findElement(By.name("userName")).sendKeys(excelLoginCredentials.getDataFromSheet(i, 1));
			driver.findElement(By.name("password")).sendKeys( excelLoginCredentials.getDataFromSheet(i, 2));
			driver.findElement(By.name("login")).click();

			ExcelUtilities excelFlightDetails = new ExcelUtilities("ExcelData/data.xlsx","Flight"+i);
			int NumberOfFlightDetails = excelFlightDetails.getRowCount();


			/***************************LOOP FOR FLIGHT BOOKING************************/


			for(int j =1 ; j<NumberOfFlightDetails;j++) {

				UserFunctions userFunctions = new UserFunctions();

				//		Selecting the oneway radio button
				List<WebElement> tripType = driver.findElements(By.name("tripType"));
				userFunctions.Dropdown(tripType, excelFlightDetails.getDataFromSheet(j, 1));

				//		Selecting passenger count
				Select drpPassengerCount = new Select(driver.findElement(By.name("passCount")));
				String passangerCount =excelFlightDetails.getDataFromSheet(j, 2);
				drpPassengerCount.selectByVisibleText(passangerCount);

				//		Select depart from place
				Select drpDepartPlace = new Select(driver.findElement(By.name("fromPort")));
				drpDepartPlace.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 3));

				//		Select depart month
				Select drpDepartmonth = new Select(driver.findElement(By.name("fromMonth")));
				drpDepartmonth.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 4));

				//		Select depart Date
				Select drpDepartDate = new Select(driver.findElement(By.name("fromDay")));
				drpDepartDate.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 5));

				//		Select Arriving point
				Select drpArrivingPoint = new Select(driver.findElement(By.name("toPort")));
				drpArrivingPoint.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 6));

				//		Select depart month
				Select drpreturnmonth = new Select(driver.findElement(By.name("toMonth")));
				drpreturnmonth.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 7));

				//		Select depart Date
				Select drpreturnDate = new Select(driver.findElement(By.name("toDay")));
				drpreturnDate.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 8));

				//		Select service class
				List<WebElement> serviceClass = driver.findElements(By.name("servClass"));
				userFunctions.Dropdown(serviceClass, excelFlightDetails.getDataFromSheet(j, 9));

				//		Select Airline
				Select drpAirline = new Select(driver.findElement(By.name("airline")));
				drpAirline.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, 10));

				//		Press Continue button
				driver.findElement(By.name("findFlights")).click();

				//		Out Flight selection
				List<WebElement> outFlight = driver.findElements(By.name("outFlight"));
				userFunctions.Dropdown(outFlight, excelFlightDetails.getDataFromSheet(j, 11));


				//		In Flight selection
				List<WebElement> inFlight = driver.findElements(By.name("inFlight"));
				userFunctions.Dropdown(inFlight, excelFlightDetails.getDataFromSheet(j, 12));

				// 		Reserve flight
				driver.findElement(By.name("reserveFlights")).click();

				//				Card number
				driver.findElement(By.name("creditnumber")).sendKeys(excelFlightDetails.getDataFromSheet(j, 13));




				/************************LOOP FOR PASSANGER DETAILS***********************/

				int columnValue =14;
				//				Passanger Details
				for (int k =0; k< Integer.parseInt(passangerCount) ; k++) {
					driver.findElement(By.name("passFirst"+k)).sendKeys(excelFlightDetails.getDataFromSheet(j, columnValue));
					columnValue++;
					driver.findElement(By.name("passLast"+k)).sendKeys(excelFlightDetails.getDataFromSheet(j, columnValue));
					columnValue++;
					Select drppass1FName = new Select(driver.findElement(By.name("pass."+k+".meal")));
					drppass1FName.selectByVisibleText(excelFlightDetails.getDataFromSheet(j, columnValue));
					columnValue++;
				}





				//		ticketLess check box
				driver.findElements(By.name("ticketLess")).get(0).click();


				//		Delivery address same as Billing address
				driver.findElements(By.name("ticketLess")).get(1).click();

				//		Book Flight
				driver.findElement(By.name("buyFlights")).click();
				
				/******************SCREENSHOT*******************************************/
				File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
				FileHandler.copy(src, new File("F:\\LIBA\\Term 3\\Automation\\bookp"+i+"_"+j+".png"));


				/***************************CHECK POINT*********************************/

				String bookingConfirmation = driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td[2]/table/tbody/tr[4]/td/table/tbody/tr[1]/td[2]/table/tbody/tr[3]/td/p/font/b/font[2]")).getText();
				if(bookingConfirmation.equalsIgnoreCase("Your itinerary has been booked!")) 
				{
					System.out.println("Ticket Booked successfully");
					
				}
				else
				{
					System.out.println("Ticket booking failed");
					
				}
				
				if(j<NumberOfFlightDetails-1) {
					driver.findElement(By.linkText("Flights")).click();	
				}
			}	
			/**********************************LOG OUT*********************************/

			driver.findElement(By.xpath("/html/body/div/table/tbody/tr/td[2]/table/tbody/tr[4]/td/table/tbody/tr[1]/td[2]/table/tbody/tr[7]/td/table/tbody/tr/td[3]/a/img")).click();
		}
		driver.close();
		driver.quit();
	}

}
