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

    private int warehouseId;
    private int itemId;

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
        warehousesPage.setNewWarehouseForm(name, location, capacity);
        warehousesPage.clickSubmitForm();
        assertTrue(warehousesPage.warehouseExists(name, location, capacity));

        // Store the id so we can use it to reference the object later:
        warehouseId = warehousesPage.findWarehouseId(name, location, capacity);
    }

    @And("the warehouse is empty")
    public void warehouseIsEmpty() {
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        assertTrue(warehouseItemsPage.warehouseIsEmpty());
    }

    @And("the warehouse is not empty")
    public void theWarehouseIsNotEmpty() {
        // Create an item to store in the warehouse:
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.clickAddItemButton();
        itemsPage.setNewItemForm("Test Item", 25);
        itemsPage.clickSubmitForm();
        // Store the id so that we can use it to reference the object later:
        itemId = itemsPage.findItemId("Test Item", 25);

        // Store the item in the warehouse:
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.clickAddItems();
        warehouseItemsPage.setAddItemsForm(itemId, 1);
        warehouseItemsPage.clickSubmitAddItemsForm(itemId);
        // Unless we want to split the warehouseExists method into separate methods for expecting true
        // vs expecting false, we cannot implicitly wait for element visibility or invisibility so we have to sleep:
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }

        assertFalse(warehouseItemsPage.warehouseIsEmpty());
    }

    @When("I click the delete button on the row for the warehouse")
    public void clickDeleteWarehouse() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseId);
    }

    @Then("the warehouse should be removed from the list")
    public void warehouseDoesNotExist() {
        // Unless we want to split the warehouseExists method into separate methods for expecting true
        // vs expecting false, we cannot implicitly wait for element visibility or invisibility so we have to sleep:
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        assertFalse(warehousesPage.warehouseExists(warehouseId));
    }

    @Then("the warehouse should not be removed from the list")
    public void warehouseStillExists() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        // Unless we want to split the warehouseExists method into separate methods for expecting true
        // vs expecting false, we cannot implicitly wait for element visibility or invisibility so we have to sleep:
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        assertTrue(warehousesPage.warehouseExists(warehouseId));
        teardown();
    }

    private void teardown() {
        removeItemFromWarehouse();
        deleteTheItem();
        deleteTheWarehouse();
    }

    private void deleteTheWarehouse() {
        // Delete the warehouse:
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickDeleteWarehouseButton(warehouseId);
    }

    private void deleteTheItem() {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
        itemsPage.deleteItem(itemId);
        // Unless we want to split the warehouseExists method into separate methods for expecting true
        // vs expecting false, we cannot implicitly wait for element visibility or invisibility so we have to sleep:
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        assertFalse(itemsPage.itemExists(itemId));
    }

    private void removeItemFromWarehouse() {
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
        warehouseItemsPage.emptyTheWarehouse();
        // Unless we want to split the warehouseExists method into separate methods for expecting true
        // vs expecting false, we cannot implicitly wait for element visibility or invisibility so we have to sleep:
        try {
            Thread.sleep(1000); // Wait for 1 second
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
        }
        assertTrue(warehouseItemsPage.warehouseIsEmpty());
    }

    @After("@deleteWarehouse")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
