package com.skillstorm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WarehouseItemsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private ItemsPage itemsPage;
    private WarehousesPage warehousesPage;

    private static String url;

    // Constructor to intialize driver and page elements:
    public WarehouseItemsPage(WebDriver driver) {
        this.driver = driver;
        itemsPage = new ItemsPage(driver);
        warehousesPage = new WarehousesPage(driver);
        wait = new WebDriverWait(driver, Duration.ofMillis(2000));
        PageFactory.initElements(driver, this);
    }

    public static void setUrl(int warehouseId) {
        url = "http://localhost:5173/warehouses/" + warehouseId + "/items";
    }


}
