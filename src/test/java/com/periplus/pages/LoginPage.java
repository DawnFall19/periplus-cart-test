package com.periplus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private final By emailInput = By.cssSelector("input[name='email']");
    private final By passwordInput = By.cssSelector("input[name='password']");
    private final By submitBtn = By.cssSelector("input#button-login, input[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public HomePage loginAs(String email, String password) {
        type(emailInput, email);
        type(passwordInput, password);
        safeClick(submitBtn);
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/account/Login")));
        } catch (Exception ignored) {
        }
        return new HomePage(driver);
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().toLowerCase().contains("/account/login");
    }
}
