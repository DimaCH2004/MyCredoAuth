package Page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class RegistrationPage extends BasePage {

    public RegistrationPage(WebDriver driver) {
        super(driver);
    }

    @FindBy(xpath = "//input[@id='personalNumber']")
    public WebElement personalNumberInput;

    @FindBy(xpath = "//form//button[@type='submit']")
    public WebElement nextButton;

    @FindBy(xpath = "//form-field[.//input[@id='personalNumber']]//crd-error")
    public WebElement personalNumberError;

    @FindBy(xpath = "//crd-date-dropdowns/following-sibling::p")
    public WebElement birthDateError;

    @FindBy(xpath = "//crd-date-dropdowns")
    public WebElement birthDateDropdowns;

    public static final By PERSONAL_NUMBER_INPUT = By.xpath("//input[@id='personalNumber']");
}
