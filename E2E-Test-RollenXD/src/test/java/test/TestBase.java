package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.LoginPage;
import page.SignupPage;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {
    private static final Duration TIMEOUT = Duration.ofSeconds(3);

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    protected LoginPage loginPage;
    protected SignupPage signupPage;

    @BeforeAll
    public void setUp() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--disable-search-engine-choice-screen");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(TIMEOUT);
        wait = new WebDriverWait(driver, TIMEOUT);
        actions = new Actions(driver);

        loginPage = new LoginPage(driver, wait, actions);
        signupPage = new SignupPage(driver, wait, actions);
    }
    @AfterAll
    public void tearDown() {
        if(driver != null) {
            driver.quit();
            driver = null;
        }
    }

    protected boolean waitForUrlToBe(String url) {
        try{
            return wait.until(ExpectedConditions.urlToBe(url));
        } catch (Exception e) {
            return false;
        }
    }

}
