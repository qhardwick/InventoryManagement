package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.WarehouseItemsPage;
import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DeleteWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;
    private WarehouseItemsPage warehouseItemsPage;

    @Before("@deleteWarehouse")
    public void before() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
        warehouseItemsPage = new WarehouseItemsPage(driver);
    }

    @Given("I am on the Warehouse Manager page")
    public void onWarehousesPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

    @And("a warehouse with the name {string} exists")
    public void warehouseExists(String warehouseName) {
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm(warehouseName, "Test Location", 1000);
        warehousesPage.submitForm();
        assertTrue(warehousesPage.doesWarehouseExist(warehouseName));
    }

    @And("the warehouse named {string} is empty")
    public void warehouseIsEmpty(String warehouseName) {
        int warehouseId = warehousesPage.findWarehouseIdByWarehouseName(warehouseName);
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        assertTrue(warehouseItemsPage.isWarehouseEmpty());
    }

    @And("the warehouse named {string} is not empty")
    public void theWarehouseIsNotEmpty(String warehouseName) {
        // Create an item to store in the warehouse:
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.clickAddItemButton();
        itemsPage.fillOutNewItemForm("Test Item", 25);
        itemsPage.submitForm();

        // Store the item in the warehouse:
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        int warehouseId = warehousesPage.findWarehouseIdByWarehouseName(warehouseName);
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.clickAddItems();
        warehouseItemsPage.fillOutAddItemsFormForAGivenItemId("Test Item", 1);
        warehouseItemsPage.clickButtonToSubmitAddItemsForm("Test Item");

        assertFalse(warehouseItemsPage.isWarehouseEmpty());
    }

    @When("I click the delete button on the row for the {string}")
    public void clickDeleteWarehouse(String warehouseName) {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseName);
    }

    @Then("the {string} should be removed from the list")
    public void warehouseDoesNotExist(String warehouseName) {
        assertFalse(warehousesPage.doesWarehouseExist(warehouseName));
    }

    @Then("the {string} should not be removed from the list")
    public void warehouseStillExists(String warehouseName) {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        assertTrue(warehousesPage.doesWarehouseExist(warehouseName));
        teardown(warehouseName);
    }

    private void teardown(String warehouseName) {
        removeItemFromWarehouse(warehouseName);
        deleteTheItem();
        deleteTheWarehouse(warehouseName);
    }

    private void deleteTheWarehouse(String warehouseName) {
        // Delete the warehouse:
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseName);
    }

    private void deleteTheItem() {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.deleteItemByName("Test Item");
        assertFalse(itemsPage.doesItemExist("Test Item"));
    }

    private void removeItemFromWarehouse(String warehouseName) {
        int warehouseId = warehousesPage.findWarehouseIdByWarehouseName(warehouseName);
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.clickRemoveItems();
        warehouseItemsPage.fillOutRemoveItemsFormForAGivenItem("Test Item", 1);
        warehouseItemsPage.clickButtonToSubmitRemoveItemsForm("Test Item");
        assertTrue(warehouseItemsPage.isWarehouseEmpty());
    }

    @After("@deleteWarehouse")
    public void after() {
        if(driver != null) {
            driver.quit();
        }
    }
}
