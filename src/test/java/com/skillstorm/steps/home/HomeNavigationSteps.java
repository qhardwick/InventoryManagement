package com.skillstorm.steps.home;

import com.skillstorm.pages.HomePage;
import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.WarehousesPage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class HomeNavigationSteps {

    private WebDriver driver;
    private HomePage homePage;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;

    @Before("@homeNavigation")
    public void before() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        homePage = new HomePage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
    }

    @Given("We are on the Home page")
    public void weAreOnTheHomePage() {
        homePage.get();
        assertTrue(homePage.onPage());
    }

    @When("we click the Warehouse Manager link")
    public void clickWarehouseManagerLink() {
        homePage.clickWarehousesLink();
    }

    @When("we click the Items Manager link")
    public void clickItemsManagerLink() {
        homePage.clickItemsLink();
    }

    @Then("we should be on the Warehouses page")
    public void weShouldBeOnWarehousesPage() {
        assertTrue(warehousesPage.onPage());
    }

    @Then("we should be on the Items page")
    public void weShouldBeOnItemsPage() {
        assertTrue(itemsPage.onPage());
    }

    @After("@homeNavigation")
    public void after() {
        if(driver != null) {
            driver.quit();
        }
    }
}
