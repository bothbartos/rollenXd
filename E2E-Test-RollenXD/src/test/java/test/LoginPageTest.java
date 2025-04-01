package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.URLS;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginPageTest extends TestBase {
    @BeforeEach
    public void setupAndNavigate() {
        driver.get(URLS.LOGIN.getUrl());
    }

    @Test
    public void testLoginWithValidCredentials() {
        loginPage.login("testUser", "testPassword");
        assertTrue(waitForUrlToBe(URLS.MAIN_PAGE.getUrl()));
    }
}
