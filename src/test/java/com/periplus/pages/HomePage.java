package com.periplus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class HomePage extends BasePage {

    private final By searchInputDesktop = By.id("filter_name_desktop");
    private final By searchInputAny = By.cssSelector("input[name='filter_name']");
    private final By cartLink = By.cssSelector("a[href*='/checkout/cart']");
    private final By cartTotalBadge = By.id("cart_total");
    private final By signOutLink = By.xpath("//a[contains(translate(., 'LOGOUT', 'logout'),'logout') or contains(translate(., 'SIGN OUT', 'sign out'),'sign out') or contains(@href,'logout') or contains(@href,'Logout')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public SearchResultsPage searchFor(String keyword) {
        WebElement input;
        if (isPresent(searchInputDesktop)) {
            input = waitVisible(searchInputDesktop);
        } else {
            input = waitVisible(searchInputAny);
        }
        scrollIntoView(input);
        input.clear();
        input.sendKeys(keyword);
        input.sendKeys(Keys.ENTER);
        return new SearchResultsPage(driver);
    }

    public CartPage openCart() {
        driver.get(siteRoot() + "/checkout/cart");
        return new CartPage(driver);
    }

    public LoginPage openLogin() {
        driver.get(siteRoot() + "/account/Login");
        return new LoginPage(driver);
    }

    public int getCartTotal() {
        if (!isPresent(cartTotalBadge)) return -1;
        String txt = driver.findElement(cartTotalBadge).getText().trim();
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean isUserLoggedIn() {
        return isDisplayedQuick(signOutLink);
    }
}
