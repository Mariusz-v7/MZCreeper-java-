package pl.mrugames.mzcreeper.configuration;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.TasksRunner;

import java.io.StreamTokenizer;

@Component
public class MainService implements pl.mrugames.common.MainService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final ApplicationContext applicationContext;
    private final TasksRunner tasksRunner;
    private volatile boolean shutdown = false;

    @Autowired
    public MainService(WebDriver wd, TasksRunner tr, ApplicationContext ac) {
        this.webDriver = wd;
        this.tasksRunner = tr;
        this.applicationContext = ac;
    }

    public void main(String[] strings) {
        try {
            logger.info("MZCreeper is starting");

            while (!shutdown) {
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            logger.error("Exception: ", e);
        } finally {
            logger.info("MZCreeper finished");
        }
    }

    @Override
    public void shutDown() {
        shutdown = true;
    }

    @Override
    public String command(StreamTokenizer streamTokenizer) {
        return "not supported";
    }

    @Override
    public String status() {
        return "running: " + !shutdown;
    }
}
