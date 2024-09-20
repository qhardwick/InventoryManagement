package com.skillstorm.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WarehouseManagerPage {
    private WebDriver driver;
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
    public WarehouseManagerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Navigate to the page:
    public void get() {
        driver.navigate().to(url);
    }

    // Verify that we're on the WarehouseManager page:
    public boolean onPage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Click on the button to open the add new warehouse form:
    public void clickAddWarehouseButton() {
        // Check for visibility before attempting to interact with the form:
        System.out.println("\n\nclickAddWarehouseButton method called \n\n");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        wait.until(ExpectedConditions.elementToBeClickable(addWarehouseButton));
        addWarehouseButton.click();
    }

    // Fill in the details of the new warehouse form:
    public void fillOutNewWarehouseForm(String name, String location, int capacity) {
        // Check for visibility before attempting to interact with the form:
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
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
        // Use WebDriverWait to ensure the element is interactable
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(),'" + name + "')]")));

        return driver.findElements(By.xpath("//td[contains(text(),'" + name + "')]")).size() > 0;
    }
}
