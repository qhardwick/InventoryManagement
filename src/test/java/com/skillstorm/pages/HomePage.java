package com.skillstorm.pages;

import com.skillstorm.utils.PropertiesLoader;
import lombok.Getter;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private final String url;

    @FindBy(css = "a.landing-link[href='/warehouses']")
    private WebElement warehousesButton;

    @FindBy(css = "a.landing-link[href='/items']")
    private WebElement itemsButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        url = PropertiesLoader.getProperty("domain") + "/";
        navbar = new Navbar(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        PageFactory.initElements(driver, this);
    }

    // Navigate directly to Home:
    public void get() {
        driver.navigate().to(url);
    }

    // Verify we are on the Home page:
    public boolean onPage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Click on the main Warehouse Manager link on the home page:
    public void clickWarehousesLink() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(warehousesButton)).click();
    }

    // Click on the main Item Manager link on the home page:
    public void clickItemsLink() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(itemsButton)).click();
    }
}
