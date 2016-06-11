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
    private final Utils utils;

    @Autowired
    public MainService(WebDriver wd, Utils u) {
        this.webDriver = wd;
        this.utils = u;
    }

    public void main(String[] strings) {
        logger.info("MZCreeper is starting");


        webDriver.get(Link.MAIN_PAGE.getLink());
        utils.takeScreenShot();


        logger.info("Closing WebDriver...");
        webDriver.quit();
        logger.info("WebDriver closed");

        logger.info("MZCreeper finished");
    }
}
