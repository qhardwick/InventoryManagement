package com.skillstorm.cucumber;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTableSteps {

    WebDriver driver;

    @Given("I am on the Items page")
    public void i_am_on_the_items_page() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.get("http://127.0.0.1:5173/items");
    }

    @Then("I should see the following items:")
    public void i_should_see_the_following_items(io.cucumber.datatable.DataTable dataTable) {
        // Get rows of data from the feature file
        List<List<String>> rows = dataTable.asLists(String.class);

        // Find the table in the DOM
        WebElement table = driver.findElement(By.cssSelector("table tbody"));

        // Verify each row in the table
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        for (int i = 0; i < rows.size(); i++) {
            List<WebElement> cells = tableRows.get(i).findElements(By.tagName("td"));
            assertEquals(rows.get(i).get(0), cells.get(0).getText()); // Part Number
            assertEquals(rows.get(i).get(1), cells.get(1).getText()); // Name
            assertEquals(rows.get(i).get(2), cells.get(2).getText()); // Volume
        }
    }

    @When("I click the Add Item button")
    public void i_click_the_add_item_button() {
        // Click the Add Item button
        WebElement addButton = driver.findElement(By.cssSelector(".btn-add"));
        addButton.click();
    }

    @When("I enter the following details:")
    public void i_enter_the_following_details(io.cucumber.datatable.DataTable dataTable) {
        // Get item details from the feature file
        List<List<String>> itemDetails = dataTable.asLists(String.class);

        // Enter name and volume in the form fields
        WebElement nameField = driver.findElement(By.name("name"));
        WebElement volumeField = driver.findElement(By.name("volume"));
        nameField.sendKeys(itemDetails.get(0).get(0)); // Name
        volumeField.sendKeys(itemDetails.get(0).get(1)); // Volume
    }

    @When("I submit the item")
    public void i_submit_the_item() {
        // Submit the form
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();
    }

    @Then("the new item should be added to the table with:")
    public void the_new_item_should_be_added_to_the_table_with(io.cucumber.datatable.DataTable dataTable) {
        // Get the new item details
        List<List<String>> rows = dataTable.asLists(String.class);

        // Wait for the new item to appear (you may need to add a wait here)
        WebElement table = driver.findElement(By.cssSelector("table tbody"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        WebElement lastRow = tableRows.get(tableRows.size() - 1); // The last row is the new item

        List<WebElement> cells = lastRow.findElements(By.tagName("td"));
        assertEquals(rows.get(0).get(0), cells.get(0).getText()); // Part Number
        assertEquals(rows.get(0).get(1), cells.get(1).getText()); // Name
        assertEquals(rows.get(0).get(2), cells.get(2).getText()); // Volume
    }

    @When("I click the delete button for the item with Part Number {int}")
    public void i_click_the_delete_button_for_the_item_with_part_number(Integer partNumber) {
        // Find the row with the given part number
        WebElement table = driver.findElement(By.cssSelector("table tbody"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));

        for (WebElement row : tableRows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.get(0).getText().equals(partNumber.toString())) {
                // Click the delete button in this row
                WebElement deleteButton = row.findElement(By.cssSelector(".btn-icon"));
                deleteButton.click();
                break;
            }
        }
    }

    @Then("the item should be removed from the table")
    public void the_item_should_be_removed_from_the_table() {
        // Verify that the row has been removed from the table
        WebElement table = driver.findElement(By.cssSelector("table tbody"));
        List<WebElement> tableRows = table.findElements(By.tagName("tr"));
        assertEquals(0, tableRows.size()); // If the item is removed, there should be no rows
    }
}
