package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends PageBase{
    @FindBy(xpath = "//input[contains(@placeholder, 'Username')]")
    private WebElement username;
    @FindBy(xpath = "//input[contains(@placeholder, 'Password')]")
    private WebElement password;
    @FindBy(xpath = "//button[text()='Login']")
    private WebElement loginButton;
    public LoginPage(WebDriver driver, WebDriverWait wait, Actions actions) {
        super(driver, wait, actions);
    }
    public void login(String username, String password) {
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        this.loginButton.click();
    }
}
