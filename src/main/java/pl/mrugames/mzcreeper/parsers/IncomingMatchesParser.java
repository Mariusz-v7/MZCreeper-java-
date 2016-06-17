package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.ConfigManager;
import pl.mrugames.mzcreeper.Link;
import pl.mrugames.mzcreeper.Utils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class IncomingMatchesParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final WebDriver webDriver;
    private final Utils utils;
    private final ConfigManager configManager;

    private final static String MATCHES_TYPE_FORM_ID = "matchListForm";
    private final static String MATCHES_TYPE_SELECT_NAME = "selectType";
    private final static String FRIENDLY_MATCHES_OPTION_VALUE = "friendly";
    private final static String MATCHES_TACTICS_FORM_ID = "saveMatchTactics";
    private final static int DATE_CELL_INDEX = 0;
    private final static int TACTIC_SELECT_CELL_INDEX = 7;

    @Autowired
    public IncomingMatchesParser(WebDriver webDriver, Utils utils, ConfigManager configManager) {
        this.webDriver = webDriver;
        this.utils = utils;
        this.configManager = configManager;
    }

    @Override
    public void parse() {
        webDriver.get(Link.INCOMING_MATCHES.getLink());

        selectOnlyFriendlyMatches();
        setTactics();
    }

    private void selectOnlyFriendlyMatches() {
        WebElement form = webDriver.findElement(By.id(MATCHES_TYPE_FORM_ID));
        Select select = new Select(form.findElement(By.name(MATCHES_TYPE_SELECT_NAME)));
        select.selectByValue(FRIENDLY_MATCHES_OPTION_VALUE);
        utils.waitForUrlChange(Link.INCOMING_MATCHES.getLink());
    }

    private void setTactics() {
        WebElement form = webDriver.findElement(By.id(MATCHES_TACTICS_FORM_ID));
        List<WebElement> rows = form.findElements(By.tagName("tr"));
        rows.stream()
                .skip(1)  // first row is empty
                .forEach(row -> {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    List<WebElement> dateElement = cells.get(DATE_CELL_INDEX).findElements(By.tagName("nobr"));
                    if (dateElement.size() == 0)
                        return;

                    LocalDateTime matchDate = LocalDateTime.parse(dateElement.get(0).getText(), configManager.getDateTimeFormatter());
                    DayOfWeek dayOfMatch = matchDate.getDayOfWeek();
                    String tactic = configManager.getTacticsForFriendlies().get(dayOfMatch);

                    List<WebElement> selectElement = cells.get(TACTIC_SELECT_CELL_INDEX).findElements(By.tagName("select"));
                    if (selectElement.size() == 0)
                        return;

                    Select select = new Select(selectElement.get(0));

                    logger.info("Setting tactic {} for match on {}", tactic, matchDate.format(configManager.getDateTimeFormatter()));

                    select.selectByVisibleText(tactic);

                });

        form.submit();
    }
}
