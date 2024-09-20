package com.skillstorm.cucumber;

import com.skillstorm.selenium.WarehouseManagerPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class CreateNewWarehouseSteps {

    private WebDriver driver;
    private WarehouseManagerPage warehousePage;

    @Before("@createWarehouse")
    public void before() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        warehousePage = new WarehouseManagerPage(driver);
    }

    @Given("I am on the WarehouseManager page")
    public void iAmOnTheWarehouseManagerPage() {
        warehousePage.get();
        assertTrue(warehousePage.onPage());
    }

    @When("I click the add warehouse button")
    public void iClickAddWarehouse() {
        warehousePage.clickAddWarehouseButton();
    }

    @And("I enter a {string} and a {string} and a {int}")
    public void iFillOutTheAddWarehouseForm(String name, String location, int capacity) {
        warehousePage.fillOutNewWarehouseForm(name, location, capacity);
    }

    @And("I click submit")
    public void iClickSubmit() {
        warehousePage.submitForm();
    }

    @Then("I should see the warehouse on the list with name {string}")
    public void iShouldSeeTheWarehouseInTheList(String name) {
        // Logic to check if the warehouse list contains the new warehouse
        boolean result = warehousePage.wasWarehouseAdded(name);
        assertTrue(result, name + "was added to the warehouse table");
    }

    @After("@createWarehouse")
    public void after() {
        if (driver != null) {
            driver.quit();
        }
    }


}
