package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import page.URLS;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignupPageTest extends TestBase {
    @BeforeEach
    public void navigateTo() {
        driver.get(URLS.SIGN_UP.getUrl());
    }

    @Test
    public void testSignupWithValidNotUsedCredentials() {
        signupPage.signup("testUser", "testEmail@email.com", "testPassword");
        assertTrue(waitForUrlToBe(URLS.LOGIN.getUrl()));
    }
}
