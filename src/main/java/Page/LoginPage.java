package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage {

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='username']")
    public WebElement usernameInput;

    @FindBy(xpath = "//input[@id='password']")
    public WebElement passwordInput;

    @FindBy(xpath = "//form//button[@type='submit']")
    public WebElement submitButton;

    @FindBy(xpath = "//button[.//app-icon[@svgicon='language']]")
    public WebElement languagePopupButton;

    @FindBy(xpath = "//a[@href='/landing/registration/customer-check']")
    public WebElement registrationLink;

    @FindBy(xpath = "//form-field[.//input[@id='username']]//crd-error")
    public WebElement usernameError;

    @FindBy(xpath = "//form-field[.//input[@id='password']]//crd-error")
    public WebElement passwordError;

    @FindBy(xpath = "//app-snackbar-container")
    public WebElement errorToast;

    public static final By USERNAME_INPUT = By.xpath("//input[@id='username']");

    public static final By USERNAME_ERROR =
            By.xpath("//form-field[.//input[@id='username']]//crd-error");

    public static final By PASSWORD_ERROR =
            By.xpath("//form-field[.//input[@id='password']]//crd-error");

    public static final By ANY_FIELD_ERROR = By.xpath("//form//crd-error");

    public static final By ERROR_TOAST = By.xpath("//app-snackbar-container");
}
