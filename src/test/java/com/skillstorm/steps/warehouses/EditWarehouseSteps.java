package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.EditWarehousePage;
import com.skillstorm.pages.Navbar;
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
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
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
        warehousesPage.submitForm();
        assertTrue(warehousesPage.doesWarehouseExist(name));

        warehouseId = warehousesPage.findWarehouseIdByWarehouseName(name);
        assertEquals(warehousesPage.findWarehouseNameByWarehouseId(warehouseId), name);
        assertEquals(warehousesPage.findWarehouseLocationByWarehouseId(warehouseId), location);
        assertEquals(warehousesPage.findWarehouseCapacityByWarehouseId(warehouseId), capacity);
    }

    @When("I click the edit warehouse button for the {string}")
    public void clickEditButtonForWarehouse(String name) {
        // Doing this to set the url on the page:
        editWarehousePage.get(warehouseId);
    }

    @And("I am on the Edit Warehouse page")
    public void onEditWarehousePage() {
        assertTrue(editWarehousePage.onpage());
    }

    @And("I update the form with new {string} {string} and {int}")
    public void updateWarehouseForm(String updatedName, String updatedLocation, int updatedCapacity) {
        System.out.println("\n\nUpdated data: " + updatedName + " " + updatedLocation + " " + updatedCapacity);
        editWarehousePage.updateNameField(updatedName);
        editWarehousePage.updateLocationField(updatedLocation);
        editWarehousePage.updateCapacityField(updatedCapacity);
    }

    @And("I click the Edit Warehouse button")
    public void clickEditWarehouseButton() {
        editWarehousePage.clickUpdateButton();
    }

    @Then("I should see that the warehouse has been updated to the new {string} {string} and {int}")
    public void warehouseHasBeenUpated(String updatedName, String updatedLocation, int updatedCapacity) {
        assertTrue(warehousesPage.onPage());
        assertTrue(warehousesPage.doesWarehouseExist(updatedName));

        assertEquals(warehousesPage.findWarehouseNameByWarehouseId(warehouseId), updatedName);
        assertEquals(warehousesPage.findWarehouseLocationByWarehouseId(warehouseId), updatedLocation);
        assertEquals(warehousesPage.findWarehouseCapacityByWarehouseId(warehouseId), updatedCapacity);

        teardown(updatedName);
    }

    private void teardown(String updatedName) {
        warehousesPage.clickDeleteWarehouseButton(updatedName);
    }

    @After("@editWarehouse")
    public void after() {
        if(driver != null) {
       //     driver.quit();
        }
    }

}
