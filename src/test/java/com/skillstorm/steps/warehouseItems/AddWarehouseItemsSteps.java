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

    private int warehouseId;
    private int itemId;

    // Set up tests:
    @Before("@addItemsToWarehouse")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehouseItemsPage = new WarehouseItemsPage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
    }

    @Given("An item with {string} and {int} exists")
    public void itemExists(String itemName, int volume) {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.clickAddItemButton();
        itemsPage.fillOutNewItemForm(itemName, volume);
        itemsPage.clickSubmitForm();
        itemId = itemsPage.findItemId(itemName, volume);
    }

    @And("a {string} located in {string} with a capacity {int} exists")
    public void warehouseExists(String warehouse, String location, int capacity) {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm(warehouse, location, capacity);
        warehousesPage.clickSubmitForm();
        warehouseId = warehousesPage.findWarehouseId(warehouse, location, capacity);
    }

    @When("I click the inspect button for that warehouse")
    public void iAmOnCorrectWarehouseItemsPage() {
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
    }

    @And("the warehouse is currently storing {int} of the existing item")
    public void getCurrentAmountOfItemInStorage(int initialQuantity) {
        if(initialQuantity != 0) {
            warehouseItemsPage.clickAddItems();
            warehouseItemsPage.fillOutAddItemsFormForAnItem(itemId, initialQuantity);
            warehouseItemsPage.clickButtonToSubmitAddItemsForm(itemId);
        }
        int initialQuantityFromTable = warehouseItemsPage.getItemQuantity(itemId);
        assertEquals(initialQuantityFromTable, initialQuantity);
    }

    @When("I click the Add Items button")
    public void iClickAddItemsButton() {
        warehouseItemsPage.clickAddItems();
    }

    @And("I see the row for the item and input a {int}")
    public void iFillOutTheAddItemsForm(int quantity) {
        warehouseItemsPage.fillOutAddItemsFormForAnItem(itemId, quantity);
    }

    @And("the warehouse has sufficient capacity to store items of that {int}")
    public void warehouseCanStoreItems(int quantity) {
        assertTrue(warehouseItemsPage.hasEnoughCapacityForItems(itemId, quantity));
    }

    @And("the warehouse does not have sufficient capacity to store items of that {int}")
    public void warehouseCannotStoreItems(int quantity) {
        assertFalse(warehouseItemsPage.hasEnoughCapacityForItems(itemId, quantity));
    }

    @And("I click the '+' button to submit the form for that item")
    public void iClickTheButtonToSubmitAddItemsForm() {
        try {
            warehouseItemsPage.clickButtonToSubmitAddItemsForm(itemId);
        } catch (UnhandledAlertException e) {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
    }

    @Then("I should see the warehouse is now storing {int} of the item on the table")
    public void theItemAndItsQuantityAreAddedToTheTable(int finalQuantity) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int resultingQuantity = warehouseItemsPage.getItemQuantity(itemId);
        assertEquals(resultingQuantity, finalQuantity);

        tearDown();
    }

    private void tearDown() {
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.emptyTheWarehouse();

        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.deleteItem(itemId);

        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseId);
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        assertFalse(warehousesPage.warehouseExists(warehouseId));
    }

    @Then("I should see that the {int} matches the {int} of the item on the table")
    public void theInitialQuantityForTheItemWasNotChanged(int initialQuantity, int finalQuantity) {
        int resultingQuantity = warehouseItemsPage.getItemQuantity(itemId);
        assertEquals(resultingQuantity, initialQuantity);
        assertEquals(resultingQuantity, finalQuantity);
    }

    @After("@addItemsToWarehouse")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
