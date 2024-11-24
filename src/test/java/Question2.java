import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import Utilities.BrowserFactory;
import Utilities.ExcelUtility;
import Utilities.ScreenshotUtility;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class Question2 {
    WebDriver driver;
    WebDriverWait wait;
    ExcelUtility excelUtility;
    String filePath = ".\\src\\test\\resources\\data\\TestData.xlsx";
    String sheetName = "Sheet1";
    FileWriter writer;

    @BeforeClass
    public void setUp() throws IOException {
        // Initialize WebDriver using BrowserFactory
        driver = BrowserFactory.getBrowser("chrome");  // Browser can be dynamically set via config or method arguments
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize Excel Utility
        excelUtility = new ExcelUtility(filePath, sheetName);

        // Initialize FileWriter to log results
        writer = new FileWriter(".\\src\\test\\resources\\data\\test_results.txt");

        // Navigate to the URL
        driver.get("https://mockexam2cpsat.agiletestingalliance.org/");
    }

    @DataProvider(name = "candidateData")
    public Object[][] getCandidateData() {
        // Fetch data from Excel using the ExcelUtility
        return excelUtility.getExcelData();
    }

    @Test(dataProvider = "candidateData")
    public void verifyCandidateDetails(String test, String city, String yearEstablished, String candidatesAppeared) throws IOException, InterruptedException {
        // Navigate to "ABOUT" menu
        WebElement aboutMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(text(),'About')])[2]")));
        aboutMenu.click();
        Thread.sleep(5000);
        // Ensure the application starts on the first page

        WebElement page1Link = driver.findElement(By.xpath("//li[@class='footable-page visible active']/a[@class='footable-page-link' and @href='#']"));

        // Click on the "Page 1" link
        page1Link.click();
        Thread.sleep(5000);
       // WebElement firstButton = driver.findElement(By.xpath("//a[contains(text(),'«')]"));  // "First" button
       // if (firstButton.isDisplayed()) {
           // firstButton.click();
           // wait.until(ExpectedConditions.stalenessOf(driver.findElement(By.id("footable_483"))));  // Wait until the table is refreshed
       // }

        boolean matchFound = false;
        int pageCounter = 0;  // Counter to limit the check to only 2 pages

        do {
            // Wait for the table to load
            WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("footable_483")));
            List<WebElement> rows = table.findElements(By.xpath("//table[@id='footable_483']/tbody/tr"));

            // Scroll down to the table (if necessary)
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true);", table);

            // Loop through the web table rows and match data from Excel
            for (WebElement row : rows) {
                List<WebElement> columns = row.findElements(By.tagName("td"));

                if (columns.size() > 0) {
                    String webCity = row.findElement(By.xpath("td[1]")).getText();
                    String webYear = row.findElement(By.xpath("td[2]")).getText();
                    String webCandidates = row.findElement(By.xpath("td[3]")).getText();

                    // Check if the row matches with the Excel data
                    if (webCity.equals(city) && webYear.equals(yearEstablished) && webCandidates.equals(candidatesAppeared)) {
                        matchFound = true;
                        break;
                    }
                }
            }

            // If a match is found, break out of the loop
            if (matchFound) {
                break;
            }

            // Check only one more page if no match found on the first page
            if (pageCounter == 0) {
                // Click on "Next" button to move to the second page
                WebElement nextButton = driver.findElement(By.xpath("//a[contains(text(),'›')]"));
                nextButton.click();
                Thread.sleep(2000);
                //wait.until(ExpectedConditions.stalenessOf(table));  // Wait for the new table to load
                pageCounter++;  // Increment the page counter to restrict to only 2 pages
            } else {
                break;  // Exit loop after checking two pages (1st and 2nd)
            }

        } while (true);

        // Log the result to a file
        if (matchFound) {
            writer.write(test + " <" + city + ", " + yearEstablished + ", " + candidatesAppeared + "> Passed\n");
        } else {
            writer.write(test + " <" + city + ", " + yearEstablished + ", " + candidatesAppeared + "> Failed\n");
            ScreenshotUtility.takeScreenshot(driver, test + "_Failed");
        }

        // Assert that a match is found for each row
        Assert.assertTrue(matchFound, "Matching row not found for Test: " + test);
    }

    @AfterMethod
    public void captureFailure(ITestResult result) {
        // If the test fails, take a screenshot
        if (ITestResult.FAILURE == result.getStatus()) {
            ScreenshotUtility.takeScreenshot(driver, result.getName() + "_Failure");
        }
    }

    @AfterClass
    public void tearDown() throws IOException {
        // Close WebDriver
        BrowserFactory.quitBrowser();

        // Close the Excel Utility
        excelUtility.close();

        // Close FileWriter
        if (writer != null) {
            writer.close();
        }
    }
}
