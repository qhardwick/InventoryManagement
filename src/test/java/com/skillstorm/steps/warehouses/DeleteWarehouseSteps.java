package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.WarehouseItemsPage;
import com.skillstorm.pages.WarehousesPage;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class DeleteWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;
    private WarehouseItemsPage warehouseItemsPage;

    private int currentWarehouseId;

    @Before("@deleteWarehouse")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
        warehouseItemsPage = new WarehouseItemsPage(driver);
    }

    @Given("I am on the Warehouse Manager page")
    public void onWarehousesPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

    @And("a warehouse with {string} {string} and {int} exists")
    public void warehouseExists(String name, String location, int capacity) {
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm(name, "Test Location", 1000);
        warehousesPage.submitForm();
        assertTrue(warehousesPage.warehouseExists(name, location, capacity));

        currentWarehouseId = warehousesPage.findWarehouseId(name, location, capacity);
    }

    @And("the warehouse is empty")
    public void warehouseIsEmpty(String warehouseName) {
        warehouseItemsPage.get(currentWarehouseId);
        assertTrue(warehouseItemsPage.onPage());
        assertTrue(warehouseItemsPage.isWarehouseEmpty());
    }

    @And("the warehouse is not empty")
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
        warehouseItemsPage.get(currentWarehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.clickAddItems();
        warehouseItemsPage.fillOutAddItemsFormForAGivenItemId("Test Item", 1);
        warehouseItemsPage.clickButtonToSubmitAddItemsForm("Test Item");

        assertFalse(warehouseItemsPage.isWarehouseEmpty());
    }

    @When("I click the delete button on the row for the warehouse")
    public void clickDeleteWarehouse() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(currentWarehouseId);
    }

    @Then("the warehouse should be removed from the list")
    public void warehouseDoesNotExist() {
        assertFalse(warehousesPage.warehouseExists(currentWarehouseId));
    }

    @Then("the {string} should not be removed from the list")
    public void warehouseStillExists(String warehouseName) {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        assertTrue(warehousesPage.warehouseExists(currentWarehouseId));
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
        warehousesPage.clickDeleteWarehouseButton(currentWarehouseId);
    }

    private void deleteTheItem() {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.deleteItemByName("Test Item");
        assertFalse(itemsPage.doesItemExist("Test Item"));
    }

    private void removeItemFromWarehouse(String warehouseName) {
        warehouseItemsPage.get(currentWarehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.clickRemoveItems();
        warehouseItemsPage.fillOutRemoveItemsFormForAGivenItem("Test Item", 1);
        warehouseItemsPage.clickButtonToSubmitRemoveItemsForm("Test Item");
        assertTrue(warehouseItemsPage.isWarehouseEmpty());
    }

    @After("@deleteWarehouse")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
