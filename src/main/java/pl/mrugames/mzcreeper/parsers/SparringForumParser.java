package pl.mrugames.mzcreeper.parsers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.mrugames.mzcreeper.FriendlyMatchesManager;
import pl.mrugames.mzcreeper.Link;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class SparringForumParser implements Parser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WebDriver webDriver;
    private final FriendlyMatchesManager friendlyMatchesManager;

    private final static int FORUM_ID = 27;
    private final static String TABLE_ID = "topics-list";
    private final static String AUTHOR_CELL_CLASS_NAME = "author-string";
    private final static Pattern PLAYER_ID_FROM_URL_PATTERN = Pattern.compile("uid=(.*?)$");
    private final int MAX_AMOUNT_OF_TARGETS_IN_A_ROW;


    @Autowired
    SparringForumParser(WebDriver webDriver, FriendlyMatchesManager friendlyMatchesManager,
                               @Value("${max_amount_of_targets_in_a_row}") String maxTargetsInRow) {
        this.webDriver = webDriver;
        this.friendlyMatchesManager = friendlyMatchesManager;

        MAX_AMOUNT_OF_TARGETS_IN_A_ROW = Integer.parseInt(maxTargetsInRow);
    }

    @Override
    public void parse() {
        if (friendlyMatchesManager.getDatesWithoutMatch().size() == 0)
            return;

        webDriver.get(Link.FORUM.getLink(FORUM_ID));

        List<Long> possibleTargets = loadPossibleTargetsIds();
        friendlyMatchesManager.setPossibleTargetsIds(possibleTargets);

        logger.info("Loaded {} possible targets for friendly matches", possibleTargets.size());
    }

    private List<Long> loadPossibleTargetsIds() {
        WebElement table = webDriver.findElement(By.id(TABLE_ID));
        List<WebElement> authors = table.findElements(By.className(AUTHOR_CELL_CLASS_NAME));

        List<String> targetsUrls = authors.stream()
                .skip(1)  // skip first player who is game admin or something
                .limit(MAX_AMOUNT_OF_TARGETS_IN_A_ROW)
                .map(e -> e.findElements(By.tagName("a")))
                .filter(list -> list.size() == 1)  // no <a> or more than one found? skip it.
                .map(list -> list.get(0))
                .map(e -> e.getAttribute("href"))
                .collect(Collectors.toList());

        return targetsUrls.stream()
                .map(this::extractTargetIdFromUrl)
                .filter(id -> id != 0)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Long extractTargetIdFromUrl(String url) {
        Matcher matcher = PLAYER_ID_FROM_URL_PATTERN.matcher(url);
        if (matcher.find())
            return Long.valueOf(matcher.group(1));

        return 0L;
    }
}
