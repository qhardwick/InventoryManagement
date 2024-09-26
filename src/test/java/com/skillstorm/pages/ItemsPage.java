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
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static final String url = "http://52.90.145.230/items";

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
        navbar = new Navbar(driver);
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
    public boolean doesItemExist(String name) {
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

    // Find the id for an item given its name:
    // TODO: Caution. Item name not guaranteed to be unique. May need to refactor
    public int getItemIdByItemName(String name) {
        // Path points to table data in column 1: //[td[1]] of the row that contains the given name in column 2: //tr[td[2]text() = 'name']]:
        String idXpath = "//tr[td[2][text() = '" + name + "']]/[td[1]]";
        String idString = driver.findElement(By.xpath(idXpath)).getText();
        return Integer.parseInt(idString);
    }

    // Delete Item:
    public void deleteItemByName(String name) {
        wait.until(ExpectedConditions.elementToBeClickable(By.id("delete-" + name))).click();
    }
}
