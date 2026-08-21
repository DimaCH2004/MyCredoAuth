package Steps;

import Utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public abstract class BaseSteps {

    protected final WebDriver driver;
    protected final WaitUtils wait;
    protected final SoftAssert softly;

    protected BaseSteps(WebDriver driver, SoftAssert softly) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
        this.softly = softly;
    }

    public void assertAll() {
        softly.assertAll();
    }
}
