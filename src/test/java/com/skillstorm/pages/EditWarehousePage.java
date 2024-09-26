package com.skillstorm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EditWarehousePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static String url;

    @FindBy(name = "Warehouse Id")
    private WebElement idField;

    @FindBy(name = "name")
    private WebElement nameField;

    @FindBy(name = "location")
    private WebElement locationField;

    @FindBy(name = "capacity")
    private WebElement capacityField;

    @FindBy(className = "btn-edit")
    private WebElement editButton;

    public EditWarehousePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        navbar = new Navbar(driver);
        PageFactory.initElements(driver, this);
    }

    // Navigate directly to Edit Warehouse page:
    public void get(int warehouseId) {
        url = "http://52.90.145.230/warehouses/" + warehouseId;
    }

    // Verify we are on the Edit Warehouse page:
    public boolean onpage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Update name field:
    public void updateNameField(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(nameField));
        nameField.clear();
        nameField.sendKeys(name);
    }

    // Update location field:
    public void updateLocationField(String location) {
        wait.until(ExpectedConditions.elementToBeClickable(locationField));
        locationField.clear();
        locationField.sendKeys(location);
    }

    // Update capacity field:
    public void updateCapacityField(int capacity) {
        wait.until(ExpectedConditions.elementToBeClickable(capacityField));
        capacityField.clear();
        capacityField.sendKeys(String.valueOf(capacity));
    }

    // Click the update button to submit form:
    public void clickUpdateButton() {
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();
    }

}
