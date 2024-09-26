package com.skillstorm.steps.warehouseItems;

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
import org.openqa.selenium.Alert;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AddWarehouseItemsSteps {

    private WebDriver driver;
    private WarehouseItemsPage warehouseItemsPage;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;

    // Created test objects:
    Map<String, Integer> createdWarehouseItemsMap;
    List<String> createdWarehouses;
    List<String> createdItems;

    // Set up tests:
    @Before("@addItemsToWarehouse")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehouseItemsPage = new WarehouseItemsPage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);

        createdWarehouseItemsMap = new HashMap<>();
        createdWarehouses = new ArrayList<>();
        createdItems = new ArrayList<>();

        createATestItem();
        createATestWarehouse();
    }

    private void createATestWarehouse() {
        warehousesPage.get();
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm("Test Warehouse", "Test Location", 1000);
        warehousesPage.submitForm();
        if(warehousesPage.doesWarehouseExist("Test Warehouse")) {
            createdWarehouses = List.of("Test Warehouse");
        }
    }

    private void createATestItem() {
        itemsPage.get();
        itemsPage.clickAddItemButton();
        itemsPage.fillOutNewItemForm("Test Item", 100);
        itemsPage.submitForm();
        if(itemsPage.doesItemExist("Test Item")) {
            createdItems = List.of("Test Item");
        }
    }

    @Given("I am on the Warehouse-Items page for a given {string}")
    public void iAmOnCorrectWarehouseItemsPage(String warehouse) {
        int warehouseId = warehousesPage.findWarehouseId(warehouse);
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
    }

    @And("the warehouse is currently storing {int} of {string}")
    public void getCurrentAmountOfItemInStorage(int initialQuantity, String itemName) {
        if(initialQuantity != 0) {
            warehouseItemsPage.clickAddItems();
            warehouseItemsPage.fillOutAddItemsFormForAGivenItemId(itemName, initialQuantity);
            warehouseItemsPage.clickButtonToSubmitAddItemsForm(itemName);
        }
        int initialQuantityFromTable = warehouseItemsPage.getItemQuantityByItemName(itemName);
        assertEquals(initialQuantityFromTable, initialQuantity);
    }

    @When("I click the Add Items button")
    public void iClickAddItemsButton() {
        warehouseItemsPage.clickAddItems();
    }

    @And("I see the row for the {string} and input a {int}")
    public void iFillOutTheAddItemsForm(String itemName, int quantity) {

        warehouseItemsPage.fillOutAddItemsFormForAGivenItemId(itemName, quantity);
    }

    @And("the warehouse has sufficient capacity to store {string} of that {int}")
    public void warehouseCanStoreItems(String itemName, int quantity) {
        assertTrue(warehouseItemsPage.hasEnoughCapacityForItems(itemName, quantity));
    }

    @And("the warehouse does not have sufficient capacity to store {string} of that {int}")
    public void warehouseCannotStoreItems(String itemName, int quantity) {
        assertFalse(warehouseItemsPage.hasEnoughCapacityForItems(itemName, quantity));
    }

    @And("I click the '+' button on the row for the {string}")
    public void iClickTheButtonToSubmitAddItemsForm(String itemName) {
        try {
            warehouseItemsPage.clickButtonToSubmitAddItemsForm(itemName);
        } catch (UnhandledAlertException e) {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
    }

    @Then("I should see the warehouse is now storing {int} of {string} on the table")
    public void theItemAndItsQuantityAreAddedToTheTable(int finalQuantity, String itemName) {

        int resultingQuantity = warehouseItemsPage.getItemQuantityByItemName(itemName);
        assertEquals(resultingQuantity, finalQuantity);

        // Add created items to the list for clean up:
        createdWarehouseItemsMap.put(itemName, finalQuantity);
    }

    @Then("I should see that the {int} matches the {int} of {string} on the table")
    public void theInitialQuantityForTheItemWasNotChanged(int initialQuantity, int finalQuantity, String itemName) {
        int resultingQuantity = warehouseItemsPage.getItemQuantityByItemName(itemName);
        assertEquals(resultingQuantity, initialQuantity);
        assertEquals(resultingQuantity, finalQuantity);

        if(initialQuantity > 0) {
            createdWarehouseItemsMap.put(itemName, initialQuantity);
        }
    }

    // Remove all created entries and close the driver:
    @After("@addItemsToWarehouse")
    public void after() {
        // Empty the warehouse:
        for(String itemName : createdWarehouseItemsMap.keySet()) {
            warehouseItemsPage.clickRemoveItems();
            warehouseItemsPage.fillOutRemoveItemsFormForAGivenItem(itemName, createdWarehouseItemsMap.get(itemName));
            warehouseItemsPage.clickButtonToSubmitRemoveItemsForm(itemName);
        }

        // Delete the warehouse:
        warehousesPage.get();
        for(String warehouseName : createdWarehouses) {
            warehousesPage.clickDeleteWarehouseButton(warehouseName);
        }

        // Delete items:
        itemsPage.get();
        for(String itemName : createdItems) {
            itemsPage.deleteItemByName(itemName);
        }

        SingletonDriver.quitDriver();
    }
}
