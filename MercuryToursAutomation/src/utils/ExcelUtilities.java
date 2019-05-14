package utils;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtilities {
	static String projectPath;
	XSSFWorkbook workbook;
	XSSFSheet sheet;

	/************* Constructor to initialize workbook and sheet **************/

	public ExcelUtilities(String excelPath, String sheetName) {
		try {

			projectPath =  System.getProperty("user.dir");
			workbook = new XSSFWorkbook(projectPath+"/"+excelPath);
			sheet = workbook.getSheet(sheetName);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	/************* To know number of data in sheet **************/

	public  int getRowCount() {
		try {
			int rowCount = sheet.getPhysicalNumberOfRows();
			
			return rowCount;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return 0;
	}

	/************* Get data from the sheet **************/
	public String getDataFromSheet(int rowNumber, int columnNumber) {
		String value = null;

		//		For String Values
		try {
			value =  sheet.getRow(rowNumber).getCell(columnNumber).getStringCellValue();
		}
		catch(Exception e){
			//			For Numeric Values
			if(e.getMessage().equals("Cannot get a STRING value from a NUMERIC cell")) {
				value =  Integer.toString ((int)(sheet.getRow(rowNumber).getCell(columnNumber).getNumericCellValue()));
			}else {
				System.out.println(e.getMessage());
			}
		}

		return value;

	}

}
