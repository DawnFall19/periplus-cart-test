package com.periplus.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchResultsPage extends BasePage {

    private final By productCards = By.cssSelector(".single-product");
    private final By productLinkInCard = By.cssSelector(".product-img a[href*='/p/']");
    private final By productTitleLinkInCard = By.cssSelector(".product-content h3 a[href*='/p/']");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public boolean hasResults() {
        try {
            waitVisible(productCards);
        } catch (Exception ignored) {
        }
        return !driver.findElements(productCards).isEmpty();
    }

    public int resultCount() {
        return driver.findElements(productCards).size();
    }

    public ProductPage openFirstInStockProduct() {
        return openProductByIndex(0);
    }

    public ProductPage openProductByIndex(int index) {
        waitVisible(productCards);
        List<WebElement> cards = driver.findElements(productCards);
        if (cards.isEmpty()) {
            throw new IllegalStateException("No search results found");
        }
        if (index >= cards.size()) {
            throw new IndexOutOfBoundsException(
                    "Requested index " + index + " but only " + cards.size() + " cards present");
        }
        WebElement card = cards.get(index);
        scrollIntoView(card);

        List<WebElement> imgLinks = card.findElements(productLinkInCard);
        WebElement target = !imgLinks.isEmpty()
                ? imgLinks.get(0)
                : card.findElement(productTitleLinkInCard);

        scrollIntoView(target);
        try {
            target.click();
        } catch (Exception e) {
            jsClick(target);
        }
        return new ProductPage(driver);
    }

    public String getTitleAtIndex(int index) {
        List<WebElement> cards = driver.findElements(productCards);
        if (index >= cards.size()) return "";
        List<WebElement> titles = cards.get(index).findElements(productTitleLinkInCard);
        return titles.isEmpty() ? "" : titles.get(0).getText().trim();
    }
}
