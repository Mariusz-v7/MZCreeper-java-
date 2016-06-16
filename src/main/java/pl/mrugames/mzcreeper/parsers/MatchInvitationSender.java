package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.FriendlyMatchesManager;
import pl.mrugames.mzcreeper.Link;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class MatchInvitationSender implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final FriendlyMatchesManager friendlyMatchesManager;

    private final static String CHALLENGE_IMAGE_URL = "http://static.managerzone.com/nocache-.*?/img/soccer/challenge_yes.gif";
    private final static String TARGET_PLANNED_MATCHES_TABLE_ID = "booked_challenges_for_team";
    private final static int TARGET_PLANNED_MATCHES_DATE_COLUMN_INDEX = 2;
    private final static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @Autowired
    public MatchInvitationSender(WebDriver webDriver, FriendlyMatchesManager friendlyMatchesManager) {
        this.webDriver = webDriver;
        this.friendlyMatchesManager = friendlyMatchesManager;
    }

    @Override
    public void parse() {
        try {
            List<LocalDateTime> freeDateSlots = friendlyMatchesManager.getDatesWithoutMatch();

            if (freeDateSlots.size() == 0) {
                logger.info("You have no free slots for friendly matches");
                return;
            }

            logger.info("You have {} free slots for friendly matches", freeDateSlots.size());

            List<Long> possibleTargetsIds = friendlyMatchesManager.getPossibleTargetsIds();
            sendInvitations(possibleTargetsIds, freeDateSlots);
        } finally {
            friendlyMatchesManager.clear();
        }
    }

    private void sendInvitations(List<Long> possibleTargetsIds, List<LocalDateTime> freeDateSlots) {
        possibleTargetsIds.forEach(targetId -> {
            goToChallengesForm(targetId);

            List<LocalDateTime> targetPlannedMatches = loadTargetPlannedMatches();
            List<LocalDateTime> targetFreeSlots = friendlyMatchesManager.getDatesWithoutMatch(targetPlannedMatches);

            if (targetFreeSlots.size() == 0)
                return;

            // TODO: send invitations
        });
    }

    private void goToChallengesForm(long targetId) {
        webDriver.get(Link.PROFILE.getLink(targetId));

        List<WebElement> images = webDriver.findElements(By.tagName("img"));

        Optional<WebElement> invitationImage = images.stream()
                .filter(img -> img.getAttribute("src").matches(CHALLENGE_IMAGE_URL))
                .findAny();

        invitationImage.get().click();
    }

    private List<LocalDateTime> loadTargetPlannedMatches() {
        List<WebElement> tables = webDriver.findElements(By.id(TARGET_PLANNED_MATCHES_TABLE_ID));
        if (tables.size() == 0)
            return Collections.emptyList();

        WebElement table = tables.get(0).findElement(By.tagName("tbody"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        List<LocalDateTime> plannedDates = rows.stream()
                .map(row -> row.findElements(By.tagName("td")))
                .map(columns -> columns.get(TARGET_PLANNED_MATCHES_DATE_COLUMN_INDEX))
                .map(WebElement::getText)
                .map(date -> LocalDateTime.parse(date, DATE_FORMATTER))
                .collect(Collectors.toList());

        return plannedDates;
    }
}
