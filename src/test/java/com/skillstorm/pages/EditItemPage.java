package com.skillstorm.pages;

import com.skillstorm.utils.PropertiesLoader;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class EditItemPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static String url;

    // Page elements:
    @FindBy(name = "name")
    private WebElement nameField;

    @FindBy(name = "volume")
    private WebElement volumeField;

    @FindBy(css = "button")
    private WebElement updateButton;

    public EditItemPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(3000));
        navbar = new Navbar(driver);
        PageFactory.initElements(driver, this);
    }

    // Navigate directly to the Edit Items page:
    public void get(int itemId) {
        String domain = PropertiesLoader.getProperty("domain");
        url = domain + "/items/" + itemId;
        driver.navigate().to(url);
    }

    // Verify that we are on the Edit Items page:
    public boolean onpage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Fill in the item name input:
    public void setNameField(String name) {
        wait.ignoring(StaleElementReferenceException.class).until(visibilityOf(nameField));
        nameField.clear();
        nameField.sendKeys(name);
    }

    // Fill in the item volume input:
    public void setVolumeField(int volume) {
        wait.ignoring(StaleElementReferenceException.class).until(visibilityOf(volumeField));
        volumeField.clear();
        volumeField.sendKeys(String.valueOf(volume));
    }

    // Click Update button to submit the form:
    public void clickUpdateButton() {
        wait.ignoring(StaleElementReferenceException.class).until(elementToBeClickable(updateButton));
    }

}