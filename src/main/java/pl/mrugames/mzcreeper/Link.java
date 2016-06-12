package pl.mrugames.mzcreeper;

import pl.mrugames.mzcreeper.parsers.ChallengesParser;
import pl.mrugames.mzcreeper.parsers.MainPageParser;
import pl.mrugames.mzcreeper.parsers.Parser;

public enum Link {
    MAIN_PAGE("http://www.managerzone.com/", MainPageParser.class),
    CHALLENGES("http://www.managerzone.com/?p=challenges", ChallengesParser.class);

    private final String link;
    private final Class<? extends Parser> parser;

    Link(String link, Class<? extends Parser> parser) {
        this.link = link;
        this.parser = parser;
    }

    public String getLink() {
        return link;
    }

    public Class<? extends Parser> getParser() {
        return parser;
    }
}
