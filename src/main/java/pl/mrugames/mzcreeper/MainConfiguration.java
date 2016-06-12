package pl.mrugames.mzcreeper;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

import java.util.Scanner;

@Configuration
public class MainConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public DesiredCapabilities desiredCapabilities(Environment environment) {
        DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, environment.getProperty("webdriver.path"));
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", environment.getProperty("webdriver.user_agent"));

        return desiredCapabilities;
    }

    @Bean
    public WebDriver webDriver(DesiredCapabilities desiredCapabilities) {
        PhantomJSDriver driver = new PhantomJSDriver(desiredCapabilities);
        driver.manage().window().setSize(new Dimension(1366, 768));

        return driver;
    }

    @Lazy
    @Bean(name = "SystemInScanner")
    public Scanner systemInScanner() {
        logger.warn("Scanner created from System.in; (should occur only if console is not available)");
        return new Scanner(System.in);
    }
}
