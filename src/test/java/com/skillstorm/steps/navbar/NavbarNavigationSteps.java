package com.skillstorm.steps.navbar;

import com.skillstorm.pages.HomePage;
import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.Navbar;
import com.skillstorm.pages.WarehousesPage;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public class NavbarNavigationSteps {

    private WebDriver driver;
    private Navbar navbar;
    private HomePage homePage;
    private WarehousesPage warehousesPage;
    private ItemsPage itemsPage;

    @Before("@navbarNavigation")
    public void before() {
        driver = SingletonDriver.getChromeDriver();

        navbar = new Navbar(driver);
        homePage = new HomePage(driver);
        warehousesPage = new WarehousesPage(driver);
        itemsPage = new ItemsPage(driver);
    }

    @Given("I am on the {string} page")
    public void onTheStartingPage(String origin) {
        switch (origin) {
            case "home":
                homePage.get();
                assertTrue(homePage.onPage());
                break;

            case "warehouses":
                warehousesPage.get();
                assertTrue(warehousesPage.onPage());
                break;

            case "items":
                itemsPage.get();
                assertTrue(itemsPage.onPage());
                break;
        }
    }

    @When("I click the navbar link to my {string}")
    public void clickTheNavbarLinkToDestination(String destination) {
        switch (destination) {
            case "home" -> navbar.clickHome();
            case "warehouses" -> navbar.clickWarehouses();
            case "items" -> navbar.clickItems();
        }
    }

    @Then("I should be on the {string} page")
    public void onDestinationPage(String destination) {
        switch (destination) {
            case "home" -> assertTrue(homePage.onPage());
            case "warehouses" -> assertTrue(warehousesPage.onPage());
            case "items" -> assertTrue(itemsPage.onPage());
        }
    }

    @After("@navbarNavigation")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
