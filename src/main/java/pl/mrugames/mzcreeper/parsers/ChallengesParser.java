package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.ConfigManager;
import pl.mrugames.mzcreeper.FriendlyMatchesManager;
import pl.mrugames.mzcreeper.Link;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChallengesParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final FriendlyMatchesManager friendlyMatchesManager;
    private final ConfigManager configManager;

    private final static String PLANNED_MATCHES_TABLE_ID = "my_booked_friendlies";
    private final static int PLANED_MATCH_DATE_COLUMN_INDEX = 2;

    @Autowired
    public ChallengesParser(WebDriver webDriver, FriendlyMatchesManager fmm, ConfigManager cm) {
        this.webDriver = webDriver;
        this.friendlyMatchesManager = fmm;
        this.configManager = cm;
    }

    @Override
    public void parse() {
        webDriver.get(Link.CHALLENGES.getLink());

        friendlyMatchesManager.setPlannedDates(loadInfoAboutCurrentlyPlannedMatches());
    }

    private List<LocalDateTime> loadInfoAboutCurrentlyPlannedMatches() {
        try {
            WebElement plannedMatchesTable = webDriver.findElement(By.id(PLANNED_MATCHES_TABLE_ID));
            WebElement tableBody = plannedMatchesTable.findElement(By.tagName("tbody"));
            List<WebElement> plannedMatchesRows = tableBody.findElements(By.tagName("tr"));

            logger.info("You have currently {} planned matches", plannedMatchesRows.size());

            return getDatesOfPlannedMatches(plannedMatchesRows);
        } catch (NoSuchElementException e) {
            return Collections.emptyList();
        }
    }

    private List<LocalDateTime> getDatesOfPlannedMatches(List<WebElement> plannedMatchesRows) {
        return plannedMatchesRows.stream()
                .map(row -> row.findElements(By.tagName("td")).get(PLANED_MATCH_DATE_COLUMN_INDEX))
                .map(WebElement::getText)
                .map(date -> LocalDateTime.parse(date, configManager.getDateTimeFormatter()))
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
