package pl.mrugames.mzcreeper;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.parsers.Parser;

import java.util.Arrays;

@Component
public class MainService implements pl.mrugames.common.MainService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final Utils utils;
    private final ApplicationContext applicationContext;

    @Autowired
    public MainService(WebDriver wd, Utils u, ApplicationContext ac) {
        this.webDriver = wd;
        this.utils = u;
        this.applicationContext = ac;
    }

    public void main(String[] strings) {
        try {
            logger.info("MZCreeper is starting");

            parseAll();
        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            logger.info("Closing WebDriver...");
            webDriver.quit();
            logger.info("WebDriver closed");

            logger.info("MZCreeper finished");
        }
    }

    private void parseAll() {
        Arrays.stream(Link.values())
                .map(Link::getParser)
                .map(applicationContext::getBean)
                .forEach(Parser::parse);
    }
}
