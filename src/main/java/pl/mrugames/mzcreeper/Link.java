package pl.mrugames.mzcreeper;

import pl.mrugames.mzcreeper.parsers.*;

public enum Link {
    AUTHENTICATION("http://www.managerzone.com/", AuthenticationParser.class),
    CHALLENGES("http://www.managerzone.com/?p=challenges", ChallengesParser.class),
    FORUM("http://www.managerzone.com/?p=forum&sub=topics&forum_id=%s&sport=soccer", SparringForumParser.class),
    PROFILE("http://www.managerzone.com/?p=profile&uid=%s", MatchInvitationSender.class),
    INCOMING_MATCHES("http://www.managerzone.com/?p=match&sub=scheduled", IncomingMatchesParser.class);

    private final String link;
    private final Class<? extends Parser> parser;

    Link(String link, Class<? extends Parser> parser) {
        this.link = link;
        this.parser = parser;
    }

    public String getLink() {
        return link;
    }

    public String getLink(Object... args) {
        return String.format(link, args);
    }

    public Class<? extends Parser> getParser() {
        return parser;
    }
}
