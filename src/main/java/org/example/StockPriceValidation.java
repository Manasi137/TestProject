package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class StockPriceValidation {

    public static void main(String[] args) throws IOException {

        //#TODO Read date from xls file and insert into hashmap
        Map<String, Double> xlsData = readDataFromXLSFile(ConfigValues.XLS_PATH);
        System.out.println("xl data" + xlsData);
        //#TODO Read data from website using selenium WebDriver and store in HashMap2
        Map<String, Double> websiteData = readDataFromWebsite();

//		Step3	: Compare the values stored in the two HashMap
        //#TODO Compare the values store into the two HashMaps
        compareDataOfHashMaps(xlsData, websiteData);

    }

    /*
      @param : filePath : XLS file path to read data
    */
    private static Map<String, Double> readDataFromXLSFile(String filePath) throws IOException {

        Map<String, Double> xlsFileData = new HashMap<>();
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet workSheet = workbook.getSheetAt(0); // Data available in 1 sheet for reading

        //#TODO Iterate each row data in the sheet
        for (Row row : workSheet) {
            Cell companyNameCell = row.getCell(0);
            Cell priceCell = row.getCell(1);

            //#TODO Retrives the string value of the company n ame cell and store it in companyName , initializes a variable price to store the price value
            if (companyNameCell != null && priceCell != null) {
                String companyName = companyNameCell.getStringCellValue();
                double price;
                if (priceCell.getCellType() == CellType.NUMERIC) {
                    price = priceCell.getNumericCellValue();
                } else if (priceCell.getCellType() == CellType.STRING) {
                    try {
                        price = Double.parseDouble(priceCell.getStringCellValue());
                    } catch (NumberFormatException e) {
                        price = 0.0;
                    }
                } else {
                    price = 0.0;
                }
                xlsFileData.put(companyName, price);
            }
        }

        workbook.close();
        fileInputStream.close();

        return xlsFileData;
    }

    private static Map<String, Double> readDataFromWebsite() {
        System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://money.rediff.com/losers/bse/daily/groupall");

        Map<String, Double> data = new HashMap<>();
        for (WebElement row : driver.findElements(By.cssSelector(".dataTable tbody tr"))) {
            String stockName = row.findElement(By.cssSelector("td:nth-child(1)")).getText();
            Double stockPrice = Double.parseDouble(row.findElement(By.cssSelector("td:nth-child(4)")).getText());
            data.put(stockName, stockPrice);
        }

        driver.quit();
        return data;

    }

    private static void compareDataOfHashMaps(Map<String, Double> xlsData, Map<String, Double> websiteData) {

        for (Map.Entry<String, Double> entry : xlsData.entrySet()) {
            String stockName = entry.getKey();
            double xlsPrice = entry.getValue();
//			If the stock name exists in the map, its corresponding price is returned.
//			If the stock name does not exist in the map, the default value -1.0 is returned.
            double websitePrice = websiteData.getOrDefault(stockName, -1.0); // default values if key not dound

            if (websitePrice == -1.0) {
                System.out.println("Stock price for " + stockName + " not found on website.");
            } else if (xlsPrice != websitePrice) {
                System.out.println("Discrepancy found for " + stockName + ": XLS price = " + xlsPrice
                        + ", Website price = " + websitePrice);
            }
        }
    }
}
