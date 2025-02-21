package com.skillstorm.pages;

import com.skillstorm.utils.PropertiesLoader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WarehouseItemsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Navbar navbar;
    private static String url;

    // Page elements:
    // Table showing all of the items stored inside the given Warehouse:
    @FindBy(id = "warehouse-items-table")
    private WebElement warehouseItemsTable;

    // <td> holding available capacity of the warehouse
    @FindBy(id = "capacity")
    private WebElement availableCapacity;

    // Button to open add items form:
    @FindBy(xpath = "//button[text()='Add Items']")
    private WebElement addItemsButton;

    // Button to open the remove items form:
    @FindBy(xpath = "//button[text()='Remove Items']")
    private WebElement removeItemsButton;

    // Constructor to intialize driver and page elements:
    public WarehouseItemsPage(WebDriver driver) {
        this.driver = driver;
        navbar = new Navbar(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(5000));
        PageFactory.initElements(driver, this);
    }

    // Use a given warehouse id to set our url:
    public void get(int warehouseId) {
        String domain = PropertiesLoader.getProperty("domain");
        url = domain + "/warehouses/" + warehouseId + "/items";
        driver.navigate().to(url);
    }

    // Verify we are on the warehouse-items page:
    public boolean onPage() {
        return url.equals(driver.getCurrentUrl());
    }

    // Verify that the name and location for our Warehouse-Items table is what were expecting when we navigated to this url:
    public boolean isWarehouseNameAndLocationCorrect(String warehouseName, String warehouseLocation) {
        String title = warehouseLocation + " (" + warehouseLocation + ")";
        String headerXpath = ".//thead/tr/th[contains(text(), '" + title + "')]";

        WebElement mainTableTitle = warehouseItemsTable.findElement(By.xpath(headerXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(mainTableTitle));
        return mainTableTitle.isDisplayed();
    }

    // See how many entires are present on the main Warehouse-Items table:
    public int getNumberOfEntries() {
        WebElement mainTableBody = warehouseItemsTable.findElement(By.cssSelector("tbody"));
        return mainTableBody.findElements(By.tagName("tr")).size();
    }

    // Check to see if the table is empty:
    public boolean warehouseIsEmpty() {
        // There should always be at least one empty row plus the row displaying capacity:
        return getNumberOfEntries() < 3;
    }

    // See if a specific item is already being stored in the warehouse:
    public int getItemQuantity(int itemId) {
        // xpath for an item row from the main table:
        String rowXpath = ".//tr[td[1][text() = '" + itemId + "']]";

        // See if an instance of the item is currently being stored:
        WebElement itemRow;
        try {
            itemRow = warehouseItemsTable.findElement(By.xpath(rowXpath));
            wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(itemRow));
        } catch (NoSuchElementException e) {
            return 0;
        }

        // Return the quantity being stored:
        return Integer.parseInt(itemRow.findElement(By.xpath(".//td[3]")).getText());
    }

    // Check the current capacity remaining for the warehouse:
    public int getCurrentCapacity() {
        // xpath to select the capacity:
        return Integer.parseInt(availableCapacity.getText());
    }

    // Press the increment button to increase the count of a given item by 1:
    public void clickIncrementButton(int itemId) {
        // Find the button located on the row whose 2nd column contains our item name:
        String buttonXpath = ".//tr[td[1][text() = '" + itemId +"']]//td[6]//button";
        WebElement incrementButton = warehouseItemsTable.findElement(By.xpath(buttonXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(incrementButton));
        incrementButton.click();
    }

    // Press the decrement button to reduce the count of a given item by 1:
    public void clickDecrementButton(int itemId) {
        // Find the button located on the row whose 2nd column contains our item name:
        String buttonXpath = ".//tr[td[1][text() = '" + itemId +"']]//td[7]//button";
        WebElement decrementButton = warehouseItemsTable.findElement(By.xpath(buttonXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(decrementButton));
        decrementButton.click();
    }

    // Click the Add Items button to open the Add Items form:
    public void clickAddItems() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(addItemsButton));
        addItemsButton.click();
    }

    // Locate the row on the 'Items' table for a given item name. Applies when adding or removing items from the warehouse:
    private String getXpathForItemTableRow(int itemId) {
        return "//table[.//th[text() = 'Items']]//tr[td[1][text() = '" + itemId + "']]";
    }

    // Select the row for the given item. Input the number of items to add:
    public void setAddItemsForm(int itemId, int quantity) {

        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRow(itemId);

        // xpath to select the input field for the given row:
        String quantityInputXpath = rowXpath + "//input";

        // Select the input field element:
        WebElement quantityInput = driver.findElement(By.xpath(quantityInputXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(quantityInput));

        // Clear the element to avoid potentially appending our value to data aready in the field:
        quantityInput.clear();

        // Fill in the desired quantity of the item to add:
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    // Check to see if the warehouse has enough capacity to store the items:
    public boolean hasCapacityForItems(int itemId, int quantity) {
        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRow(itemId);

        // xpath to select the volume of a single item:
        String volumeXpath = rowXpath + "/td[3]";

        // Use the path to get the volume for the given item:
        int unitVolume = Integer.parseInt(driver.findElement(By.xpath(volumeXpath)).getText());

        return getCurrentCapacity() >= quantity * unitVolume;
    }

    // Check to see if we can increment without opening the add items menu:
    public boolean canIncrement(int itemId) {
        WebElement volumeValue = driver.findElement(By.xpath(".//tr[td[1][text() = '" + itemId +"']]/td[4]"));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(volumeValue));
        int spaceNeeded = Integer.parseInt(volumeValue.getText());

        return getCurrentCapacity() >= spaceNeeded;
    }

    // Click on the add button to add the items to the warehouse:
    public void clickSubmitAddItemsForm(int itemId) {
        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRow(itemId);

        // There should only be one button on the row, so our xpath can just look for the only button on the row:
        String submitButtonXpath = rowXpath + "//button";

        // Click on the button:
        WebElement submitButton = driver.findElement(By.xpath(submitButtonXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        handleAlert();
    }

    // Click the Remove Items button to open the Remove Items form:
    public void clickRemoveItems() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(removeItemsButton));
        removeItemsButton.click();
    }

    // Select the row for the given part number. Input the number of items to remove:
    public void setRemoveItemsForm(int itemId, int quantity) {

        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRow(itemId);

        // xpath to select the input field for the given row:
        String quantityInputXpath = rowXpath + "//input";

        // Select the input field element:
        WebElement quantityInput = driver.findElement(By.xpath(quantityInputXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(quantityInput));

        // Clear the element to avoid potentially appending our value to data aready in the field:
        quantityInput.clear();

        // Fill in the desired quantity of the item to remove:
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    // Click on the remove button to remove the items to the warehouse:
    public void clickSubmitRemoveItemsForm(int itemId) {
        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRow(itemId);

        // There should only be one button on the row, so our xpath can just look for the only button on the row:
        String submitButtonXpath = rowXpath + "//button";

        // Click on the button:
        WebElement submitButton = driver.findElement(By.xpath(submitButtonXpath));
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.elementToBeClickable(submitButton));
        submitButton.click();
        handleAlert();
    }

    private void handleAlert() {
        try {
            wait.ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
        } catch (TimeoutException e) {
            // No alert was triggered. Move on with your day.
        }
    }

    // Empty all contents from the Warehouse:
    // Note: Does not represent a single button on page, but rather a convenience function for testing:
    public void emptyTheWarehouse() {
        wait.ignoring(StaleElementReferenceException.class).until(ExpectedConditions.visibilityOf(warehouseItemsTable));
        List<WebElement> itemRows = warehouseItemsTable.findElements(By.xpath(".//tbody/tr[td[1][normalize-space()]]"));

        for(WebElement row : itemRows) {
            int itemId = Integer.parseInt(row.findElement(By.xpath(".//td[1]")).getText());
            int quantity = Integer.parseInt(row.findElement(By.xpath(".//td[3]")).getText());

            removeItemsButton.click();
            setRemoveItemsForm(itemId, quantity);
            clickSubmitRemoveItemsForm(itemId);
        }
    }

}
