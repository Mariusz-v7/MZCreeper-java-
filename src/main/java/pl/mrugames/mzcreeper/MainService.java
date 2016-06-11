package pl.mrugames.mzcreeper;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainService implements pl.mrugames.common.MainService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;

    @Autowired
    public MainService(WebDriver wd) {
        this.webDriver = wd;
    }

    public void main(String[] strings) {
        logger.info("MZCreeper is starting");



        logger.info("Closing WebDriver...");
        webDriver.quit();
        logger.info("WebDriver closed");

        logger.info("MZCreeper finished");
    }
}
