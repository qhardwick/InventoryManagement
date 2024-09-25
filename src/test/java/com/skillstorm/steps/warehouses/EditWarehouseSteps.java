package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.EditWarehousePage;
import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class EditWarehouseSteps {

    private WebDriver driver;
    private WarehousesPage warehousesPage;
    private EditWarehousePage editWarehousePage;

    @Before("@editWarehouse")
    public void before() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        warehousesPage = new WarehousesPage(driver);
        editWarehousePage = new EditWarehousePage(driver);

        warehousesPage.get();
        warehousesPage.clickAddWarehouseButton();
        //warehousesPage.fillOutNewWarehouseForm();
    }

    @Given("I am on the Warehouses page")
    public void onWarehousesPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

    @And("a warehouse exists with the {string} {string} and {int}")
    public void matchingWarehouseExists(String name, String location, int capacity) {

    }

    @When("I click the edit warehouse button for the {string}")
    public void clickEditButtonForWarehouse(String name) {

    }

    @And("I am on the Edit Warehouse page")
    public void onEditWarehousePage() {

    }

    @And("I update the form with new {string} {string} and {int}")
    public void updateWarehouseForm(String updatedName, String updatedLocation, int updatedCapacity) {

    }

    @And("I click the Edit Warehouse button")
    public void clickEditWarehouseButton() {

    }

    @Then("I should see that the warehouse has been updated to the new {string} {string} and {int}")
    public void warehouseHasBeenUpated(String updatedName, String updatedLocation, int updatedCapacity) {

    }

}
