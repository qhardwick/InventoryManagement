package com.skillstorm.pages;

import com.skillstorm.utils.PropertiesLoader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public class ItemsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static String url;

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
        String domain = PropertiesLoader.getProperty("domain");
        url = domain + "/items";
        navbar = new Navbar(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
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
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(addItemButton));
        addItemButton.click();
    }

    // Fill in the details of the new warehouse form:
    public void setNewItemForm(String name, int volume) {

        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(nameField));

        nameField.clear();
        nameField.sendKeys(name);

        volumeField.clear();
        volumeField.sendKeys(String.valueOf(volume));
    }

    // Click on the button to submit the form:
    public void clickSubmitForm() {
        submitButton.click();
    }

    // See if an entry matches the given id:
    public boolean itemExists(int id) {
        return getItemRow(id).isPresent();
    }

    // See if any entry matches both Item fields
    public boolean itemExists(String name, int volume) {
        List<WebElement> matchingRows = driver.findElements(By.xpath("//tbody/tr[td[2][text()='" + name + "']]"));
        String volumeXpath = ".//td[3]";

        for (WebElement row : matchingRows) {
            if(Integer.parseInt(row.findElement(By.xpath(volumeXpath)).getText()) != volume) {
                continue;
            }
            return true;
        }
        return false;
    }

    // Get row for an Item by its id:
    // I forgot why I chose to make this an optional but I'm too tired to change it now:
    public Optional<WebElement> getItemRow(int id) {
        String rowPath = "//tr[td[1][text() = '" + id + "']]";
        try {
            WebElement row = driver.findElement(By.xpath(rowPath));
            return Optional.of(row);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    // Get the row for an Item by its name and volume:
    // I forgot why I chose to make this an optional but I'm too tired to change it now:
    public Optional<WebElement> getItemRow(String name, int volume) {
        List<WebElement> mathingRows = driver.findElements(By.xpath("//tbody/tr[td[2][text()='" + name + "']]"));
        String volumeXpath = ".//td[3]";

        for(WebElement row : mathingRows) {
            if(Integer.parseInt(row.findElement(By.xpath(volumeXpath)).getText()) != volume) {
                continue;
            }
            return Optional.of(row);
        }
        return Optional.empty();
    }

    // Find the id for an item given its name and volume:
    public int findItemId(String name, int volume) {
        Optional<WebElement> rowOptional = getItemRow(name, volume);
        return rowOptional.map(webElement -> Integer.parseInt(webElement
                .findElement(By.xpath(".//td[1]"))
                .getText()))
                .orElse(-1);
    }

    // Find Item name by its id:
    public String findItemName(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getItemRow(id).get()));
        return row.findElement(By.xpath(".//td[2]")).getText();
    }

    // Find Item capacity by its id:
    public int findItemCapacity(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getItemRow(id).get()));
        return Integer.parseInt(row.findElement(By.xpath(".//td[3]")).getText());
    }

    // Delete Item:
    public void deleteItem(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getItemRow(id).get()));
        row.findElement(By.xpath(".//button"))
                .click();
    }
}
