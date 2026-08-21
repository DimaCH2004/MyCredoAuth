package Utils;

import Data.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

public class WaitUtils {

    private final WebDriver driver;

    public WaitUtils(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement waitUntilVisible(WebElement element) {
        return fluent().until(d -> element.isDisplayed() ? element : null);
    }

    public void click(WebElement element) {
        fluent()
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    if (!element.isDisplayed() || !element.isEnabled()) {
                        return null;
                    }
                    element.click();
                    return Boolean.TRUE;
                });
    }

    public void click(By locator) {
        fluent()
                .ignoring(ElementClickInterceptedException.class)
                .ignoring(ElementNotInteractableException.class)
                .until(d -> {
                    WebElement element = d.findElement(locator);
                    if (!element.isDisplayed() || !element.isEnabled()) {
                        return null;
                    }
                    element.click();
                    return Boolean.TRUE;
                });
    }

    public String waitForNonBlankText(WebElement element) {
        return fluent().until(d -> {
            String text = element.getText();
            return (text == null || text.isBlank()) ? null : text.trim();
        });
    }

    public String waitForTextToBe(WebElement element, String expected) {
        return fluent().until(d -> {
            String text = element.getText();
            return text != null && expected.equals(text.trim()) ? text.trim() : null;
        });
    }

    public String waitForText(WebElement element, String expected) {
        try {
            return waitForTextToBe(element, expected);
        } catch (TimeoutException e) {
            try {
                String text = element.getText();
                return text == null ? "" : text.trim();
            } catch (RuntimeException ignored) {
                return "";
            }
        }
    }

    public WebElement waitForPresence(By locator) {
        return fluent().until(d -> {
            WebElement element = d.findElement(locator);
            return element.isDisplayed() ? element : null;
        });
    }

    private FluentWait<WebDriver> fluent() {
        return new FluentWait<>(driver)
                .withTimeout(Constants.FLUENT_TIMEOUT)
                .pollingEvery(Constants.POLL_INTERVAL)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
    }

    public boolean isVisible(WebElement element) {
        try {
            return waitUntilVisible(element) != null;
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isPresentNow(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    public int countNow(By locator) {
        return driver.findElements(locator).size();
    }
}
