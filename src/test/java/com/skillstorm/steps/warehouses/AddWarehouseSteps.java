package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.WarehousesPage;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class AddWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private int currentWarehouseId;
    private List<Integer> createdWarehouses;

    @Before("@createWarehouse")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        warehousesPage = new WarehousesPage(driver);
        createdWarehouses = new ArrayList<>();
    }

    @Given("I am on the WarehouseManager page")
    public void iAmOnTheWarehouseManagerPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

    @When("I click the add warehouse button")
    public void iClickAddWarehouse() {
        warehousesPage.clickAddWarehouseButton();
    }

    @And("I enter a {string} and a {string} and a {int}")
    public void iFillOutTheAddWarehouseForm(String name, String location, int capacity) {
        warehousesPage.setNewWarehouseForm(name, location, capacity);
    }

    @And("I click submit")
    public void iClickSubmit() {
        warehousesPage.clickSubmitForm();
    }

    @Then("I should see the warehouse on the list with matching {string} {string} and {int}")
    public void iShouldSeeTheWarehouseInTheList(String name, String location, int capacity) {
        boolean result = warehousesPage.warehouseExists(name, location, capacity);
        assertTrue(result, name + "was added to the warehouse table");


        // Add to the list of warehouses that were created so that we can delete them afterwards
        currentWarehouseId = warehousesPage.findWarehouseId(name, location, capacity);
        warehousesPage.clickDeleteWarehouseButton(currentWarehouseId);
    }

    @Then("I should not see the warehouse on the list with matching {string} {string} and {int}")
    public void verifyWarehouseNotAdded(String name, String location, int capacity) {
        boolean exists = warehousesPage.warehouseExists(name, location, capacity);
        assertFalse("Warehouse should not be added", exists);
    }

    @After("@createWarehouse")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
