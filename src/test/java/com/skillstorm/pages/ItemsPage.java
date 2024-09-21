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

public class ItemsPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String url = "http://localhost:5173/items";

    // Button to open add new item form:
    @FindBy(className = "btn-add")
    private WebElement addItemButton;

    // Name of the item to be added:
    @FindBy(name = "name")
    private WebElement nameField;

    // Volume of the item to be added:
    @FindBy(name = "volume")
    private WebElement volumeField;

    // Button to submit the add new warehouse form:
    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement submitButton;

    // Constructor to initialize driver and page elements:
    public ItemsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        PageFactory.initElements(driver, this);
    }

    // Navigate to the page:
    public void get() {
        driver.navigate().to(url);
    }

    // Verify that we're on the ItemManager page:
    public boolean onPage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Click on the button to open the add new warehouse form:
    public void clickAddItemButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addItemButton));
        addItemButton.click();
    }

    // Fill in the details of the new warehouse form:
    public void fillOutNewItemForm(String name, int volume) {

        wait.until(ExpectedConditions.visibilityOf(nameField));

        nameField.sendKeys(name);
        volumeField.sendKeys(String.valueOf(volume));
    }

    // Click on the button to submit the form:
    public void submitForm() {
        submitButton.click();
    }

    // Verify the Item had been added:
    public boolean wasItemAdded(String name) {
        try {
            // Use a wait to ensure the table is loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table//tbody")));

            // Check if any elements match the provided name
            // TODO: This returns true if the name is given as an empty string (possibly because of the button icons), even if no item was created. Need a better method.
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

    // Delete Item:
    public void deleteItemByName(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + name))).click();
    }
}