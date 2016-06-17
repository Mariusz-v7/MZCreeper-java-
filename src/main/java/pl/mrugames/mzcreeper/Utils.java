package pl.mrugames.mzcreeper;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class Utils {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;

    @Autowired
    public Utils(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void takeScreenShot() {
        File file = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(file, new File(String.format("/tmp/MZCreeper-screenshot-%s.png", LocalDateTime.now())));
        } catch (IOException e) {
            logger.warn("Failed to save screenshot", e);
        }
    }

    public void waitForUrlChange() {
        waitForUrlChange(webDriver.getCurrentUrl());
    }

    public void waitForUrlChange(String currentUrl) {
        int timeoutSeconds = 30;

        new WebDriverWait(webDriver, timeoutSeconds).until(
                ExpectedConditions.not(ExpectedConditions.urlToBe(currentUrl))
        );
    }
}
