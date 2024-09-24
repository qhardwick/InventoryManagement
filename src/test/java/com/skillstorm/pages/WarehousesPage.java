package com.skillstorm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WarehousesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static final String url = "http://localhost:5173/warehouses";

    // Button to open add new warehouse form:
    @FindBy(className = "btn-add")
    private WebElement addWarehouseButton;

    // Name of the warehouse to be added:
    @FindBy(name = "name")
    private WebElement nameField;

    // Location of the warehouse to be added:
    @FindBy(name = "location")
    private WebElement locationField;

    // Capacity of the warehouse to be added:
    @FindBy(name = "capacity")
    private WebElement capacityField;

    // Button to submit the add new warehouse form:
    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement submitButton;

    // Constructor to initialize driver and page elements:
    public WarehousesPage(WebDriver driver) {
        this.driver = driver;
        this.navbar = new Navbar(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        PageFactory.initElements(driver, this);
    }

    // Navigate to the page:
    public void get() {
        driver.navigate().to(url);
    }

    // Verify that we are on the WarehouseManager page:
    public boolean onPage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Click on the button to open the add new warehouse form:
    public void clickAddWarehouseButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addWarehouseButton));
        addWarehouseButton.click();
    }

    // Fill in the details of the new warehouse form:
    public void fillOutNewWarehouseForm(String name, String location, int capacity) {

        wait.until(ExpectedConditions.visibilityOf(nameField));

        nameField.sendKeys(name);
        locationField.sendKeys(location);
        capacityField.sendKeys(String.valueOf(capacity));
    }

    // Click on the button to submit the form:
    public void submitForm() {
        submitButton.click();
    }

    // Verify the Warehouse had been added:
    public boolean wasWarehouseAdded(String name) {
        try {
            // Use a wait to ensure the table is loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tbody")));

            // Check if any elements match the provided name
            // TODO: This returns true if the name is given as an empty string (possibly because of the button icons), even if no warehouse was created. Need a better method.
            return !driver.findElements(By.xpath("//td[contains(text(),'" + name + "')]")).isEmpty();
        } catch (NoSuchElementException e) {
            // If the element cannot be found, return false
            return false;
        } catch (Exception e) {
            // Handle any other exceptions that might occur
            e.printStackTrace();
            return false;
        }
    }

    // Find the id of a Warehouse by its name:
    public int findWarehouseIdByWarehouseName(String name) {
        // Path points to table data in column 1: //[td[1]] of the row that contains the given name in column 2: //tr[td[2]text() = 'name']]:
        String idXpath = "//tr[td[2][text() = '" + name + "']]/td[1]";
        String idString = driver.findElement(By.xpath(idXpath)).getText();
        return Integer.parseInt(idString);
    }

    // Navigate to Warehouse-Items page:
    public void clickInspectWarehouseButton(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("inspect-" + name))).click();
    }

    // Navigate to Edit Warehouse form:
    public void clickEditWarehouseButton(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("edit-" + name))).click();
    }

    // Delete Warehouse:
    public void clickDeleteWarehouseButton(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + name))).click();
    }
}
