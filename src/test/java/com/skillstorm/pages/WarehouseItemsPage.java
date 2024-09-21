package com.skillstorm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WarehouseItemsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private static String url;

    // Page elements:
    // Table showing all of the items stored inside the given Warehouse:
    @FindBy(id = "warehouse-items-table")
    private WebElement warehouseItemsTable;

    // Button to open add items form:
    @FindBy(xpath = "//button[text()='Add Items']")
    private WebElement addItemsButton;

    // Button to open the remove items form:
    @FindBy(xpath = "//button[text()'Remove Items']")
    private WebElement removeItemsButton;

    // Constructor to intialize driver and page elements:
    public WarehouseItemsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        PageFactory.initElements(driver, this);
    }

    // Use a given warehouse id to set our url:
    public void get(int warehouseId) {
        url = "http://localhost:5173/warehouses/" + warehouseId + "/items";
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
        wait.until(ExpectedConditions.visibilityOf(mainTableTitle));
        return mainTableTitle.isDisplayed();
    }

    // Press the increment button to increase the count of a given item by 1:
    private void pressTheIncrementButtonForAGivenItemId(int itemId) {
        // Find the button located on the row whose first column contains our itemId:
        String buttonXpath = ".//tr[td[1][text() = '" + itemId +"']]//td[6]//button";
        WebElement incrementButton = warehouseItemsTable.findElement(By.xpath(buttonXpath));
        incrementButton.click();
    }

    // Press the decrement button to reduce the count of a given item by 1:
    private void pressTheDecrementButtonForAGivenItemId(int itemId) {
        // Find the button located on the row whose first column contains our itemId:
        String buttonXpath = ".//tr[td[1][text() = '" + itemId +"']]//td[7]//button";
        WebElement decrementButton = warehouseItemsTable.findElement(By.xpath(buttonXpath));
        decrementButton.click();
    }

    // Click the Add Items button to open the Add Items form:
    public void clickAddItems() {
        wait.until(ExpectedConditions.elementToBeClickable(addItemsButton));
        addItemsButton.click();
    }

    // Locate the row on the 'Items' table for a given itemId. Applies when adding or removing items from the warehouse:
    private String getXpathForItemTableRowByItemId(int itemId) {
        return "//table[.//th[text() = 'Items']]//tr[td[1][text() = '" + itemId + "']]";
    }

    // Select the row for the given part number. Input the number of items to add:
    public void fillOutAddItemsFormForAGivenItemId(int itemId, int quantity) {

        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRowByItemId(itemId);

        // xpath to select the input field for the given row:
        String quantityInputXpath = rowXpath + "//input";

        // Select the input field element:
        WebElement quantityInput = driver.findElement(By.xpath(quantityInputXpath));
        wait.until(ExpectedConditions.visibilityOf(quantityInput));

        // Clear the element to avoid potentially appending our value to data aready in the field:
        quantityInput.clear();

        // Fill in the desired quantity of the item to add:
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    // Click on the add button to add the items to the warehouse:
    public void clickButtonToSubmitAddItemsForm(int itemId) {
        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRowByItemId(itemId);

        // There should only be one button on the row, so our xpath can just look for the only button on the row:
        String submitButtonXpath = rowXpath + "//button";

        // Click on the button:
        WebElement submitButton = driver.findElement(By.xpath(submitButtonXpath));
        submitButton.click();
    }

    // Click the Remove Items button to open the Remove Items form:
    public void clickRemoveItems() {
        wait.until(ExpectedConditions.elementToBeClickable(removeItemsButton));
        removeItemsButton.click();
    }

    // Select the row for the given part number. Input the number of items to remove:
    public void fillOutRemoveItemsFormForAGivenItemId(int itemId, int quantity) {

        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRowByItemId(itemId);

        // xpath to select the input field for the given row:
        String quantityInputXpath = rowXpath + "//input";

        // Select the input field element:
        WebElement quantityInput = driver.findElement(By.xpath(quantityInputXpath));
        wait.until(ExpectedConditions.visibilityOf(quantityInput));

        // Clear the element to avoid potentially appending our value to data aready in the field:
        quantityInput.clear();

        // Fill in the desired quantity of the item to remove:
        quantityInput.sendKeys(String.valueOf(quantity));
    }

    // Click on the remove button to remove the items to the warehouse:
    public void clickButtonToSubmitRemoveItemsForm(int itemId) {
        // xpath to select the row in the 'Items' table with the specified itemId in the first column:
        String rowXpath = getXpathForItemTableRowByItemId(itemId);

        // There should only be one button on the row, so our xpath can just look for the only button on the row:
        String submitButtonXpath = rowXpath + "//button";

        // Click on the button:
        WebElement submitButton = driver.findElement(By.xpath(submitButtonXpath));
        submitButton.click();
    }


}
