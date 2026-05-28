package com.periplus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class ProductPage extends BasePage {

    private final By productTitle = By.cssSelector(".quickview-content h2");
    private final By addToCartBtn = By.cssSelector("button.btn-add-to-cart");
    private final By cartTotalBadge = By.id("cart_total");
    private final By visibleModal = By.cssSelector(".modal.show, .modal.in, .swal2-popup, .modal[style*='display: block']");
    private final By modalCloseBtns = By.cssSelector(".modal.show .close, .modal.show button[data-dismiss='modal'], .modal.in .close, .swal2-confirm, .modal-footer .btn-primary, .modal-footer .btn");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return waitVisible(productTitle).getText().trim();
    }

    public boolean isAddToCartAvailable() {
        return isDisplayedQuick(addToCartBtn);
    }

    public ProductPage addToCart() {
        int before = readCartTotal();
        safeClick(addToCartBtn);

        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> readCartTotal() > before || isDisplayedQuick(visibleModal));
        } catch (Exception ignored) {
            // no badge update and no modal — proceed anyway, the cart page will reveal the truth
        }

        dismissAnyModal();
        dismissAnyBrowserAlert();
        return this;
    }

    private void dismissAnyModal() {
        List<WebElement> modals = driver.findElements(visibleModal);
        if (modals.isEmpty()) return;
        List<WebElement> btns = driver.findElements(modalCloseBtns);
        for (WebElement b : btns) {
            try {
                if (b.isDisplayed()) {
                    jsClick(b);
                    break;
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void dismissAnyBrowserAlert() {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException | UnhandledAlertException ignored) {
        } catch (Exception ignored) {
        }
    }

    public CartPage goToCart() {
        driver.get(siteRoot() + "/checkout/cart");
        return new CartPage(driver);
    }

    public int readCartTotal() {
        if (!isPresent(cartTotalBadge)) return -1;
        String txt = driver.findElement(cartTotalBadge).getText().trim();
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean isOnProductPage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(productTitle));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
