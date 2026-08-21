package Steps;

import Data.Language;
import Page.LanguagePopup;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.asserts.SoftAssert;

public class LanguagePopupSteps extends BaseSteps {

    private final LoginPageSteps loginSteps;

    LanguagePopupSteps(WebDriver driver, SoftAssert softly, LoginPageSteps loginSteps) {
        super(driver, softly);
        this.loginSteps = loginSteps;
    }

    @Step("Select the {language} option")
    public LoginPageSteps selectLanguage(Language language) {
        wait.click(LanguagePopup.optionFor(language.label()));
        return loginSteps.waitForLanguageSwitch(language);
    }
}
