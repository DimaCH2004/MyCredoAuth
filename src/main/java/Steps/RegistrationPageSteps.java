package Steps;

import Data.Language;
import Page.RegistrationPage;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class RegistrationPageSteps extends BaseSteps {

    private final RegistrationPage page;

    RegistrationPageSteps(WebDriver driver, SoftAssert softly) {
        super(driver, softly);
        this.page = new RegistrationPage(driver);
    }

    @Step("Enter personal number '{personalNumber}'")
    public RegistrationPageSteps enterPersonalNumber(String personalNumber) {
        WebElement field = wait.waitUntilVisible(page.personalNumberInput);
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        if (!personalNumber.isEmpty()) {
            field.sendKeys(personalNumber);
        }
        return this;
    }

    @Step("Click next")
    public RegistrationPageSteps clickNext() {
        wait.click(page.nextButton);
        return this;
    }

    @Step("Submit the registration form with no personal number")
    public RegistrationPageSteps submitEmptyForm() {
        return enterPersonalNumber("").clickNext();
    }

    @Step("Expect the personal number to be reported as required in {language}")
    public RegistrationPageSteps shouldShowRequiredErrorOnPersonalNumber(Language language) {
        softly.assertEquals(
                wait.waitForText(page.personalNumberError, language.requiredFieldError()),
                language.requiredFieldError(),
                "Personal number should be reported as required in " + language);
        return this;
    }

    @Step("Expect the personal number length error in {language}")
    public RegistrationPageSteps shouldShowLengthErrorOnPersonalNumber(Language language) {
        softly.assertEquals(
                wait.waitForText(page.personalNumberError, language.personalNumberLengthError()),
                language.personalNumberLengthError(),
                "Personal number should be rejected for its length in " + language);
        return this;
    }

    @Step("Expect the birth date to be reported as required in {language}")
    public RegistrationPageSteps shouldShowRequiredErrorOnBirthDate(Language language) {
        softly.assertEquals(
                wait.waitForText(page.birthDateError, language.requiredFieldError()),
                language.requiredFieldError(),
                "Birth date should be reported as required in " + language);
        return this;
    }

    @Step("Expect the birth date fields to be offered")
    public RegistrationPageSteps shouldOfferBirthDateFields() {
        softly.assertTrue(wait.isVisible(page.birthDateDropdowns),
                "The birth date fields should be shown");
        return this;
    }

    @Step("Expect to remain on the registration page")
    public RegistrationPageSteps shouldStayOnRegistrationPage() {
        softly.assertTrue(driver.getCurrentUrl().contains("/landing/registration/customer-check"),
                "A rejected registration must leave the browser on the customer check step");
        softly.assertTrue(wait.isPresentNow(RegistrationPage.PERSONAL_NUMBER_INPUT),
                "The personal number field should still be on screen");
        return this;
    }
}
