package com.skillstorm.cucumber;

import com.skillstorm.dtos.ItemDto;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ItemTableSteps {

    WebDriver driver;
    WebDriverWait wait;

    @Given("I am on the Items page")
    public void i_am_on_the_items_page() {
        WebDriverManager.chromedriver().driverVersion("129.0.6668.59").setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Initialize WebDriverWait with a timeout of 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://localhost:5173/items");
    }

    @Then("I should see the following items:")
    public void i_should_see_the_following_items(io.cucumber.datatable.DataTable dataTable) {
        // Get rows of data from the feature file
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Wait for the table to be present
        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody")));

        // Verify each row in the table
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> expectedRow = rows.get(i);
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName("td"));
            Assertions.assertEquals(expectedRow.get("Part Number"), cells.get(0).getText()); // Part Number
            Assertions.assertEquals(expectedRow.get("Name"), cells.get(1).getText());        // Name
            Assertions.assertEquals(expectedRow.get("Volume"), cells.get(2).getText());      // Volume
        }
    }

    @When("I click the Add Item button")
    public void i_click_the_add_item_button() {
        // Wait for the Add Item button to be clickable
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add")));
        addButton.click();
    }

    @When("I enter the following details:")
    public void i_enter_the_following_details(io.cucumber.datatable.DataTable dataTable) {
        // Get item details from the feature file
        List<Map<String, String>> itemDetails = dataTable.asMaps(String.class, String.class);

        // Wait for the form fields to be visible
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        WebElement volumeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("volume")));

        nameField.clear();
        nameField.sendKeys(itemDetails.get(0).get("name"));         // Name

        volumeField.clear();
        volumeField.sendKeys(itemDetails.get(0).get("volume"));     // Volume
    }

    @When("I submit the item")
    public void i_submit_the_item() {
        // Wait for the submit button to be clickable
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();
    }

    @Then("the new item should be added to the table with:")
    public void the_new_item_should_be_added_to_the_table_with(io.cucumber.datatable.DataTable dataTable) {
        // Get the new item details
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        // Wait for the table to be updated
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("table tbody tr"), 0));

        // Find the table and its rows
        WebElement table = driver.findElement(By.cssSelector("table tbody"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        WebElement lastRow = tableRows.get(tableRows.size() - 1); // The last row is the new item

        // Wait for the cells in the last row to be visible
        wait.until(ExpectedConditions.visibilityOfAllElements(lastRow.findElements(By.tagName("td"))));

        List<WebElement> cells = lastRow.findElements(By.tagName("td"));
        Assertions.assertEquals(rows.get(0).get("Part Number"), cells.get(0).getText()); // Part Number
        Assertions.assertEquals(rows.get(0).get("Name"), cells.get(1).getText());        // Name
        Assertions.assertEquals(rows.get(0).get("Volume"), cells.get(2).getText());      // Volume
    }

    @Given("the following item exists:")
    public void the_following_item_exists(io.cucumber.datatable.DataTable dataTable) {
        // Get item details from the feature file
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> itemData : items) {
            // Create an ItemDto object
            ItemDto itemDto = new ItemDto();
            itemDto.setId(Integer.parseInt(itemData.get("Part Number")));
            itemDto.setName(itemData.get("Name"));
            itemDto.setVolume(Integer.parseInt(itemData.get("Volume")));

            // Send a POST request to add the item to the backend
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8080/inventory/items";
            restTemplate.postForObject(url, itemDto, ItemDto.class);
        }
    }

    @When("I click the delete button for the item with Part Number {int}")
    public void i_click_the_delete_button_for_the_item_with_part_number(Integer partNumber) {
        // Wait for the table to be visible
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody")));

        // Wait for the specific row to be present
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='" + partNumber + "']/..")));

        // Find the row with the given part number
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));

        for (WebElement row : tableRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(0).getText().equals(partNumber.toString())) {
                // Wait for the delete button to be clickable
                WebElement deleteButton = row.findElement(By.cssSelector(".btn-icon"));
                wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
                deleteButton.click();
                break;
            }
        }
    }

    @Then("the item should be removed from the table")
    public void the_item_should_be_removed_from_the_table() {
        // Wait until the row with the item is no longer present
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("table tbody tr")));

        // Verify that the row has been removed from the table
        List<WebElement> tableRows = driver.findElements(By.cssSelector("table tbody tr"));
        Assertions.assertEquals(0, tableRows.size()); // If the item is removed, there should be no rows
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
