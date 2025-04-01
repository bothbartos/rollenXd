package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PageBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Actions actions;

    public PageBase(WebDriver driver, WebDriverWait wait, Actions actions) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        this.wait = wait;
        this.actions = actions;
    }
}
