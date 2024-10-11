package com.skillstorm.steps.items;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.Navbar;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

public class DeleteItemsSteps {

    private WebDriver driver;
    private ItemsPage itemsPage;
    private Navbar navbar;
    private int itemId;

    @Before("@createItems")
    public void before() {
        driver = SingletonDriver.getChromeDriver();
        itemsPage = new ItemsPage(driver);
        navbar = new Navbar(driver);
    }
}
