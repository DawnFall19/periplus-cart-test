package com.periplus.tests;

import com.periplus.base.BaseTest;
import com.periplus.pages.CartPage;
import com.periplus.pages.HomePage;
import com.periplus.pages.LoginPage;
import com.periplus.pages.ProductPage;
import com.periplus.pages.SearchResultsPage;
import com.periplus.utils.ConfigReader;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

public class LoginAddToCartTest extends BaseTest {

    @Test(description = "TC-01: User logs in, adds a product to the cart, and verifies it was added")
    public void loginAndAddProductToCart() {
        String email = ConfigReader.env("PERIPLUS_EMAIL");
        String password = ConfigReader.env("PERIPLUS_PASSWORD");
        if (email.isBlank() || password.isBlank()) {
            throw new SkipException(
                    "Set PERIPLUS_EMAIL and PERIPLUS_PASSWORD (in .env or environment variables) to run this test.");
        }
        String keyword = ConfigReader.get("searchKeyword", "harry potter");

        HomePage home = new HomePage(driver);
        LoginPage login = home.openLogin();
        login.loginAs(email, password);
        Assert.assertFalse(login.isOnLoginPage(),
                "Login should redirect away from /account/Login on success. Check credentials.");

        HomePage homeAfterLogin = new HomePage(driver);
        SearchResultsPage results = homeAfterLogin.searchFor(keyword);
        Assert.assertTrue(results.hasResults(), "Search returned no results for keyword: " + keyword);

        ProductPage product = results.openFirstInStockProduct();
        Assert.assertTrue(product.isOnProductPage(), "Should be on a product detail page after clicking first result");
        String productTitle = product.getTitle();
        Assert.assertFalse(productTitle.isBlank(), "Captured product title should not be blank");
        Assert.assertTrue(product.isAddToCartAvailable(), "Add-to-cart button should be visible");

        product.addToCart();

        CartPage cart = product.goToCart();
        Assert.assertFalse(cart.isEmpty(),
                "Cart page should not show the empty-cart state after adding a product. " +
                        "headerCartTotal=" + cart.headerCartTotal());
        Assert.assertFalse(cart.containsText("Your shopping cart is empty"),
                "Cart page should not contain the empty-cart message after adding a product.");
    }
}
