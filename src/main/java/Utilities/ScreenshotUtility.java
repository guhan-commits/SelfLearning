package Utilities;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtility {

    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        // Generate timestamp to append to the screenshot name
        String timestamp = new SimpleDateFormat("yyyy_MM_dd__hh_mm_ss").format(new Date());
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
        try {
            // Store screenshots in a specific directory
            FileUtils.copyFile(srcFile, new File("./Screenshots/" + screenshotName + "_" + timestamp + ".png"));
        } catch (IOException e) {
            System.out.println("Error taking screenshot: " + e.getMessage());
        }
    }
}
