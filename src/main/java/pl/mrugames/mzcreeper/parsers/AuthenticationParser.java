package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.AuthenticationManager;
import pl.mrugames.mzcreeper.Link;
import pl.mrugames.mzcreeper.Utils;
import pl.mrugames.mzcreeper.exceptions.FailedToLoginException;

@Component
public class AuthenticationParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WebDriver webDriver;
    private final Utils utils;
    private final AuthenticationManager authenticationManager;

    private final static String LOGIN_CONTENT_ID = "login-form-wrapper";
    private final static String LOGIN_FIELD_ID = "login_username";
    private final static String PASSWORD_FIELD_ID = "login_password";
    private final static String LOGIN_BUTTON_ID = "login";

    @Autowired
    public AuthenticationParser(WebDriver webDriver, Utils utils, AuthenticationManager am) {
        this.webDriver = webDriver;
        this.utils = utils;
        this.authenticationManager = am;
    }

    @Override
    public void parse() {
        webDriver.get(Link.AUTHENTICATION.getLink());
        login();
    }

    private void login() {
        logger.info("Performing login");

        if (isUserLogged())
            return;

        fillAndSendForm();
        waitForSubmitResult();

        logger.info("Login successful");
    }

    private boolean isUserLogged() {
        boolean isLoginFormPresent = webDriver.findElements(By.id(LOGIN_CONTENT_ID)).size() != 0;
        if (!isLoginFormPresent) {
            logger.info("Login form not found! User is probably logged on!");
        }

        return !isLoginFormPresent;
    }

    private void fillAndSendForm() {
        webDriver.findElement(By.id(LOGIN_FIELD_ID)).sendKeys(authenticationManager.getMZLogin());
        webDriver.findElement(By.id(PASSWORD_FIELD_ID)).sendKeys(authenticationManager.getMzPassword());
        webDriver.findElement(By.id(LOGIN_BUTTON_ID)).click();
    }

    private void waitForSubmitResult() {
        try {
            utils.waitForUrlChange();
        } catch (Exception e) {
            logger.error("Failed to login");
            authenticationManager.reset();
            throw new FailedToLoginException(e);
        }
    }
}
