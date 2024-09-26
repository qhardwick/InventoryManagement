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
    private static final String url = "http://52.90.145.230/warehouses";

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
    public boolean doesWarehouseExist(String name) {
        try {
            // Use a wait to ensure the table is loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tbody")));

            // Check if any elements match the provided name
            return !driver.findElements(By.xpath("//tbody/tr/td[2][contains(text(), '" + name + "')]")).isEmpty();
        } catch (NoSuchElementException e) {
            // If the element cannot be found, return false
            return false;
        }
    }

    // Get the row for a Warehouse entry by its id:
    public WebElement getWarehouseRow(int id) {
        String rowPath = "//tr[td[1][text() = '" + id + "']]";
        return driver.findElement(By.xpath(rowPath));
    }

    // Get the row for a Warehouse entry by its name. No guarantee of uniqueness:
    public WebElement getWarehouseRow(String name) {
        String rowPath = "//tr[td[2][text() = '" + name + "']]";
        return driver.findElement(By.xpath(rowPath));
    }

    // Find the id of a Warehouse by its name:
    public int findWarehouseId(String name) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(name)));
        String idString = getWarehouseRow(name).findElement(By.xpath(".//td[1]")).getText();
        return Integer.parseInt(idString);
    }

    // Find Warehouse name by its id:
    public String findWarehouseName(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        return getWarehouseRow(id).findElement(By.xpath(".//td[2]")).getText();
    }

    // Find Warehouse location by its id:
    public String findWarehouseLocation(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        return getWarehouseRow(id).findElement(By.xpath(".//td[3]")).getText();
    }

    // Find Warehouse location by its name:
    public String findWarehouseLocation(String name) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(name)));
        return getWarehouseRow(name).findElement(By.xpath(".//td[3]")).getText();
    }

    // Find Warehouse capacity by its id:
    public int findWarehouseCapacityByWarehouseId(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        String idString = getWarehouseRow(id).findElement(By.xpath(".//td[4]"))
                .getText();
        return Integer.parseInt(idString);
    }

    // Navigate to Warehouse-Items page by Warehouse id:
    public void clickInspectWarehouseButton(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        getWarehouseRow(id).findElement(By.xpath(".//a[svg[@data-icon='magnifying-glass']]"))
                .click();
    }

    // Navigate to Warehouse-Items page by Warehouse name:
    public void clickInspectWarehouseButton(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inspect-" + name))).click();
    }

    // Navigate to Edit Warehouse page by Warehouse id:
    public void clickEditWarehouseButton(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        getWarehouseRow(id).findElement(By.xpath(".//a[svg[@data-icon='pen-to-square']]"))
                .click();
    }

    // Navigate to Edit Warehouse form by Warehouse Name:
    public void clickEditWarehouseButton(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-" + name))).click();
    }

    // Delete Warehouse page by Warehouse id:
    public void clickDeleteWarehouseButton(int id) {
        wait.until(ExpectedConditions.visibilityOf(getWarehouseRow(id)));
        getWarehouseRow(id).findElement(By.xpath(".//a[svg[@data-icon='trash']]"))
                .click();
    }

    // Delete Warehouse by name:
    public void clickDeleteWarehouseButton(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-" + name))).click();
    }
}
