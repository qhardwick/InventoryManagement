package com.skillstorm.cucumber;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemTableSteps {

    WebDriver driver;
    WebDriverWait wait;
    private List<Integer> addedItemIds = new ArrayList<>();

    @Given("I am on the Items page")
    public void i_am_on_the_items_page() {
        driver = SingletonDriver.getChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://52.90.145.230/items");
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

            // Ensure the correct number of cells in each row
            Assertions.assertEquals(5, cells.size(), "Expected 5 cells in the table row, but found: " + cells.size());

            // Compare the expected values with the actual table values
            Assertions.assertEquals(expectedRow.get("Name"), cells.get(1).getText());
            Assertions.assertEquals(expectedRow.get("Volume"), cells.get(2).getText());
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
        nameField.sendKeys(itemDetails.get(0).get("Name"));

        volumeField.clear();
        volumeField.sendKeys(itemDetails.get(0).get("Volume"));
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
        Map<String, String> newItem = dataTable.asMaps(String.class, String.class).get(0);
        String expectedName = newItem.get("Name");
        String expectedVolume = newItem.get("Volume");

        // Wait for the row containing the expected name to appear
        String xpath = "//table/tbody/tr[td[2][text()='" + expectedName + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        // Find the cells in the row
        List<WebElement> cells = row.findElements(By.tagName("td"));

        System.out.println("Number of cells found: " + cells.size());
        for (int i = 0; i < cells.size(); i++) {
            System.out.println("Cell " + i + ": " + cells.get(i).getText());
        }

        // Ensure there are 5 cells
        Assertions.assertEquals(5, cells.size(), "Expected 5 cells in the table row, but found: " + cells.size());

        // Compare the expected values with the actual table values
        Assertions.assertEquals(expectedName, cells.get(1).getText());
        Assertions.assertEquals(expectedVolume, cells.get(2).getText());

        // Get the item ID from the first cell and store it
        String idText = cells.get(0).getText();
        int itemId = Integer.parseInt(idText.trim());
        addedItemIds.add(itemId);
    }

    @Given("the following item exists:")
    public void the_following_item_exists(io.cucumber.datatable.DataTable dataTable) {
        // Get item details from the feature file
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> itemData : items) {
            // Create an ItemDto object
            ItemDto itemDto = new ItemDto();
            itemDto.setName(itemData.get("Name"));
            itemDto.setVolume(Integer.parseInt(itemData.get("Volume")));

            // Send a POST request to add the item to the backend
            RestTemplate restTemplate = new RestTemplate();
            String url = "http://3.95.37.62:8080/inventory/items";
            ItemDto createdItem = restTemplate.postForObject(url, itemDto, ItemDto.class);
            addedItemIds.add(createdItem.getId());
        }
    }

    @When("I click the delete button for the item named {string}")
    public void i_click_the_delete_button_for_the_item_named(String name) {
        // Wait for the table to be visible
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody")));

        // Wait for the specific row to be present
        String xpath = "//table/tbody/tr[td[2][text()='" + name + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        // Wait for the delete button to be clickable
        WebElement deleteButton = row.findElement(By.cssSelector(".btn-icon"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();

        // Wait for the row to be removed from the table
        wait.until(ExpectedConditions.stalenessOf(row));
    }

    @When("I click the edit button for the item named {string}")
    public void i_click_the_edit_button_for_the_item_named(String name) {
        // Wait for the table to be visible
        WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody")));

        // Wait for the specific row to be present
        String xpath = "//table/tbody/tr[td[2][text()='" + name + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        // Wait for the edit button to be clickable
        WebElement editButton = row.findElement(By.cssSelector(".btn-edit"));
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();
    }

    @When("I update the volume to {int}")
    public void i_update_the_volume_to(Integer volume) {
        // Wait for the volume input field to be visible
        WebElement volumeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("volume")));

        // Clear the field and enter the new volume
        volumeField.clear();
        volumeField.sendKeys(volume.toString());

        // Submit the updated item
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();
    }

    @Then("the item should be updated in the table with:")
    public void the_item_should_be_updated_in_the_table_with(io.cucumber.datatable.DataTable dataTable) {
        // Get updated item details
        Map<String, String> updatedItem = dataTable.asMaps(String.class, String.class).get(0);
        String expectedName = updatedItem.get("Name");
        String expectedVolume = updatedItem.get("Volume");

        // Wait for the row containing the expected name to appear
        String xpath = "//table/tbody/tr[td[2][text()='" + expectedName + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        // Find the cells in the row
        List<WebElement> cells = row.findElements(By.tagName("td"));

        System.out.println("Number of cells found: " + cells.size());
        for (int i = 0; i < cells.size(); i++) {
            System.out.println("Cell " + i + ": " + cells.get(i).getText());
        }

        // Ensure there are 5 cells
        Assertions.assertEquals(5, cells.size(), "Expected 5 cells in the table row, but found: " + cells.size());

        // Compare the expected volume with the actual table value
        Assertions.assertEquals(expectedVolume, cells.get(2).getText());
    }

    @Then("the item should be removed from the table")
    public void the_item_should_be_removed_from_the_table() {
        // Wait until the row with the item is no longer present
        String xpath = "//table/tbody/tr[td[2][text()='Stroller']]";
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));

        // Verify that the row has been removed from the table
        List<WebElement> tableRows = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals(0, tableRows.size(), "Expected the item to be removed, but it is still present.");
    }

    @After
    public void tearDown() {
        // Delete test items from the backend
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://3.95.37.62:8080/inventory/items/";

        for (Integer itemId : addedItemIds) {
            try {
                restTemplate.delete(url + itemId);
            } catch (Exception e) {
                // Handle exception if item does not exist
                System.out.println("Failed to delete item with ID: " + itemId);
            }
        }

        SingletonDriver.quitDriver();
    }
}
