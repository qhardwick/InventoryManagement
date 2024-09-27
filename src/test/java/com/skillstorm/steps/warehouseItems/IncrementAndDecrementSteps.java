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
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class IncrementAndDecrementSteps {

    private WebDriver driver;
    private WarehouseItemsPage warehouseItemsPage;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;

    private int warehouseId;
    private int itemId;

    // Set up tests:
    @Before("@increment or @decrement")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehouseItemsPage = new WarehouseItemsPage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
    }

    @Given("an item exists with {string} and {int}")
    public void itemExists(String itemName, int volume) {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.clickAddItemButton();
        itemsPage.fillOutNewItemForm(itemName, volume);
        itemsPage.clickSubmitForm();
        itemId = itemsPage.findItemId(itemName, volume);
        assertTrue(itemsPage.itemExists(itemId));
    }

    @And("a warehouse exists with the {string}, {string}, and {int}")
    public void warehouseExists(String warehouseName, String warehouseLocation, int capacity) {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm(warehouseName, warehouseLocation, capacity);
        warehousesPage.clickSubmitForm();
        warehouseId = warehousesPage.findWarehouseId(warehouseName, warehouseLocation, capacity);
        assertTrue(warehousesPage.warehouseExists(warehouseId));
    }

    @And("I am on the warehouse-items page for the warehouse")
    public void onWarehouseItemsPage() {
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
    }

    @And("it is already storing some {int} of the item")
    public void alreadyStoringTheItem(int quantity) {
        warehouseItemsPage.clickAddItems();
        warehouseItemsPage.fillOutAddItemsFormForAnItem(itemId, quantity);
        warehouseItemsPage.clickButtonToSubmitAddItemsForm(itemId);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(warehouseItemsPage.getItemQuantity(itemId), quantity);
    }

    @And("it has the capacity to add one more")
    public void roomForOneMore() {
        assertTrue(warehouseItemsPage.canIncrement(itemId));
    }

    @When("I click the increment button")
    public void clickIncrement() {
        warehouseItemsPage.clickIncrementButton(itemId);
    }

    @When("I click the decrement button")
    public void clickDecrement() {
        warehouseItemsPage.clickDecrementButton(itemId);
    }

    @Then("the {int} should have increased by one")
    public void incremented(int finalQuantity) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int result = warehouseItemsPage.getItemQuantity(itemId);
        assertEquals(result, finalQuantity);

        teardown();
    }

    @Then("the {int} should have decreased by one")
    public void decremented(int finalQuantity) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int result = warehouseItemsPage.getItemQuantity(itemId);
        assertEquals(result, finalQuantity);

        teardown();
    }

    private void teardown() {
        warehouseItemsPage.emptyTheWarehouse();

        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.deleteItem(itemId);

        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseId);
    }

    @After("@increment or @decrement")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
