package com.skillstorm.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WarehouseSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set up Chrome WebDriver
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:5173/warehouses"); // Adjust this URL to your warehouse management page
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close browser after each test
        }
    }

    @Test
    public void testCreateWarehouse() throws InterruptedException {
        // Create a new warehouse by filling out the form
        WebElement nameInput = driver.findElement(By.id("warehouseName"));
        WebElement locationInput = driver.findElement(By.id("warehouseLocation"));
        WebElement capacityInput = driver.findElement(By.id("warehouseCapacity"));
        WebElement createButton = driver.findElement(By.id("createWarehouseBtn"));

        nameInput.sendKeys("New Warehouse");
        locationInput.sendKeys("New York");
        capacityInput.sendKeys("500");

        createButton.click();

        // Wait for the page to reload
        Thread.sleep(1000);

        // Verify that the warehouse was added
        WebElement addedWarehouse = driver.findElement(By.xpath("//td[text()='New Warehouse']"));
        assertEquals("New Warehouse", addedWarehouse.getText());

        WebElement addedLocation = driver.findElement(By.xpath("//td[text()='New York']"));
        assertEquals("New York", addedLocation.getText());
    }

    @Test
    public void testReadWarehouse() throws InterruptedException {
        // Wait for the warehouses to load
        Thread.sleep(1000);

        // Verify that a warehouse exists on the page
        WebElement warehouseName = driver.findElement(By.xpath("//td[text()='Test Warehouse']"));
        assertTrue(warehouseName.isDisplayed());

        WebElement warehouseLocation = driver.findElement(By.xpath("//td[text()='Test Location']"));
        assertTrue(warehouseLocation.isDisplayed());
    }

    @Test
    public void testUpdateWarehouse() throws InterruptedException {
        // Click the edit button for the first warehouse
        WebElement editButton = driver.findElement(By.id("editWarehouseBtn-1")); // Adjust ID or locator as needed
        editButton.click();

        // Update warehouse details
        WebElement nameInput = driver.findElement(By.id("warehouseName"));
        nameInput.clear();
        nameInput.sendKeys("Updated Warehouse");

        WebElement updateButton = driver.findElement(By.id("updateWarehouseBtn"));
        updateButton.click();

        // Wait for the update to complete
        Thread.sleep(1000);

        // Verify that the warehouse was updated
        WebElement updatedWarehouse = driver.findElement(By.xpath("//td[text()='Updated Warehouse']"));
        assertEquals("Updated Warehouse", updatedWarehouse.getText());
    }

    @Test
    public void testDeleteWarehouse() throws InterruptedException {
        // Click the delete button for the first warehouse
        WebElement deleteButton = driver.findElement(By.id("deleteWarehouseBtn-1")); // Adjust ID or locator as needed
        deleteButton.click();

        // Wait for deletion to complete
        Thread.sleep(1000);

        // Verify that the warehouse was deleted
        boolean warehouseExists = driver.findElements(By.xpath("//td[text()='Test Warehouse']")).isEmpty();
        assertTrue(warehouseExists);
    }

    @Test
    public void testCheckWarehouseCapacity() throws InterruptedException {
        // Navigate to the warehouse capacity page or section (if applicable)
        WebElement capacityLink = driver.findElement(By.id("checkCapacityBtn-1")); // Adjust ID or locator as needed
        capacityLink.click();

        // Wait for the capacity check to complete
        Thread.sleep(1000);

        // Verify the warehouse capacity
        WebElement capacityDisplay = driver.findElement(By.id("warehouseCapacityDisplay")); // Adjust ID or locator as needed
        assertEquals("500", capacityDisplay.getText());
    }
}
