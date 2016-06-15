package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.FriendlyMatchesManager;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class MatchInvitationSender implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final FriendlyMatchesManager friendlyMatchesManager;

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
        // TODO
    }
}
