//package tests;

import Utilities.BrowserFactory;
import Utilities.ScreenshotUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public class Question3 {

    private WebDriver driver;
    private Question3POM question3POM;

    WebDriverWait wait;

    @Before
    public void setUp() {
        driver = BrowserFactory.getBrowser("chrome");  // Browser can be dynamically set via config or method arguments
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://mockexam2cpsat.agiletestingalliance.org/");
        //System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        // driver = new ChromeDriver();
        //driver.manage().window().maximize();
        //driver.get("https://mockexam2cpsat.agiletestingalliance.org/");
        question3POM = new Question3POM(driver);
    }



    @Test
    public void testSearchYearWiseParticipants() throws InterruptedException {
        // Navigate to "ABOUT" menu
        // driver.findElement(By.xpath("//a[text()='ABOUT']")).click();

        WebElement aboutMenu = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(text(),'About')])[2]")));
        aboutMenu.click();
        sleep(5000);

        // Scroll down to List of Candidates table
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("window.scrollBy(0,500);");

        // Search for participants by year
        List<String> participants = question3POM.searchYearWiseParticipants("199");

        // Expected results
        List<String> expectedResults = List.of("City 3 -> 6000+", "City 4 -> 8000+");

        // Assertion
        assertEquals(expectedResults, participants);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
