//package pages;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class Question3POM {

    private WebDriver driver;

    // Locators
    private By rowsLocator = By.xpath("//table/tbody/tr");
    private By nextButton = By.xpath("//a[contains(@class, 'next')]"); // Next button in pagination
    private By yearColumnLocator = By.xpath("./td[2]"); // 2nd column is 'Year Established'
    private By cityColumnLocator = By.xpath("./td[1]"); // 1st column is 'City'
    private By participantsColumnLocator = By.xpath("./td[3]"); // 3rd column is 'No. Candidates Appeared'

    // Constructor
    public Question3POM(WebDriver driver) {
        this.driver = driver;
    }

    // Method to search year-wise participants
    @Test
    public List<String> searchYearWiseParticipants(String year) {
        List<String> participantsList = new ArrayList<>();

        boolean hasNextPage = true;
        while (hasNextPage) {
            // Get all rows in the current page
            List<WebElement> rows = driver.findElements(rowsLocator);

            // Iterate through rows and find matches
            for (WebElement row : rows) {
                String yearValue = row.findElement(yearColumnLocator).getText();
                if (yearValue.contains(year)) {
                    String city = row.findElement(cityColumnLocator).getText();
                    String participants = row.findElement(participantsColumnLocator).getText();
                    participantsList.add(city + " -> " + participants);
                }
            }

            // Check if the next button is enabled, then click it
            WebElement nextBtn = driver.findElement(nextButton);
            if (nextBtn.isEnabled()) {
                nextBtn.click();
                try {
                    Thread.sleep(1000); // Allow table to reload
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                hasNextPage = false;
            }
        }

        return participantsList;
    }
}
