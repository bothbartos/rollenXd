package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SignupPage extends PageBase{
    @FindBy(id = "username")
    private WebElement username;
    @FindBy(id = "email")
    private WebElement email;
    @FindBy(xpath = "//input[contains(@placeholder, 'Password')]")
    private WebElement password;
    @FindBy(xpath = "//input[contains(@placeholder, 'Confirm Password')]")
    private WebElement confirmPassword;
    @FindBy(xpath = "//button[text()='Submit']")
    private WebElement submit;

    public SignupPage(WebDriver driver, WebDriverWait wait, Actions actions) {
        super(driver, wait, actions);
    }

    public void signup(String username, String email, String password){
        this.username.sendKeys(username);
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        this.confirmPassword.sendKeys(password);
        this.submit.click();
    }

    public void signupWithWrongSecondPassword(String username, String email, String password, String wrongSecondPassword){
        this.username.sendKeys(username);
        this.email.sendKeys(email);
        this.password.sendKeys(password);
        this.confirmPassword.sendKeys(wrongSecondPassword);
        this.submit.click();
    }
}
