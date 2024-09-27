package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.EditWarehousePage;
import com.skillstorm.pages.Navbar;
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

public class EditWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private EditWarehousePage editWarehousePage;
    private Navbar navbar;
    private int warehouseId;

    @Before("@editWarehouse")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehousesPage = new WarehousesPage(driver);
        editWarehousePage = new EditWarehousePage(driver);
        navbar = new Navbar(driver);
    }

    @Given("I am on the Warehouses page")
    public void onWarehousesPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

    @And("a warehouse exists with the {string} {string} and {int}")
    public void matchingWarehouseExists(String name, String location, int capacity) {
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm(name, location, capacity);
        warehousesPage.clickSubmitForm();
        assertTrue(warehousesPage.warehouseExists(name, location, capacity));

        warehouseId = warehousesPage.findWarehouseId(name,location, capacity);
        assertEquals(warehousesPage.findWarehouseName(warehouseId), name);
        assertEquals(warehousesPage.findWarehouseLocation(warehouseId), location);
        assertEquals(warehousesPage.findWarehouseCapacityByWarehouseId(warehouseId), capacity);
    }

    @When("I click the edit warehouse button for that warehouse")
    public void clickEditButtonForWarehouse() {
        // We need to first use the command to navigate directly to the Edit Warehouses page to
        // initialize it and the set the url:
        editWarehousePage.get(warehouseId);
        assertTrue(editWarehousePage.onpage());

        // But let's test the button:
        navbar.clickWarehouses();
        assertTrue(warehousesPage.onPage());
        warehousesPage.clickEditWarehouseButton(warehouseId);
    }

    @Then("I should be on the Edit Warehouses page")
    public void onEditWarehousePage() {
        assertTrue(editWarehousePage.onpage());
    }

    @When("I update the form with new {string} {string} and {int}")
    public void updateWarehouseForm(String updatedName, String updatedLocation, int updatedCapacity) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        editWarehousePage.updateNameField(updatedName);
        editWarehousePage.updateLocationField(updatedLocation);
        editWarehousePage.updateCapacityField(updatedCapacity);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @And("I click the Edit Warehouse button")
    public void clickEditWarehouseButton() {
        editWarehousePage.clickUpdateButton();
    }

    @Then("I should see that the warehouse has been updated to the new {string} {string} and {int}")
    public void warehouseHasBeenUpated(String updatedName, String updatedLocation, int updatedCapacity) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(warehousesPage.onPage());
        assertTrue(warehousesPage.warehouseExists(warehouseId));
        assertTrue(warehousesPage.warehouseExists(updatedName, updatedLocation, updatedCapacity));

        assertEquals(warehousesPage.findWarehouseName(warehouseId), updatedName);
        assertEquals(warehousesPage.findWarehouseLocation(warehouseId), updatedLocation);
        assertEquals(warehousesPage.findWarehouseCapacityByWarehouseId(warehouseId), updatedCapacity);

        teardown();
    }

    private void teardown() {
        warehousesPage.clickDeleteWarehouseButton(warehouseId);
    }

    @After("@editWarehouse")
    public void after() {
        SingletonDriver.quitDriver();
    }

}
