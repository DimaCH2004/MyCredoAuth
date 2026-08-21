package Steps;

import Data.BlankField;
import Data.Language;
import Data.TestCredentials;
import Page.LanguagePopup;
import Page.LoginPage;
import Page.RegistrationPage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.asserts.SoftAssert;

public class LoginPageSteps extends BaseSteps {

    private final LoginPage page;
    private final LanguagePopupSteps languagePopup;
    private final RegistrationPageSteps registration;

    public LoginPageSteps(WebDriver driver) {
        this(driver, new SoftAssert());
    }

    public LoginPageSteps(WebDriver driver, SoftAssert softly) {
        super(driver, softly);
        this.page = new LoginPage(driver);
        this.languagePopup = new LanguagePopupSteps(driver, softly, this);
        this.registration = new RegistrationPageSteps(driver, softly);
    }

    @Step("Open the language popup")
    public LanguagePopupSteps openLanguagePopup() {
        wait.click(page.languagePopupButton);
        wait.waitForPresence(LanguagePopup.OPTIONS);
        return languagePopup;
    }

    @Step("Switch the interface language to {language}")
    public LoginPageSteps switchLanguageTo(Language language) {
        if (language.shortLabel().equals(currentLanguageLabel())) {
            return this;
        }
        return openLanguagePopup().selectLanguage(language);
    }

    LoginPageSteps waitForLanguageSwitch(Language language) {
        wait.waitForTextToBe(page.languagePopupButton, language.shortLabel());
        wait.waitForPresence(LoginPage.USERNAME_INPUT);
        return this;
    }

    @Step("Read the language shown on the switcher")
    public String currentLanguageLabel() {
        return wait.waitForNonBlankText(page.languagePopupButton);
    }

    @Step("Follow the registration link")
    public RegistrationPageSteps goToRegistration() {
        wait.click(page.registrationLink);
        wait.waitForPresence(RegistrationPage.PERSONAL_NUMBER_INPUT);
        return registration;
    }

    @Step("Enter username '{username}'")
    public LoginPageSteps enterUsername(String username) {
        type(wait.waitUntilVisible(page.usernameInput), username);
        return this;
    }

    @Step("Enter password '{password}'")
    public LoginPageSteps enterPassword(String password) {
        type(wait.waitUntilVisible(page.passwordInput), password);
        return this;
    }

    @Step("Click sign in")
    public LoginPageSteps clickSubmit() {
        wait.click(page.submitButton);
        return this;
    }

    @Step("Attempt sign-in with {credentials}")
    public LoginPageSteps signInWith(TestCredentials credentials) {
        return enterUsername(credentials.username())
                .enterPassword(credentials.password())
                .clickSubmit();
    }

    @Step("Submit the form with both fields empty")
    public LoginPageSteps submitEmptyForm() {
        return enterUsername("").enterPassword("").clickSubmit();
    }

    private void type(WebElement field, String value) {
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        if (!value.isEmpty()) {
            field.sendKeys(value);
        }
    }

    @Step("Expect the username field to report the required-field error in {language}")
    public LoginPageSteps shouldShowRequiredErrorOnUsername(Language language) {
        softly.assertEquals(wait.waitForText(page.usernameError, language.requiredFieldError()),
                language.requiredFieldError(),
                "Username should report the required-field error in " + language);
        return this;
    }

    @Step("Expect the password field to report the required-field error in {language}")
    public LoginPageSteps shouldShowRequiredErrorOnPassword(Language language) {
        softly.assertEquals(wait.waitForText(page.passwordError, language.requiredFieldError()),
                language.requiredFieldError(),
                "Password should report the required-field error in " + language);
        return this;
    }

    @Step("Expect the {blank} field to report the required-field error in {language}")
    public LoginPageSteps shouldShowRequiredErrorOn(BlankField blank, Language language) {
        return blank == BlankField.USERNAME
                ? shouldShowRequiredErrorOnUsername(language)
                : shouldShowRequiredErrorOnPassword(language);
    }

    @Step("Expect no error on the {field} field")
    public LoginPageSteps shouldNotShowErrorOn(BlankField field) {
        By error = field == BlankField.USERNAME
                ? LoginPage.USERNAME_ERROR
                : LoginPage.PASSWORD_ERROR;
        softly.assertFalse(wait.isPresentNow(error),
                "The filled " + field + " field should not be flagged");
        return this;
    }

    @Step("Expect exactly {expected} field error(s)")
    public LoginPageSteps shouldShowExactlyFieldErrors(int expected) {
        softly.assertEquals(wait.countNow(LoginPage.ANY_FIELD_ERROR), expected,
                "Unexpected number of field-level errors");
        return this;
    }

    @Step("Expect the refusal toast in {language}")
    public LoginPageSteps shouldShowInvalidCredentialsToast(Language language) {
        if (!wait.isVisible(page.errorToast)) {
            softly.fail("No error toast appeared; expected the refusal message in " + language);
            return this;
        }
        softly.assertEquals(wait.waitForText(page.errorToast, language.invalidCredentialsError()),
                language.invalidCredentialsError(),
                "The refusal message should be the localised generic one for " + language);
        return this;
    }

    @Step("Expect no request to have reached the server")
    public LoginPageSteps shouldNotContactServer() {
        softly.assertFalse(wait.isPresentNow(LoginPage.ERROR_TOAST),
                "No server error toast should appear: the request should never have been sent");
        return this;
    }

    @Step("Expect to remain on the authorization page")
    public LoginPageSteps shouldStayOnAuthPage() {
        softly.assertTrue(driver.getCurrentUrl().contains("/landing/"),
                "A rejected sign-in must leave the browser on the authorization page");
        softly.assertTrue(wait.isVisible(page.submitButton),
                "The sign-in form should still be on screen");
        return this;
    }
}
