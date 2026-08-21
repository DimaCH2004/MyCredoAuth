package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LanguagePopup extends BasePage {

    public LanguagePopup(WebDriver driver) {
        super(driver);
    }

    public static final By OPTIONS = By.xpath("//li[.//img[@alt='flag']]");

    public static By optionFor(String languageLabel) {
        return By.xpath("//li[.//img[@alt='flag']][.//p[normalize-space()='" + languageLabel + "']]");
    }
}
