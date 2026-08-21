package Utils;

import Data.Constants;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BrowserConfig {

    private static final Logger log = LoggerFactory.getLogger(BrowserConfig.class);

    @BeforeSuite(alwaysRun = true)
    public void resolveDriverBinary() {
        DriverManager.resolveDriverBinary();
        log.info("Chromedriver resolved (headless={})", Constants.HEADLESS);
    }

    /**
     * Navigates to the authorization page before each scenario.
     *
     * <p>Retried once. A failure here is a configuration failure, which skips every remaining
     * test rather than just this one, and the site occasionally answers the first request with a
     * Cloudflare challenge or takes long enough to hit the page-load timeout. A second attempt
     * costs a few seconds; losing the rest of the suite to a transient costs the whole run. If
     * the retry fails too, the exception stands — something is genuinely wrong.
     */
    @BeforeMethod(alwaysRun = true)
    public void openAuthPage() {
        WebDriver driver = DriverManager.getDriver();
        try {
            driver.get(Constants.CREDO_URL);
        } catch (RuntimeException first) {
            log.warn("Could not open the authorization page ({}); retrying once",
                    first.getClass().getSimpleName());
            driver.get(Constants.CREDO_URL);
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitAll();
        log.info("All browsers closed");
    }
}
