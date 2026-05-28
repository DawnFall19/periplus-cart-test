package com.periplus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartTotalBadge = By.id("cart_total");
    private final By emptyText = By.xpath("//*[contains(translate(., 'YOUR SHOPPING CART IS EMPTY','your shopping cart is empty'),'your shopping cart is empty')]");
    private final By cartRows = By.cssSelector("table tbody tr");
    private final By qtyInputs = By.cssSelector("input[name^='quantity'], input.qty, input[id^='qty_']");
    private final By removeButtons = By.cssSelector("a[href*='remove'], button[onclick*='remove'], a[onclick*='remove'], button.remove, a.remove");
    private final By checkoutLinks = By.xpath("//a[contains(translate(.,'CHECKOUT','checkout'),'checkout')] | //button[contains(translate(.,'CHECKOUT','checkout'),'checkout')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int headerCartTotal() {
        if (!isPresent(cartTotalBadge)) return -1;
        try {
            return Integer.parseInt(driver.findElement(cartTotalBadge).getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean isEmpty() {
        return isDisplayedQuick(emptyText) || headerCartTotal() == 0;
    }

    public boolean containsText(String needle) {
        try {
            String body = driver.findElement(By.tagName("body")).getText();
            return body != null && body.toLowerCase().contains(needle.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }

    public int getItemRowCount() {
        return driver.findElements(cartRows).size();
    }

    public CartPage updateFirstQuantity(int newQty) {
        List<WebElement> inputs = driver.findElements(qtyInputs);
        if (inputs.isEmpty()) {
            throw new IllegalStateException("No quantity input found on cart page");
        }
        WebElement input = inputs.get(0);
        scrollIntoView(input);
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(String.valueOf(newQty));
        input.sendKeys(Keys.TAB);
        return this;
    }

    public int getFirstQuantity() {
        List<WebElement> inputs = driver.findElements(qtyInputs);
        if (inputs.isEmpty()) return -1;
        String v = inputs.get(0).getAttribute("value");
        try {
            return Integer.parseInt(v.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public CartPage removeFirstItem() {
        List<WebElement> btns = driver.findElements(removeButtons);
        if (btns.isEmpty()) {
            throw new IllegalStateException("No remove control found on cart page");
        }
        WebElement btn = btns.get(0);
        scrollIntoView(btn);
        try {
            btn.click();
        } catch (Exception e) {
            jsClick(btn);
        }
        return this;
    }

    public boolean hasCheckoutControl() {
        return isDisplayedQuick(checkoutLinks);
    }

    public void proceedToCheckout() {
        safeClick(checkoutLinks);
    }
}
