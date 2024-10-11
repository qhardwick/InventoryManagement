package com.skillstorm.steps.items;

import com.skillstorm.pages.ItemsPage;
import com.skillstorm.pages.Navbar;
import com.skillstorm.utils.SingletonDriver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AddItemsSteps {

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

    @When("I click the Add Item button")
    public void clickAddItemButton() {
        itemsPage.clickAddItemButton();
    }

    @And("I fill in a valid {string} and {int}")
    public void setValidItemData(String name, int volume) {
        itemsPage.setNewItemForm(name, volume);
    }

    @And("I fill in an invalid {string} or {int}")
    public void setInvalidItemData(String name, int volume) {
        itemsPage.setNewItemForm(name, volume);
    }

    @And("I click submit for Add Item")
    public void submitAddItemForm() {
        itemsPage.clickSubmitForm();
    }

    @Then("the new item should be present on the items table with {string} and {int}")
    public void newItemExists(String name, int volume) {
        assertTrue(itemsPage.itemExists(name, volume));

        itemId = itemsPage.findItemId(name, volume);
        itemsPage.deleteItem(itemId);
    }

    @Then("the new item should not be present on the items table with {string} and {int}")
    public void newItemDoesNotExist(String name, int volume) {
        assertFalse(itemsPage.itemExists(name, volume));
    }

    @After("@createItems")
    public void after() {
        SingletonDriver.quitDriver();
    }
}
