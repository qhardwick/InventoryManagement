package com.skillstorm.cucumber;

import com.skillstorm.dtos.ItemDto;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ItemTableSteps {

    WebDriver driver;
    WebDriverWait wait;
    private List<Integer> addedItemIds = new java.util.ArrayList<>();

    // Scenario: View the list of items
    @Given("I am on the Items page")
    public void i_am_on_the_items_page() {
        driver = SingletonDriver.getChromeDriver();
        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("http://52.90.145.230/items");
    }

    @Then("I should see the following items:")
    public void i_should_see_the_following_items(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        WebElement table = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("table tbody")));

        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        for (int i = 0; i < rows.size(); i++) {
            Map<String, String> expectedRow = rows.get(i);
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName("td"));

            Assertions.assertEquals(5, cells.size(), "Expected 5 cells in the table row, but found: " + cells.size());

            Assertions.assertEquals(expectedRow.get("Name"), cells.get(1).getText().trim());
            Assertions.assertEquals(expectedRow.get("Volume"), cells.get(2).getText().trim());
        }
    }

    // Scenario: Add a new item
    @When("I click the Add Item button")
    public void i_click_the_add_item_button() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-add")));
        addButton.click();
    }

    @When("I enter the following details:")
    public void i_enter_the_following_details(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> itemDetails = dataTable.asMaps(String.class, String.class);

        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
        WebElement volumeField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("volume")));

        nameField.clear();
        nameField.sendKeys(itemDetails.get(0).get("Name"));

        volumeField.clear();
        volumeField.sendKeys(itemDetails.get(0).get("Volume"));
    }

    @When("I submit the item")
    public void i_submit_the_item() {
        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']")));
        submitButton.click();
    }

    @Then("the new item should be added to the table with:")
    public void the_new_item_should_be_added_to_the_table_with(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> newItem = dataTable.asMaps(String.class, String.class).get(0);
        String expectedName = newItem.get("Name");
        String expectedVolume = newItem.get("Volume");

        String xpath = "//table/tbody/tr[td[2][normalize-space(text())='" + expectedName + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        List<WebElement> cells = row.findElements(By.tagName("td"));

        System.out.println("Number of cells found: " + cells.size());
        for (int i = 0; i < cells.size(); i++) {
            System.out.println("Cell " + i + ": '" + cells.get(i).getText() + "'");
        }

        Assertions.assertEquals(5, cells.size(), "Expected 5 cells in the table row, but found: " + cells.size());
        Assertions.assertEquals(expectedName, cells.get(1).getText().trim());
        Assertions.assertEquals(expectedVolume, cells.get(2).getText().trim());

        String idText = cells.get(0).getText();
        int itemId = Integer.parseInt(idText.trim());
        addedItemIds.add(itemId);
    }

    // Scenario: Delete an existing item
    @Given("the following item exists:")
    public void the_following_item_exists(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> items = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> itemData : items) {
            ItemDto itemDto = new ItemDto();
            itemDto.setName(itemData.get("Name"));
            itemDto.setVolume(Integer.parseInt(itemData.get("Volume")));

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://3.95.37.62:8080/inventory/items";
            ItemDto createdItem = restTemplate.postForObject(url, itemDto, ItemDto.class);
            addedItemIds.add(createdItem.getId());
        }

        // Refresh the Items page to reflect the new item
        driver.navigate().refresh();

        // Wait for the new item to appear in the table
        for (Map<String, String> itemData : items) {
            String expectedName = itemData.get("Name");
            String xpath = "//table/tbody/tr[td[2][normalize-space(text())='" + expectedName + "']]";
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        }
    }

    @When("I click the delete button for the item named {string}")
    public void i_click_the_delete_button_for_the_item_named(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table tbody")));

        String xpath = "//table/tbody/tr[td[2][normalize-space(text())='" + name + "']]";
        WebElement row = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        WebElement deleteButton = row.findElement(By.cssSelector(".btn-icon"));
        wait.until(ExpectedConditions.elementToBeClickable(deleteButton));
        deleteButton.click();

        wait.until(ExpectedConditions.stalenessOf(row));
    }

    @Then("the item should be removed from the table")
    public void the_item_should_be_removed_from_the_table() {
        String expectedName = "Stroller";
        String xpath = "//table/tbody/tr[td[2][normalize-space(text())='" + expectedName + "']]";
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));

        List<WebElement> tableRows = driver.findElements(By.xpath(xpath));
        Assertions.assertEquals(0, tableRows.size(), "Expected the item to be removed, but it is still present.");
    }

    @After
    public void tearDown() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://3.95.37.62:8080/inventory/items/";

        for (Integer itemId : addedItemIds) {
            try {
                restTemplate.delete(url + itemId);
            } catch (Exception e) {
                System.out.println("Failed to delete item with ID: " + itemId);
            }
        }

        SingletonDriver.quitDriver();
    }
}
