package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

public class CreateNewWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private List<String> createdWarehouses;

    @Before("@createWarehouse")
    public void before() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
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
        warehousesPage.fillOutNewWarehouseForm(name, location, capacity);
    }

    @And("I click submit")
    public void iClickSubmit() {
        warehousesPage.submitForm();
    }

    @Then("I should see the warehouse on the list with name {string}")
    public void iShouldSeeTheWarehouseInTheList(String name) {
        boolean result = warehousesPage.wasWarehouseAdded(name);
        assertTrue(result, name + "was added to the warehouse table");

        // Add to the list of warehouses that were created so that we can delete them afterwards
        createdWarehouses.add(name);
    }

    @Then("I should not see the warehouse on the list with name {string}")
    public void verifyWarehouseNotAdded(String name) {
        boolean exists = warehousesPage.wasWarehouseAdded(name);
        assertFalse("Warehouse should not be added", exists);
    }

    @After("@createWarehouse")
    public void after() {
        // Clean up all the warehouses we have created:
        for(String name : createdWarehouses) {
            warehousesPage.deleteWarehouseByName(name);
        }
        if (driver != null) {
            driver.quit();
        }
    }


}