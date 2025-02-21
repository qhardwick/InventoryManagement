package com.skillstorm.pages;

import com.skillstorm.utils.PropertiesLoader;
import org.openqa.selenium.StaleElementReferenceException;
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
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        navbar = new Navbar(driver);
        PageFactory.initElements(driver, this);
    }

    // Navigate directly to Edit Warehouse page:
    public void get(int warehouseId) {
        String domain = PropertiesLoader.getProperty("domain");
        url = domain + "/warehouses/" + warehouseId;
        driver.navigate().to(url);
    }

    // Verify we are on the Edit Warehouse page:
    public boolean onpage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Update name field:
    public void setNameField(String name) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(nameField));
        nameField.clear();
        nameField.sendKeys(name);
    }

    // Update location field:
    public void setLocationField(String location) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(locationField));
        locationField.clear();
        locationField.sendKeys(location);
    }

    // Update capacity field:
    public void setCapacityField(int capacity) {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(capacityField));
        capacityField.clear();
        capacityField.sendKeys(String.valueOf(capacity));
    }

    // Click the update button to submit form:
    public void clickUpdateButton() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(editButton));
        editButton.click();
    }

}
