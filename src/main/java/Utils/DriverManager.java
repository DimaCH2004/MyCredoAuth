package Utils;

import Data.Constants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final Set<WebDriver> OPEN_DRIVERS = ConcurrentHashMap.newKeySet();

    private DriverManager() {
    }

    public static void resolveDriverBinary() {
        WebDriverManager.chromedriver().setup();
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            driver = create();
            DRIVER.set(driver);
        }
        return driver;
    }

    public static WebDriver currentDriver() {
        return DRIVER.get();
    }

    public static void quitAll() {
        OPEN_DRIVERS.forEach(DriverManager::quietlyQuit);
        OPEN_DRIVERS.clear();
        DRIVER.remove();
    }

    private static WebDriver create() {
        WebDriver driver = new ChromeDriver(chromeOptions());
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Constants.PAGE_LOAD_TIMEOUT);
        OPEN_DRIVERS.add(driver);
        return driver;
    }

    private static void quietlyQuit(WebDriver driver) {
        try {
            driver.quit();
        } catch (Exception ignored) {
        }
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();

        if (Constants.HEADLESS) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-search-engine-choice-screen");
        options.addArguments("--disable-features=PasswordLeakDetection,AutofillServerCommunication");

        return options;
    }
}
