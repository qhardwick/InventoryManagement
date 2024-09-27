package com.skillstorm.pages;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Navbar {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public Navbar(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        PageFactory.initElements(driver, this);
    }

    @FindBy(linkText = "Inventory Manager")
    private WebElement homeButton;

    @FindBy(linkText = "Warehouse Manager")
    private WebElement warehousesButton;

    @FindBy(linkText = "Item Manager")
    private WebElement itemsButton;

    // Click the company logo to navigate to Home:
    public void clickHome() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(homeButton)).click();
    }

    // Click the Warehouses Manager link to navigate to Warehouses page:
    public void clickWarehouses() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(warehousesButton)).click();
    }

    // Click the Items Manager link to navigate to the Items page:
    public void clickItems() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(itemsButton)).click();
    }
}
