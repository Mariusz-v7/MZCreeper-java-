package pl.mrugames.mzcreeper.configuration;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "pl.mrugames")
public class MainConfiguration {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public DesiredCapabilities desiredCapabilities(@Value("${webdriver.path}") String webDriverPath, @Value("${webdriver.user_agent}") String userAgent) {
        String system = System.getProperty("os.name");
        if (system.contains("Windows")) {
            webDriverPath += ".exe";
        }

        DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, webDriverPath);
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", userAgent);

        return desiredCapabilities;
    }

    @Bean(destroyMethod = "quit")
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
