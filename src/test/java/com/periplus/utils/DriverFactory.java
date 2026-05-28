package com.periplus.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver create() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver driver;
        switch (browser) {
            case "chrome":
            default:
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new");
                }
                options.addArguments("--window-size=1440,900");
                options.addArguments("--disable-notifications");
                options.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(options);
                break;
        }

        int implicitTimeout = ConfigReader.getInt("shortTimeoutSeconds", 5);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitTimeout));
        driver.manage().window().maximize();
        return driver;
    }
}
