package com.skillstorm.steps.warehouses;

import com.skillstorm.pages.EditWarehousePage;
import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
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
        warehousesPage.fillOutNewWarehouseForm();
    }

    @Given("I am on the Warehouses page")
    public void onWarehousesPage() {
        warehousesPage.get();
        assertTrue(warehousesPage.onPage());
    }

}
