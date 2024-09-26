package com.skillstorm.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class SingletonDriver {

    private static WebDriver driver;
    private static ChromeOptions options;

    private SingletonDriver() {
    }

    private static void setOptions() {
        options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-debugging-port=9222");
    }

    public static synchronized WebDriver getChromeDriver() {
        if(driver == null) {
            WebDriverManager.chromedriver().setup();
            setOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
        }
        return driver;
    }

    // Set driver back to null on quit so that we can quit the driver without preventing other
    // classes from reinitializing it:
    public static void quitDriver() {
        if(driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
