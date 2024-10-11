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

public class WarehousesPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static String url;

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
        String domain = PropertiesLoader.getProperty("domain");
        url = domain + "/warehouses";
        navbar = new Navbar(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
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
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(addWarehouseButton));
        addWarehouseButton.click();
    }

    // Fill in the details of the new warehouse form:
    public void setNewWarehouseForm(String name, String location, int capacity) {

        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(nameField));

        nameField.clear();
        nameField.sendKeys(name);

        locationField.clear();
        locationField.sendKeys(location);

        capacityField.clear();
        capacityField.sendKeys(String.valueOf(capacity));
    }

    // Click on the button to submit the form:
    public void clickSubmitForm() {
        submitButton.click();
    }

    public boolean warehouseExists(int id) {
        return getWarehouseRow(id).isPresent();
    }

    // Check for a Warehouse containing all 3 fields:
    public boolean warehouseExists(String name, String location, int capacity) {
        List<WebElement> matchingRows = driver.findElements(By.xpath("//tbody/tr[td[2][text()='" + name + "']]"));
        String locationXpath = ".//td[3]";
        String capacityXpath = ".//td[4]";


        for(WebElement row : matchingRows) {
            if(!location.equals(row.findElement(By.xpath(locationXpath)).getText())) {
                continue;
            }
            if(Integer.parseInt(row.findElement(By.xpath(capacityXpath)).getText()) != capacity) {
                continue;
            }
            return true;
        }
        return false;
    }

    // Get the row for a Warehouse entry by its id:
    // I forgot why I chose to make this an optional but I'm too tired to change it now:
    public Optional<WebElement> getWarehouseRow(int id) {
        String rowPath = "//tr[td[1][text() = '" + id + "']]";
        try {
            WebElement row = driver.findElement(By.xpath(rowPath));
            return Optional.of(row);
        } catch (NoSuchElementException e) {
            return Optional.empty();
        }
    }

    // Get the row for a Warehouse entry by its name, location, and capacity:
    // I forgot why I chose to make this an optional but I'm too tired to change it now:
    public Optional<WebElement> getWarehouseRow(String name, String location, int capacity) {
        // Find all rows with the specified name in the second column:
        List<WebElement> matchingRows = driver.findElements(By.xpath("//tbody/tr[td[2][text()='" + name + "']]"));

        for (WebElement row : matchingRows) {
            // Check location (third column)
            String rowLocation = row.findElement(By.xpath(".//td[3]")).getText();
            if (!rowLocation.equals(location)) {
                continue;
            }

            // Check capacity (fourth column)
            String capacityText = row.findElement(By.xpath(".//td[4]")).getText();
            int rowCapacity = Integer.parseInt(capacityText);

            if (rowCapacity == capacity) {
                return Optional.of(row);
            }
        }
        return Optional.empty();
    }

    // Find the id of a Warehouse by its name, location, and capacity:
    public int findWarehouseId(String name, String location, int capacity) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Optional<WebElement> rowOptional = getWarehouseRow(name, location, capacity);
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(rowOptional.get()));
        return rowOptional.map(webElement -> Integer.parseInt(webElement
                .findElement(By.xpath(".//td[1]"))
                .getText()))
                .orElse(-1);
    }

    // Find Warehouse name by its id:
    public String findWarehouseName(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        return row.findElement(By.xpath(".//td[2]")).getText();
    }

    // Find Warehouse location by its id:
    public String findWarehouseLocation(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        return row.findElement(By.xpath(".//td[3]")).getText();
    }

    // Find Warehouse capacity by its id:
    public int findWarehouseCapacity(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        String capacityString = row.findElement(By.xpath(".//td[4]"))
                .getText();
        return Integer.parseInt(capacityString);
    }

    // Navigate to Warehouse-Items page by Warehouse id:
    public void clickInspectWarehouseButton(int id) {
        WebElement row =wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        row.findElement(By.cssSelector("a[href='/warehouses/" + id + "/items']"))
                .click();
    }

    // Navigate to Edit Warehouse page by Warehouse id:
    public void clickEditWarehouseButton(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        row.findElement(By.cssSelector("a[href='/warehouses/" + id + "']")).click();
    }

    // Delete Warehouse page by Warehouse id:
    public void clickDeleteWarehouseButton(int id) {
        WebElement row = wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(getWarehouseRow(id).get()));
        row.findElement(By.xpath(".//button"))
                .click();
    }
}
