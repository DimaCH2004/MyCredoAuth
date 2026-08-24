package org.example;

import Data.BlankField;
import Data.DataSets;
import Data.Language;
import Data.TestCredentials;
import Steps.LoginPageSteps;
import Utils.BrowserConfig;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static Utils.DriverManager.getDriver;

@Epic("Authentication")
@Feature("Authorization")
public class LoginNegativeScenario extends BrowserConfig {

    private static final ThreadLocal<LoginPageSteps> LOGIN_STEPS = new ThreadLocal<>();

    @BeforeMethod(alwaysRun = true)
    public void initSteps() {
        LOGIN_STEPS.set(new LoginPageSteps(getDriver()));
    }

    @AfterMethod(alwaysRun = true)
    public void releaseSteps() {
        LOGIN_STEPS.remove();
    }

    private LoginPageSteps loginSteps() {
        return LOGIN_STEPS.get();
    }

    @Story("Empty credentials are rejected before any request is made")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Submits an empty form and expects both fields to be reported as required, in "
            + "the selected language, without contacting the server or leaving the page.")
    @Test(dataProvider = "languages", dataProviderClass = DataSets.class,
            description = "Empty form reports both fields as required")
    public void emptyCredentialsAreRejected(Language language) {
        loginSteps().switchLanguageTo(language)
                .submitEmptyForm()
                .shouldShowRequiredErrorOnUsername(language)
                .shouldShowRequiredErrorOnPassword(language)
                .shouldShowExactlyFieldErrors(2)
                .shouldNotContactServer()
                .shouldStayOnAuthPage()
                .assertAll();
    }

    @Story("A single empty field is reported on that field alone")
    @Severity(SeverityLevel.NORMAL)
    @Description("Fills one field and leaves the other empty, expecting the required-field error "
            + "on the empty field only.")
    @Test(dataProvider = "blankFieldPerLanguage", dataProviderClass = DataSets.class,
            description = "One empty field is reported on that field only")
    public void singleBlankFieldIsReportedOnThatFieldOnly(Language language,
                                                          BlankField blank,
                                                          TestCredentials credentials) {
        loginSteps().switchLanguageTo(language)
                .signInWith(credentials)
                .shouldShowRequiredErrorOn(blank, language)
                .shouldNotShowErrorOn(blank.other())
                .shouldShowExactlyFieldErrors(1)
                .shouldStayOnAuthPage()
                .assertAll();
    }

    @Story("Invalid credentials are refused without granting access")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Submits credentials that clear client-side validation but cannot be valid — a "
            + "random unregistered user, whitespace-only input, a SQL tautology and a "
            + "300-character string — and expects the same generic localised refusal for each.")
    @Test(dataProvider = "rejectedCredentialsPerLanguage", dataProviderClass = DataSets.class,
            description = "Invalid credentials are refused with a localised toast")
    public void invalidCredentialsAreRefused(Language language, TestCredentials credentials) {
        loginSteps().switchLanguageTo(language)
                .signInWith(credentials)
                .shouldShowInvalidCredentialsToast(language)
                .shouldStayOnAuthPage()
                .assertAll();
    }

    @Story("Registration rejects an empty personal number")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Follows the registration link from the login page and submits the customer "
            + "check with no personal number, expecting the required-field error.")
    @Test(dataProvider = "languages", dataProviderClass = DataSets.class,
            description = "Registration reports an empty personal number as required")
    public void emptyPersonalNumberIsRejected(Language language) {
        loginSteps().switchLanguageTo(language)
                .goToRegistration()
                .submitEmptyForm()
                .shouldShowRequiredErrorOnPersonalNumber(language)
                .shouldStayOnRegistrationPage()
                .assertAll();
    }

    @Story("Registration rejects a personal number of the wrong length")
    @Severity(SeverityLevel.NORMAL)
    @Description("Submits a personal number shorter than the required 11 characters and expects "
            + "the localised length error.")
    @Test(dataProvider = "shortPersonalNumberPerLanguage", dataProviderClass = DataSets.class,
            description = "Registration rejects a personal number shorter than 11 characters")
    public void shortPersonalNumberIsRejected(Language language, String shortPersonalNumber) {
        loginSteps().switchLanguageTo(language)
                .goToRegistration()
                .enterPersonalNumber(shortPersonalNumber)
                .clickNext()
                .shouldShowLengthErrorOnPersonalNumber(language)
                .shouldStayOnRegistrationPage()
                .assertAll();
    }

    @Story("Registration rejects a missing birth date")
    @Severity(SeverityLevel.NORMAL)
    @Description("Enters a personal number of the correct length, leaves the birth date fields "
            + "empty, then submits and expects the required-field error.")
    @Test(dataProvider = "personalNumberPerLanguage", dataProviderClass = DataSets.class,
            description = "Registration reports a missing birth date as required")
    public void missingBirthDateIsRejected(Language language, String personalNumber) {
        loginSteps().switchLanguageTo(language)
                .goToRegistration()
                .enterPersonalNumber(personalNumber)
                .shouldOfferBirthDateFields()
                .clickNext()
                .shouldShowRequiredErrorOnBirthDate(language)
                .shouldStayOnRegistrationPage()
                .assertAll();
    }
}
