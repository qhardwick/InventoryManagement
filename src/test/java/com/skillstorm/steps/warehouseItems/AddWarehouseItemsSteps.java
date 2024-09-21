package com.skillstorm.steps.warehouseItems;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.WarehouseItemsPage;
import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

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
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        warehouseItemsPage = new WarehouseItemsPage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);

        createATestItem();
        createATestWarehouse();
    }

    private void createATestWarehouse() {
        warehousesPage.get();
        warehousesPage.clickAddWarehouseButton();
        warehousesPage.fillOutNewWarehouseForm("Test Warehouse", "Test Location", 1000);
        if(warehousesPage.wasWarehouseAdded("Test Warehouse")) {
            createdWarehouses = List.of("Test Warehouse");
        }
    }

    private void createATestItem() {
        itemsPage.get();
        itemsPage.clickAddItemButton();
        itemsPage.fillOutNewItemForm("Test Item", 100);
        if(itemsPage.wasItemAdded("Test Item")) {
            createdItems = List.of("Test Item");
        }
    }

    @Given("I am on the Warehouse-Items page for a given {string}")
    public void iAmOnCorrectWarehouseItemsPage(String warehouse) {
        int warehouseId = warehousesPage.findWarehouseIdByWarehouseName(warehouse);
        warehouseItemsPage.get(warehouseId);
        assertTrue(warehouseItemsPage.onPage());
    }

    @When("I click the Add Items button")
    public void iClickAddItemsButton() {
        warehouseItemsPage.clickAddItems();
    }

//    @And("I see the row for the {string} and input a {int}")
//    public iFillOutTheAddItemsForm(String itemName, int quantity) {
//
//        warehouseItemsPage.fillOutAddItemsFormForAGivenItemId(quantity, quantity);
//    }

}
