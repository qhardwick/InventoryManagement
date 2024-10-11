package com.skillstorm.steps.items;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertTrue;

public class CommonItemsPageSteps {

    private WebDriver driver;
    private ItemsPage itemsPage;

    @Before("@createItems or @deleteItems")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        itemsPage = new ItemsPage(driver);
    }

    @Given("I am on the Items page")
    public void onItemsPage() {
        itemsPage.get();
        assertTrue(itemsPage.onPage());
    }
}
