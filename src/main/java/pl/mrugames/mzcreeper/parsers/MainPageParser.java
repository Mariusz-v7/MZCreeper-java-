package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.AuthenticationManager;
import pl.mrugames.mzcreeper.Link;
import pl.mrugames.mzcreeper.Utils;
import pl.mrugames.mzcreeper.exceptions.FailedToLoginException;

@Component
public class MainPageParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WebDriver webDriver;
    private final Utils utils;
    private final AuthenticationManager authenticationManager;

    private final static String LOGIN_FIELD_ID = "login_username";
    private final static String PASSWORD_FIELD_ID = "login_password";
    private final static String LOGIN_BUTTON_ID = "login";

    @Autowired
    public MainPageParser(WebDriver webDriver, Utils utils, AuthenticationManager am) {
        this.webDriver = webDriver;
        this.utils = utils;
        this.authenticationManager = am;
    }

    @Override
    public void parse() {
        webDriver.get(Link.MAIN_PAGE.getLink());
        login();
    }

    private void login() {
        int timeoutSeconds = 30;

        logger.info("Performing login");

        webDriver.findElement(By.id(LOGIN_FIELD_ID)).sendKeys(authenticationManager.getMZLogin());
        webDriver.findElement(By.id(PASSWORD_FIELD_ID)).sendKeys(authenticationManager.getMzPassword());
        webDriver.findElement(By.id(LOGIN_BUTTON_ID)).click();

        try {
            new WebDriverWait(webDriver, timeoutSeconds).until(
                    ExpectedConditions.not(ExpectedConditions.urlToBe(Link.MAIN_PAGE.getLink()))
            );
        } catch (Exception e) {
            logger.error("Failed to login");
            authenticationManager.reset();
            throw new FailedToLoginException(e);
        }

        logger.info("Login successful");
    }
}
