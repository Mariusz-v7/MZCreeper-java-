package pl.mrugames.mzcreeper;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MainService implements pl.mrugames.common.MainService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final ApplicationContext applicationContext;
    private final TasksRunner tasksRunner;

    @Autowired
    public MainService(WebDriver wd, TasksRunner tr, ApplicationContext ac) {
        this.webDriver = wd;
        this.tasksRunner = tr;
        this.applicationContext = ac;
    }

    public void main(String[] strings) {
        try {
            logger.info("MZCreeper is starting");

            while (true) { // TODO - how to stop
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            logger.info("Closing WebDriver...");
            webDriver.quit();
            logger.info("WebDriver closed");

            logger.info("MZCreeper finished");
        }
    }
}
