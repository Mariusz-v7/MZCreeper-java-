package pl.mrugames.mzcreeper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MainConfiguration {
    @Bean
    public DesiredCapabilities desiredCapabilities(Environment environment) {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, environment.getProperty("webdriver.path"));

        return desiredCapabilities;
    }

    @Bean
    public WebDriver webDriver(DesiredCapabilities desiredCapabilities) {
        return new PhantomJSDriver(desiredCapabilities);
    }
}
